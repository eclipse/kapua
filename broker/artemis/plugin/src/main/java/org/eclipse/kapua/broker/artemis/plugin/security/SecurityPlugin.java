/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security;

import java.security.cert.Certificate;
import java.util.Set;

import javax.jms.JMSException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.auth.Subject;
import javax.security.auth.login.CredentialException;
import javax.security.cert.X509Certificate;

import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.ActiveMQExceptionType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyServerConnection;
import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.spi.core.protocol.RemotingConnection;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager5;
import org.eclipse.kapua.client.security.ServiceClient.SecurityAction;
import org.eclipse.kapua.client.security.bean.AccountRequest;
import org.eclipse.kapua.client.security.bean.AccountResponse;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.bean.ConnectionInfo;
import org.eclipse.kapua.client.security.bean.KapuaPrincipalImpl;
import org.eclipse.kapua.client.security.context.SessionContext;
import org.eclipse.kapua.client.security.context.Utils;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.client.security.metric.PublishMetric;
import org.eclipse.kapua.client.security.metric.SubscribeMetric;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer.Context;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.handler.ssl.SslHandler;

/**
 * Kapua Artemis security plugin implementation (authentication/authorization)
 *
 */
public class SecurityPlugin implements ActiveMQSecurityManager5 {

    protected static Logger logger = LoggerFactory.getLogger(SecurityPlugin.class);

    private LoginMetric loginMetric = LoginMetric.getInstance();
    private PublishMetric publishMetric = PublishMetric.getInstance();
    private SubscribeMetric subscribeMetric = SubscribeMetric.getInstance();

    protected ServerContext serverContext;
    protected String systemAccountName;
    //to avoid deadlock this filed will be initialized by the first internal login call
    protected KapuaId adminScopeId;

    public SecurityPlugin() {
        logger.info("Initializing SecurityPlugin...");
        serverContext = ServerContext.getInstance();
        systemAccountName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_ACCOUNT);
        logger.info("Initializing SecurityPlugin... DONE");
    }

    @Override
    public Subject authenticate(String username, String password, RemotingConnection remotingConnection, String securityDomain) {
        //like a cache looks for an already authenticated user in context
        //Artemis does the authenticate call even when checking for authorization (publish, subscribe, manage)
        //since we keep a "Kapua session" map that is cleaned when the connection is dropped no security issues will come if this cache is used to avoid redundant login process
        String connectionId = PluginUtility.getConnectionId(remotingConnection);
        logger.info("### authenticate user: {} - clientId: {} - connectionId: {}", username, remotingConnection.getClientID(), connectionId);
        if (!remotingConnection.getTransportConnection().isOpen()) {
            logger.info("Connection {} is closed (stealing link occurred?)", connectionId);
            //TODO add metrics?
            return null;
        }
        KapuaPrincipal kapuaPrincipal = serverContext.getSecurityContextHandler().getPrincipal(connectionId);
        if (kapuaPrincipal!=null) {
            logger.info("### authenticate user (cache found): {} - clientId: {} - connectionId: {}", username, remotingConnection.getClientID(), connectionId);
            loginMetric.getSuccessFromCache().inc();
            return serverContext.getSecurityContextHandler().buildFromPrincipal(kapuaPrincipal);
        }
        else {
            logger.info("### authenticate user (no cache): {} - clientId: {} - connectionId: {}", username, remotingConnection.getClientID(), connectionId);
            ConnectionInfo connectionInfo = new ConnectionInfo(
                PluginUtility.getConnectionId(remotingConnection),//connectionId
                remotingConnection.getClientID(),//clientId
                remotingConnection.getTransportConnection().getRemoteAddress(),//clientIp
                remotingConnection.getTransportConnection().getConnectorConfig().getName(),//connectorName
                remotingConnection.getProtocolName(),//transportProtocol
                (String)remotingConnection.getTransportConnection().getConnectorConfig().getCombinedParams().get("sslEnabled"),//sslEnabled
                getPeerCertificates(remotingConnection));//clientsCertificates
            return PluginUtility.isInternal(remotingConnection) ?
                authenticateInternalConn(connectionInfo, connectionId, username, password, remotingConnection) :
                authenticateExternalConn(connectionInfo, connectionId, username, password, remotingConnection);
        }
    }

    private Subject authenticateInternalConn(ConnectionInfo connectionInfo, String connectionId, String username, String password, RemotingConnection remotingConnection) {
        loginMetric.getInternalConnectorAttempt().inc();
        String usernameToCompare = SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_USERNAME);
        String passToCompare = SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_PASSWORD);
        if (usernameToCompare==null || !usernameToCompare.equals(username) ||
                passToCompare==null || !passToCompare.equals(password)) {
            return null;
//            throw new SecurityException("User not allowed!");
        }
        else {
            try {
                loginMetric.getInternalConnectorConnected().inc();
                logger.info("Authenticate internal: user: {} - clientId: {} - connectionIp: {} - connectionId: {} isOpen: {}",
                        username, connectionInfo.getClientId(), connectionInfo.getClientIp(), remotingConnection.getID(), remotingConnection.getTransportConnection().isOpen());
                //TODO double check why the client id is null once coming from AMQP connection (the Kapua connection factory with custom client id generation is called)
                String clientId = connectionInfo.getClientId()!=null ? connectionInfo.getClientId() : connectionInfo.getClientIp();
                KapuaPrincipal kapuaPrincipal = buildInternalKapuaPrincipal(getAdminScopeId(), clientId);
                //update client id with account|clientId (see pattern)
                String fullClientId = Utils.getFullClientId(getAdminScopeId(), clientId);
                remotingConnection.setClientID(fullClientId);
                Subject subject = buildInternalSubject(kapuaPrincipal);
                SessionContext sessionContext = new SessionContext(kapuaPrincipal, connectionInfo,
                    serverContext.getBrokerIdentity().getBrokerId(), serverContext.getBrokerIdentity().getBrokerHost());
                serverContext.getSecurityContextHandler().setSessionContext(sessionContext, null);
                return subject;
            }
            catch (Exception e) {
                loginMetric.getFailure().inc();
                logger.error("Authenticate internal: error: {}", e.getMessage());
                return null;
//                throw new ActiveMQException(ActiveMQExceptionType.SECURITY_EXCEPTION, "User not authorized!", e);
            }
        }
    }

    private Subject authenticateExternalConn(ConnectionInfo connectionInfo, String connectionId, String username, String password, RemotingConnection remotingConnection) {
        loginMetric.getAttempt().inc();
        //do login
        Context loginTotalContext = loginMetric.getAddConnectionTime().time();
        try {
            logger.info("Authenticate external: user: {} - clientId: {} - connectionIp: {} - connectionId: {} isOpen: {}",
                username, connectionInfo.getClientId(), connectionInfo.getClientIp(), remotingConnection.getID(), remotingConnection.getTransportConnection().isOpen());
            String fullClientId = Utils.getFullClientId(getScopeId(username), connectionInfo.getClientId());
            Context loginShiroLoginTimeContext = loginMetric.getShiroLoginTime().time();
            AuthRequest authRequest = new AuthRequest(
                serverContext.getBrokerIdentity().getBrokerHost(), SecurityAction.brokerConnect.name(),
                username, password, connectionInfo,
                serverContext.getBrokerIdentity().getBrokerHost(), serverContext.getBrokerIdentity().getBrokerId());
            SessionContext currentSessionContext = serverContext.getSecurityContextHandler().getSessionContextByClientId(fullClientId);
            serverContext.getSecurityContextHandler().updateStealingLinkAndIllegalState(authRequest, connectionId, currentSessionContext!=null ? currentSessionContext.getConnectionId() : null);
            AuthResponse authResponse = serverContext.getAuthServiceClient().brokerConnect(authRequest);
            validateAuthResponse(authResponse);
            KapuaPrincipal principal = new KapuaPrincipalImpl(authResponse);
            SessionContext sessionContext = new SessionContext(principal, connectionInfo, authResponse.getKapuaConnectionId(),
                serverContext.getBrokerIdentity().getBrokerId(), serverContext.getBrokerIdentity().getBrokerHost());
            loginShiroLoginTimeContext.stop();

            //update client id with account|clientId (see pattern)
            remotingConnection.setClientID(fullClientId);
            logger.info("Authenticate external: connectionId: {} - old: {}", sessionContext.getConnectionId(), currentSessionContext!=null ? currentSessionContext.getConnectionId() : "N/A");
//            if (authRequest.isStealingLink()) {
//                logger.info("======> Disconnecting client by connection id: {}", currentSessionContext!=null ? currentSessionContext.getConnectionId() : "N/A");
//                //force client disconnection if stealing link happens
//                disconnectClient(currentSessionContext.getConnectionId());
//            }
            Subject subject = null;
            //this call is synchronized on sessionId value
            if (serverContext.getSecurityContextHandler().setSessionContext(sessionContext, authResponse.getAcls())) {
                subject = serverContext.getSecurityContextHandler().buildFromPrincipal(sessionContext.getPrincipal());
            }
            loginMetric.getSuccess().inc();
            return subject;
        }
        catch (Exception e) {
            loginMetric.getFailure().inc();
            logger.error("Authenticate external: error: {}", e.getMessage());
            return null;
//            throw new ActiveMQException(ActiveMQExceptionType.SECURITY_EXCEPTION, "User not authorized!", e);
        }
        finally {
            loginTotalContext.stop();
        }
    }

    @Override
    public boolean authorize(Subject subject, Set<Role> roles, CheckType checkType, String address) {
        boolean allowed = false;
        //TODO improve it to check for null
        KapuaPrincipal principal = getKapuaPrincipal(subject);
        logger.info("### authorizing address: {} - check type: {}", address, checkType.name());
        if (principal!=null) {
            logger.info("### authorizing address: {} - check type: {} - clientId: {} - clientIp: {}", address, checkType.name(), principal.getClientId(), principal.getClientIp());
            if (!principal.isInternal()) {
                SessionContext sessionContext = serverContext.getSecurityContextHandler().getSessionContextByClientId(
                    Utils.getFullClientId(principal.getAccountId(), principal.getClientId()));
                switch (checkType) {
                case CONSUME:
                    allowed = serverContext.getSecurityContextHandler().checkConsumerAllowed(sessionContext, address);
                    if (!allowed) {
                        subscribeMetric.getNotAllowedMessages().inc();
                    }
                    break;
                case SEND:
                    allowed = serverContext.getSecurityContextHandler().checkPublisherAllowed(sessionContext, address);
                    if (!allowed) {
                        publishMetric.getNotAllowedMessages().inc();
                    }
                    break;
                case BROWSE:
                    allowed = serverContext.getSecurityContextHandler().checkConsumerAllowed(sessionContext, address);
                    break;
                case DELETE_DURABLE_QUEUE:
                    allowed = true;
                    break;
                default:
                    allowed = serverContext.getSecurityContextHandler().checkAdminAllowed(sessionContext, address);
                    break;
                }
            }
            else {
                allowed = true;
            }
            logger.info("### authorizing address: {} - check type: {} - clientId: {} - clientIp: {} - RESULT: {}", address, checkType.name(), principal.getClientId(), principal.getClientIp(), allowed);
        }
        //otherwise no principal (or error while getting it) so no authorization
        return allowed;
    }

    @Override
    public boolean validateUser(String user, String password) {
        logger.info("### validate {} - {}", user, password);
        return false;
    }

    @Override
    public boolean validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType) {
        logger.info("### validate user and role {} - {}", user, password);
        return false;
    }

    //
    //Utilities methods
    //
    private void validateAuthResponse(AuthResponse authResponse) throws CredentialException, ActiveMQException {
        if (authResponse==null) {
            throw new ActiveMQException(ActiveMQExceptionType.SECURITY_EXCEPTION, "User not authorized!");
        }
        else if (authResponse.getErrorCode()!=null) {
            //analyze response code
            String errorCode = authResponse.getErrorCode();
            if (KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL.name().equals(errorCode) ||
                    KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS.name().equals(errorCode) ||
                    KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED.name().equals(errorCode)) {
                logger.warn("Invalid username or password for user {} ({})", authResponse.getUsername(), errorCode);
                // activeMQ will map CredentialException into a CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD message (see javadoc on top of this method)
                loginMetric.getInvalidUserPassword().inc();
                throw new CredentialException("Invalid username and/or password or disabled or expired account!");
            } else if (KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL.name().equals(errorCode) ||
                    KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL.name().equals(errorCode) ||
                    KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS.name().equals(errorCode)) {
                logger.warn("User {} not authorized ({})", authResponse.getUsername(), errorCode);
                //TODO check if it's still valid with Artemis
                // activeMQ-MQ will map SecurityException into a CONNECTION_REFUSED_NOT_AUTHORIZED message (see javadoc on top of this method)
                throw new SecurityException("User not authorized!");
            }
        }
    }

    private Certificate[] getPeerCertificates(RemotingConnection remotingConnection) {
        NettyServerConnection nettyServerConnection = ((NettyServerConnection)remotingConnection.getTransportConnection());
        try {
            SslHandler sslhandler = (SslHandler) nettyServerConnection.getChannel().pipeline().get("ssl");
            if (sslhandler!=null) {
                Certificate[] localCertificates = sslhandler.engine().getSession().getLocalCertificates();
                Certificate[] clientCertificates = sslhandler.engine().getSession().getPeerCertificates();
                X509Certificate[] x509ClientCertificates = sslhandler.engine().getSession().getPeerCertificateChain();//???
                return clientCertificates;
            }
            else {
                return null;
            }
        } catch (SSLPeerUnverifiedException e) {
            logger.error("", e);
            return null;
        }
    }

    private KapuaPrincipal getKapuaPrincipal(Subject subject) {
        try {
            //return the first Principal if it's a KapuaPrincipal, otherwise catch every Exception and return null
            return (KapuaPrincipal)subject.getPrincipals().iterator().next();
        }
        catch (Exception e) {
            return null;
        }
    }

    private KapuaPrincipal buildInternalKapuaPrincipal(KapuaId accountId, String clientId) {
        return new KapuaPrincipalImpl(accountId, clientId);
    }

    private Subject buildInternalSubject(KapuaPrincipal kapuaPrincipal) {
        Subject subject = new Subject();
        subject.getPrincipals().add(kapuaPrincipal);
        return subject;
    }

    private KapuaId getAdminScopeId() throws JsonProcessingException, JMSException, InterruptedException {
        //no synchronization needed. At the worst the getScopeId will be called few times instead of just one but the overall performances will be better without synchronization
        if (adminScopeId==null) {
            adminScopeId = getScopeId(systemAccountName);
        }
        return adminScopeId;
    }

    /**
     * Return the scopeId, if user exist, otherwise throws SecurityException
     * No checks for user validity, just return scope id to be used, for example, to build full client id
     * @param username
     * @return
     * @throws InterruptedException
     * @throws JMSException
     * @throws JsonProcessingException
     */
    private KapuaId getScopeId(String username) throws JsonProcessingException, JMSException, InterruptedException {
        AccountRequest accountRequest = new AccountRequest(serverContext.getBrokerIdentity().getBrokerHost(), SecurityAction.getAccount.name(), username);
        AccountResponse accountResponse = serverContext.getAuthServiceClient().getAccount(accountRequest);
        if (accountResponse != null) {
            return KapuaEid.parseCompactId(accountResponse.getScopeId());
        }
        throw new SecurityException("User not authorized!");
    }

}
