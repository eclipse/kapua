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
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;

import java.util.Date;

/**
 * Device event creator service definition.
 *
 * @since 1.0
 */
public interface DeviceEventCreator extends KapuaEntityCreator<DeviceEvent> {

    /**
     * Get the device identifier
     *
     * @return
     */
    public KapuaId getDeviceId();

    /**
     * Set the device identifier
     *
     * @param deviceId
     */
    public void setDeviceId(KapuaId deviceId);

    /**
     * Get the sent on date
     *
     * @return
     */
    public Date getSentOn();

    /**
     * Set the sent on date
     *
     * @param sentOn
     */
    public void setSentOn(Date sentOn);

    /**
     * Get the received on date
     *
     * @return
     */
    public Date getReceivedOn();

    /**
     * Set the received on date
     *
     * @param receivedOn
     */
    public void setReceivedOn(Date receivedOn);

    /**
     * Get device position
     *
     * @return
     */
    public KapuaPosition getPosition();

    /**
     * Set device position
     *
     * @param position
     */
    public void setPosition(KapuaPosition position);

    /**
     * Get resource
     *
     * @return
     */
    public String getResource();

    /**
     * Set resource
     *
     * @param resource
     */
    public void setResource(String resource);

    /**
     * GHet action
     *
     * @return
     */
    public KapuaMethod getAction();

    /**
     * Set action
     *
     * @param action
     */
    public void setAction(KapuaMethod action);

    /**
     * Get response code
     *
     * @return
     */
    public KapuaResponseCode getResponseCode();

    /**
     * Set response code
     *
     * @param responseCode
     */
    public void setResponseCode(KapuaResponseCode responseCode);

    /**
     * Get event message
     *
     * @return
     */
    public String getEventMessage();

    /**
     * Set event message
     *
     * @param eventMessage
     */
    public void setEventMessage(String eventMessage);
}
