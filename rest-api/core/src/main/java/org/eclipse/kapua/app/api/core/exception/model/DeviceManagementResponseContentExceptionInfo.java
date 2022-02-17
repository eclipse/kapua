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

import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseContentException;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "deviceManagementResponseContentExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManagementResponseContentExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "responsePayload")
    private KapuaResponsePayload responsePayload;

    protected DeviceManagementResponseContentExceptionInfo() {
        // Required by JAXB
    }

    public DeviceManagementResponseContentExceptionInfo(DeviceManagementResponseContentException deviceManagementResponseContentException) {
        super(Status.INTERNAL_SERVER_ERROR, deviceManagementResponseContentException.getCode(), deviceManagementResponseContentException);

        this.responsePayload = deviceManagementResponseContentException.getResponsePayload();
    }

    public KapuaResponsePayload getResponsePayload() {
        return responsePayload;
    }
}
