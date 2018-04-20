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
package org.eclipse.kapua.service.device.registry.connection.internal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * {@link DeviceConnection} entity.
 * 
 * @since 1.0.0
 */

@Entity(name = "DeviceConnection")
@Table(name = "dvc_device_connection")
public class DeviceConnectionImpl extends AbstractKapuaUpdatableEntity implements DeviceConnection {

    private static final long serialVersionUID = 8928343233144731836L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    private DeviceConnectionStatus status;

    @Basic
    @Column(name = "client_id", nullable = false, updatable = false)
    private String clientId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id", nullable = false, updatable = true))
    })
    private KapuaEid userId;

    @Basic
    @Column(name = "allow_user_change", nullable = false, updatable = true)
    private boolean allowUserChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_coupling_mode", nullable = false)
    private ConnectionUserCouplingMode userCouplingMode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "reserved_user_id", nullable = true, updatable = true))
    })
    private KapuaEid reservedUserId;

    @Basic
    @Column(name = "protocol", nullable = false)
    private String protocol;

    @Basic
    @Column(name = "client_ip")
    private String clientIp;

    @Basic
    @Column(name = "server_ip")
    private String serverIp;

    /**
     * Constructor
     */
    protected DeviceConnectionImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public DeviceConnectionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public DeviceConnectionImpl(DeviceConnection deviceConnection) throws KapuaException {
        super((KapuaUpdatableEntity) deviceConnection);

        setStatus(deviceConnection.getStatus());
        setClientId(deviceConnection.getClientId());
        setUserId(deviceConnection.getUserId());
        setAllowUserChange(deviceConnection.getAllowUserChange());
        setUserCouplingMode(deviceConnection.getUserCouplingMode());
        setReservedUserId(deviceConnection.getReservedUserId());
        setProtocol(deviceConnection.getProtocol());
        setClientIp(deviceConnection.getClientIp());
        setServerIp(deviceConnection.getServerIp());
    }

    @Override
    public DeviceConnectionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DeviceConnectionStatus connectionStatus) {
        this.status = connectionStatus;
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
        this.userId = KapuaEid.parseKapuaId(userId);
    }

    public boolean getAllowUserChange() {
        return allowUserChange;
    }

    public void setAllowUserChange(boolean allowUserChange) {
        this.allowUserChange = allowUserChange;
    }

    public ConnectionUserCouplingMode getUserCouplingMode() {
        return userCouplingMode;
    }

    public void setUserCouplingMode(ConnectionUserCouplingMode userCouplingMode) {
        this.userCouplingMode = userCouplingMode;
    }

    public KapuaId getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(KapuaId reservedUserId) {
        this.reservedUserId = KapuaEid.parseKapuaId(reservedUserId);
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
