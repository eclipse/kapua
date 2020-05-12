/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import com.codahale.metrics.Timer.Context;
import com.google.common.collect.ImmutableList;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.Connection;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.Message;
import org.apache.activemq.filter.DestinationMapEntry;
import org.apache.activemq.security.AuthorizationEntry;
import org.apache.activemq.security.DefaultAuthorizationMap;
import org.apache.activemq.security.SecurityContext;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.ThreadContext;
import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.plugin.authentication.Authenticator;
import org.eclipse.kapua.broker.core.plugin.authentication.DefaultAuthenticator;
import org.eclipse.kapua.broker.core.plugin.authorization.Authorizer;
import org.eclipse.kapua.broker.core.plugin.authorization.Authorizer.ActionType;
import org.eclipse.kapua.broker.core.plugin.authorization.DefaultAuthorizer;
import org.eclipse.kapua.broker.core.plugin.metric.LoginMetric;
import org.eclipse.kapua.broker.core.plugin.metric.PublishMetric;
import org.eclipse.kapua.broker.core.plugin.metric.SubscribeMetric;
import org.eclipse.kapua.broker.core.pool.JmsConsumerWrapper;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ActiveMQ broker filter plugin implementation (security filter).<br>
 * <br>
 * <p>
 * Filter allow unfiltered connection/disconnection and publishing/subscribe action for pass through connection (embedded broker and filter also). This connection type is used by broker assistant
 * bundle.<br>
 * Otherwise perform all kapua authorization/check action.<br>
 * <br>
 * <p>
 * This filter is added inside ActiveMQ filter chain plugin by {@link org.eclipse.kapua.broker.core.KapuaBrokerSecurityPlugin}
 */
public class KapuaSecurityBrokerFilter extends BrokerFilter {

    protected static final Logger logger = LoggerFactory.getLogger(KapuaSecurityBrokerFilter.class);
    protected static final List<String> VT_DURABLE_PREFIX = ImmutableList.of("Consumer.{0}:AT_LEAST_ONCE.{1}", "Consumer.{0}:EXACTLY_ONCE.{1}");
    protected static final String VT_CONSUMER_PREFIX = "Consumer";

    // full client id, with account prepended
    protected static final String MULTI_ACCOUNT_CLIENT_ID = "{0}:{1}";
    public static final String VT_TOPIC_PREFIX = "VirtualTopic.";

    private static final String CONNECT_MESSAGE_TOPIC_PATTERN = "VirtualTopic.%s.%s.%s.MQTT.CONNECT";
    private static final String DISCONNECT_MESSAGE_TOPIC_PATTERN = "VirtualTopic.%s.%s.%s.MQTT.DISCONNECT";
    private static final String MISSING_TOPIC_SUFFIX = "MQTT.LWT";
    private static final String BROKER_IP_RESOLVER_CLASS_NAME;
    private static final String BROKER_ID_RESOLVER_CLASS_NAME;
    private static final String AUTHENTICATOR_CLASS_NAME;
    private static final String AUTHORIZER_CLASS_NAME;
    private static final Long STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME;
    private static boolean stealingLinkEnabled;
    private Future<?> stealingLinkManagerFuture;

    static {
        BrokerSetting config = BrokerSetting.getInstance();
        BROKER_IP_RESOLVER_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_IP_RESOLVER_CLASS_NAME);
        BROKER_ID_RESOLVER_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_ID_RESOLVER_CLASS_NAME);
        AUTHENTICATOR_CLASS_NAME = config.getString(BrokerSettingKey.AUTHENTICATOR_CLASS_NAME);
        AUTHORIZER_CLASS_NAME = config.getString(BrokerSettingKey.AUTHORIZER_CLASS_NAME);
        STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME = config.getLong(BrokerSettingKey.STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME);
        stealingLinkEnabled = config.getBoolean(BrokerSettingKey.BROKER_STEALING_LINK_ENABLED);
    }

    protected BrokerService brokerService;
    protected BrokerIpResolver brokerIpResolver;
    protected BrokerIdResolver brokerIdResolver;
    protected JmsConsumerWrapper stealingLinkManagerConsumer;
    protected String brokerId;

    protected static final Map<String, String> CONNECTION_MAP = new ConcurrentHashMap<>();
    private static final String CONNECTOR_NAME_VM = String.format("vm://%s", BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_NAME));
    private Authenticator authenticator;
    private Authorizer authorizer;

    private AuthenticationService authenticationService = KapuaLocator.getInstance().getService(AuthenticationService.class);
    private CredentialsFactory credentialsFactory = KapuaLocator.getInstance().getFactory(CredentialsFactory.class);
    private AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);

    private Map<String, Object> options;

    private LoginMetric loginMetric = LoginMetric.getInstance();
    private PublishMetric publishMetric = PublishMetric.getInstance();
    private SubscribeMetric subscribeMetric = SubscribeMetric.getInstance();

    public KapuaSecurityBrokerFilter(Broker next) throws KapuaException {
        super(next);
        options = new HashMap<>();
        options.put(Authenticator.ADDRESS_ADVISORY_PREFIX_KEY, "ActiveMQ.Advisory.>");
        options.put(Authenticator.ADDRESS_PREFIX_KEY, VT_TOPIC_PREFIX);
        options.put(Authenticator.ADDRESS_CLASSIFIER_KEY, SystemSetting.getInstance().getMessageClassifier());
        options.put(Authenticator.ADDRESS_CONNECT_PATTERN_KEY, CONNECT_MESSAGE_TOPIC_PATTERN);
        options.put(Authenticator.ADDRESS_DISCONNECT_PATTERN_KEY, DISCONNECT_MESSAGE_TOPIC_PATTERN);
    }

    @Override
    public void start()
            throws Exception {
        logger.info(">>> Security broker filter: calling start...");
        logger.info(">>> Security broker filter: calling start... Initialize authenticator {}", AUTHENTICATOR_CLASS_NAME);
        authenticator = ClassUtil.newInstance(AUTHENTICATOR_CLASS_NAME, DefaultAuthenticator.class, new Class<?>[] { Map.class }, new Object[] { options });
        logger.info(">>> Security broker filter: calling start... Initialize authorizer {}", AUTHORIZER_CLASS_NAME);
        authorizer = ClassUtil.newInstance(AUTHORIZER_CLASS_NAME, DefaultAuthorizer.class, new Class<?>[] {}, new Object[] {});
        logger.info(">>> Security broker filter: calling start... Initialize broker ip resolver");
        brokerIpResolver = ClassUtil.newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
        logger.info(">>> Security broker filter: calling start... Initialize broker id resolver");
        brokerIdResolver = ClassUtil.newInstance(BROKER_ID_RESOLVER_CLASS_NAME, DefaultBrokerIdResolver.class);
        brokerId = brokerIdResolver.getBrokerId(this);
        // start the stealing link manager
        if (stealingLinkEnabled) {
            logger.info(">>> Security broker filter: calling start... Initialize stealing link manager...");
            registerStealingLinkManager();
        }
        super.start();
        logger.info(">>> Security broker filter: calling start... DONE");
    }

    @Override
    public void stop()
            throws Exception {
        logger.info(">>> Security broker filter: calling stop...");
        // stop the stealing link manager unregister
        if (stealingLinkEnabled) {
            logger.info(">>> Security broker filter: calling stop... Unregister stealing link manager");
            unregisterStealingLinkManager();
        }
        super.stop();
        logger.info(">>> Security broker filter: calling stop... DONE");
    }

    /**
     * Register the stealink link manager (if enabled)
     *
     * @throws KapuaRuntimeException
     * @throws JMSException
     * @throws KapuaException
     */
    protected void registerStealingLinkManager() throws KapuaRuntimeException, JMSException, KapuaException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        stealingLinkManagerFuture = executorService.submit(() -> {
            getBrokerService().waitUntilStarted(STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME);
            if (!getBrokerService().isStarted()) {
                logger.error(
                        ">>> Security broker filter: calling start... Register stealing link manager... ERROR - The broker is not started after {} milliseconds... Stealing link manager initialization will be skipped!",
                        STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME);
                // should be more appropriate to shutdown the system?
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR,
                        String.format("Cannot start the stealing link manager. The broker instance is not ready after %d milliseconds!", STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME));
            }
            logger.info(">>> Security broker filter: calling start... Register stealing link manager...");
            try {
                subscribeStealingLinkManager();
                logger.info(">>> Security broker filter: calling start... Register stealing link manager... DONE");
            } catch (KapuaRuntimeException | JMSException | KapuaException e) {
                logger.error(">>> Security broker filter: calling start... Register stealing link manager... ERROR: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Subscribe and process the connect messages to enforce the stealing link
     *
     * @throws KapuaRuntimeException
     * @throws JMSException
     * @throws KapuaException
     */
    protected void subscribeStealingLinkManager() throws JMSException, KapuaException {
        stealingLinkManagerConsumer = new JmsConsumerWrapper(
            String.format(KapuaSecurityBrokerFilter.CONNECT_MESSAGE_TOPIC_PATTERN + ".>", SystemSetting.getInstance().getMessageClassifier(), "*", "*"),
            false, true, message -> {
                // just for logging purpose
                String destination = null;
                String messageId = null;
                try {
                    destination = message.getJMSDestination().toString();
                    messageId = message.getJMSMessageID();
                } catch (JMSException e1) {
                    // ignore it
                }
                logger.debug("Received connect message topic: '{}' - message id: '{}'", destination, messageId);
                String messageBrokerId;
                try {
                    messageBrokerId = message.getStringProperty(MessageConstants.PROPERTY_BROKER_ID);
                    if (!brokerId.equals(messageBrokerId)) {
                        logger.debug("Received connect message from another broker id: '{}' topic: '{}' - message id: '{}'", messageBrokerId, destination, messageId);
                        KapuaConnectionContext kcc = getKapuaConnectionContext(message);
                        if (CONNECTION_MAP.get(kcc.getFullClientId()) != null) {
                            logger.debug("Stealing link detected - broker id: '{}' topic: '{}' - message id: '{}'", messageBrokerId, destination, messageId);
                            // iterate over all connected clients
                            disconnectClients(kcc);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Cannot enforce stealing link check in the received message on topic '{}'", destination, e);
                }
        });
    }

    private void disconnectClients(KapuaConnectionContext kcc) throws Exception {
        for (Connection conn : getClients()) {
            logger.debug("Checking if {} equals {}", kcc.getFullClientId(), conn.getConnectionId());
            if (kcc.getFullClientId().equals(conn.getConnectionId())) {
                // Include Exception to notify the security broker filter
                logger.info("New connection detected for {} on another broker.  Stopping the current connection...", kcc.getFullClientId());
                loginMetric.getRemoteStealingLinkDisconnect().inc();
                conn.serviceExceptionAsync(new IOException(new KapuaDuplicateClientIdException(kcc.getFullClientId())));
                // assume only one connection since this broker should have already handled any duplicates
                return;
            }
        }
    }

    private KapuaConnectionContext getKapuaConnectionContext(javax.jms.Message message) throws JMSException, KapuaException {
        // try parsing from message context (if the message is coming from other brokers it has these fields evaluated)
        try {
            logger.debug("Get connected device informations from the message session");
            return parseMessageSession(message);
        } catch (JMSException | KapuaException e) {
            logger.debug("Get connected device informations from the topic");
            // otherwise looking for these informations by looking at the topic
            return parseTopicInfo(message);
        }
    }

    private KapuaConnectionContext parseMessageSession(javax.jms.Message message) throws JMSException, KapuaException {
        Long scopeId = message.propertyExists(MessageConstants.PROPERTY_SCOPE_ID) ? message.getLongProperty(MessageConstants.PROPERTY_SCOPE_ID) : null;
        String clientId = message.getStringProperty(MessageConstants.PROPERTY_CLIENT_ID);
        if (scopeId == null || scopeId <= 0 || StringUtils.isEmpty(clientId)) {
            logger.debug("Invalid message context. Try parsing the topic.");
            throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Invalid message context");
        }
        return new KapuaConnectionContext(scopeId, clientId, MULTI_ACCOUNT_CLIENT_ID);
    }

    private KapuaConnectionContext parseTopicInfo(javax.jms.Message message) throws JMSException, KapuaException {
        String originalTopic = message.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC);
        String[] topic = originalTopic.split("\\.");
        if (topic.length != 5) {
            logger.error("Invalid topic format. Cannot process connect message.");
            throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "wrong connect message topic");
        }
        String accountName = topic[1];
        String clientId = topic[2];
        Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(accountName));
        Long scopeId = account.getId().getId().longValue();
        return new KapuaConnectionContext(scopeId, clientId, MULTI_ACCOUNT_CLIENT_ID);
    }

    /**
     * Unregister the stealing link manager
     */
    protected void unregisterStealingLinkManager() {
        if (stealingLinkManagerConsumer != null) {
            stealingLinkManagerConsumer.close();
        }
        if (stealingLinkManagerFuture != null) {
            stealingLinkManagerFuture.cancel(true);
        }
    }

    // ------------------------------------------------------------------
    // Connections
    // ------------------------------------------------------------------

    /**
     * Check if the connection is pass through.
     * Pass through connection is a connection with null connector (Advisory topic connection) or embedded broker connection (connection string starts by vm://)
     *
     * @param context
     * @return
     */
    private boolean isPassThroughConnection(ConnectionContext context) {
        if (context != null) {
            if (context.getConnector() == null || CONNECTOR_NAME_VM.equals(((TransportConnector) context.getConnector()).getName())) {
                return true;
            }

            // network connector
            if (context.getConnection().isNetworkConnection()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if security context is broker context
     * Return true if security context is a broker context or if is a pass through connection
     * False if connection context is null or if security context is null and the connection context is not a pass through connection
     * Return true if
     *
     * @param context
     * @return
     */
    private boolean isBrokerContext(ConnectionContext context) {
        if (context == null) {
            return false;
        } else if (context.getSecurityContext() != null) {
            return context.getSecurityContext().isBrokerContext();
        } else {
            return isPassThroughConnection(context);
        }
    }

    /**
     * Add connection.
     * If connection is not a pass through connection check username/password credential and device limits and then register the connection into kapua environment
     * <p>
     * Return error code is compliant to fix ENTMQ-731
     * Extract of MQTTProtocolConverter.java
     * <p>
     * if (exception instanceof InvalidClientIDException) {
     * ack.code(CONNACK.Code.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
     * }
     * else if (exception instanceof SecurityException) {
     * ack.code(CONNACK.Code.CONNECTION_REFUSED_NOT_AUTHORIZED);
     * }
     * else if (exception instanceof CredentialException) {
     * ack.code(CONNACK.Code.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD);
     * }
     * else {
     * ack.code(CONNACK.Code.CONNECTION_REFUSED_SERVER_UNAVAILABLE);
     * }
     */
    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info)
            throws Exception {
        if (!isPassThroughConnection(context)) {
            addExternalConnection(context, info);
            loginMetric.getSuccess().inc();
        }
        super.addConnection(context, info);
    }

    private void addExternalConnection(ConnectionContext context, ConnectionInfo info)
            throws Exception {
        // Clean-up credentials possibly associated with the current thread by previous connection.
        ThreadContext.unbindSubject();
        Context loginTotalContext = loginMetric.getAddConnectionTime().time();
        KapuaConnectionContext kcc = new KapuaConnectionContext(brokerIdResolver.getBrokerId(this), info);

        try {
            logger.info("User name {} - client id: {}, connection id: {}", kcc.getUserName(), kcc.getClientId(), kcc.getConnectionId());
            Context loginShiroLoginTimeContext = loginMetric.getShiroLoginTime().time();
            LoginCredentials credentials = credentialsFactory.newUsernamePasswordCredentials(kcc.getUserName(), info.getPassword());
            AccessToken accessToken = authenticationService.login(credentials);
            Account account = getAccount(accessToken.getScopeId());
            kcc.update(accessToken, account.getName(), accessToken.getScopeId(), accessToken.getUserId(), (((TransportConnector) context.getConnector()).getName()),
                    brokerIpResolver.getBrokerIpOrHostName(), MULTI_ACCOUNT_CLIENT_ID);
            kcc.updateOldConnectionId(CONNECTION_MAP.get(kcc.getFullClientId()));
            loginShiroLoginTimeContext.stop();

            List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> authorizationEntries = authenticator.connect(kcc);
            CONNECTION_MAP.put(kcc.getFullClientId(), info.getConnectionId().getValue());
            DefaultAuthorizationMap authMap = buildAuthorization(kcc, authorizationEntries);
            context.setSecurityContext(new KapuaSecurityContext(kcc, authMap));

            // multiple account stealing link fix
            info.setClientId(kcc.getFullClientId());
            context.setClientId(kcc.getFullClientId());
        } catch (KapuaAuthenticationException e) {
            // fix ENTMQ-731
            loginMetric.getFailure().inc();
            KapuaAuthenticationException kapuaException = e;
            KapuaErrorCode errorCode = kapuaException.getCode();
            if (errorCode.equals(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL) ||
                    errorCode.equals(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS) ||
                    errorCode.equals(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED)) {
                logger.warn("Invalid username or password for user {} ({})", kcc.getUserName(), e.getMessage());
                // activeMQ will map CredentialException into a CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD message (see javadoc on top of this method)
                CredentialException ce = new CredentialException("Invalid username and/or password or disabled or expired account!");
                ce.setStackTrace(e.getStackTrace());
                loginMetric.getInvalidUserPassword().inc();
                throw ce;
            } else if (errorCode.equals(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL) ||
                    errorCode.equals(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL) ||
                    errorCode.equals(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS)) {
                logger.warn("User {} not authorized ({})", kcc.getUserName(), e.getMessage());
                // activeMQ-MQ will map SecurityException into a CONNECTION_REFUSED_NOT_AUTHORIZED message (see javadoc on top of this method)
                SecurityException se = new SecurityException("User not authorized!");
                se.setStackTrace(e.getStackTrace());
                throw se;
            }
        } catch (Exception e) {
            loginMetric.getFailure().inc();
            // Excluded CredentialException, InvalidClientIDException, SecurityException all others exceptions will be mapped by activeMQ to a CONNECTION_REFUSED_SERVER_UNAVAILABLE message (see
            // javadoc on top of this method)
            // Not trapped exception now:
            // KapuaException
            throw e;
        } finally {
            // 7) logout
            Context loginShiroLogoutTimeContext = loginMetric.getShiroLogoutTime().time();
            authenticationService.logout();
            ThreadContext.unbindSubject();
            loginShiroLogoutTimeContext.stop();
            loginTotalContext.stop();
        }
    }

    @Override
    public void removeConnection(ConnectionContext context, ConnectionInfo info, Throwable error)
            throws Exception {
        logger.error("Throwable on remove connection: {}", error!=null ? error.getMessage() : "N/A");
        if (!isPassThroughConnection(context)) {
            Context loginRemoveConnectionTimeContext = loginMetric.getRemoveConnectionTime().time();
            KapuaConnectionContext kcc = null;
            try {
                KapuaSecurityContext kapuaSecurityContext = getKapuaSecurityContext(context);
                KapuaPrincipal kapuaPrincipal = ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal());
                Account account = getAccount(kapuaPrincipal.getAccountId());
                kcc = new KapuaConnectionContext(brokerIdResolver.getBrokerId(this), brokerIpResolver.getBrokerIpOrHostName(), kapuaPrincipal, account.getName(), info, MULTI_ACCOUNT_CLIENT_ID, kapuaSecurityContext.isMissing());
                kcc.updateOldConnectionId(CONNECTION_MAP.get(kcc.getFullClientId()));
                // TODO fix the kapua session when run as feature will be implemented
                KapuaSecurityUtils.setSession(new KapuaSession(kapuaPrincipal));
                authenticator.disconnect(kcc, error);
                // multiple account stealing link fix
                info.setClientId(kcc.getFullClientId());
                context.setClientId(kcc.getFullClientId());
            } finally {
                loginRemoveConnectionTimeContext.stop();
                authenticationService.logout();
                if (kcc != null && kcc.getFullClientId() != null) {
                    if (info.getConnectionId().getValue().equals(CONNECTION_MAP.get(kcc.getFullClientId()))) {
                        // cleanup stealing link detection map
                        CONNECTION_MAP.remove(kcc.getFullClientId());
                    } else {
                        logger.info("Cannot find client id in the connection map. May be it's due to a stealing link. ({})", kcc.getFullClientId());
                    }
                } else {
                    logger.warn("Cannot find Kapua connection context or client id is null");
                }
            }
        }
        super.removeConnection(context, info, error);
        context.setSecurityContext(null);
    }

    private Account getAccount(KapuaId accountId) {
        try {
            return KapuaSecurityUtils.doPrivileged(() -> accountService.find(accountId));
        }
        catch(AuthenticationException e) {
            throw (AuthenticationException) e;
        }
        catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }
    }

    // ------------------------------------------------------------------
    //
    // Destinations
    //
    // ------------------------------------------------------------------

    // ------------------------------------------------------------------
    //
    // Producer
    //
    // ------------------------------------------------------------------

    @Override
    public void send(ProducerBrokerExchange producerExchange, Message messageSend)
            throws Exception {
        Context sendTimeContext = publishMetric.getTime().time();
        try {
            internalSend(producerExchange, messageSend);
        } finally {
            sendTimeContext.stop();
        }
    }

    private void internalSend(ProducerBrokerExchange producerExchange, Message messageSend)
            throws Exception {
        if (!StringUtils.containsNone(messageSend.getDestination().getPhysicalName(), '+', '#')) {
            String message = MessageFormat.format("The caracters '+' and '#' cannot be included in a topic! Destination: {0}", messageSend.getDestination());
            throw new SecurityException(message);
        }
        String originalTopic = null;
        ActiveMQDestination destination = messageSend.getDestination();
        if (destination instanceof ActiveMQTopic) {
            ActiveMQTopic destinationTopic = (ActiveMQTopic) destination;
            originalTopic = destinationTopic.getTopicName().substring(VT_TOPIC_PREFIX.length());
        }
        messageSend.setProperty(MessageConstants.HEADER_KAPUA_RECEIVED_TIMESTAMP, KapuaDateUtils.getKapuaSysDate().toEpochMilli());
        if (!isBrokerContext(producerExchange.getConnectionContext())) {
            KapuaSecurityContext kapuaSecurityContext = getKapuaSecurityContext(producerExchange.getConnectionContext());
            if (!messageSend.getDestination().isTemporary() && !authorizer.isAllowed(ActionType.WRITE, kapuaSecurityContext, messageSend.getDestination())) {
                String message = MessageFormat.format("User {0} ({1} - {2} - conn id {3}) is not authorized to write to: {4}",
                        kapuaSecurityContext.getUserName(),
                        ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId(),
                        ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientIp(),
                        kapuaSecurityContext.getConnectionId(),
                        messageSend.getDestination());
                logger.warn(message);
                publishMetric.getMessageSizeNotAllowed().update(messageSend.getSize());
                publishMetric.getNotAllowedMessages().inc();
                // IMPORTANT
                // restored the throw exception because otherwise we got acl's issues
                throw new SecurityException(message);
            }
            if (isLwt(originalTopic)) {
                //handle the missing message case
                logger.info("Detected missing message for client {}... Flag session to tell disconnector to avoid disconnect event sending", ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId());
                kapuaSecurityContext.setMissing();
            }
            // FIX #164
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_CONNECTION_ID, Base64.getEncoder().encodeToString(SerializationUtils.serialize(kapuaSecurityContext.getConnectionId())));
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_CLIENT_ID, ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId());
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL,
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(kapuaSecurityContext.getConnectorDescriptor())));
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_SESSION, Base64.getEncoder().encodeToString(SerializationUtils.serialize(kapuaSecurityContext.getKapuaSession())));
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, false);
        } else {
            messageSend.setProperty(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, true);
        }
        publishMetric.getMessageSizeAllowed().update(messageSend.getSize());
        messageSend.setProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, originalTopic);
        publishMetric.getAllowedMessages().inc();
        super.send(producerExchange, messageSend);
    }

    private boolean isLwt(String originalTopic) {
        return originalTopic != null && originalTopic.endsWith(MISSING_TOPIC_SUFFIX);
    }

    // ------------------------------------------------------------------
    //
    // Consumer
    //
    // ------------------------------------------------------------------

    @Override
    public Subscription addConsumer(ConnectionContext context, ConsumerInfo info)
            throws Exception {
        Context subscribeTimeContext = subscribeMetric.getTime().time();
        try {
            return internalAddConsumer(context, info);
        } finally {
            subscribeTimeContext.stop();
        }
    }

    private Subscription internalAddConsumer(ConnectionContext context, ConsumerInfo info)
            throws Exception {
        info.setClientId(context.getClientId());
        if (!isBrokerContext(context)) {
            String[] destinationsPath = info.getDestination().getDestinationPaths();
            String destination = info.getDestination().getPhysicalName();
            KapuaSecurityContext kapuaSecurityContext = getKapuaSecurityContext(context);
            if (destinationsPath != null && destinationsPath.length >= 2 && destinationsPath[0].equals(VT_CONSUMER_PREFIX)) {
                StringBuilder sb = new StringBuilder();
                sb.append(destination.substring(0, destinationsPath[0].length() + 1));
                sb.append(context.getClientId());
                if (destinationsPath[1].endsWith(":EXACTLY_ONCE")) {
                    sb.append(":EXACTLY_ONCE");
                } else if (destinationsPath[1].endsWith(":AT_LEAST_ONCE")) {
                    sb.append(":AT_LEAST_ONCE");
                } else {
                    throw new SecurityException(
                            MessageFormat.format("Wrong suscription path attempts for client {0} - destination {1}", context.getClientId(), info.getDestination().getPhysicalName()));
                }
                sb.append(destination.substring(destinationsPath[0].length() + destinationsPath[1].length() + 1));
                destination = sb.toString();
            }
            info.getDestination().setPhysicalName(destination);
            if (!info.getDestination().isTemporary() && !authorizer.isAllowed(ActionType.READ, kapuaSecurityContext, info.getDestination())) {
                String message = MessageFormat.format("User {0} ({1} - {2} - conn id {3}) is not authorized to read from: {4}",
                        kapuaSecurityContext.getUserName(),
                        ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId(),
                        ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientIp(),
                        kapuaSecurityContext.getConnectionId(),
                        info.getDestination());
                logger.warn(message);
                subscribeMetric.getNotAllowedMessages().inc();
                // IMPORTANT
                // restored the throw exception because otherwise we got acl's issues
                throw new SecurityException(message);
            }
        }
        subscribeMetric.getAllowedMessages().inc();
        return super.addConsumer(context, info);
    }

    // ------------------------------------------------------------------
    //
    // Protected
    //
    // ------------------------------------------------------------------

    protected DefaultAuthorizationMap buildAuthorization(KapuaConnectionContext kcc, List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> authorizationEntries) {
        @SuppressWarnings("rawtypes")
        List<DestinationMapEntry> entries = new ArrayList<>();
        for (org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry entry : authorizationEntries) {
            entries.add(createAuthorizationEntry(kcc, entry.getAcl(), entry.getAddress()));
            // added to support the vt topic name space for durable subscriptions
            if (entry.getAcl().isRead()) {
                // logger.info("pattern {} - clientid {} - topic {} - evaluated {}", new Object[]{JmsConstants.ACL_VT_DURABLE_PREFIX[0], clientId, topic,
                // MessageFormat.format(JmsConstants.ACL_VT_DURABLE_PREFIX[0], fullClientId, topic)});
                entries.add(createAuthorizationEntry(kcc, entry.getAcl(), MessageFormat.format(VT_DURABLE_PREFIX.get(0), kcc.getFullClientId(), entry.getAddress())));
                // logger.info("pattern {} - clientid {} - topic {} - evaluated {}", new Object[]{JmsConstants.ACL_VT_DURABLE_PREFIX[1], clientId, topic,
                // MessageFormat.format(JmsConstants.ACL_VT_DURABLE_PREFIX[1], fullClientId, topic)});
                entries.add(createAuthorizationEntry(kcc, entry.getAcl(), MessageFormat.format(VT_DURABLE_PREFIX.get(1), kcc.getFullClientId(), entry.getAddress())));
            }
        }
        return new DefaultAuthorizationMap(entries);
    }

    protected AuthorizationEntry createAuthorizationEntry(KapuaConnectionContext kcc, Acl acl, String address) {
        AuthorizationEntry authorizationEntry = new AuthorizationEntry();
        authorizationEntry.setDestination(ActiveMQDestination.createDestination(address, ActiveMQDestination.TOPIC_TYPE));
        Set<Object> writeACLs = new HashSet<>();
        Set<Object> readACLs = new HashSet<>();
        Set<Object> adminACLs = new HashSet<>();
        if (acl.isRead()) {
            readACLs.add(kcc.getPrincipal());
        }
        if (acl.isWrite()) {
            writeACLs.add(kcc.getPrincipal());
        }
        if (acl.isAdmin()) {
            adminACLs.add(kcc.getPrincipal());
        }
        authorizationEntry.setWriteACLs(writeACLs);
        authorizationEntry.setReadACLs(readACLs);
        authorizationEntry.setAdminACLs(adminACLs);
        return authorizationEntry;
    }

    protected KapuaSecurityContext getKapuaSecurityContext(ConnectionContext context)
            throws SecurityException {
        SecurityContext securityContext = context.getSecurityContext();
        if (securityContext instanceof KapuaSecurityContext) {
            return (KapuaSecurityContext) securityContext;
        }
        throw new SecurityException("Invalid SecurityContext.");
    }

}
