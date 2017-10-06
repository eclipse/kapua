/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.security.auth.login.CredentialException;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.TransportConnection;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.ConnectionId;
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
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.message.system.DefaultSystemMessageCreator;
import org.eclipse.kapua.broker.core.message.system.SystemMessageCreator;
import org.eclipse.kapua.broker.core.message.system.SystemMessageCreator.SystemMessageType;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerPool;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerPool.DESTINATIONS;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerWrapper;
import org.eclipse.kapua.broker.core.pool.JmsConsumerWrapper;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
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
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionPredicates;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionQuery;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

/**
 * ActiveMQ broker filter plugin implementation (security filter).<br>
 * <br>
 * 
 * Filter allow unfiltered connection/disconnection and publishing/subscribe action for pass through connection (embedded broker and filter also). This connection type is used by broker assistant
 * bundle.<br>
 * Otherwise perform all kapua authorization/check action.<br>
 * <br>
 * 
 * This filter is added inside ActiveMQ filter chain plugin by {@link org.eclipse.kapua.broker.core.KapuaBrokerSecurityPlugin}
 */
public class KapuaSecurityBrokerFilter extends BrokerFilter {

    protected final static Logger logger = LoggerFactory.getLogger(KapuaSecurityBrokerFilter.class);

    private final static String CONNECT_MESSAGE_TOPIC_PATTERN = "VirtualTopic.%s.%s.%s.MQTT.CONNECT";
    private final static String CANNOT_LOAD_INSTANCE_ERROR_MSG = "Cannot load instance %s for %s. Please check the configuration file!";
    private final static String BROKER_IP_RESOLVER_CLASS_NAME;
    private final static String BROKER_ID_RESOLVER_CLASS_NAME;
    private final static String SYSTEM_MESSAGE_CREATOR_CLASS_NAME;
    private final static Long STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME;
    private static boolean stealingLinkEnabled;
    private Future<?> stealingLinkManagerFuture;

    static {
        BrokerSetting config = BrokerSetting.getInstance();
        BROKER_IP_RESOLVER_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_IP_RESOLVER_CLASS_NAME);
        BROKER_ID_RESOLVER_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_ID_RESOLVER_CLASS_NAME);
        SYSTEM_MESSAGE_CREATOR_CLASS_NAME = config.getString(BrokerSettingKey.SYSTEM_MESSAGE_CREATOR_CLASS_NAME);
        STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME = config.getLong(BrokerSettingKey.STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME);
        stealingLinkEnabled = config.getBoolean(BrokerSettingKey.BROKER_STEALING_LINK_ENABLED);
    }

    protected BrokerService brokerService;
    protected BrokerIpResolver brokerIpResolver;
    protected BrokerIdResolver brokerIdResolver;
    protected SystemMessageCreator systemMessageCreator;
    protected JmsConsumerWrapper stealingLinkManagerConsumer;
    protected String brokerId;

    protected final static Map<String, ConnectionId> CONNECTION_MAP = new ConcurrentHashMap<>();
    private final static String CONNECTOR_NAME_VM = String.format("vm://%s", BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_NAME));

    private static final Domain BROKER_DOMAIN = new BrokerDomain();
    private static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    private static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    // login
    private Counter metricLoginSuccess;
    private Counter metricLoginFailure;
    private Counter metricLoginInvalidUserPassword;
    private Counter metricLoginInvalidClientId;
    private Counter metricLoginKapuasysTokenAttempt;
    private Counter metricLoginNormalUserAttempt;
    private Counter metricLoginStealingLinkConnect;
    private Counter metricLoginStealingLinkDisconnect;
    protected Counter metricLoginRemoteStealingLinkDisconnect;
    private Timer metricLoginAddConnectionTime;
    private Timer metricLoginNormalUserTime;
    private Timer metricLoginPreCheckTime;
    private Timer metricLoginShiroLoginTime;
    private Timer metricLoginCheckAccessTime;
    private Timer metricLoginFindClientIdTime;
    private Timer metricLoginFindDevTime;
    private Timer metricLoginShiroLogoutTime;
    private Timer metricLoginSendLoginUpdateMsgTime;
    private Timer metricLoginRemoveConnectionTime;
    // publish/subscribe
    private Counter metricPublishAllowedMessages;
    private Counter metricPublishNotAllowedMessages;
    private Timer metricPublishTime;
    private Counter subscribeAllowedMessages;
    private Counter subscribeNotAllowedMessages;
    private Timer subscribeTime;
    // clients
    private Counter metricClientConnectedClient;
    private Counter metricClientConnectedKapuasys;
    private Counter metricClientDisconnectionClient;
    private Counter metricClientDisconnectionKapuasys;
    // message size
    private Histogram metricPublishMessageSizeAllowed;
    private Histogram metricPublishMessageSizeNotAllowed;

    private AuthenticationService authenticationService = KapuaLocator.getInstance().getService(AuthenticationService.class);
    private AuthorizationService authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
    private PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
    private CredentialsFactory credentialsFactory = KapuaLocator.getInstance().getFactory(CredentialsFactory.class);
    private AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private DeviceConnectionService deviceConnectionService = KapuaLocator.getInstance().getService(DeviceConnectionService.class);
    private DeviceConnectionFactory deviceConnectionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionFactory.class);
    private DeviceConnectionOptionFactory deviceConnectionOptionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionOptionFactory.class);
    private DeviceConnectionOptionService deviceConnectionOptionService = KapuaLocator.getInstance().getService(DeviceConnectionOptionService.class);
    private MetricsService metricsService = MetricServiceFactory.getInstance();

    public KapuaSecurityBrokerFilter(Broker next) throws KapuaException {
        super(next);

        // login
        metricLoginSuccess = metricsService.getCounter("security", "login", "success", "count");
        metricLoginFailure = metricsService.getCounter("security", "login", "failure", "count");
        metricLoginInvalidUserPassword = metricsService.getCounter("security", "login", "failure_password", "count");
        metricLoginInvalidClientId = metricsService.getCounter("security", "login", "failure_client_id", "count");
        metricLoginKapuasysTokenAttempt = metricsService.getCounter("security", "login", "kapuasys", "count");
        metricLoginNormalUserAttempt = metricsService.getCounter("security", "login", "normal", "count");
        metricLoginStealingLinkConnect = metricsService.getCounter("security", "login", "stealing_link", "connect", "count");
        metricLoginStealingLinkDisconnect = metricsService.getCounter("security", "login", "stealing_link", "disconnect", "count");
        metricLoginRemoteStealingLinkDisconnect = metricsService.getCounter("security", "login", "remote_stealing_link", "disconnect", "count");
        // login time
        metricLoginAddConnectionTime = metricsService.getTimer("security", "login", "add_connection", "time", "s");
        metricLoginNormalUserTime = metricsService.getTimer("security", "login", "user", "time", "s");
        metricLoginPreCheckTime = metricsService.getTimer("security", "login", "pre_check", "time", "s");
        metricLoginShiroLoginTime = metricsService.getTimer("security", "login", "shiro", "login", "time", "s");
        metricLoginCheckAccessTime = metricsService.getTimer("security", "login", "check_access", "time", "s");
        metricLoginFindClientIdTime = metricsService.getTimer("security", "login", "find_client_id", "time", "s");
        metricLoginFindDevTime = metricsService.getTimer("security", "login", "find_device", "time", "s");
        metricLoginShiroLogoutTime = metricsService.getTimer("security", "login", "shiro", "logout", "time", "s");
        metricLoginSendLoginUpdateMsgTime = metricsService.getTimer("security", "login", "send_login_update", "time", "s");
        metricLoginRemoveConnectionTime = metricsService.getTimer("security", "login", "remove_connection", "time", "s");
        // publish/subscribe
        metricPublishAllowedMessages = metricsService.getCounter("security", "publish", "allowed", "count");
        metricPublishNotAllowedMessages = metricsService.getCounter("security", "publish", "not_allowed", "count");
        metricPublishTime = metricsService.getTimer("security", "publish", "time", "s");
        subscribeAllowedMessages = metricsService.getCounter("security", "subscribe", "allowed", "count");
        subscribeNotAllowedMessages = metricsService.getCounter("security", "subscribe", "not_allowed", "count");
        subscribeTime = metricsService.getTimer("security", "subscribe", "time", "s");
        // clients connected
        metricClientConnectedClient = metricsService.getCounter("security", "login", "clients", "connected", "count");
        metricClientConnectedKapuasys = metricsService.getCounter("security", "login", "kapuasys", "connected", "count");
        metricClientDisconnectionClient = metricsService.getCounter("security", "login", "clients", "disconnected", "count");
        metricClientDisconnectionKapuasys = metricsService.getCounter("security", "login", "kapuasys", "disconnected", "count");
        // message size
        metricPublishMessageSizeAllowed = metricsService.getHistogram("security", "publish", "messages", "allowed", "size", "bytes");
        metricPublishMessageSizeNotAllowed = metricsService.getHistogram("security", "publish", "messages", "not_allowed", "size", "bytes");

        XmlUtil.setContextProvider(new BrokerJAXBContextProvider());
    }

    @Override
    public void start()
            throws Exception {
        logger.info(">>> Security broker filter: calling start...");
        logger.info(">>> Security broker filter: calling start... Initialize broker ip resolver");
        brokerIpResolver = newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
        logger.info(">>> Security broker filter: calling start... Initialize broker id resolver");
        brokerIdResolver = newInstance(BROKER_ID_RESOLVER_CLASS_NAME, DefaultBrokerIdResolver.class);
        brokerId = brokerIdResolver.getBrokerId(this);
        logger.info(">>> Security broker filter: calling start... Initialize system message creator");
        systemMessageCreator = newInstance(SYSTEM_MESSAGE_CREATOR_CLASS_NAME, DefaultSystemMessageCreator.class);
        // start the stealing link manager
        if (stealingLinkEnabled) {
            logger.info(">>> Security broker filter: calling start... Initialize stealing link manager...");
            registerStealingLinkManager();
        }
        super.start();
    }

    @Override
    public void stop()
            throws Exception {
        logger.info(">>> Security broker filter: calling stop...");
        // stop the stealing link manager unregister
        if (stealingLinkEnabled) {
            logger.info(">>> Security broker filter: calling start... Unregister stealing link manager");
            unregisterStealingLinkManager();
        }
        super.stop();
    }

    protected void registerStealingLinkManager() throws KapuaRuntimeException, JMSException, KapuaException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        stealingLinkManagerFuture = executorService.submit(new Runnable() {

            @Override
            public void run() {
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
            }
        });
    }

    protected void subscribeStealingLinkManager() throws KapuaRuntimeException, JMSException, KapuaException {
        stealingLinkManagerConsumer = new JmsConsumerWrapper(
                String.format(KapuaSecurityBrokerFilter.CONNECT_MESSAGE_TOPIC_PATTERN + ".>", SystemSetting.getInstance().getMessageClassifier(), "*", "*"),
                false, true, new MessageListener() {

                    @Override
                    public void onMessage(javax.jms.Message message) {
                        // just for logging purpose
                        String destination = null;
                        String messageId = null;
                        try {
                            destination = message.getJMSDestination().toString();
                            messageId = message.getJMSMessageID();
                        } catch (JMSException e1) {
                            // ignore it
                        }
                        logger.debug("Received connect message topic: '{}' - message id: '{}'", new Object[] { destination, messageId });
                        String messageBrokerId;
                        try {
                            messageBrokerId = message.getStringProperty(MessageConstants.PROPERTY_BROKER_ID);
                            if (!brokerId.equals(messageBrokerId)) {
                                logger.debug("Received connect message from another broker id: '{}' topic: '{}' - message id: '{}'", new Object[] { messageBrokerId, destination, messageId });
                                KapuaBrokerContextContainer kbcc = null;
                                // try parsing from message context (if the message is coming from other brokers it has these fields evaluated)
                                try {
                                    logger.debug("Get connected device informations from the message session");
                                    kbcc = parseMessageSession(message);
                                } catch (JMSException | KapuaException e) {
                                    logger.debug("Get connected device informations from the topic");
                                    // otherwise looking for these informations by looking at the topic
                                    kbcc = parseTopicInfo(message);
                                }
                                String fullClientId = getFullClientId(kbcc);
                                if (CONNECTION_MAP.get(fullClientId) != null) {
                                    logger.debug("Stealing link detected - broker id: '{}' topic: '{}' - message id: '{}'", new Object[] { messageBrokerId, destination, messageId });
                                    // stealing link detected!
                                    List<TransportConnector> transportConnectors = getBrokerService().getTransportConnectors();
                                    // iterate over all the transport connectors
                                    for (TransportConnector transportConnector : transportConnectors) {
                                        // assume that only one client with this client id is connected to the broker by one of the connection
                                        for (TransportConnection conn : transportConnector.getConnections()) {
                                            if (fullClientId.equals(conn.getConnectionId())) {
                                                logger.info("New connection detected for {} on another broker.  Stopping the current connection on transport connector: {}.", fullClientId,
                                                        transportConnector.getName());
                                                metricLoginRemoteStealingLinkDisconnect.inc();
                                                // Include KapuaDuplicateClientIdException to notify the security broker filter
                                                conn.stopAsync(new KapuaDuplicateClientIdException(fullClientId));
                                                // assume only one connection since this broker should have already handled any duplicates
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JMSException | KapuaException e) {
                            logger.error("Cannot enforce stealing link check in the received message on topic '{}'", destination, e);
                        }
                    }

                    private KapuaBrokerContextContainer parseMessageSession(javax.jms.Message message) throws JMSException, KapuaException {
                        Long scopeId = message.propertyExists(MessageConstants.PROPERTY_SCOPE_ID) ? message.getLongProperty(MessageConstants.PROPERTY_SCOPE_ID) : null;
                        String clientId = message.getStringProperty(MessageConstants.PROPERTY_CLIENT_ID);
                        if (scopeId == null || scopeId.longValue() <= 0 || StringUtils.isEmpty(clientId)) {
                            logger.debug("Invalid message context. Try parsing the topic.");
                            throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Invalid message context");
                        }
                        return new KapuaBrokerContextContainer(scopeId, clientId);
                    }

                    private KapuaBrokerContextContainer parseTopicInfo(javax.jms.Message message) throws JMSException, KapuaException {
                        String originalTopic = message.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC);
                        String topic[] = originalTopic.split("\\.");
                        if (topic.length != 5) {
                            logger.error("Invalid topic format. Cannot process connect message.");
                            throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "wrong connect message topic");
                        }
                        String accountName = topic[1];
                        String clientId = topic[2];
                        Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(accountName));
                        Long scopeId = account.getId().getId().longValue();
                        return new KapuaBrokerContextContainer(scopeId, clientId);
                    }

                });
    }

    protected void unregisterStealingLinkManager() {
        if (stealingLinkManagerConsumer != null) {
            stealingLinkManagerConsumer.close();
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
     * 
     * Return error code is compliant to fix ENTMQ-731
     * Extract of MQTTProtocolConverter.java
     * 
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
     * 
     */
    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info)
            throws Exception {
        if (!isPassThroughConnection(context)) {
            addExternalConnection(context, info);
            metricLoginSuccess.inc();
        }
        super.addConnection(context, info);
    }

    private void addExternalConnection(ConnectionContext context, ConnectionInfo info)
            throws Exception {
        // Clean-up credentials possibly associated with the current thread by previous connection.
        ThreadContext.unbindSubject();

        Context loginTotalContext = metricLoginAddConnectionTime.time();
        String username = info.getUserName();
        String password = info.getPassword();
        String clientId = info.getClientId();
        String clientIp = info.getClientIp();
        ConnectionId connectionId = info.getConnectionId();
        KapuaBrokerContextContainer kbcc = new KapuaBrokerContextContainer(brokerIdResolver.getBrokerId(this), username, clientId, clientIp);

        List<String> authDestinations = null;
        if (logger.isDebugEnabled()) {
            authDestinations = new ArrayList<>();
        }
        try {
            // Build KapuaUsername
            // User username = User.parse(username);//KapuaUserName

            logger.info("User name {} - client id: {}, connection id: {}", username, clientId, connectionId);

            Context loginPreCheckTimeContext = metricLoginPreCheckTime.time();
            // 1) validate client id
            // Check the device Mqtt ClientId

            // TODO move to deviceservice
            // MqttUtils.checkDeviceClientId(clientId);
            loginPreCheckTimeContext.stop();

            Context loginShiroLoginTimeContext = metricLoginShiroLoginTime.time();
            LoginCredentials credentials = credentialsFactory.newUsernamePasswordCredentials(username, password);
            AccessToken accessToken = authenticationService.login(credentials);

            KapuaId scopeId = accessToken.getScopeId();
            KapuaId userId = accessToken.getUserId();
            kbcc.setScopeId(scopeId.getId().longValue());

            final Account account;
            try {
                account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(scopeId));
            } catch (Exception e) {
                // to preserve the original exception message (if possible)
                if (e instanceof AuthenticationException) {
                    throw (AuthenticationException) e;
                } else {
                    throw new ShiroException("Error while find account!", e);
                }
            }

            String accountName = account.getName();
            kbcc.setAccountName(accountName);
            loginShiroLoginTimeContext.stop();

            // if a user acts as a child MOVED INSIDE KapuaAuthorizingRealm otherwise through REST API and console this @accountName won't work

            // get account id and name from kapua session methods that check for the run as
            //
            // String accountName = kapuaSession.getSessionAccountName();
            // long accountId = kapuaSession.getSessionAccountId();

            // multiple account stealing link fix
            String fullClientId = getFullClientId(scopeId.getId().longValue(), clientId);

            KapuaPrincipal principal = new KapuaPrincipalImpl(accessToken,
                    username,
                    clientId,
                    clientIp);
            DeviceConnection deviceConnection = null;

            final String connectorName = (((TransportConnector) context.getConnector()).getName());
            final ConnectorDescriptor connectorDescriptor = ConnectorDescriptorProviders.getDescriptor(connectorName);
            if (connectorDescriptor == null) {
                throw new IllegalStateException(String.format("Unable to find connector descriptor for connector '%s'", connectorName));
            }

            // 3) check authorization
            DefaultAuthorizationMap authMap = null;
            if (isAdminUser(kbcc)) {
                metricLoginKapuasysTokenAttempt.inc();
                // 3-1) admin authMap
                authMap = buildAdminAuthMap(authDestinations, principal, fullClientId);
                metricClientConnectedKapuasys.inc();
            } else {
                Context loginNormalUserTimeContext = metricLoginNormalUserTime.time();
                metricLoginNormalUserAttempt.inc();
                // 3-3) check permissions

                Context loginCheckAccessTimeContext = metricLoginCheckAccessTime.time();
                boolean[] hasPermissions = new boolean[] {
                        // TODO check the permissions... move them to a constants class?
                        authorizationService.isPermitted(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, scopeId)),
                        authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId)),
                        authorizationService.isPermitted(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.read, scopeId)),
                        authorizationService.isPermitted(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.write, scopeId))
                };
                if (!hasPermissions[AclConstants.BROKER_CONNECT_IDX]) {
                    throw new KapuaIllegalAccessException(permissionFactory.newPermission(BROKER_DOMAIN, Actions.connect, scopeId).toString());
                }
                loginCheckAccessTimeContext.stop();

                // 3-4) build authMap
                authMap = buildAuthMap(authDestinations, principal, hasPermissions, accountName, clientId, fullClientId);

                // 4) find device
                Context loginFindClientIdTimeContext = metricLoginFindClientIdTime.time();
                deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(scopeId, clientId));
                loginFindClientIdTimeContext.stop();
                // enforce the user-device bound
                enforceDeviceConnectionUserBound(KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.getConfigValues(scopeId)), deviceConnection, scopeId, userId);

                Context loginFindDevTimeContext = metricLoginFindDevTime.time();

                // send connect message
                ConnectionId previousConnectionId = CONNECTION_MAP.put(fullClientId, info.getConnectionId());
                boolean stealingLinkDetected = (previousConnectionId != null);
                if (deviceConnection == null) {
                    DeviceConnectionCreator deviceConnectionCreator = deviceConnectionFactory.newCreator(scopeId);
                    deviceConnectionCreator.setClientId(clientId);
                    deviceConnectionCreator.setClientIp(clientIp);
                    deviceConnectionCreator.setProtocol(connectorDescriptor.getTransportProtocol());
                    deviceConnectionCreator.setServerIp(brokerIpResolver.getBrokerIpOrHostName());
                    deviceConnectionCreator.setUserId(userId);
                    deviceConnectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.INHERITED);
                    deviceConnectionCreator.setAllowUserChange(false);
                    deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.create(deviceConnectionCreator));
                } else {
                    deviceConnection.setClientIp(clientIp);
                    deviceConnection.setProtocol(connectorDescriptor.getTransportProtocol());
                    deviceConnection.setServerIp(brokerIpResolver.getBrokerIpOrHostName());
                    deviceConnection.setUserId(userId);
                    deviceConnection.setStatus(DeviceConnectionStatus.CONNECTED);
                    deviceConnection.setAllowUserChange(false);
                    final DeviceConnection deviceConnectionToUpdate = deviceConnection;
                    KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnectionToUpdate));
                    // TODO implement the banned status
                    // if (DeviceStatus.DISABLED.equals(device.getStatus())) {
                    // throw new KapuaIllegalAccessException("clientId - This client ID is disabled and cannot connect");
                    // }
                    // TODO manage the stealing link event (may be a good idea to use different connect status (connect -stealing)?
                    if (stealingLinkDetected) {
                        metricLoginStealingLinkConnect.inc();

                        // stealing link detected, skip info
                        logger.warn("Detected Stealing link for cliend id {} - account - last connection id was {} - current connection id is {} - IP: {} - No connection status changes!",
                                new Object[] { clientId, accountName, previousConnectionId, info.getConnectionId(), info.getClientIp() });
                    }
                }
                loginFindDevTimeContext.stop();

                loginNormalUserTimeContext.stop();
                sendConnectMessage(kbcc);
                metricClientConnectedClient.inc();
            }
            logAuthDestinationToLog(authDestinations);

            KapuaSecurityContext securityCtx = new KapuaSecurityContext(principal,
                    authMap,
                    (deviceConnection != null ? deviceConnection.getId() : null),
                    connectionId,
                    connectorDescriptor);
            context.setSecurityContext(securityCtx);

            // multiple account stealing link fix
            info.setClientId(fullClientId);
            context.setClientId(fullClientId);
        } catch (Exception e) {
            metricLoginFailure.inc();

            // fix ENTMQ-731
            if (e instanceof KapuaAuthenticationException) {
                KapuaAuthenticationException kapuaException = (KapuaAuthenticationException) e;
                KapuaErrorCode errorCode = kapuaException.getCode();
                if (errorCode.equals(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL) ||
                        errorCode.equals(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS) ||
                        errorCode.equals(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED)) {
                    logger.warn("Invalid username or password for user {} ({})", username, e.getMessage());
                    // activeMQ will map CredentialException into a CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD message (see javadoc on top of this method)
                    CredentialException ce = new CredentialException("Invalid username and/or password or disabled or expired account!");
                    ce.setStackTrace(e.getStackTrace());
                    metricLoginInvalidUserPassword.inc();
                    throw ce;
                } else if (errorCode.equals(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL) ||
                        errorCode.equals(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL) ||
                        errorCode.equals(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS)) {
                    logger.warn("User {} not authorized ({})", username, e.getMessage());
                    // activeMQ-MQ will map SecurityException into a CONNECTION_REFUSED_NOT_AUTHORIZED message (see javadoc on top of this method)
                    SecurityException se = new SecurityException("User not authorized!");
                    se.setStackTrace(e.getStackTrace());
                    throw se;
                }

            }
            // Excluded CredentialException, InvalidClientIDException, SecurityException all others exceptions will be mapped by activeMQ to a CONNECTION_REFUSED_SERVER_UNAVAILABLE message (see
            // javadoc on top of this method)
            // Not trapped exception now:
            // KapuaException
            logger.info("@@ error", e);
            throw e;
        } finally {
            // 7) logout
            Context loginShiroLogoutTimeContext = metricLoginShiroLogoutTime.time();
            authenticationService.logout();
            ThreadContext.unbindSubject();
            loginShiroLogoutTimeContext.stop();
            loginTotalContext.stop();
        }
    }

    protected <T> T newInstance(String clazz, Class<T> defaultInstance) throws KapuaException {
        logger.info("Initializing instance...");
        T instance;
        // lazy synchronization
        try {
            if (!StringUtils.isEmpty(clazz)) {
                logger.info("Initializing instance of {}...", clazz);
                @SuppressWarnings("unchecked")
                Class<T> clazzToInstantiate = (Class<T>) Class.forName(clazz);
                instance = clazzToInstantiate.newInstance();
            } else {
                logger.info("Initializing instance of. Instantiate default instance {} ...", defaultInstance);
                instance = defaultInstance.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, String.format(CANNOT_LOAD_INSTANCE_ERROR_MSG, clazz));
        }
        logger.info("Initializing broker ip resolver... DONE");
        return instance;
    }

    protected String getFullClientId(Long scopeId, String clientId) {
        return MessageFormat.format(AclConstants.MULTI_ACCOUNT_CLIENT_ID, scopeId, clientId);
    }

    protected String getFullClientId(KapuaBrokerContextContainer kbcc) {
        return MessageFormat.format(AclConstants.MULTI_ACCOUNT_CLIENT_ID, kbcc.getScopeId(), kbcc.getClientId());
    }

    private void enforceDeviceConnectionUserBound(Map<String, Object> options, DeviceConnection deviceConnection, KapuaId scopeId, KapuaId userId) throws KapuaException {
        if (deviceConnection != null) {
            ConnectionUserCouplingMode connectionUserCouplingMode = deviceConnection.getUserCouplingMode();
            if (ConnectionUserCouplingMode.INHERITED.equals(deviceConnection.getUserCouplingMode())) {
                connectionUserCouplingMode = loadConnectionUserCouplingModeFromConfig(scopeId, options);
            }
            enforceDeviceUserBound(connectionUserCouplingMode, deviceConnection, scopeId, userId);
        } else {
            logger.debug("Enforce Device-User bound - no device connection found so user account settings for enforcing the bound (user id - '{}')", userId);
            enforceDeviceUserBound(loadConnectionUserCouplingModeFromConfig(scopeId, options), deviceConnection, scopeId, userId);
        }
    }

    private void enforceDeviceUserBound(ConnectionUserCouplingMode connectionUserCouplingMode, DeviceConnection deviceConnection, KapuaId scopeId, KapuaId userId)
            throws KapuaException {
        if (ConnectionUserCouplingMode.STRICT.equals(connectionUserCouplingMode)) {
            if (deviceConnection == null) {
                checkConnectionCountByReservedUserId(scopeId, userId, 0);
            } else {
                if (deviceConnection.getReservedUserId() == null) {
                    checkConnectionCountByReservedUserId(scopeId, userId, 0);
                    if (!deviceConnection.getAllowUserChange() && !userId.equals(deviceConnection.getUserId())) {
                        throw new SecurityException("User not authorized!");
                        // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
                    }
                }
                else {
                    checkConnectionCountByReservedUserId(scopeId, deviceConnection.getReservedUserId(), 1);
                    if (!userId.equals(deviceConnection.getReservedUserId())) {
                        throw new SecurityException("User not authorized!");
                        // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
                    }
                }
            }
        }
        else {
            if (deviceConnection != null && deviceConnection.getReservedUserId() != null && userId.equals(deviceConnection.getReservedUserId())) {
                checkConnectionCountByReservedUserId(scopeId, userId, 1);
            } else {
                checkConnectionCountByReservedUserId(scopeId, userId, 0);
            }
        }
    }

    private void checkConnectionCountByReservedUserId(KapuaId scopeId, KapuaId userId, long count) throws KapuaException {
        // check that no devices have this user as strict user
        DeviceConnectionOptionQuery query = deviceConnectionOptionFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(DeviceConnectionOptionPredicates.RESERVED_USER_ID, userId));
        query.setPredicate(andPredicate);
        query.setLimit(1);

        Long connectionCountByReservedUserId = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionOptionService.count(query));
        if (connectionCountByReservedUserId != null && connectionCountByReservedUserId > count) {
            throw new SecurityException("User not authorized!");
            // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
        }
    }

    private ConnectionUserCouplingMode loadConnectionUserCouplingModeFromConfig(KapuaId scopeId, Map<String, Object> options) throws KapuaException {
        String tmp = (String) options.get("deviceConnectionUserCouplingDefaultMode");// TODO move to constants
        if (tmp != null) {
            ConnectionUserCouplingMode tmpConnectionUserCouplingMode = ConnectionUserCouplingMode.valueOf(tmp);
            if (tmpConnectionUserCouplingMode == null) {
                throw new SecurityException(String
                        .format("Cannot parse the default Device-User coupling mode in the registry service configuration! (found '%s' - allowed values are 'LOOSE' - 'STRICT')", tmp));
                // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
            } else {
                return tmpConnectionUserCouplingMode;
            }
        } else {
            throw new SecurityException("Cannot find default Device-User coupling mode in the registry service configuration! (deviceConnectionUserCouplingDefaultMode");
            // TODO manage the error message. is it better to throw a more specific exception or keep it obfuscated for security reason?
        }
    }

    protected void sendConnectMessage(KapuaBrokerContextContainer kbcc) {
        Context loginSendLogingUpdateMsgTimeContex = metricLoginSendLoginUpdateMsgTime.time();
        String message = systemMessageCreator.createMessage(SystemMessageType.CONNECT, kbcc);
        JmsAssistantProducerWrapper producerWrapper = null;
        try {
            producerWrapper = JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).borrowObject();
            producerWrapper.send(String.format(CONNECT_MESSAGE_TOPIC_PATTERN,
                    SystemSetting.getInstance().getMessageClassifier(), kbcc.getAccountName(), kbcc.getClientId()),
                    message,
                    kbcc);
        } catch (Exception e) {
            logger.error("Exception sending the connect message: {}", e.getMessage(), e);
        } finally {
            if (producerWrapper != null) {
                JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).returnObject(producerWrapper);
            }
        }
        loginSendLogingUpdateMsgTimeContex.stop();
    }

    @Override
    public void removeConnection(ConnectionContext context, ConnectionInfo info, Throwable error)
            throws Exception {
        if (!isPassThroughConnection(context)) {
            Context loginRemoveConnectionTimeContext = metricLoginRemoveConnectionTime.time();
            String fullClientId = null;
            try {

                KapuaSecurityContext kapuaSecurityContext = getKapuaSecurityContext(context);
                // TODO fix the kapua session when run as feature will be implemented
                KapuaPrincipal kapuaPrincipal = ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal());
                KapuaSession kapuaSession = new KapuaSession(null,
                        kapuaPrincipal.getAccountId(),
                        kapuaPrincipal.getUserId());
                KapuaSecurityUtils.setSession(kapuaSession);

                String clientId = kapuaPrincipal.getClientId();
                KapuaId accountId = kapuaPrincipal.getAccountId();
                String username = kapuaSecurityContext.getUserName();

                KapuaId scopeId = ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getAccountId();
                KapuaBrokerContextContainer kbcc = new KapuaBrokerContextContainer(brokerIdResolver.getBrokerId(this), username, clientId, info.getClientIp());

                // multiple account stealing link fix
                fullClientId = getFullClientId(scopeId.getId().longValue(), clientId);

                if (!isAdminUser(kbcc)) {
                    // Stealing link check
                    ConnectionId connectionId = CONNECTION_MAP.get(fullClientId);

                    boolean stealingLinkDetected = false;
                    if (connectionId != null) {
                        stealingLinkDetected = !connectionId.equals(info.getConnectionId());
                    } else {
                        logger.error("Cannot find connection id for client id {} on connection map. Correct connection id is {} - IP: {}",
                                new Object[] { clientId, info.getConnectionId(), info.getClientIp() });
                    }
                    if (stealingLinkDetected) {
                        metricLoginStealingLinkDisconnect.inc();
                        // stealing link detected, skip info
                        logger.warn("Detected Stealing link for cliend id {} - account id {} - last connection id was {} - current connection id is {} - IP: {} - No disconnection info will be added!",
                                new Object[] { clientId, accountId, connectionId, info.getConnectionId(), info.getClientIp() });
                    } else {
                        final DeviceConnection deviceConnection;
                        try {
                            deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(scopeId, clientId));
                        } catch (Exception e) {
                            throw new ShiroException("Error while looking for device connection on updating the device!", e);
                        }
                        if (deviceConnection != null) {
                            // the device connection must be not null
                            // update device connection (if the disconnection wasn't caused by a stealing link)
                            if (error instanceof KapuaDuplicateClientIdException) {
                                logger.debug("Skip device connection status update since is coming from a stealing link condition. Client id: {} - Connection id: {}",
                                        new Object[] { clientId, connectionId != null ? connectionId.getValue() : "NULL" });
                            } else {
                                deviceConnection.setStatus(error == null ? DeviceConnectionStatus.DISCONNECTED : DeviceConnectionStatus.MISSING);
                                try {
                                    KapuaSecurityUtils.doPrivileged(() -> {
                                        deviceConnectionService.update(deviceConnection);
                                        return null;
                                    });
                                } catch (Exception e) {
                                    throw new ShiroException("Error while updating the device connection status!", e);
                                }
                            }
                        }
                    }
                    metricClientDisconnectionClient.inc();
                } else {
                    metricClientDisconnectionKapuasys.inc();
                }
                // multiple account stealing link fix
                info.setClientId(fullClientId);
                context.setClientId(fullClientId);
            } finally {
                loginRemoveConnectionTimeContext.stop();
                authenticationService.logout();
                if (fullClientId != null) {
                    // cleanup stealing link detection map
                    CONNECTION_MAP.remove(fullClientId);
                }
            }
        }
        super.removeConnection(context, info, error);
        context.setSecurityContext(null);
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
        Context sendTimeContext = metricPublishTime.time();
        try {
            internalSend(producerExchange, messageSend);
        } finally {
            sendTimeContext.stop();
        }
    }

    private void internalSend(ProducerBrokerExchange producerExchange, Message messageSend)
            throws Exception {
        if (!StringUtils.containsNone(messageSend.getDestination().getPhysicalName(), new char[] { '+', '#' })) {
            String message = MessageFormat.format("The caracters '+' and '#' cannot be included in a topic! Destination: {}",
                    messageSend.getDestination());
            throw new SecurityException(message);
        }
        if (!isBrokerContext(producerExchange.getConnectionContext())) {
            KapuaSecurityContext kapuaSecurityContext = getKapuaSecurityContext(producerExchange.getConnectionContext());
            if (!messageSend.getDestination().isTemporary()) {
                Set<?> allowedACLs = kapuaSecurityContext.getAuthorizationMap().getWriteACLs(messageSend.getDestination());
                if (allowedACLs != null && !kapuaSecurityContext.isInOneOf(allowedACLs)) {
                    String message = MessageFormat.format("User {0} ({1} - {2} - conn id {3}) is not authorized to write to: {4}",
                            kapuaSecurityContext.getUserName(),
                            ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId(),
                            ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientIp(),
                            kapuaSecurityContext.getConnectionId(),
                            messageSend.getDestination());
                    logger.warn(message);
                    metricPublishMessageSizeNotAllowed.update(messageSend.getSize());
                    metricPublishNotAllowedMessages.inc();
                    // IMPORTANT
                    // restored the throw exception because otherwise we got acl's issues
                    throw new SecurityException(message);
                }
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
        metricPublishMessageSizeAllowed.update(messageSend.getSize());
        ActiveMQDestination destination = messageSend.getDestination();
        if (destination instanceof ActiveMQTopic) {
            ActiveMQTopic destinationTopic = (ActiveMQTopic) destination;
            messageSend.setProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, destinationTopic.getTopicName().substring(AclConstants.VT_TOPIC_PREFIX.length()));
        }
        metricPublishAllowedMessages.inc();
        super.send(producerExchange, messageSend);
    }

    // ------------------------------------------------------------------
    //
    // Consumer
    //
    // ------------------------------------------------------------------

    @Override
    public Subscription addConsumer(ConnectionContext context, ConsumerInfo info)
            throws Exception {
        Context subscribeTimeContext = subscribeTime.time();
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
            if (destinationsPath != null && destinationsPath.length >= 2 && destinationsPath[0].equals(AclConstants.VT_CONSUMER_PREFIX)) {
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
            // if (!kapuaSecurityContext.getAuthorizedReadDests().contains(info.getDestination()))
            // {
            if (!info.getDestination().isTemporary()) {
                Set<?> allowedACLs = null;
                allowedACLs = kapuaSecurityContext.getAuthorizationMap().getReadACLs(info.getDestination());
                if (allowedACLs != null && !kapuaSecurityContext.isInOneOf(allowedACLs)) {
                    String message = MessageFormat.format("User {0} ({1} - {2} - conn id {3}) is not authorized to read from: {4}",
                            kapuaSecurityContext.getUserName(),
                            ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientId(),
                            ((KapuaPrincipal) kapuaSecurityContext.getMainPrincipal()).getClientIp(),
                            kapuaSecurityContext.getConnectionId(),
                            info.getDestination());
                    logger.warn(message);
                    subscribeNotAllowedMessages.inc();
                    // IMPORTANT
                    // restored the throw exception because otherwise we got acl's issues
                    throw new SecurityException(message);
                }
                // kapuaSecurityContext.getAuthorizedReadDests().put(info.getDestination(), info.getDestination());
            }
            // }
        }
        subscribeAllowedMessages.inc();
        return super.addConsumer(context, info);
    }

    // ------------------------------------------------------------------
    //
    // Protected
    //
    // ------------------------------------------------------------------

    protected KapuaSecurityContext getKapuaSecurityContext(ConnectionContext context)
            throws SecurityException {
        SecurityContext securityContext = context.getSecurityContext();
        if (securityContext == null || !(securityContext instanceof KapuaSecurityContext)) {
            throw new SecurityException("Invalid SecurityContext.");
        }

        return (KapuaSecurityContext) securityContext;
    }

    // ------------------------------------------------------------------
    //
    // Private
    //
    // ------------------------------------------------------------------

    @SuppressWarnings("rawtypes")
    private DefaultAuthorizationMap buildAdminAuthMap(List<String> authDestinations, KapuaPrincipal principal, String fullClientId) {
        ArrayList<DestinationMapEntry> dme = new ArrayList<DestinationMapEntry>();
        String clientId = principal.getClientId();
        dme.addAll(createAuthorizationEntries(authDestinations, AclConstants.ACL_HASH,
                principal, clientId, fullClientId, true, true, true));// (topic, principal, read, write, admin)
        dme.addAll(createAuthorizationEntries(authDestinations, AclConstants.ACL_AMQ_ADVISORY,
                principal, clientId, fullClientId, false, true, true));// (topic, principal, read, write, admin)
        return new DefaultAuthorizationMap(dme);
    }

    @SuppressWarnings("unused")
    private DefaultAuthorizationMap buildProvisioningAuthMap(List<String> authDestinations, KapuaPrincipal principal, String accountName, String fullClientId) {
        @SuppressWarnings("rawtypes")
        final List<DestinationMapEntry> dme = new ArrayList<>();

        String clientId = principal.getClientId();
        // Write reply to any client Id and any application
        dme.addAll(createAuthorizationEntries(authDestinations, MessageFormat.format(AclConstants.ACL_CTRL_ACC_REPLY, accountName),
                principal, clientId, fullClientId, false, true, false));// (topic, principal, read, write, admin)

        // Publish only on MQTT/# for life-cycle messages and message that will trigger the provision service
        dme.addAll(createAuthorizationEntries(authDestinations, MessageFormat.format(AclConstants.ACL_CTRL_ACC_CLI_MQTT_LIFE_CYCLE, accountName, clientId),
                principal, clientId, fullClientId, false, true, false));// (topic, principal, read, write, admin)

        // Read any control topic on its client id to be able to receive configurations and bundles
        dme.addAll(createAuthorizationEntries(authDestinations, MessageFormat.format(AclConstants.ACL_CTRL_ACC_CLI, accountName, clientId),
                principal, clientId, fullClientId, true, false, false));// (topic, principal, read, write, admin)

        dme.addAll(createAuthorizationEntries(authDestinations, MessageFormat.format(AclConstants.ACL_CTRL_ACC, accountName),
                principal, clientId, fullClientId, false, false, true));// (topic, principal, read, write, admin)

        dme.addAll(createAuthorizationEntries(authDestinations, AclConstants.ACL_AMQ_ADVISORY,
                principal, clientId, fullClientId, false, true, true));// (topic, principal, read, write, admin)

        return new DefaultAuthorizationMap(dme);
    }

    @SuppressWarnings("rawtypes")
    private DefaultAuthorizationMap buildAuthMap(List<String> authDestinations, KapuaPrincipal principal,
            boolean[] hasPermissions,
            String accountName,
            String clientId,
            String fullClientId) {
        ArrayList<DestinationMapEntry> dme = new ArrayList<DestinationMapEntry>();

        dme.addAll(createAuthorizationEntries(authDestinations, AclConstants.ACL_AMQ_ADVISORY,
                principal, clientId, fullClientId, false, true, true));// (topic, principal, read, write, admin)

        // addConnection checks BROKER_CONNECT_IDX permission before call this method
        // then here user has BROKER_CONNECT_IDX permission and if check isn't needed
        // if (hasPermissions[BROKER_CONNECT_IDX]) {
        if (hasPermissions[AclConstants.DEVICE_MANAGE_IDX]) {
            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_CTRL_ACC, accountName),
                    principal,
                    clientId,
                    fullClientId,
                    true,
                    true,
                    true)); // (topic, principal, read, write, admin)
        } else {
            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_CTRL_ACC_CLI, accountName, clientId),
                    principal,
                    clientId,
                    fullClientId,
                    true,
                    true,
                    true)); // (topic, principal, read, write, admin)
        }

        if (hasPermissions[AclConstants.DATA_MANAGE_IDX]) {
            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_DATA_ACC, accountName),
                    principal,
                    clientId,
                    fullClientId,
                    true,
                    true,
                    true)); // (topic, principal, read, write, admin)
        } else if (hasPermissions[AclConstants.DATA_VIEW_IDX]) {
            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_DATA_ACC, accountName),
                    principal,
                    clientId,
                    fullClientId,
                    true,
                    false,
                    true)); // (topic, principal, read, write, admin)

            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_DATA_ACC_CLI, accountName, clientId),
                    principal,
                    clientId,
                    fullClientId,
                    false,
                    true,
                    false)); // (topic, principal, read, write, admin)
        } else {
            dme.addAll(createAuthorizationEntries(authDestinations,
                    MessageFormat.format(AclConstants.ACL_DATA_ACC_CLI, accountName, clientId),
                    principal,
                    clientId,
                    fullClientId,
                    true,
                    true,
                    true)); // (topic, principal, read, write, admin)
        }

        dme.addAll(createAuthorizationEntries(authDestinations,
                MessageFormat.format(AclConstants.ACL_CTRL_ACC_REPLY, accountName),
                principal,
                clientId,
                fullClientId,
                false,
                true,
                true)); // (topic, principal, read, write, admin)

        // Write notify to any client Id and any application and operation
        dme.addAll(createAuthorizationEntries(authDestinations,
                MessageFormat.format(AclConstants.ACL_CTRL_ACC_NOTIFY, accountName, clientId),
                principal,
                clientId,
                fullClientId,
                false,
                true,
                false));// (topic, principal, read, write, admin)

        return new DefaultAuthorizationMap(dme);
    }

    protected List<AuthorizationEntry> createAuthorizationEntries(List<String> authDestinations, String topic, KapuaPrincipal principal, String clientId, String fullClientId, boolean read,
            boolean write, boolean admin) {
        List<AuthorizationEntry> entries = new ArrayList<AuthorizationEntry>();
        entries.add(createAuthorizationEntry(authDestinations, topic, principal, read, write, admin));
        // added to support the vt topic name space for durable subscriptions
        if (read) {
            // logger.info("pattern {} - clientid {} - topic {} - evaluated {}", new Object[]{JmsConstants.ACL_VT_DURABLE_PREFIX[0], clientId, topic,
            // MessageFormat.format(JmsConstants.ACL_VT_DURABLE_PREFIX[0], fullClientId, topic)});
            entries.add(createAuthorizationEntry(authDestinations, MessageFormat.format(AclConstants.ACL_VT_DURABLE_PREFIX.get(0), fullClientId, topic), principal));
            // logger.info("pattern {} - clientid {} - topic {} - evaluated {}", new Object[]{JmsConstants.ACL_VT_DURABLE_PREFIX[1], clientId, topic,
            // MessageFormat.format(JmsConstants.ACL_VT_DURABLE_PREFIX[1], fullClientId, topic)});
            entries.add(createAuthorizationEntry(authDestinations, MessageFormat.format(AclConstants.ACL_VT_DURABLE_PREFIX.get(1), fullClientId, topic), principal));
        }
        return entries;
    }

    protected AuthorizationEntry createAuthorizationEntry(List<String> authDestinations, String topic, KapuaPrincipal principal, boolean read, boolean write, boolean admin) {
        AuthorizationEntry entry = new AuthorizationEntry();
        entry.setDestination(ActiveMQDestination.createDestination(topic, ActiveMQDestination.TOPIC_TYPE));
        Set<Object> writeACLs = new HashSet<Object>();
        Set<Object> readACLs = new HashSet<Object>();
        Set<Object> adminACLs = new HashSet<Object>();
        if (read) {
            readACLs.add(principal);
        }
        if (write) {
            writeACLs.add(principal);
        }
        if (admin) {
            adminACLs.add(principal);
        }
        entry.setWriteACLs(writeACLs);
        entry.setReadACLs(readACLs);
        entry.setAdminACLs(adminACLs);
        addAuthDestinationToLog(authDestinations, MessageFormat.format(AclConstants.PERMISSION_LOG,
                read ? "r" : "_",
                write ? "w" : "_",
                admin ? "a" : "_",
                topic));
        return entry;
    }

    protected AuthorizationEntry createAuthorizationEntry(List<String> authDestinations, String topic, KapuaPrincipal principal) {
        AuthorizationEntry entry = new AuthorizationEntry();
        entry.setDestination(ActiveMQDestination.createDestination(topic, ActiveMQDestination.QUEUE_TYPE));
        Set<Object> writeACLs = new HashSet<Object>();
        Set<Object> readACLs = new HashSet<Object>();
        Set<Object> adminACLs = new HashSet<Object>();
        readACLs.add(principal);
        entry.setWriteACLs(writeACLs);
        entry.setReadACLs(readACLs);
        entry.setAdminACLs(adminACLs);
        addAuthDestinationToLog(authDestinations, MessageFormat.format(AclConstants.PERMISSION_LOG, "r", "_", "_", topic));
        return entry;
    }

    protected boolean isAdminUser(KapuaBrokerContextContainer kbcc) {
        String adminUsername = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        return kbcc.getUserName().equals(adminUsername);
    }

    protected void addAuthDestinationToLog(List<String> authDestinations, String message) {
        if (logger.isDebugEnabled()) {
            authDestinations.add(message);
        }
    }

    private void logAuthDestinationToLog(List<String> authDestinations) {
        if (authDestinations != null) {
            logger.debug("Authorization map:");
            for (String str : authDestinations) {
                logger.debug(str);
            }
        }
    }

}
