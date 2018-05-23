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
package org.eclipse.kapua.connector.amqp;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Future;
import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.connector.AbstractConnectorVerticle;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.KapuaConnectorException;
import org.eclipse.kapua.connector.Processor;
import org.eclipse.kapua.connector.amqp.settings.ConnectorSettings;
import org.eclipse.kapua.connector.amqp.settings.ConnectorSettingsKey;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;

public class AmqpConnectorVerticle extends AbstractConnectorVerticle<byte[], TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpConnectorVerticle.class);

    // TODO: Make this parametrizable
    private final static String VIRTUAL_TOPIC_PREFIX = "topic://VirtualTopic.";
    private final static int VIRTUAL_TOPIC_PREFIX_LENGTH = VIRTUAL_TOPIC_PREFIX.length();
    private final static String ACTIVEMQ_QOS = "ActiveMQ.MQTT.QoS";
    private final static String QUEUE_PATTERN = "queue://Consumer.%s.VirtualTopic.>";// Consumer.*.VirtualTopic.>

    private ProtonClient client;
    private ProtonConnection connection;

    // providers
    private String brokerHost;
    private int brokerPort;

    public AmqpConnectorVerticle(Converter<byte[], TransportMessage> converter, Processor<TransportMessage> processor) {
        super(converter, processor);

        brokerHost = ConnectorSettings.getInstance().getString(ConnectorSettingsKey.BROKER_HOST);
        brokerPort = ConnectorSettings.getInstance().getInt(ConnectorSettingsKey.BROKER_PORT);
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
                ConnectorSettings.getInstance().getString(ConnectorSettingsKey.BROKER_USERNAME),
                ConnectorSettings.getInstance().getString(ConnectorSettingsKey.BROKER_PASSWORD),
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
                ConnectorSettings.getInstance().getString(ConnectorSettingsKey.BROKER_CLIENT_ID));
        logger.info("Register consumer for queue {}...", queue);
        connection.open();
        this.connection = connection;

        // The client ID is set implicitly into the queue subscribed
        connection.createReceiver(queue).handler(this::handleProtonMessage).open();
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
    public void handleProtonMessage(ProtonDelivery delivery, Message message) {
        logger.info("handleProtonMessage...");
        try {

            // build the message properties
            Map<String, Object> convertedMsgProperties = new HashMap<String, Object>();

            // extract original MQTT topic
            String mqttTopic = message.getProperties().getTo(); // topic://VirtualTopic.kapua-sys.02:42:AC:11:00:02.heater.data
            mqttTopic = mqttTopic.substring(VIRTUAL_TOPIC_PREFIX_LENGTH);
            mqttTopic = mqttTopic.replace(".", "/");
            convertedMsgProperties.put(Converter.MESSAGE_DESTINATION, mqttTopic);

            // extract the original QoS
            Object activeMqQos = message.getApplicationProperties().getValue().get(ACTIVEMQ_QOS);
            if (activeMqQos != null && activeMqQos instanceof Integer) {
                int activeMqQosInt = (int) activeMqQos;                
                switch (activeMqQosInt) {
                case 0:
                    convertedMsgProperties.put(Converter.MESSAGE_QOS, TransportQos.AT_MOST_ONCE);
                    break;
                case 1:
                    convertedMsgProperties.put(Converter.MESSAGE_QOS, TransportQos.AT_LEAST_ONCE);
                    break;
                case 2:
                    convertedMsgProperties.put(Converter.MESSAGE_QOS, TransportQos.EXACTLY_ONCE);
                    break;
                }                
            }

            // process the incoming message
            byte[] messageBody = extractBytePayload(message.getBody());
            super.handleMessage(convertedMsgProperties, messageBody);
        } catch (KapuaConnectorException e) {
            logger.warn("Error processing message: " + e.getMessage(), e);
        }

        // By default, the receiver automatically accepts (and settles) the delivery
        // when the handler returns, if no other disposition has been applied.
        // To change this and always manage dispositions yourself, use the
        // setAutoAccept method on the receiver.
    }

    private static byte[] extractBytePayload(Section body) throws KapuaConnectorException {
        logger.info("Received message with body: {}", body);
        if (body instanceof Data) {
            Binary data = ((Data) body).getValue();
            logger.info("Received DATA message");
            return data.getArray();
        } else if (body instanceof AmqpValue) {
            String content = (String) ((AmqpValue) body).getValue();
            logger.info("Received message with content: {}", content);
            return content.getBytes();
        } else {
            logger.warn("Recevide message with unknown message type! ({})", body != null ? body.getClass() : "NULL");
            // TODO use custom exception
            throw new KapuaConnectorException(KapuaErrorCodes.INTERNAL_ERROR);
        }
    }
}
