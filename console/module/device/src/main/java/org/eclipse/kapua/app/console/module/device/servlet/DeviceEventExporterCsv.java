/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import com.opencsv.CSVWriter;

public class DeviceEventExporterCsv extends DeviceEventExporter {

    private String scopeId;
    private String clientId;
    private DateFormat dateFormat;
    private CSVWriter writer;

    private final Map<String, String> namesCache = new HashMap<String, String>();

    public DeviceEventExporterCsv(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(final String scopeId, String clientId)
            throws ServletException, IOException, KapuaException {
        this.scopeId = scopeId;
        this.clientId = clientId;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(clientId, "UTF-8") + "_device_events.csv");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Charset.forName("UTF-8"));
        writer = new CSVWriter(osw);

        List<String> cols = new ArrayList<String>();
        for (String property : DEVICE_PROPERTIES) {
            cols.add(property);
        }
        writer.writeNext(cols.toArray(new String[] {}));
    }

    @Override
    public void append(KapuaListResult<DeviceEvent> deviceEvents)
            throws ServletException, IOException {

        KapuaLocator locator = KapuaLocator.getInstance();
        final UserService userService = locator.getService(UserService.class);
        for (final DeviceEvent deviceEvent : deviceEvents.getItems()) {

            List<String> cols = new ArrayList<String>();

            // Account id
            cols.add(scopeId);

            // Account name
            cols.add(clientId);

            // Event id
            cols.add(deviceEvent.getId().toCompactId());

            // Created on
            cols.add(deviceEvent.getCreatedOn() != null ? dateFormat.format(deviceEvent.getCreatedOn()) : BLANK);

            // Created by
            if (deviceEvent.getCreatedBy().toCompactId() != null) {
                if (!namesCache.containsKey(deviceEvent.getCreatedBy().toCompactId())) {
                    try {
                        User user = KapuaSecurityUtils.doPrivileged(new Callable<User>() {
                            @Override
                            public User call() throws Exception {
                                return userService.find(KapuaEid.parseCompactId(scopeId), deviceEvent.getCreatedBy());
                            }
                        });
                        namesCache.put(user.getId().toCompactId(), user.getName());
                    } catch (KapuaException kaex) {
                        namesCache.put(deviceEvent.getCreatedBy().toCompactId(), BLANK);
                    }
                }
                cols.add(namesCache.get(deviceEvent.getCreatedBy().toCompactId()));
            } else {
                cols.add(BLANK);
            }
            // Device id
            cols.add(deviceEvent.getDeviceId() != null ? deviceEvent.getDeviceId().toCompactId() : BLANK);

            // Received on
            cols.add(deviceEvent.getReceivedOn() != null ? dateFormat.format(deviceEvent.getReceivedOn()) : BLANK);

            // Sent on
            cols.add(deviceEvent.getSentOn() != null ? dateFormat.format(deviceEvent.getSentOn()) : BLANK);

            if (deviceEvent.getPosition() != null) {
                KapuaPosition eventPosition = deviceEvent.getPosition();

                // Position Longitude
                cols.add(eventPosition.getLongitude() != null ? eventPosition.getLongitude().toString() : BLANK);

                // Position Latitude
                cols.add(eventPosition.getLatitude() != null ? eventPosition.getLatitude().toString() : BLANK);

                // Position Altitude
                cols.add(eventPosition.getAltitude() != null ? eventPosition.getAltitude().toString() : BLANK);

                // Position Precision
                cols.add(eventPosition.getPrecision() != null ? eventPosition.getPrecision().toString() : BLANK);

                // Position Heading
                cols.add(eventPosition.getHeading() != null ? eventPosition.getHeading().toString() : BLANK);

                // Position Latitude
                cols.add(eventPosition.getSpeed() != null ? eventPosition.getSpeed().toString() : BLANK);

                // Position Timestamp
                cols.add(eventPosition.getTimestamp() != null ? eventPosition.getTimestamp().toString() : BLANK);

                // Position Satellites
                cols.add(eventPosition.getSatellites() != null ? eventPosition.getSatellites().toString() : BLANK);

                // Position Status
                cols.add(eventPosition.getStatus() != null ? eventPosition.getStatus().toString() : BLANK);
            } else {
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
                cols.add(BLANK);
            }

            // Resource
            cols.add(deviceEvent.getResource() != null ? deviceEvent.getResource() : BLANK);

            // Action
            cols.add(deviceEvent.getAction() != null ? deviceEvent.getAction().toString() : BLANK);

            // Response Code
            cols.add(deviceEvent.getResponseCode() != null ? deviceEvent.getResponseCode().toString() : BLANK);

            // Event Message
            cols.add(deviceEvent.getEventMessage() != null ? deviceEvent.getEventMessage() : BLANK);

            writer.writeNext(cols.toArray(new String[] {}));
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        writer.flush();

        writer.close();
    }
}
