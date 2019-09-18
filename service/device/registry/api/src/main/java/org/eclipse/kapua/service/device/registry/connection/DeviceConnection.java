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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Device connection entity definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceConnection")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "status",
        "clientId",
        "userId",
        "allowUserChange",
        "userCouplingMode",
        "reservedUserId",
        "protocol",
        "clientIp",
        "serverIp" }, //
        factoryClass = DeviceConnectionXmlRegistry.class, //
        factoryMethod = "newDeviceConnection")
public interface DeviceConnection extends KapuaUpdatableEntity {

    String TYPE = "deviceConnection";

    @Override
    default String getType() {
        return TYPE;
    }

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
    @XmlElement(name = "clientId")
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
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Set the user identifier
     *
     * @param userId
     */
    void setUserId(KapuaId userId);

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
     * Get the device connection user coupling mode.
     *
     * @return
     */
    @XmlElement(name = "userCouplingMode")
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
    @XmlElement(name = "reservedUserId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getReservedUserId();

    /**
     * Set the reserved user identifier
     *
     * @param reservedUserId
     */
    void setReservedUserId(KapuaId reservedUserId);

    /**
     * Get the device protocol
     *
     * @return
     */
    @XmlElement(name = "protocol")
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
    @XmlElement(name = "clientIp")
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
    @XmlElement(name = "serverIp")
    String getServerIp();

    /**
     * Set the server ip
     *
     * @param serverIp
     */
    void setServerIp(String serverIp);
}
