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

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * {@link DeviceConnection} implementation.
 * 
 * @since 1.0.0
 *
 */
@Entity(name = "DeviceConnection")
@Table(name = "dvc_device_connection")
public class DeviceConnectionImpl extends AbstractKapuaUpdatableEntity implements DeviceConnection {

    private static final long serialVersionUID = 8928343233144731836L;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false, updatable = true)
    private DeviceConnectionStatus connectionStatus;

    @Basic
    @Column(name = "client_id", nullable = false, updatable = false)
    private String clientId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "credential_id", nullable = true, updatable = true))
    })
    private KapuaEid credentialId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "last_credential_id", nullable = true, updatable = true))
    })
    private KapuaEid lastCredentialId;

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

    @Override
    public DeviceConnectionStatus getStatus() {
        return connectionStatus;
    }

    @Override
    public void setStatus(DeviceConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
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
    public KapuaId getCredentialId() {
        return credentialId;
    }

    @Override
    public void setCredentialId(KapuaId credentialId) {
        this.credentialId = credentialId != null ? (credentialId instanceof KapuaEid ? (KapuaEid) credentialId : new KapuaEid(credentialId)) : null;
    }

    @Override
    public KapuaId getLastCredentialId() {
        return lastCredentialId;
    }

    @Override
    public void setLastCredentialId(KapuaId lastCredentialId) {
        this.lastCredentialId = lastCredentialId != null ? (lastCredentialId instanceof KapuaEid ? (KapuaEid) lastCredentialId : new KapuaEid(lastCredentialId)) : null;
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
