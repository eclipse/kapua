/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.bean;

import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.client.security.AuthErrorCodes;
import org.eclipse.kapua.model.id.KapuaId;

public class AuthContext {

    private String username;
    private String userId;
    private String accountName;
    private String scopeId;
    private String clientId;
    private String clientIp;
    private String connectionId;
    private String oldConnectionId;
    private String brokerId;
    private String brokerHost;
    private String transportProtocol;
    private Certificate[] clientCertificates;

    private String exceptionClass;
    private AuthErrorCodes authErrorCode;

    private boolean admin;
    private boolean missing;
    private String kapuaConnectionId;

    private Map<String, Object> property;

    public AuthContext(AuthRequest authRequest) {
        initCommonFields(authRequest);
        accountName = authRequest.getAccountName();
        scopeId = authRequest.getScopeId();
        userId = authRequest.getUserId();
        clientCertificates = authRequest.getCertificates();
    }

    public AuthContext(AuthRequest authRequest, AuthResponse authResponse) {
        initCommonFields(authRequest);
        accountName = authResponse.getAccountName();
        scopeId = authResponse.getScopeId();
        userId = authResponse.getUserId();
        clientCertificates = authRequest.getCertificates();
    }

    private void initCommonFields(AuthRequest authRequest) {
        property = new HashMap<>();
        username = authRequest.getUsername();
        clientId = authRequest.getClientId();
        clientIp = authRequest.getClientIp();
        connectionId = authRequest.getConnectionId();
        oldConnectionId = authRequest.getOldConnectionId();
        brokerId = authRequest.getBrokerId();
        brokerHost = authRequest.getBrokerHost();
        transportProtocol = authRequest.getTransportProtocol();
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getScopeId() {
        return scopeId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public Certificate[] getClientCertificates() {
        return clientCertificates;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getOldConnectionId() {
        return oldConnectionId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public AuthErrorCodes getAuthErrorCode() {
        return authErrorCode;
    }

    public void setAuthErrorCode(AuthErrorCodes authErrorCode) {
        this.authErrorCode = authErrorCode;
    }

    public String getKapuaConnectionId() {
        return kapuaConnectionId;
    }

    public void setKapuaConnectionId(KapuaId kapuaConnectionId) {
        this.kapuaConnectionId = kapuaConnectionId.toCompactId();
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public <T> void setProperty(String key, T value) {
        property.put(key, value);
    }

    public <T> T getProperty(String key, T defaultValue) {
        try {
            @SuppressWarnings("unchecked")
            T value = (T)property.get(key);
            if (value==null) {
                return defaultValue;
            }
            else {
                return value;
            }
        }
        catch (ClassCastException e) {
            return defaultValue;
        }
    }
}
