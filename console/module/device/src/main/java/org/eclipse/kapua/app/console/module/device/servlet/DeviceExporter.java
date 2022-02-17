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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.Device;

public abstract class DeviceExporter {

    protected static final String BLANK = "";

    protected static final String[] DEVICE_PROPERTIES = {
            "Account Id",
            "Account Name",
            "Client ID",
            "Status",
            "Connection Status",
            "Created On (UTC)",
            // "Created By",
            "Last Event On (UTC)",
            "Last Event Type",
            "Connection IP",
            // "MQTT connection IP",
            "Display Name",
            "Serial Number",
            "IMEI",
            "IMSI",
            "ICCID",
            "Model ID",
            "Model Name",
            "Bios Version",
            "Firmware Version",
            "OS Version",
            "JVM Version",
            "OSGi Version",
            "ESF/Kura Version",
            "Application Identifiers",
            "Accept Encoding",
            // "GPS Longitude",
            // "GPS Latitude",
            "Custom Attribute 1",
            "Custom Attribute 2",
            "Custom Attribute 3",
            "Custom Attribute 4",
            "Custom Attribute 5",
            // "Certificate Id"
            // "Optlock"
    };

    protected HttpServletResponse response;

    protected DeviceExporter(HttpServletResponse response) {
        this.response = response;
    }

    public abstract void init(String account, String accountName)
            throws ServletException, IOException;

    public abstract void append(KapuaListResult<Device> messages)
            throws ServletException, IOException, KapuaException;

    public abstract void close()
            throws ServletException, IOException;
}
