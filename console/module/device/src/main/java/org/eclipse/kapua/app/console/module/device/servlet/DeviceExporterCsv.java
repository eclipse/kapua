/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.Device;

import com.opencsv.CSVWriter;

public class DeviceExporterCsv extends DeviceExporter {

    private String account;
    private DateFormat dateFormat;
    private CSVWriter writer;

    public DeviceExporterCsv(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(String account)
            throws ServletException, IOException {
        this.account = account;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Charset.forName("UTF-8"));
        writer = new CSVWriter(osw);

        List<String> cols = new ArrayList<String>();
        for (String property : DEVICE_PROPERTIES) {
            cols.add(property);
        }
        writer.writeNext(cols.toArray(new String[] {}));
    }

    @Override
    public void append(KapuaListResult<Device> devices)
            throws ServletException, IOException {

        AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
        Account account = null;
        try {
            account = accountService.find(KapuaEid.parseCompactId(this.account));
        } catch (KapuaException e) {
            KapuaException.internalError(e);
        }

        for (Device device : devices.getItems()) {

            List<String> cols = new ArrayList<String>();

            // Account id
            cols.add(this.account);

            // Account name
            cols.add(account.getName());

            // Client id
            cols.add(device.getClientId() != null ? device.getClientId() : BLANK);

            // Device status
            cols.add(device.getStatus() != null ? device.getStatus().name() : BLANK);

            // Device connection status
            cols.add(device.getConnection() != null ? device.getConnection().getStatus().name() : BLANK);

            // Created on
            cols.add(device.getCreatedOn() != null ? dateFormat.format(device.getCreatedOn()) : BLANK);

            // Last event on
            cols.add(device.getLastEvent() != null ? dateFormat.format(device.getLastEvent().getReceivedOn()) : BLANK);

            // Last event type
            cols.add(device.getLastEvent() != null ? device.getLastEvent().getResource() : BLANK);

            // Client ip
            cols.add(device.getConnection() != null ? device.getConnection().getClientIp() : BLANK);

            // Display name
            cols.add(device.getDisplayName() != null ? device.getDisplayName() : BLANK);

            // Serial number
            cols.add(device.getSerialNumber() != null ? device.getSerialNumber() : BLANK);

            // Imei
            cols.add(device.getImei() != null ? device.getImei() : BLANK);

            // Imsi
            cols.add(device.getImsi() != null ? device.getImsi() : BLANK);

            // Iccid
            cols.add(device.getIccid() != null ? device.getIccid() : BLANK);

            // Model Id
            cols.add(device.getModelId() != null ? device.getModelId() : BLANK);

            // Bios version
            cols.add(device.getBiosVersion() != null ? device.getBiosVersion() : BLANK);

            // Firmware version
            cols.add(device.getFirmwareVersion() != null ? device.getFirmwareVersion() : BLANK);

            // OS version
            cols.add(device.getOsVersion() != null ? device.getOsVersion() : BLANK);

            // JVM version
            cols.add(device.getJvmVersion() != null ? device.getJvmVersion() : BLANK);

            // OSGi framework version
            cols.add(device.getOsgiFrameworkVersion() != null ? device.getOsgiFrameworkVersion() : BLANK);

            // Application framework version
            cols.add(device.getApplicationFrameworkVersion() != null ? device.getApplicationFrameworkVersion() : BLANK);

            // Application identifiers
            cols.add(device.getApplicationIdentifiers() != null ? device.getApplicationIdentifiers() : BLANK);

            // Accept encoding
            cols.add(device.getAcceptEncoding() != null ? device.getAcceptEncoding() : BLANK);

            // Custom attribute 1
            cols.add(device.getCustomAttribute1() != null ? device.getCustomAttribute1() : BLANK);

            // Custom attribute 2
            cols.add(device.getCustomAttribute2() != null ? device.getCustomAttribute2() : BLANK);

            // Custom attribute 3
            cols.add(device.getCustomAttribute3() != null ? device.getCustomAttribute3() : BLANK);

            // Custom attribute 4
            cols.add(device.getCustomAttribute4() != null ? device.getCustomAttribute4() : BLANK);

            // Custom attribute 5
            cols.add(device.getCustomAttribute5() != null ? device.getCustomAttribute5() : BLANK);

            writer.writeNext(cols.toArray(new String[] {}));
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(account, "UTF-8") + "_devices.csv");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        writer.flush();

        writer.close();
    }
}
