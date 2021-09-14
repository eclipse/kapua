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
package org.eclipse.kapua.transport.amqp;

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.transport.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.eclipse.kapua.transport.amqp.pooling.setting.AmqpClientPoolSetting;
import org.eclipse.kapua.transport.amqp.pooling.setting.AmqpClientPoolSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmqpQpidClient {

    protected static final Logger logger = LoggerFactory.getLogger(AmqpQpidClient.class);

    private static final int CACHE_MAX_SIZE = AmqpClientPoolSetting.getInstance().getInt(
        AmqpClientPoolSettingKeys.CLIENT_POOL_FACTORY_CACHE_MAX_ELEMENTS, 100);
    private static final int CACHE_TTL = AmqpClientPoolSetting.getInstance().getInt(
        AmqpClientPoolSettingKeys.CLIENT_POOL_FACOTRY_CACHE_TTL, 3600);

    private static LocalCache<String, ConnectionFactoryImpl> connectionFactoryCache =
        new LocalCache<String, ConnectionFactoryImpl>(CACHE_MAX_SIZE, CACHE_TTL, null);

    private ConnectionStatus connectionStatus;
    private String clientId;
    private Connection connection;
    private Session session;

    private MessageConsumerHandler messageConsumerHandler;

    private ClientOptions options;
    private DestinationTranslator destinationTranslator;

    public AmqpQpidClient(ClientOptions options) {
        this.options = options;
        connectionStatus = new ConnectionStatus();
        clientId = options.getString(AmqpClientOptions.CLIENT_ID);
        Object tmp = options.get(AmqpClientOptions.DESTINATION_TRANSLATOR);
        if (tmp == null) {
            logger.info("No destination translator defined.");
        }
        else if (tmp instanceof DestinationTranslator) {
            destinationTranslator = (DestinationTranslator) tmp;
        }
        else {
            KapuaException.internalError(String.format("The destination translator must be null or an instance of DestinationTranslator! found {}", tmp));
        }
        logger.info("Creating client: {}", clientId);
    }

    public void connect() throws IOException, NamingException, JMSException {
        String host = options.getString(AmqpClientOptions.BROKER_HOST);
        int port = options.getInt(AmqpClientOptions.BROKER_PORT, 5672);
        logger.info("Connecting client '{}' to '{}:{}'", clientId, host, port);
        connection = getConnection(getConnectionFactory(host, port));
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connectionStatus.setConnectionAlive();
        logger.info("Connecting client '{}' to '{}:{}'... DONE", clientId, host, port);
    }

    private synchronized ConnectionFactoryImpl getConnectionFactory(String host, int port) {
//      String remoteURI = new StringBuilder().
//      append("amqp://").
//      append(options.getString(AmqpClientOptions.USERNAME)).
//      append(":").
//      append(new String((char[])options.get(AmqpClientOptions.PASSWORD))).
//      append("@").
//      append(options.getString(AmqpClientOptions.BROKER_HOST)).
//      append(":").
//      append(options.getInt(AmqpClientOptions.BROKER_PORT, 5672)).toString();
//  ConnectionFactoryImpl connectionFactory = ConnectionFactoryImpl.createFromURL(remoteURI);
//  remoteURI += "?amqp.saslLayer=false";
        String cacheKey = getCacheKey(host, port);
        ConnectionFactoryImpl connectionFactory = connectionFactoryCache.get(cacheKey);
        //the method is synchronized so no concurrency issue here
        if (connectionFactory==null) {
            connectionFactory = new ConnectionFactoryImpl(
                host, port,
                options.getString(AmqpClientOptions.USERNAME),
                new String((char[])options.get(AmqpClientOptions.PASSWORD)));
            connectionFactoryCache.put(cacheKey, connectionFactory);
        }
        return connectionFactory;
    }

    private Connection getConnection(ConnectionFactoryImpl connectionFactory) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.setExceptionListener(new JMSExceptionListner(connectionStatus, clientId));
        connection.setClientID(options.getString(AmqpClientOptions.CLIENT_ID));
        connection.start();
        return connection;
    }

    private String getCacheKey(String host, int port) {
        return host + ":" + port;
    }

    public boolean isConnected() {
        try {
            return connection!=null && connection.getClientID()!=null && connectionStatus.isAlive();
        } catch (JMSException e) {
            logger.warn("Error while validating connection: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void subscribe(String subscriptionTopic, long timeout, AmqpClientConsumerHandler amqpClientConsumerHandler) throws KapuaException {
        try {
            if (messageConsumerHandler!=null) {
                throw KapuaException.internalError("Message consumer already initialized! Clean up the consumer before proceeding!");
            }
            messageConsumerHandler = new MessageConsumerHandler(session, subscriptionTopic, amqpClientConsumerHandler, destinationTranslator);
        } catch (JMSException e) {
            throw KapuaException.internalError(e, "Cannot subscribe consumer to " + subscriptionTopic);
        }
    }

    public void unsubscribe() {
        if (messageConsumerHandler!=null) {
            messageConsumerHandler.cleanup(session);
        }
    }

    public void unsubscribe(AmqpTopic amqpTopic, AmqpClientConsumerHandler amqpClientConsumerHandler) {
        unsubscribe();
    }

    public void send(byte[] message, String destination) throws KapuaException {
        try {
            Topic topic = session.createTopic(destinationTranslator.translateFromClientDomain(destination));
            MessageProducer messageProducer = session.createProducer(topic);
            BytesMessage byteMessage = session.createBytesMessage();
            byteMessage.writeBytes(message);
            messageProducer.send(byteMessage);
            byteMessage.acknowledge();
        } catch (JMSException e) {
            throw KapuaException.internalError(e, "Cannot send message to " + destination);
        }
    }

    public void clean() {
        logger.debug("Cleaning client {}", clientId);
        try {
            unsubscribe();
        }
        finally {
            messageConsumerHandler = null;
        }
    }

    public void disconnect() {
        logger.info("Disconnecting client '{}'...", clientId);
        clean();
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                logger.warn("An error occurred while closing connection for client: {}", clientId, e);
            }
        }
        logger.info("Disconnecting client '{}'... DONE", clientId);
    }

}

class ConnectionStatus {

    private boolean alive;

    boolean isAlive() {
        return alive;
    }

    void setConnectionAlive() {
        alive = true;
    }

    void setConnectionFault() {
        alive = false;
    }
}

class MessageConsumerHandler {

    private MessageConsumer messageConsumer;

    MessageConsumerHandler(Session session, String subscriptionTopic, AmqpClientConsumerHandler amqpClientConsumerHandler, DestinationTranslator destinationTranslator) throws JMSException {
        Topic topic = session.createTopic(destinationTranslator.translateFromClientDomain(subscriptionTopic));
        messageConsumer = session.createConsumer(topic);
        messageConsumer.setMessageListener(new AmqpMessageListener(amqpClientConsumerHandler, destinationTranslator));
    }

    void cleanup(Session session) {
        try {
            if (messageConsumer!=null) {
                messageConsumer.close();
            }
        } catch (JMSException e) {
            AmqpQpidClient.logger.error("Error while closing message consumer: {}", e.getMessage(), e);
        }
        finally {
            messageConsumer = null;
        }
    }
}

class AmqpMessageListener implements MessageListener {

    private AmqpClientConsumerHandler consumerHandler;
    private DestinationTranslator destinationTranslator;

    AmqpMessageListener(AmqpClientConsumerHandler consumerHandler, DestinationTranslator destinationTranslator) {
        this.consumerHandler = consumerHandler;
        this.destinationTranslator = destinationTranslator;
    }

    @Override
    public void onMessage(Message message) {
        if (message != null) {
            consumerHandler.consumeMessage(message, destinationTranslator);
        }
    }

}

class JMSExceptionListner implements ExceptionListener {

    private ConnectionStatus connectionStatus;
    private String clientId;

    JMSExceptionListner(ConnectionStatus connectionStatus, String clientId) {
        this.connectionStatus = connectionStatus;
        this.clientId = clientId;
    }

    @Override
    public void onException(JMSException e) {
        connectionStatus.setConnectionFault();
        AmqpQpidClient.logger.warn("Client: {} - Error: {} ", clientId, e.getMessage());
    }
}
