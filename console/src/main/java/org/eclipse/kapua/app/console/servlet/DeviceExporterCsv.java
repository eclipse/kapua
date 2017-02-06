/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.servlet;

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

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.Device;

import com.opencsv.CSVWriter;

public class DeviceExporterCsv extends DeviceExporter {

    private String m_account;
    private DateFormat m_dateFormat;
    private CSVWriter m_writer;

    public DeviceExporterCsv(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(String account)
            throws ServletException, IOException {
        m_account = account;
        m_dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        OutputStreamWriter osw = new OutputStreamWriter(m_response.getOutputStream(), Charset.forName("UTF-8"));
        m_writer = new CSVWriter(osw);

        List<String> cols = new ArrayList<String>();
        for (String property : s_deviceProperties) {
            cols.add(property);
        }
        m_writer.writeNext(cols.toArray(new String[] {}));
    }

    @Override
    public void append(KapuaListResult<Device> devices)
            throws ServletException, IOException {
        for (Device device : devices.getItems()) {

            List<String> cols = new ArrayList<String>();
            cols.add(m_account);

            if (device.getClientId() != null) {
                cols.add(device.getClientId());
            } else {
                cols.add("");
            }

            if (device.getStatus() != null) {
                cols.add(device.getStatus().name());
            } else {
                cols.add("");
            }

            cols.add("");

            if (device.getCreatedOn() != null) {
                cols.add(m_dateFormat.format(device.getCreatedOn()));
            } else {
                cols.add("");
            }

            if (device.getLastEvent() != null) {
                cols.add(m_dateFormat.format(device.getLastEvent().getReceivedOn()));
            } else {
                cols.add("");
            }

            if (device.getLastEvent() != null) {
                cols.add(device.getLastEvent().getType());
            } else {
                cols.add("");
            }

            cols.add("");

            if (device.getDisplayName() != null) {
                cols.add(device.getDisplayName());
            } else {
                cols.add("");
            }

            if (device.getSerialNumber() != null) {
                cols.add(device.getSerialNumber());
            } else {
                cols.add("");
            }

            if (device.getImei() != null) {
                cols.add(device.getImei());
            } else {
                cols.add("");
            }

            if (device.getImsi() != null) {
                cols.add(device.getImsi());
            } else {
                cols.add("");
            }

            if (device.getIccid() != null) {
                cols.add(device.getIccid());
            } else {
                cols.add("");
            }

            if (device.getModelId() != null) {
                cols.add(device.getModelId());
            } else {
                cols.add("");
            }

            if (device.getBiosVersion() != null) {
                cols.add(device.getBiosVersion());
            } else {
                cols.add("");
            }

            if (device.getFirmwareVersion() != null) {
                cols.add(device.getFirmwareVersion());
            } else {
                cols.add("");
            }

            if (device.getOsVersion() != null) {
                cols.add(device.getOsVersion());
            } else {
                cols.add("");
            }

            if (device.getJvmVersion() != null) {
                cols.add(device.getJvmVersion());
            } else {
                cols.add("");
            }

            cols.add("");

            cols.add("");

            if (device.getApplicationIdentifiers() != null) {
                cols.add(device.getApplicationIdentifiers());
            } else {
                cols.add("");
            }

            if (device.getAcceptEncoding() != null) {
                cols.add(device.getAcceptEncoding());
            } else {
                cols.add("");
            }

            if (device.getCustomAttribute1() != null) {
                cols.add(device.getCustomAttribute1());
            } else {
                cols.add("");
            }

            if (device.getCustomAttribute2() != null) {
                cols.add(device.getCustomAttribute2());
            } else {
                cols.add("");
            }

            if (device.getCustomAttribute3() != null) {
                cols.add(device.getCustomAttribute3());
            } else {
                cols.add("");
            }

            if (device.getCustomAttribute4() != null) {
                cols.add(device.getCustomAttribute4());
            } else {
                cols.add("");
            }

            if (device.getCustomAttribute5() != null) {
                cols.add(device.getCustomAttribute5());
            } else {
                cols.add("");
            }

            cols.add("");

            m_writer.writeNext(cols.toArray(new String[] {}));
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        m_response.setContentType("text/csv");
        m_response.setCharacterEncoding("UTF-8");
        m_response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(m_account, "UTF-8") + "_devices.csv");
        m_response.setHeader("Cache-Control", "no-transform, max-age=0");

        m_writer.flush();

        m_writer.close();
    }
}
