/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.security.AuthorizationMap;
import org.apache.activemq.security.SecurityContext;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua security context implementation of ActiveMQ broker {@link SecurityContext}
 *
 * @since 1.0
 */
public class KapuaSecurityContext extends SecurityContext {

    protected static final Logger logger = LoggerFactory.getLogger(KapuaSecurityContext.class);

    public static final int BROKER_CONNECT_IDX = 0;
    public static final int DEVICE_MANAGE_IDX = 1;
    public static final int DATA_VIEW_IDX = 2;
    public static final int DATA_MANAGE_IDX = 3;
    public static final int DEVICE_VIEW_IDX = 4;

    public static final String PARAM_KEY_PROFILE_ADMIN = "profile_admin";
    public static final String PARAM_KEY_STATUS_MISSING = "status_missing";

    private KapuaPrincipal principal;
    private KapuaSession kapuaSession;
    private KapuaId kapuaConnectionId;
    private String connectionId;
    private Set<Principal> principals;
    private ConnectorDescriptor connectorDescriptor;

    private AuthorizationMap authorizationMap;

    private String brokerId;
    private KapuaId scopeId;
    private KapuaId userId;
    private String accountName;
    private String clientId;
    private String fullClientId;
    private String clientIp;
    private String oldConnectionId;
    private boolean[] hasPermissions;
    private String brokerIpOrHostName;
    private Certificate[] clientCertificates;

    private Map<String, Object> properties = new HashMap<>();

    // use to track the allowed destinations for debug purpose
    private List<String> authDestinations;

    public KapuaSecurityContext(Long scopeId, String clientId) {
        super(null);
        this.scopeId = new KapuaEid(BigInteger.valueOf(scopeId));
        this.clientId = clientId;
        updateFullClientId(scopeId);
    }

    public KapuaSecurityContext(KapuaPrincipal principal, String brokerId, String brokerIpOrHostName, String accountName, ConnectionInfo info, String connectorName) {
        super(principal!=null ? principal.getName() : null);
        this.principal = principal;
        this.brokerId = brokerId;
        this.brokerIpOrHostName = brokerIpOrHostName;
        this.accountName = accountName;
        principals = new HashSet<Principal>();
        if (principal != null) {
            userId = principal.getUserId();
            scopeId = principal.getAccountId();
            principals.add(principal);
        }
        authDestinations = new ArrayList<>();
        clientId = principal.getClientId();
        scopeId = principal.getAccountId();
        clientId = info.getClientId();
        clientIp = info.getClientIp();
        connectionId = info.getConnectionId().getValue();
        kapuaSession = KapuaSession.createFrom();
        if (connectorName == null) {
            throw new IllegalStateException("Connector name is empty!");
        }
        connectorDescriptor = ConnectorDescriptorProviders.getDescriptor(connectorName);
        if (connectorDescriptor == null) {
            throw new IllegalStateException(String.format("Unable to find connector descriptor for connector '%s'", connectorName));
        }
        if(info.getTransportContext() instanceof Certificate[]) {
            clientCertificates = (Certificate[]) info.getTransportContext();
        }
        updateFullClientId();
    }

    public KapuaPrincipal getKapuaPrincipal() throws KapuaException {
        return principal;
    }

    public Principal getMainPrincipal() {
        return principal;
    }

    public Set<Principal> getPrincipals() {
        return principals;
    }

    public void setAuthorizationMap(AuthorizationMap authorizationMap) {
        this.authorizationMap = authorizationMap;
    }

    public AuthorizationMap getAuthorizationMap() {
        return authorizationMap;
    }

    public KapuaId getKapuaConnectionId() {
        return kapuaConnectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public ConnectorDescriptor getConnectorDescriptor() {
        return connectorDescriptor;
    }

    public KapuaSession getKapuaSession() {
        return kapuaSession;
    }

    public void setMissing() {
        setProperty(PARAM_KEY_STATUS_MISSING, Boolean.TRUE);
    }

    public boolean isMissing() {
        return getProperty(PARAM_KEY_STATUS_MISSING, Boolean.FALSE);
    }

    public void updatePermissions(boolean[] hasPermissions) {
        this.hasPermissions = hasPermissions;
    }

    public void updateKapuaConnectionId(DeviceConnection deviceConnection) {
        kapuaConnectionId = deviceConnection != null ? deviceConnection.getId() : null;
    }

    public void updateOldConnectionId(String oldConnectionId) {
        this.oldConnectionId = oldConnectionId;
    }

    private void updateFullClientId() {
        fullClientId = MessageFormat.format(KapuaSecurityBrokerFilter.MULTI_ACCOUNT_CLIENT_ID, scopeId.getId().longValue(), clientId);
    }

    private void updateFullClientId(Long scopeId) {
        fullClientId = MessageFormat.format(KapuaSecurityBrokerFilter.MULTI_ACCOUNT_CLIENT_ID, scopeId, clientId);
    }

    public String getFullClientId() {
        return fullClientId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public Certificate[] getClientCertificates() {
        return clientCertificates;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public long getScopeIdAsLong() {
        return scopeId != null ? scopeId.getId().longValue() : 0;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public KapuaPrincipal getPrincipal() {
        return principal;
    }

    public String getOldConnectionId() {
        return oldConnectionId;
    }

    public KapuaId getUserId() {
        return userId;
    }

    public boolean[] getHasPermissions() {
        return hasPermissions;
    }

    public void setAdmin(boolean admin) {
        setProperty(PARAM_KEY_PROFILE_ADMIN, admin);
    }

    public boolean isAdmin() {
        return getProperty(PARAM_KEY_PROFILE_ADMIN, Boolean.FALSE);
    }

    public boolean isBrokerConnect() {
        return hasPermissions[BROKER_CONNECT_IDX];
    }

    public boolean isDeviceView() {
        return hasPermissions[DEVICE_VIEW_IDX];
    }

    public boolean isDeviceManage() {
        return hasPermissions[DEVICE_MANAGE_IDX];
    }

    public boolean isDataView() {
        return hasPermissions[DATA_VIEW_IDX];
    }

    public boolean isDataManage() {
        return hasPermissions[DATA_MANAGE_IDX];
    }

    public String getBrokerIpOrHostName() {
        return brokerIpOrHostName;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    /**
     *
     * @param key
     * @param defaultValue
     *
     * @throws ClassCastException if the property object is different from what expected by the caller (in other words no check before casting is done!)
     * @return
     */
    public <T> T getProperty(String key, T defaultValue) {
        T value = (T)properties.get(key);
        return value!=null ? value : defaultValue;
    }

    public void addAuthDestinationToLog(String message) {
        if (logger.isDebugEnabled()) {
            authDestinations.add(message);
        }
    }

    public void logAuthDestinationToLog() {
        if (!authDestinations.isEmpty()) {
            logger.debug("Authorization map:");
            for (String str : authDestinations) {
                logger.debug(str);
            }
        }
    }
}
