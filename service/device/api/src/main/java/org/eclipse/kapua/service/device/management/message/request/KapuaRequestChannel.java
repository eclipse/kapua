/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.request;

import org.eclipse.kapua.service.device.management.message.KapuaAppChannel;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.request.xml.RequestMessageXmlRegistry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link KapuaRequestMessage} {@link KapuaAppChannel} definition.
 * <p>
 * This object defines the channel for a Kapua request message.
 * The request message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "channel")
@XmlType(factoryClass = RequestMessageXmlRegistry.class, factoryMethod = "newRequestChannel")
public interface KapuaRequestChannel extends KapuaAppChannel {

    /**
     * Gets the {@link KapuaMethod}
     *
     * @return The {@link KapuaMethod}
     * @since 1.0.0
     */
    @XmlElement(name = "method")
    KapuaMethod getMethod();

    /**
     * Sets the {@link KapuaMethod}
     *
     * @param method The {@link KapuaMethod}
     * @since 1.0.0
     */
    void setMethod(KapuaMethod method);

    /**
     * Gets the requested resource.
     *
     * @return The requested resource.
     * @since 1.5.0
     */
    @XmlElement(name = "resource")
    String getResource();

    /**
     * Sets the requested resource.
     *
     * @param resource The requested resource.
     * @since 1.5.0
     */
    void setResource(String resource);
}
