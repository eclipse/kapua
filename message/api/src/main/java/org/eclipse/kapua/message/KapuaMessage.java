/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * Kapua message object definition.
 *
 * @param <C> channel type
 * @param <P> payload type
 * @since 1.0
 */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "id", //
        "scopeId", //
        "deviceId", //
        "clientId", //
        "receivedOn", //
        "sentOn", //
        "capturedOn", //
        "position", //
        "channel", //
        "payload", //
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newKapuaMessage") //
@XmlSeeAlso(KapuaDataMessage.class)
public interface KapuaMessage<C extends KapuaChannel, P extends KapuaPayload> extends Message<C, P> {

    /**
     * Get the message identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    public UUID getId();

    /**
     * Set the message identifier
     *
     * @param id
     */
    public void setId(UUID id);

    /**
     * Get scope identifier
     *
     * @return
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getScopeId();

    /**
     * Set scope identifier
     *
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId);

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
     * Get device identifier
     *
     * @return
     */
    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getDeviceId();

    /**
     * Set device identifier
     *
     * @param deviceId
     */
    public void setDeviceId(KapuaId deviceId);

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
     * Get the message captured on date
     *
     * @return
     */
    @XmlElement(name = "capturedOn")
    public Date getCapturedOn();

    /**
     * Set the message captured on date
     *
     * @param capturedOn
     */
    public void setCapturedOn(Date capturedOn);

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
