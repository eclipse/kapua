/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.activemq;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.Map;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.connector.AmqpAbstractConnector;
import org.eclipse.kapua.connector.KapuaConnectorException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.activemq.settings.ConnectorActiveMQSettings;
import org.eclipse.kapua.connector.activemq.settings.ConnectorActiveMQSettingsKey;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.message.transport.TransportQos;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;

/**
 * Amqp ActiveMQ connector implementation
 *
 */
public class AmqpActiveMQConnector extends AmqpAbstractConnector<TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpActiveMQConnector.class);

    private final static String ACTIVEMQ_QOS = "ActiveMQ.MQTT.QoS";
    private final static String VIRTUAL_TOPIC_PREFIX = "topic://VirtualTopic.";
    private final static int VIRTUAL_TOPIC_PREFIX_LENGTH = VIRTUAL_TOPIC_PREFIX.length();
    private final static String QUEUE_PATTERN = "queue://Consumer.%s.VirtualTopic.>";

    private ProtonClient client;
    private ProtonConnection connection;

    private String brokerHost;
    private int brokerPort;

    public AmqpActiveMQConnector(Vertx vertx, Converter<byte[], TransportMessage> converter, Processor<TransportMessage> processor) {
        super(vertx, converter, processor);

        brokerHost = ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_HOST);
        brokerPort = ConnectorActiveMQSettings.getInstance().getInt(ConnectorActiveMQSettingsKey.BROKER_PORT);
    }

    @Override
    public void startInternal(Future<Void> startFuture) throws KapuaConnectorException {
        // make sure connection is already closed
        closeConnection();

        logger.info("Connecting to broker {}:{}...", brokerHost, brokerPort);
        client = ProtonClient.create(vertx);
        client.connect(
                brokerHost,
                brokerPort,
                ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_USERNAME),
                ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_PASSWORD),
                this::handleProtonConnection);

        logger.info("Connecting to broker {}:{}... Done.", brokerHost, brokerPort);

    }

    @Override
    public void stopInternal(Future<Void> stopFuture) throws KapuaConnectorException {
        logger.info("Closing broker connection...");
        closeConnection();
    }

    private void closeConnection() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private void registerConsumer(ProtonConnection connection) {

        String queue = String.format(QUEUE_PATTERN,
                ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_CLIENT_ID));
        logger.info("Register consumer for queue {}...", queue);
        connection.open();
        this.connection = connection;

        // The client ID is set implicitly into the queue subscribed
        connection.createReceiver(queue).handler(this::handleInternalMessage).open();
        logger.info("Register consumer for queue {}... DONE", queue);
    }

    /**
     * Callback for Connection Handler implementing interface Handler<AsyncResult<ProtonConnection>>
     *
     * @param event
     */
    public void handleProtonConnection(AsyncResult<ProtonConnection> event) {
        if (event.succeeded()) {
            // register the message consumer
            logger.info("Connecting to broker {}:{}... Creating receiver...", brokerHost, brokerPort);
            registerConsumer(event.result());
            logger.info("Connecting to broker {}:{}... Creating receiver... DONE", brokerHost, brokerPort);
        } else {
            logger.error("Cannot register kafka consumer! ", event.cause().getCause());
        }
    }

    /**
     * Callback for Proton Message receiver implementing interface ProtonMessageHandler
     *
     * @param delivery
     * @param message
     */
    public void handleInternalMessage(ProtonDelivery delivery, Message message) {
        try {
            super.handleMessage(new MessageContext<Message>(message));
        } catch (KapuaConnectorException | KapuaConverterException | KapuaProcessorException e) {
            logger.error("Exception while processing message: {}", e.getMessage(), e);
        }
    }

    @Override
    protected Map<String, Object> getMessageParameters(Message message) throws KapuaConverterException {
        Map<String, Object> parameters = new HashMap<>();
        // extract original MQTT topic
        String mqttTopic = message.getProperties().getTo(); // topic://VirtualTopic.kapua-sys.02:42:AC:11:00:02.heater.data
        mqttTopic = mqttTopic.substring(VIRTUAL_TOPIC_PREFIX_LENGTH);
        mqttTopic = mqttTopic.replace(".", "/");
        // process prefix and extract message type
        // FIXME: pluggable message types and dialects
        if ("$EDC".equals(mqttTopic)) {
            parameters.put(Converter.MESSAGE_TYPE, TransportMessageType.CONTROL);
            mqttTopic = mqttTopic.substring("$EDC".length());
        } else {
            parameters.put(Converter.MESSAGE_TYPE, TransportMessageType.TELEMETRY);
        }
        parameters.put(Converter.MESSAGE_DESTINATION, mqttTopic);

        // extract the original QoS
        Object activeMqQos = message.getApplicationProperties().getValue().get(ACTIVEMQ_QOS);
        if (activeMqQos != null && activeMqQos instanceof Integer) {
            int activeMqQosInt = (int) activeMqQos;
            switch (activeMqQosInt) {
            case 0:
                parameters.put(Converter.MESSAGE_QOS, TransportQos.AT_MOST_ONCE);
                break;
            case 1:
                parameters.put(Converter.MESSAGE_QOS, TransportQos.AT_LEAST_ONCE);
                break;
            case 2:
                parameters.put(Converter.MESSAGE_QOS, TransportQos.EXACTLY_ONCE);
                break;
            }
        }
        return parameters;
    }

}
