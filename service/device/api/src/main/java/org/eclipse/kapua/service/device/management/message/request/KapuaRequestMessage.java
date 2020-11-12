/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message.request;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.management.message.request.xml.RequestMessageXmlRegistry;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Kapua request message definition.<br>
 * This object defines the for a Kapua request message.<br>
 * The request message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 *
 * @since 1.0
 */

@XmlType(factoryClass = RequestMessageXmlRegistry.class, factoryMethod = "newRequestMessage")
public interface KapuaRequestMessage<C extends KapuaRequestChannel, P extends KapuaRequestPayload> extends KapuaMessage<C, P> {

    /**
     * Get the request message class type
     *
     * @return
     */
    @XmlTransient
    <M extends KapuaRequestMessage<C, P>> Class<M> getRequestClass();

    /**
     * Get the response message class type
     *
     * @return
     */
    @XmlTransient
    <RSC extends KapuaResponseChannel, RSP extends KapuaResponsePayload, M extends KapuaResponseMessage<RSC, RSP>> Class<M> getResponseClass();

}
