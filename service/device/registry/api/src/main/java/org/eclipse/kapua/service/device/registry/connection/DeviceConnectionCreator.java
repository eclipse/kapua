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
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;

import javax.xml.bind.annotation.XmlElement;

/**
 * Device connection creator service definition.
 *
 * @since 1.0
 */
public interface DeviceConnectionCreator extends KapuaUpdatableEntityCreator<DeviceConnection> {

    /**
     * Get the device connection status
     *
     * @return
     */
    @XmlElement(name = "status")
    DeviceConnectionStatus getStatus();

    /**
     * Set the device connection status
     *
     * @param status
     */
    void setStatus(DeviceConnectionStatus status);

    /**
     * Get the client identifier
     *
     * @return
     */
    String getClientId();

    /**
     * Set the client identifier
     *
     * @param clientId
     */
    void setClientId(String clientId);

    /**
     * Get the user identifier
     *
     * @return
     */
    KapuaId getUserId();

    /**
     * Set the user identifier
     *
     * @param userId
     */
    void setUserId(KapuaId userId);

    /**
     * Get the device connection user coupling mode.
     *
     * @return
     */
    ConnectionUserCouplingMode getUserCouplingMode();

    /**
     * Set the device connection user coupling mode.
     *
     * @param userCouplingMode
     */
    void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode);

    /**
     * Get the reserved user identifier
     *
     * @return
     */
    KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    void setReservedUserId(KapuaId reservedUserId);

    /**
     * Gets whether or not the {@link DeviceConnection} can change user on the next login.
     *
     * @return <code>true</code> if device can changhe user to connect, <code>false</code> if not.
     */
    @XmlElement(name = "allowUserChange")
    boolean getAllowUserChange();

    /**
     * Sets whether or not the {@link DeviceConnection} can change user on the next login.
     *
     * @param allowUserChange
     */
    void setAllowUserChange(boolean allowUserChange);

    /**
     * Get the device protocol
     *
     * @return
     */
    String getProtocol();

    /**
     * Set the device protocol
     *
     * @param protocol
     */
    void setProtocol(String protocol);

    /**
     * Get the client ip
     *
     * @return
     */
    String getClientIp();

    /**
     * Set the client ip
     *
     * @param clientIp
     */
    void setClientIp(String clientIp);

    /**
     * Get the server ip
     *
     * @return
     */
    String getServerIp();

    /**
     * Set the server ip
     *
     * @param serverIp
     */
    void setServerIp(String serverIp);
}
