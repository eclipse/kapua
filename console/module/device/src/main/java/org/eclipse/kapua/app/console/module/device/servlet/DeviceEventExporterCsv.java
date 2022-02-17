/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import com.opencsv.CSVWriter;
import org.apache.commons.lang3.CharEncoding;

public class DeviceEventExporterCsv extends DeviceEventExporter {

    private String scopeId;
    private String clientId;
    private DateFormat dateFormat;
    private CSVWriter writer;
    private String accountName;

    private static final AccountService ACCOUNT_SERVICE = KapuaLocator.getInstance().getService(AccountService.class);

    public DeviceEventExporterCsv(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(final String scopeId, String clientId)
            throws IOException {
        this.scopeId = scopeId;
        this.clientId = clientId;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        response.setContentType("text/csv");
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(clientId, CharEncoding.UTF_8) + "_device_events.csv");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Charset.forName(CharEncoding.UTF_8));
        writer = new CSVWriter(osw);
        writer.writeNext(DEVICE_PROPERTIES);
    }

    @Override
    public void append(KapuaListResult<DeviceEvent> deviceEvents) throws KapuaException {

        Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(new Callable<Account>() {

                @Override
                public Account call() throws Exception {
                    return ACCOUNT_SERVICE.find(KapuaEid.parseCompactId(scopeId));
                }
            });
        } catch (KapuaException e) {
            throw KapuaException.internalError(e);
        }

        for (final DeviceEvent deviceEvent : deviceEvents.getItems()) {

            List<String> cols = new ArrayList<String>();

            // Account id
            cols.add(scopeId);

            // Account name
            cols.add(account != null ? account.getName() : BLANK);

            // Event id
            cols.add(deviceEvent.getId().toCompactId());

            // Created on
            cols.add(deviceEvent.getCreatedOn() != null ? dateFormat.format(deviceEvent.getCreatedOn()) : BLANK);

            // Created By
            cols.add(deviceEvent.getCreatedBy() != null ? deviceEvent.getCreatedBy().toCompactId() : BLANK);

            // Client id
            cols.add(clientId);

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

            writer.writeNext(cols.toArray(new String[]{ }));
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        writer.flush();

        writer.close();
    }

}
