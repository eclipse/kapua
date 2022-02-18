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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * Device connection creator service implementation.
 *
 * @since 1.0
 */
public class DeviceConnectionCreatorImpl extends AbstractKapuaUpdatableEntityCreator<DeviceConnection> implements DeviceConnectionCreator {

    private static final long serialVersionUID = 2740394157765904615L;

    private DeviceConnectionStatus status;
    private String clientId;
    private KapuaId userId;
    private ConnectionUserCouplingMode userCouplingMode;
    private KapuaId reservedUserId;
    private boolean allowUserChange;
    private String protocol;
    private String clientIp;
    private String serverIp;

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceConnectionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public DeviceConnectionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DeviceConnectionStatus status) {
        this.status = status;
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
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public ConnectionUserCouplingMode getUserCouplingMode() {
        return userCouplingMode;
    }

    @Override
    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode) {
        this.userCouplingMode = userCouplingMode;
    }

    @Override
    public KapuaId getReservedUserId() {
        return reservedUserId;
    }

    @Override
    public void setReservedUserId(KapuaId reservedUserId) {
        this.reservedUserId = reservedUserId;
    }

    @Override
    public boolean getAllowUserChange() {
        return allowUserChange;
    }

    @Override
    public void setAllowUserChange(boolean allowUserChange) {
        this.allowUserChange = allowUserChange;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getClientIp() {
        return clientIp;
    }

    @Override
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String getServerIp() {
        return serverIp;
    }

    @Override
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
