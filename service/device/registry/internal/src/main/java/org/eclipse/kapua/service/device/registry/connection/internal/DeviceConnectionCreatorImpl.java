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

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;

/**
 * Device connection creator service implementation.
 *
 * @since 1.0
 */
public class DeviceConnectionCreatorImpl extends AbstractKapuaEntityCreator<DeviceConnection> implements DeviceConnectionCreator {

    private static final long serialVersionUID = 2740394157765904615L;

    @XmlElement(name = "clientId")
    private String clientId;

    @XmlElement(name = "userId")
    private KapuaId userId;

    @XmlElement(name = "protocol")
    private String protocol;

    @XmlElement(name = "clientIp")
    private String clientIp;

    @XmlElement(name = "serverIp")
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
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

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
