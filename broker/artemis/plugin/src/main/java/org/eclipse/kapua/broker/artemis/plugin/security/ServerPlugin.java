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

import java.util.Base64;
import java.util.Map;

import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.Message;
import org.apache.activemq.artemis.core.remoting.FailureListener;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ServerConsumer;
import org.apache.activemq.artemis.core.server.ServerSession;
import org.apache.activemq.artemis.core.server.plugin.ActiveMQServerPlugin;
import org.apache.activemq.artemis.core.transaction.Transaction;
import org.apache.activemq.artemis.spi.core.protocol.RemotingConnection;
import org.apache.activemq.artemis.utils.critical.CriticalComponent;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.broker.artemis.plugin.security.connector.AcceptorHandler;
import org.eclipse.kapua.broker.artemis.plugin.security.event.BrokerEvent;
import org.eclipse.kapua.broker.artemis.plugin.security.event.BrokerEvent.EventType;
import org.eclipse.kapua.broker.artemis.plugin.security.event.BrokerEventHanldler;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.eclipse.kapua.client.security.ServiceClient.SecurityAction;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.context.SessionContext;
import org.eclipse.kapua.client.security.context.SessionContextContainer;
import org.eclipse.kapua.client.security.context.Utils;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.client.security.metric.PublishMetric;
import org.eclipse.kapua.client.security.metric.SubscribeMetric;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.client.DatabaseCheckUpdate;
import org.eclipse.kapua.service.client.message.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerPlugin implements ActiveMQServerPlugin {

    protected static Logger logger = LoggerFactory.getLogger(ServerPlugin.class);

    private static final int DEFAULT_PUBLISHED_MESSAGE_SIZE_LOG_THRESHOLD = 100000;
    private static final String MISSING_TOPIC_SUFFIX = "MQTT.LWT";

    /**
     * publish message size threshold for printing message information
     */
    private int publishInfoMessageSizeLimit;

    private LoginMetric loginMetric = LoginMetric.getInstance();
    private PublishMetric publishMetric = PublishMetric.getInstance();
    private SubscribeMetric subscribeMetric = SubscribeMetric.getInstance();

    protected BrokerEventHanldler brokerEventHanldler;
    protected AcceptorHandler acceptorHandler;
    protected ActiveMQServer server;
    protected String version;
    protected ServerContext serverContext;

    public ServerPlugin() {
        publishInfoMessageSizeLimit = BrokerSetting.getInstance().getInt(BrokerSettingKey.PUBLISHED_MESSAGE_SIZE_LOG_THRESHOLD, DEFAULT_PUBLISHED_MESSAGE_SIZE_LOG_THRESHOLD);
        //TODO find a proper way to initialize database
        DatabaseCheckUpdate databaseCheckUpdate = new DatabaseCheckUpdate();
        serverContext = ServerContext.getInstance();
        brokerEventHanldler = BrokerEventHanldler.getInstance();
        brokerEventHanldler.registerConsumer((brokerEvent) -> disconnectClient(brokerEvent));
        brokerEventHanldler.start();
    }

    @Override
    public void registered(ActiveMQServer server) {
        logger.info("registering plugin {}...", this.getClass().getName());
        try {
            this.server = server;
            serverContext.init(server);
            acceptorHandler = new AcceptorHandler(server,
                BrokerSetting.getInstance().getMap(String.class, BrokerSettingKey.ACCEPTORS));
            //init acceptors
            acceptorHandler.syncAcceptors();
        } catch (Exception e) {
            logger.error("Error while initializing {} plugin: {}", this.getClass().getName(), e.getMessage(), e);
        }
        logger.info("registering plugin {}... DONE", this.getClass().getName());
        ActiveMQServerPlugin.super.registered(server);
    }

    @Override
    public void unregistered(ActiveMQServer server) {
        logger.info("Unregistering plugin {}...", this.getClass().getName());
        ActiveMQServerPlugin.super.unregistered(server);
        shutdownAuthService();
        logger.info("Unregistering plugin {}... DONE", this.getClass().getName());
    }

    private void shutdownAuthService() {
        //nothing to do
    }

    @Override
    public void init(Map<String, String> properties) {
        version = properties.get("version");
        logger.info("Init plugin {} (version {})", this.getClass().getName(), version);
        ActiveMQServerPlugin.super.init(properties);
    }

    /**
     * CONNECT
     */

    @Override
    public void afterCreateConnection(RemotingConnection connection) throws ActiveMQException {
        String connectionId = PluginUtility.getConnectionId(connection);
        connection.addCloseListener(() -> {
            logger.info("Connection closed: {} - {}", connectionId, connectionId.getClass());
            cleanUpConnectionData(connectionId);
        });
        connection.addFailureListener(new FailureListener() {

            @Override
            public void connectionFailed(ActiveMQException exception, boolean failedOver, String scaleDownTargetNodeID) {
                logger.info("connectionFailed: {} - {} - {} - {} - {}", connectionId, connectionId.getClass(), exception.getMessage(), failedOver, scaleDownTargetNodeID);
                cleanUpConnectionData(connectionId);
            }

            @Override
            public void connectionFailed(ActiveMQException exception, boolean failedOver) {
                logger.info("connectionFailed: {} - {} - {} - {}", connectionId, connectionId.getClass(), exception.getMessage(), failedOver);
                cleanUpConnectionData(connectionId);
            }
        });
//        String clientId = ((MQTTConnection)connection).getClientID();
        logger.info("afterCreateConnection: {}", connectionId, connectionId.getClass());
        ActiveMQServerPlugin.super.afterCreateConnection(connection);//no info
    }

    @Override
    public void afterCreateSession(ServerSession session) throws ActiveMQException {
        //TODO add metrics?
        ActiveMQServerPlugin.super.afterCreateSession(session);
    }

    /**
     * DISCONNECT
     */
    @Override
    public void afterDestroyConnection(RemotingConnection connection) throws ActiveMQException {
        //TODO add metrics
//        String connectionId = securityContextHandler.getConnectionId(session.getRemotingConnection());
//        SessionContext sessionContext = securityContextHandler.getSessionContextByConnectionId(connectionId);
        logger.info("### afterDestroyConnection connection {}", connection.getID());
        serverContext.getSecurityContextHandler().printContent("afterDestroyConnection start", PluginUtility.getConnectionId(connection));
        ActiveMQServerPlugin.super.afterDestroyConnection(connection);
        serverContext.getSecurityContextHandler().printContent("afterDestroyConnection end", PluginUtility.getConnectionId(connection));
    }

    @Override
    public void beforeCloseSession(ServerSession session, boolean failed) throws ActiveMQException {
        //TODO add metrics?
        logger.info("### beforeCloseSession connection {}", session.getRemotingConnection().getID());
        ActiveMQServerPlugin.super.beforeCloseSession(session, failed);
    }

    /**
     * SUBSCRIBE
     */
    @Override
    public void afterCreateConsumer(ServerConsumer consumer) throws ActiveMQException {
        subscribeMetric.getAllowedMessages().inc();
        ActiveMQServerPlugin.super.afterCreateConsumer(consumer);
    }

    /**
     * PUBLISH
     */
    @Override
    public void beforeSend(ServerSession session, Transaction tx, Message message, boolean direct,
        boolean noAutoCreateQueue) throws ActiveMQException {
        String address = message.getAddress();
        logger.info("Publishing message on address {}", address);
        int messageSize = message.getEncodeSize();
        SessionContext sessionContext = serverContext.getSecurityContextHandler().getSessionContextByConnectionId(PluginUtility.getConnectionId(session));
        message.putStringProperty(MessageConstants.HEADER_KAPUA_CLIENT_ID, sessionContext.getClientId());
        message.putStringProperty(MessageConstants.HEADER_KAPUA_CONNECTOR_NAME, sessionContext.getConnectorName());
        message.putStringProperty(MessageConstants.HEADER_KAPUA_SESSION, Base64.getEncoder().encodeToString(SerializationUtils.serialize(sessionContext.getKapuaSession())));
        message.putLongProperty(MessageConstants.HEADER_KAPUA_RECEIVED_TIMESTAMP, KapuaDateUtils.getKapuaSysDate().getEpochSecond());
        if (!sessionContext.isInternal()) {
            if (isLwt(address)) {
                //handle the missing message case
                logger.info("Detected missing message for client {}... Flag session to tell disconnector to avoid disconnect event sending", sessionContext.getClientId());
                sessionContext.setMissing(true);
            }
            // FIX #164
            message.putStringProperty(MessageConstants.HEADER_KAPUA_CONNECTION_ID, Base64.getEncoder().encodeToString(SerializationUtils.serialize(sessionContext.getKapuaConnectionId())));
            message.putBooleanProperty(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, false);
            if (publishInfoMessageSizeLimit < messageSize) {
                logger.info("Published message size over threshold. size: {} - destination: {} - account id: {} - username: {} - clientId: {}",
                        messageSize, address, sessionContext.getAccountName(), sessionContext.getUsername(), sessionContext.getClientId());
            }
        } else {
            if (publishInfoMessageSizeLimit < messageSize) {
                logger.info("Published message size over threshold. size: {} - destination: {}",
                        messageSize, address);
            }
            message.putBooleanProperty(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, true);
        }
        message.putStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, address);
        publishMetric.getAllowedMessages().inc();
        publishMetric.getMessageSizeAllowed().update(messageSize);
        ActiveMQServerPlugin.super.beforeSend(session, tx, message, direct, noAutoCreateQueue);
    }

    private boolean isLwt(String originalTopic) {
        return originalTopic != null && originalTopic.endsWith(MISSING_TOPIC_SUFFIX);
    }

    /**
     * UTILS
     * @throws ActiveMQException
     */

    private int disconnectClient(BrokerEvent brokerEvent) {
        serverContext.getSecurityContextHandler().printContent("disconnectClient", null);
        int disconnectedClients = 0;
        if (EventType.disconnectClientByClientId.equals(brokerEvent.getEventType())) {
            disconnectedClients = disconnectClient(brokerEvent.getScopeId(), brokerEvent.getClientId());
        }
        else if (EventType.disconnectClientByConnectionId.equals(brokerEvent.getEventType())) {
            disconnectedClients = disconnectClient(brokerEvent.getOldConnectionId());
        }
        logger.info("Disconnected clients: {}", disconnectedClients);
        loginMetric.getDisconnectByEvent().inc(disconnectedClients);
        return disconnectedClients;
    }

    private int disconnectClient(KapuaId scopeId, String clientId) {
        logger.info("Disconnecting client for scopeId: {} - client id: {}", scopeId.toCompactId(), clientId);
        String fullClientId = Utils.getFullClientId(scopeId, clientId);
        return server.getSessions().stream().map(session -> {
            RemotingConnection remotingConnection = session.getRemotingConnection();
            String clientIdToCheck = PluginUtility.getConnectionId(remotingConnection);
            SessionContext sessionContext = serverContext.getSecurityContextHandler().getSessionContextByClientId(clientIdToCheck);
            String connectionFullClientId = Utils.getFullClientId(sessionContext);
            if (fullClientId.equals(connectionFullClientId)) {
                logger.info("\tclientId to check: {} - full client id: {}... CLOSE", clientIdToCheck, connectionFullClientId);
                remotingConnection.disconnect(false);
                remotingConnection.destroy();
                return 1;
            }
            else {
                logger.info("\tclientId to check: {} - full client id: {}... no action", clientIdToCheck, connectionFullClientId);
            }
            return 0;
        }).mapToInt(Integer::new).sum();
    }

    private int disconnectClient(String connectionId) {
        logger.info("Disconnecting client for connection: {}", connectionId);
        return server.getRemotingService().getConnections().stream().map(remotingConnection -> {
            int removed = 0;
            String connectionIdTmp = PluginUtility.getConnectionId(remotingConnection);
            if (connectionId.equals(connectionIdTmp)) {
                logger.info("\tconnection: {} - compared to: {} ... CLOSE", connectionId, connectionIdTmp);
                remotingConnection.disconnect(false);
                remotingConnection.destroy();
                removed++;
            }
            else {
                logger.info("\tclientId to check: {} - compared to: {} ... no action", connectionId, connectionIdTmp);
            }
            return removed;
        }).mapToInt(Integer::new).sum();
    }

    @Override
    public void duplicateSessionMetadataFailure(ServerSession session, String key, String data) throws ActiveMQException {
        logger.error("Duplicate session for key: {} - data: {}", key, data);
        //TODO add metrics?
        ActiveMQServerPlugin.super.duplicateSessionMetadataFailure(session, key, data);
    }

    @Override
    public void criticalFailure(CriticalComponent components) throws ActiveMQException {
        logger.error("Critical failure on component {}", components.toString());
        //TODO add metrics?
        ActiveMQServerPlugin.super.criticalFailure(components);
    }

    private void cleanUpConnectionData(String connectionId) {
        logger.info("### cleanUpConnectionData connection {}", connectionId);
        serverContext.getSecurityContextHandler().printContent("cleanUpConnectionData before", null);
        try {
            SessionContextContainer sessionContextContainer = serverContext.getSecurityContextHandler().getAndCleanSessionContextByConnectionId(connectionId);
            SessionContext sessionContext = sessionContextContainer.getCurrent();
            if (sessionContext!=null && !sessionContext.isInternal()) {
                serverContext.getAuthServiceClient().brokerDisconnect(new AuthRequest(
                    serverContext.getBrokerIdentity().getBrokerHost(), SecurityAction.brokerDisconnect.name(), sessionContext, sessionContextContainer.getOldConnectionId()));
            }
        }
        catch (Exception e) {
            loginMetric.getFailure().inc();
            logger.error("Cleanup connection data error: {}", e.getMessage(), e);
//            throw new ActiveMQException(ActiveMQExceptionType.SECURITY_EXCEPTION, "User not authorized!", e);
        }
        serverContext.getSecurityContextHandler().printContent("cleanUpConnectionData after", null);
    }

}