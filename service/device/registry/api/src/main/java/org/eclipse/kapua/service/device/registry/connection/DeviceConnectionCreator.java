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
package org.eclipse.kapua.service.device.registry.connection;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;

/**
 * Device connection creator service definition.
 *
 * @since 1.0
 */
public interface DeviceConnectionCreator extends KapuaUpdatableEntityCreator<DeviceConnection> {

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
     * Get the device connection user coupling mode.
     *
     * @return
     */
    public ConnectionUserCouplingMode getUserCouplingMode();

    /**
     * Set the device connection user coupling mode.
     *
     * @param userCouplingMode
     */
    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode);

    /**
     * Get the reserved user identifier
     *
     * @return
     */
    public KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    public void setReservedUserId(KapuaId reservedUserId);

    /**
     * Gets whether or not the {@link DeviceConnection} can change user on the next login.
     * 
     * @return <code>true</code> if device can changhe user to connect, <code>false</code> if not.
     */
    @XmlElement(name = "allowUserChange")
    public boolean getAllowUserChange();

    /**
     * Sets whether or not the {@link DeviceConnection} can change user on the next login.
     * 
     * @param allowUserChange
     */
    public void setAllowUserChange(boolean allowUserChange);

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
