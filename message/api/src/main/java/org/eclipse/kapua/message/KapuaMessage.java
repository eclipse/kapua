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
     * Gets the unique identifier.
     *
     * @return The unique identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    UUID getId();

    /**
     * Sets the unique identifier.
     *
     * @param id The unique identifier.
     * @since 1.0.0
     */
    void setId(UUID id);

    /**
     * Gets the scope {@link KapuaId}
     *
     * @return The scope {@link KapuaId}
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Gets the device client identifier
     *
     * @return The device client identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    String getClientId();

    /**
     * Sets the device client identifier.
     *
     * @param clientId The device client identifier.
     * @since 1.0.0
     */
    void setClientId(String clientId);

    /**
     * Gets the device {@link KapuaId}.
     *
     * @return The device {@link KapuaId}.
     * @since 1.0.0
     */
    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getDeviceId();

    /**
     * Sets the device {@link KapuaId}.
     *
     * @param deviceId The device {@link KapuaId}.
     * @since 1.0.0
     */
    void setDeviceId(KapuaId deviceId);

    /**
     * Gets the received on {@link Date}.
     *
     * @return The received on {@link Date}.
     * @since 1.0.0
     */
    @XmlElement(name = "receivedOn")
    Date getReceivedOn();

    /**
     * Sets the received on {@link Date}.
     *
     * @param receivedOn The received on {@link Date}.
     * @since 1.0.0
     */
    void setReceivedOn(Date receivedOn);

    /**
     * Gets the sent on {@link Date}.
     *
     * @return The sent on {@link Date}.
     * @since 1.0.0
     */
    @XmlElement(name = "sentOn")
    Date getSentOn();

    /**
     * Sets the sent on {@link Date}.
     *
     * @param sentOn The sent on {@link Date}.
     * @since 1.0.0
     */
    void setSentOn(Date sentOn);

    /**
     * Gets the captured on {@link Date}.
     *
     * @return The captured on {@link Date}.
     * @since 1.0.0
     */
    @XmlElement(name = "capturedOn")
    Date getCapturedOn();

    /**
     * Sets the captured on {@link Date}.
     *
     * @param capturedOn The captured on {@link Date}.
     * @since 1.0.0
     */
    void setCapturedOn(Date capturedOn);

    /**
     * Gets the device {@link KapuaPosition}.
     *
     * @return The device {@link KapuaPosition}.
     * @since 1.0.0
     */
    @XmlElement(name = "position")
    KapuaPosition getPosition();

    /**
     * Sets the device {@link KapuaPosition}.
     *
     * @param position The device {@link KapuaPosition}.
     * @since 1.0.0
     */
    void setPosition(KapuaPosition position);

    /**
     * Gets the {@link KapuaChannel}.
     *
     * @return The {@link KapuaChannel}.
     * @since 1.0.0
     */
    @XmlElement(name = "channel")
    C getChannel();

    /**
     * Sets the {@link KapuaChannel}.
     *
     * @param semanticChannel The {@link KapuaChannel}.
     * @since 1.0.0
     */
    void setChannel(C semanticChannel);

    /**
     * Gets the {@link KapuaPayload}.
     *
     * @return The {@link KapuaPayload}.
     * @since 1.0.0
     */
    @XmlElement(name = "payload")
    P getPayload();

    /**
     * Sets the {@link KapuaPayload}.
     *
     * @param payload The {@link KapuaPayload}.
     * @since 1.0.0
     */
    void setPayload(P payload);

}
