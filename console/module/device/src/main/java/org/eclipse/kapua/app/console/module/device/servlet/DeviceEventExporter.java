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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

public abstract class DeviceEventExporter {

    protected static final String BLANK = "";

    protected static final String[] DEVICE_PROPERTIES = {
            "Account ID",
            "Account Name",
            "Event ID",
            "Created On",
            "Created By",
            "Device ID",
            "Received On",
            "Sent On",
            "Position Longitude",
            "Position Latitude",
            "Position Altitude",
            "Position Precision",
            "Position Heading",
            "Position Speed",
            "Position Timestamp",
            "Position Satellites",
            "Position Status",
            "Resource",
            "Action",
            "Response Code",
            "Event Message"
    };

    protected HttpServletResponse response;

    protected DeviceEventExporter(HttpServletResponse response) {
        this.response = response;
    }

    public abstract void init(String account)
            throws ServletException, IOException;

    public abstract void append(KapuaListResult<DeviceEvent> messages)
            throws ServletException, IOException;

    public abstract void close()
            throws ServletException, IOException;
}
