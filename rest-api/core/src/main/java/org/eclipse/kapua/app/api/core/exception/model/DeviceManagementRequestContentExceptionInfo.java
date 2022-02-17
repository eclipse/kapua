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

import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestContentException;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "deviceManagementRequestContentExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManagementRequestContentExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "requestContent")
    private Object requestContent;

    protected DeviceManagementRequestContentExceptionInfo() {
        // Required by JAXB
    }

    public DeviceManagementRequestContentExceptionInfo(DeviceManagementRequestContentException deviceManagementRequestContentException) {
        super(Response.Status.INTERNAL_SERVER_ERROR, deviceManagementRequestContentException.getCode(), deviceManagementRequestContentException);

        this.requestContent = deviceManagementRequestContentException.getRequestContent();
    }

    /**
     * Gets the request payload which was not serializable.
     *
     * @return The request payload which was not serializable.
     * @since 1.5.0
     */
    public Object getRequestPayload() {
        return requestContent;
    }
}
