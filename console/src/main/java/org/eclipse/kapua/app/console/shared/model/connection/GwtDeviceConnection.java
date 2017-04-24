/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model.connection;

import java.io.Serializable;

import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates.GwtDeviceConnectionStatus;

public class GwtDeviceConnection extends GwtUpdatableEntityModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5059430797167078209L;
    
    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(String property) {
        if ("connectionStatusEnum".equals(property)) {
            return (X) GwtDeviceConnectionStatus.valueOf(getConnectionStatus());
        } else {
            return super.get(property);
        }
    }

    public String getConnectionStatus() {
        return get("connectionStatus");
    }
    
    public GwtDeviceConnectionStatus getConnectionStatusEnum() {
        return get("connectionStatusEnum");
    }
    
    public void setConnectionStatus(String connectionStatus) {
        set("connectionStatus", connectionStatus);
    }
    
    public String getClientId() {
        return get("clientId");
    }
    
    public void setClientId(String clientId) {
        set("clientId", clientId);
    }
    
    public String getUserId() {
        return get("userId");
    }
    
    public void setUserId(String userId) {
        set("userId", userId);
    }
    
    public String getProtocol() {
        return get("protocol");
    }
    
    public void setProtocol(String protocol) {
        set("protocol", protocol);
    }
    
    public String getClientIp() {
        return get("clientIp");
    }
    
    public void setClientIp(String clientIp) {
        set("clientIp", clientIp);
    }
    
    public String getServerIp() {
        return get("serverIp");
    }
    
    public void setServerIp(String serverIp) {
        set("serverIp", serverIp);
    }

}
