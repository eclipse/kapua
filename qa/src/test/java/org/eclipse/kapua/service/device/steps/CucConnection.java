/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * Data object used in Gherkin to input Device Connection parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 */
public class CucConnection {

    KapuaId scopeId = null;
    KapuaId userId = null;
    String clientId = null;
    DeviceConnectionStatus connectionStatus = null;
    String protocol = null;
    String clientIp = null;
    String serverIp = null;

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(long scopeId) {
        this.scopeId = new KapuaEid(BigInteger.valueOf(scopeId));
    }

    public KapuaId getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = new KapuaEid(BigInteger.valueOf(userId));
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public DeviceConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = null;

        switch (connectionStatus.trim().toUpperCase()) {
        case "CONNECTED":
            this.connectionStatus = DeviceConnectionStatus.CONNECTED;
            break;
        case "DISCONNECTED":
            this.connectionStatus = DeviceConnectionStatus.DISCONNECTED;
            break;
        case "MISSING":
            this.connectionStatus = DeviceConnectionStatus.MISSING;
            break;
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
