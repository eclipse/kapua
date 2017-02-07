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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device connection entity definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceConnection extends KapuaUpdatableEntity
{
    public static final String TYPE = "deviceConnection";

    default public String getType()
    {
        return TYPE;
    }

    /**
     * Get the device connection status
     * 
     * @return
     */
    public DeviceConnectionStatus getStatus();

    /**
     * Set the device connection status
     * 
     * @param status
     */
    public void setStatus(DeviceConnectionStatus status);

    /**
     * Get the client identifier
     * 
     * @return
     */
    public String getClientId();

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Get the user identifier
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Set the user identifier
     * 
     * @param userId
     */
    public void setUserId(KapuaId userId);

    /**
     * Get the device protocol
     * 
     * @return
     */
    public String getProtocol();

    /**
     * Set the device protocol
     * 
     * @param protocol
     */
    public void setProtocol(String protocol);

    /**
     * Get the client ip
     * 
     * @return
     */
    public String getClientIp();

    /**
     * Set the client ip
     * 
     * @param clientIp
     */
    public void setClientIp(String clientIp);

    /**
     * Get the server ip
     * 
     * @return
     */
    public String getServerIp();

    /**
     * Set the server ip
     * 
     * @param serverIp
     */
    public void setServerIp(String serverIp);
}
