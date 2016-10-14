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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "DeviceConnection")
@Table(name = "dvc_device_connection")
public class DeviceConnectionImpl extends AbstractKapuaUpdatableEntity implements DeviceConnection
{
    private static final long      serialVersionUID = 8928343233144731836L;

    @XmlElement(name = "connectionStatus")
    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false)
    private DeviceConnectionStatus connectionStatus;

    @XmlElement(name = "clientId")
    @Basic
    @Column(name = "client_id", nullable = false, updatable = false)
    private String                 clientId;

    @XmlElement(name = "userId")
    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "user_id", nullable = false))
    })
    private KapuaEid               userId;

    @XmlElement(name = "protocol")
    @Basic
    @Column(name = "protocol", nullable = false)
    private String                 protocol;

    @XmlElement(name = "clientIp")
    @Basic
    @Column(name = "client_ip")
    private String                 clientIp;

    @XmlElement(name = "serverIp")
    @Basic
    @Column(name = "server_ip")
    private String                 serverIp;

    protected DeviceConnectionImpl()
    {
        super();
    }

    public DeviceConnectionImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    public DeviceConnectionStatus getStatus()
    {
        return connectionStatus;
    }

    public void setStatus(DeviceConnectionStatus connectionStatus)
    {
        this.connectionStatus = connectionStatus;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public KapuaId getUserId()
    {
        return userId;
    }

    public void setUserId(KapuaId userId)
    {
        this.userId = (KapuaEid) userId;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }
}
