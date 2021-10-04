/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected DeviceManagementResponseContentExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param deviceManagementResponseContentException The root exception.
     * @since 1.0.0
     */
    public DeviceManagementResponseContentExceptionInfo(DeviceManagementResponseContentException deviceManagementResponseContentException) {
        super(Status.INTERNAL_SERVER_ERROR, deviceManagementResponseContentException);

        this.responsePayload = deviceManagementResponseContentException.getResponsePayload();
    }

    /**
     * Gets the {@link DeviceManagementResponseContentException#getResponsePayload()}.
     *
     * @return The {@link DeviceManagementResponseContentException#getResponsePayload()}.
     * @since 1.0.0
     */
    public KapuaResponsePayload getResponsePayload() {
        return responsePayload;
    }
}
