/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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

import java.security.Principal;
import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.Set;

import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.security.AuthorizationMap;
import org.apache.activemq.security.SecurityContext;
import org.eclipse.kapua.client.security.KapuaIllegalDeviceStateException;
import org.eclipse.kapua.client.security.bean.AuthContext;
import org.eclipse.kapua.client.security.context.SessionContext;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptor;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptorProviders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua security context implementation of ActiveMQ broker {@link SecurityContext}
 *
 * @since 1.0
 */
public class KapuaSecurityContext extends SecurityContext {

    protected static final Logger logger = LoggerFactory.getLogger(KapuaSecurityContext.class);

    private SessionContext sessionContext;

    private KapuaSession kapuaSession;
    private Set<Principal> principals;
    private ProtocolDescriptor connectorDescriptor;

    private AuthorizationMap authorizationMap;

    public KapuaSecurityContext(KapuaPrincipal principal, String brokerId, String brokerIpOrHostName, String accountName, ConnectionInfo info, String connectorName) {
        //principal cannot be null!
        super(principal.getName());
        principals = new HashSet<Principal>();
        principals.add(principal);
        kapuaSession = KapuaSession.createFrom();
        if (connectorName == null) {
            throw new IllegalStateException("Connector name is empty!");
        }
        connectorDescriptor = ProtocolDescriptorProviders.getDescriptor(connectorName);
        if (connectorDescriptor == null) {
            throw new IllegalStateException(String.format("Unable to find connector descriptor for connector '%s'", connectorName));
        }
        Certificate[] clientCertificates = null;
        if(info.getTransportContext() instanceof Certificate[]) {
            clientCertificates = (Certificate[]) info.getTransportContext();
        }
        org.eclipse.kapua.client.security.bean.ConnectionInfo connectionInfo = new org.eclipse.kapua.client.security.bean.ConnectionInfo(
                info.getConnectionId().getValue(),//connectionId
                info.getClientId(),//clientId
                info.getClientIp(),//clientIp
                connectorName,//connectorName
                connectorDescriptor.getTransportProtocol(),//transportProtocol
                clientCertificates);//clientsCertificates
        sessionContext = new SessionContext(principal, connectionInfo, brokerId, brokerIpOrHostName);
        sessionContext.setAccountName(accountName);
    }

    public AuthContext toAuthContext(Throwable error) {
        AuthContext authContext = new AuthContext(null);
        if (error != null && error instanceof KapuaIllegalDeviceStateException) {
            authContext.setExceptionClass(KapuaIllegalDeviceStateException.class.getCanonicalName());
        }
        if (error != null && error.getCause() != null && error.getCause() instanceof KapuaIllegalDeviceStateException) {
            authContext.setExceptionClass(KapuaIllegalDeviceStateException.class.getCanonicalName());
        }
        //TODO
        return authContext;
    }

    public SessionContext toSessionContext() {
        return sessionContext;
    }

    public KapuaPrincipal getPrincipal() {
        return sessionContext.getPrincipal();
    }

    public Principal getMainPrincipal() {
        return sessionContext.getPrincipal();
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
        return sessionContext.getKapuaConnectionId();
    }

    public ProtocolDescriptor getConnectorDescriptor() {
        return connectorDescriptor;
    }

    public KapuaSession getKapuaSession() {
        return kapuaSession;
    }

    public void setMissing(boolean missing) {
        sessionContext.setMissing(missing);
    }

    public void updateOldConnectionId(String oldConnectionId) {
        //TODO
//        sessionContext.setOldConnectionId(oldConnectionId);
    }

    public String getFullClientId() {
        //TODO
        return null;
//        return sessionContext.getFullClientId();
    }

    public void setAdmin(boolean admin) {
        sessionContext.setAdmin(admin);
    }

}
