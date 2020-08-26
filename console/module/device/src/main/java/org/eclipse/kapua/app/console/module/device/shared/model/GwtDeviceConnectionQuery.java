/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQueryPredicates.GwtDeviceConnectionReservedUser;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQueryPredicates.GwtDeviceConnectionUser;

public class GwtDeviceConnectionQuery extends GwtQuery {

    /**
     * 
     */
    private static final long serialVersionUID = -6689364882832589557L;

    private String clientId;
    private String connectionStatus;
    private String clientIP;
    private String userName;
    private GwtDeviceConnectionUser gwtDeviceConnectionUser;
    private String userId;
    private String protocol;
    private String reservedUserId;
    private GwtDeviceConnectionReservedUser gwtDeviceConnectionReservedUser;

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GwtDeviceConnectionUser getGwtDeviceConnectionUser() {
        return gwtDeviceConnectionUser;
    }

    public void setGwtDeviceConnectionUser(GwtDeviceConnectionUser gwtDeviceConnectionUser) {
        this.gwtDeviceConnectionUser = gwtDeviceConnectionUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(String reservedUserId) {
        this.reservedUserId = reservedUserId;
    }

    public GwtDeviceConnectionReservedUser getGwtDeviceConnectionReservedUser() {
        return gwtDeviceConnectionReservedUser;
    }

    public void setGwtDeviceConnectionReservedUser(GwtDeviceConnectionReservedUser gwtDeviceConnectionReservedUser) {
        this.gwtDeviceConnectionReservedUser = gwtDeviceConnectionReservedUser;
    }
}
