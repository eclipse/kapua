/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.UUID;

/**
 * {@link KapuaMessage} definition.
 *
 * @param <C> channel type
 * @param <P> payload type
 * @since 1.0.0
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
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newKapuaMessage")
@XmlSeeAlso(KapuaDataMessage.class)
public interface KapuaMessage<C extends KapuaChannel, P extends KapuaPayload> extends Message<C, P> {

    /**
     * Get the message identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    UUID getId();

    /**
     * Set the message identifier
     *
     * @param id
     * @since 1.0.0
     */
    void setId(UUID id);

    /**
     * Get scope identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getScopeId();

    /**
     * Set scope identifier
     *
     * @param scopeId
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Get client identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    String getClientId();

    /**
     * Set client identifier
     *
     * @param clientId
     * @since 1.0.0
     */
    void setClientId(String clientId);

    /**
     * Get device identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getDeviceId();

    /**
     * Set device identifier
     *
     * @param deviceId
     * @since 1.0.0
     */
    void setDeviceId(KapuaId deviceId);

    /**
     * Get the message received on date
     *
     * @return
     */
    @XmlElement(name = "receivedOn")
    Date getReceivedOn();

    /**
     * Set the message received on date
     *
     * @param receivedOn
     * @since 1.0.0
     */
    void setReceivedOn(Date receivedOn);

    /**
     * Get the message sent on date
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "sentOn")
    Date getSentOn();

    /**
     * Set the message sent on date
     *
     * @param sentOn
     * @since 1.0.0
     */
    void setSentOn(Date sentOn);

    /**
     * Get the message captured on date
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "capturedOn")
    Date getCapturedOn();

    /**
     * Set the message captured on date
     *
     * @param capturedOn
     * @since 1.0.0
     */
    void setCapturedOn(Date capturedOn);

    /**
     * Get the device position
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "position")
    KapuaPosition getPosition();

    /**
     * Set the device position
     *
     * @param position
     * @since 1.0.0
     */
    void setPosition(KapuaPosition position);

    /**
     * Get the message channel
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "channel")
    C getChannel();

    /**
     * Set the message channel
     *
     * @param semanticChannel
     * @since 1.0.0
     */
    void setChannel(C semanticChannel);

    /**
     * Get the message payload
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "payload")
    P getPayload();

    /**
     * Set the message payload
     *
     * @param payload
     * @since 1.0.0
     */
    void setPayload(P payload);

}
