/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.client.security.bean.serializer.CertificateToStringConverter;
import org.eclipse.kapua.client.security.bean.serializer.StringToCertificateConverter;
import org.eclipse.kapua.client.security.context.SessionContext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AuthRequest implements Request {

    @JsonProperty("requester")
    private String requester;

    @JsonProperty("action")
    private String action;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("clientIp")
    private String clientIp;

    @JsonProperty("connectionId")
    private String connectionId;

    @JsonProperty("oldConnectionId")
    private String oldConnectionId;

    @JsonProperty("transportProtocol")
    private String transportProtocol;

    @JsonProperty("brokerHost")
    private String brokerHost;

    @JsonProperty("brokerId")
    private String brokerId;

    @JsonProperty("sslEnabled")
    private Boolean sslEnabled;

    @JsonProperty("certificates")
    @JsonSerialize(converter = CertificateToStringConverter.class)
    @JsonDeserialize(converter = StringToCertificateConverter.class)
    private Certificate[] certificates;

    //for disconnection use
    //TODO could be a good idea to use 2 different objects?

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("scopeId")
    private String scopeId;

    @JsonProperty("userId")
    private String userId;

    public AuthRequest() {
    }

    public AuthRequest(String requester, String action, String username, String password, ConnectionInfo connectionInfo, String oldConnectionId, String brokerHost, String brokerIp) {
        this.requester = requester;
        this.action = action;
        this.username = username;
        this.clientId = connectionInfo.getClientId();
        this.clientIp = connectionInfo.getClientIp();
        this.connectionId = connectionInfo.getConnectionId();
        this.oldConnectionId = oldConnectionId;
        this.transportProtocol = connectionInfo.getTransportProtocol();
        this.brokerHost = brokerHost;
        this.brokerId = brokerIp;
        this.password = password;
        this.sslEnabled = connectionInfo.getSslEnabled();
        this.certificates = connectionInfo.getCertificates();
    }

    public AuthRequest(String requester, String action, SessionContext sessionContext, String oldConnectionId) {
        this.requester = requester;
        this.action = action;
        this.username = sessionContext.getUsername();
        this.clientId = sessionContext.getClientId();
        this.clientIp = sessionContext.getClientIp();
        this.connectionId = sessionContext.getConnectionId();
        this.oldConnectionId = oldConnectionId;
        this.transportProtocol = sessionContext.getTransportProtocol();
        this.brokerHost = sessionContext.getBrokerHost();
        this.brokerId = sessionContext.getBrokerId();
        accountName = sessionContext.getAccountName();
        scopeId = sessionContext.getScopeId().toCompactId();
        userId = sessionContext.getUserId().toCompactId();
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getOldConnectionId() {
        return oldConnectionId;
    }

    public void setOldConnectionId(String oldConnectionId) {
        this.oldConnectionId = oldConnectionId;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public Certificate[] getCertificates() {
        return certificates;
    }

    public void setCertificates(Certificate[] certificates) {
        this.certificates = certificates;
    }

    //for disconnection use
    //TODO could be a good idea to use 2 different objects?

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getScopeId() {
       return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}