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

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
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
    @Column(name = "connection_status", nullable = false)
    private DeviceConnectionStatus connectionStatus;

    @Basic
    @Column(name = "client_id", nullable = false, updatable = false)
    private String clientId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "credential_id", nullable = false))
    })
    private KapuaEid credentialId;

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
        if (credentialId != null) {
            this.credentialId = new KapuaEid(credentialId);
        } else {
            this.credentialId = null;
        }
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

    /**
     * This methods needs override because {@link DeviceConnection}s can be created from the broker plugin.
     */
    @Override
    protected void prePersistsAction()
            throws KapuaException {
        if (KapuaSecurityUtils.getSession().getSubject().getId() != null) {
            super.prePersistsAction();
        } else {
            this.id = new KapuaEid(IdGenerator.generate());

            this.createdBy = KapuaEid.ONE;
            this.createdOn = new Date();

            this.modifiedBy = this.createdBy;
            this.modifiedOn = this.createdOn;
        }
    }

    /**
     * This methods needs override because {@link DeviceConnection}s can be created from the broker plugin.
     */
    @PreUpdate
    protected void preUpdateAction()
            throws KapuaException {
        if (KapuaSecurityUtils.getSession().getSubject().getId() != null) {
            super.preUpdateAction();
        } else {
            this.modifiedBy = KapuaEid.ONE;
            this.modifiedOn = new Date();
        }
    }
}
