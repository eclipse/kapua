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
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Date;
import java.util.UUID;

/**
 * {@link KapuaMessage} provides an abstraction over the {@link org.eclipse.kapua.message.Message}s sent in and out of the Kapua platform.
 * <p>
 * It encapsulates all the information regarding the message: the topic it was addressed to, the timestamp
 * when it was received by the platform, and the payload contained in the message.
 * <p>
 * The payload can be represented by a raw binary array or by an {@link KapuaPayload} object if it was formatted
 * as such when the message was composed and sent. Refer to the {@link KapuaPayload} documentation for more details on
 * how {@link KapuaPayload} are modelled and how they can be constructed.
 * <p>
 * The {@link KapuaMessage} class is used both by the messages/search API to return message results from the platform,
 * as well as by messages/store and messages/publish API to send messages to the platform.
 *
 * @since 1.0.0
 */
public class KapuaMessageImpl<C extends KapuaChannel, P extends KapuaPayload> implements Comparable<KapuaMessageImpl<C, P>>, KapuaMessage<C, P> {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private KapuaId scopeId;
    private KapuaId deviceId;
    private String clientId;

    private Date receivedOn;
    private Date sentOn;
    private Date capturedOn;

    private KapuaPosition position;

    private C channel;

    private P payload;

    /**
     * Constructor
     */
    public KapuaMessageImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param channel The {@link KapuaChannel} of the {@link KapuaMessage}
     * @param payload The {@link KapuaPayload} of the {@link KapuaMessage}
     * @since 1.0.0
     */
    public KapuaMessageImpl(C channel, P payload) {
        this();

        this.channel = channel;
        this.payload = payload;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public KapuaId getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Date getReceivedOn() {
        return receivedOn;
    }

    @Override
    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    @Override
    public Date getSentOn() {
        return sentOn;
    }

    @Override
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    @Override
    public Date getCapturedOn() {
        return capturedOn;
    }

    @Override
    public void setCapturedOn(Date capturedOn) {
        this.capturedOn = capturedOn;
    }

    @Override
    public KapuaPosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(KapuaPosition position) {
        this.position = position;
    }

    @Override
    public C getChannel() {
        return channel;
    }

    @Override
    public void setChannel(C channel) {
        this.channel = channel;
    }

    @Override
    public P getPayload() {
        return payload;
    }

    @Override
    public void setPayload(P payload) {
        this.payload = payload;
    }

    @Override
    public int compareTo(KapuaMessageImpl<C, P> msg) {
        return (receivedOn.compareTo(msg.getReceivedOn()) * (-1));
    }

}
