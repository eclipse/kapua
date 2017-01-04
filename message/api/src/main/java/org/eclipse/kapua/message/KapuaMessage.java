/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.message;

import java.util.Date;
import java.util.UUID;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua message object definition.
 *
 * @param <C> channel type
 * @param <P> payload type
 * 
 * @since 1.0
 * 
 */
public interface KapuaMessage<C extends KapuaChannel, P extends KapuaPayload> extends Message<C, P>
{

    /**
     * Get client identifier
     * 
     * @return
     */
    public String getClientId();

    /**
     * Set client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Get the message identifier
     * 
     * @return
     */
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
    public KapuaId getScopeId();

    /**
     * Set scope identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId);

    /**
     * Get device identifier
     * 
     * @return
     */
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
    public KapuaPosition getPosition();

    /**
     * Set the device position
     * 
     * @param position
     */
    public void setPosition(KapuaPosition position);

    /**
     * Get the message channel
     * 
     * @return
     */
    public C getChannel();

    /**
     * Set the message channel
     * 
     * @param semanticChannel
     */
    public void setChannel(C semanticChannel);

    /**
     * Get the message payload
     * 
     * @return
     */
    public P getPayload();

    /**
     * Set the message payload
     * 
     * @param payload
     */
    public void setPayload(P payload);

}
