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

import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;

/**
 * Wrapper around DeviceConnection to make the DeviceConnection class comparable by its attributes.
 * It provides equals() method.
 */
public class CompConnection {

    private final DeviceConnection devConn;

    public CompConnection(DeviceConnection devConn) {
        this.devConn = devConn;
    }

    public DeviceConnection getConnection() {
        return devConn;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompConnection) {
            CompConnection other = (CompConnection) obj;
            return compareAllConnectionAtt(devConn, other.getConnection());
        } else {
            return false;
        }
    }

    private boolean compareAllConnectionAtt(DeviceConnection thisConn, DeviceConnection othConn) {

        if (thisConn.getScopeId() == null != (othConn.getScopeId() == null)) {
            return false;
        }
        if (thisConn.getScopeId() != null && othConn.getScopeId() != null) {
            if (!thisConn.getScopeId().equals(othConn.getScopeId())) {
                return false;
            }
        }

        if (thisConn.getClientId() == null != (othConn.getClientId() == null)) {
            return false;
        }
        if (thisConn.getClientId() != null && othConn.getClientId() != null) {
            if (!thisConn.getClientId().equals(othConn.getClientId())) {
                return false;
            }
        }

        if (thisConn.getUserId() == null != (othConn.getUserId() == null)) {
            return false;
        }
        if (thisConn.getUserId() != null && othConn.getUserId() != null) {
            if (!thisConn.getUserId().equals(othConn.getUserId())) {
                return false;
            }
        }

        if (thisConn.getProtocol() == null != (othConn.getProtocol() == null)) {
            return false;
        }
        if (thisConn.getProtocol() != null && othConn.getProtocol() != null) {
            if (!thisConn.getProtocol().equals(othConn.getProtocol())) {
                return false;
            }
        }

        if (thisConn.getStatus() == null != (othConn.getStatus() == null)) {
            return false;
        }
        if (thisConn.getStatus() != null && othConn.getStatus() != null) {
            if (!thisConn.getStatus().equals(othConn.getStatus())) {
                return false;
            }
        }

        if (thisConn.getServerIp() == null != (othConn.getServerIp() == null)) {
            return false;
        }
        if (thisConn.getServerIp() != null && othConn.getServerIp() != null) {
            if (!thisConn.getServerIp().equals(othConn.getServerIp())) {
                return false;
            }
        }

        if (thisConn.getClientIp() == null != (othConn.getClientIp() == null)) {
            return false;
        }
        if (thisConn.getClientIp() != null && othConn.getClientIp() != null) {
            if (!thisConn.getClientIp().equals(othConn.getClientIp())) {
                return false;
            }
        }

        return true;
    }
}