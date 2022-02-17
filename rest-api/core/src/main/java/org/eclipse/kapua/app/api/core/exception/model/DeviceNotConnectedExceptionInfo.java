/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.management.exception.DeviceNotConnectedException;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "deviceNotConnectedExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceNotConnectedExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId deviceId;

    @XmlElement(name = "connectionStatus")
    private DeviceConnectionStatus connectionStatus;

    protected DeviceNotConnectedExceptionInfo() {
        // Required by JAXB
    }

    public DeviceNotConnectedExceptionInfo(DeviceNotConnectedException deviceNotConnectedException) {
        super(Response.Status.INTERNAL_SERVER_ERROR, deviceNotConnectedException.getCode(), deviceNotConnectedException);

        this.deviceId = deviceNotConnectedException.getDeviceId();
        this.connectionStatus = deviceNotConnectedException.getCurrentConnectionStatus();
    }

    public KapuaId getDeviceId() {
        return deviceId;
    }

    public DeviceConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
}
