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
package org.eclipse.kapua.service.device.call.message;

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.Payload;

/**
 * Device payload definition.
 * 
 * @since 1.0
 *
 */
public interface DevicePayload extends Payload {

    /**
     * Get the message timestamp
     * 
     * @return
     */
    public Date getTimestamp();

    /**
     * Get the device position
     * 
     * @return
     */
    public DevicePosition getPosition();

    /**
     * Get the metrics (if defined)
     * 
     * @return
     */
    public Map<String, Object> getMetrics();

    /**
     * Get the message body (if defined)
     * 
     * @return
     */
    public byte[] getBody();

    void setTimestamp(Date timestamp);

    void setPosition(DevicePosition position);

    void setMetrics(Map<String, Object> metrics);

    void setBody(byte[] body);

    //
    // Encode/Decode stuff
    //
    /**
     * Convert the message to a well formed byte array
     * 
     * @return
     */
    public byte[] toByteArray();

    /**
     * Read and instantiate a message from well formed byte array
     * 
     * @param rawPayload
     * @throws KapuaException
     */
    public void readFromByteArray(byte[] rawPayload)
            throws KapuaException;

}
