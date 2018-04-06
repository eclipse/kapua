/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.transport;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;

/**
 * Kapua data message object definition.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "transportMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "scopeName", //
        "clientId", //
        "sentOn", //
        "receivedOn", //
        "position", //
        "channel", //
        "payload" //
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newTransportMessage") 
public interface TransportMessage extends Message<TransportChannel, TransportPayload> {

    /**
     * Get scope identifier
     *
     * @return
     */
    @XmlElement(name = "scopeName")
    public String getScopeName();

    /**
     * Set scope identifier
     *
     * @param scopeName
     */
    public void setScopeName(String scopeName);

    /**
     * Get client identifier
     *
     * @return
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Set client identifier
     *
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Gets the message typeused in the transport message
     * 
     * @return
     */
    public TransportMessageType getMessageType();

    /**
     * Sets the message type used in the transport message
     */
    public void setMessageType(TransportMessageType messageType);

    /**
     * Gets the qos used in the in the transport of the message
     * 
     * @return
     */
    public TransportQos getQoS();

    /**
     * Sets the qos used in the in the transport of the message
     */
    public void setQoS(TransportQos qos);

    /**
     * Get the message sent on date
     *
     * @return
     */
    @XmlElement(name = "sentOn")
    public Date getSentOn();

    /**
     * Set the message sent on date
     *
     * @param sentOn
     */
    public void setSentOn(Date sentOn);

    /**
     * Get the message received on date
     *
     * @return
     */
    @XmlElement(name = "receivedOn")
    public Date getReceivedOn();

    /**
     * Set the message received on date
     *
     * @param receivedOn
     */
    public void setReceivedOn(Date receivedOn);

    /**
     * Get the device position
     *
     * @return
     */
    @XmlElement(name = "position")
    public KapuaPosition getPosition();

    /**
     * Set the device position
     *
     * @param position
     */
    public void setPosition(KapuaPosition position);
}
