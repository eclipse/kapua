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

import java.util.HashMap;
import java.util.Map;
//import java.util.function.Consumer;

import org.apache.qpid.proton.message.Message;
//import org.eclipse.hono.client.MessageConsumer;
//import org.eclipse.hono.util.MessageTap;
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

import io.vertx.core.Context;
import io.vertx.core.Future;
//import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;

/**
 * AMQP ActiveMQ connector implementation
 *
 */
public class AmqpActiveMQConnector extends AmqpAbstractConnector<TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpActiveMQConnector.class);

    private final static String ACTIVEMQ_QOS = "ActiveMQ.MQTT.QoS";
    private final static String VIRTUAL_TOPIC_PREFIX = "topic://VirtualTopic.";
    private final static int VIRTUAL_TOPIC_PREFIX_LENGTH = VIRTUAL_TOPIC_PREFIX.length();
    private final static String QUEUE_PATTERN = "queue://Consumer.%s.VirtualTopic.>";

    private Context context;
    private ProtonClient client;
    private ProtonConnection connection;

    private String brokerHost;
    private int brokerPort;

    public AmqpActiveMQConnector(Vertx vertx, Converter<byte[], TransportMessage> converter, Processor<TransportMessage> processor) {
        super(vertx, converter, processor);

        brokerHost = ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_HOST);
        brokerPort = ConnectorActiveMQSettings.getInstance().getInt(ConnectorActiveMQSettingsKey.BROKER_PORT);
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(AmqpAbstractConnector.KEY_MAX_RECONNECTION_ATTEMPTS, ConnectorActiveMQSettings.getInstance().getInt(ConnectorActiveMQSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        configuration.put(AmqpAbstractConnector.KEY_EXIT_CODE, ConnectorActiveMQSettings.getInstance().getInt(ConnectorActiveMQSettingsKey.EXIT_CODE));
        setConfiguration(configuration);

        context = vertx.getOrCreateContext();
    }

    @Override
    public void startInternal(Future<Void> startFuture) {
        connect(startFuture);
    }

    protected void connect(Future<Void> startFuture) {
        logger.info("Connecting to broker {}:{}...", brokerHost, brokerPort);
        // make sure connection is already closed
        if (connection != null && !connection.isDisconnected()) {
            if (!startFuture.isComplete()) {
                startFuture.fail("Unable to connect: still connected");
            }
            return;
        }

        client = ProtonClient.create(vertx);
        client.connect(
                brokerHost,
                brokerPort,
                ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_USERNAME),
                ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_PASSWORD),
                asynchResult ->{
                    if (asynchResult.succeeded()) {
                        logger.info("Connecting to broker {}:{}... Creating receiver... DONE", brokerHost, brokerPort);
                        context.executeBlocking(future -> registerConsumer(asynchResult.result(), future), result -> {
                            if (result.succeeded()) {
                                logger.debug("Starting connector...DONE");
                                startFuture.complete();
                            } else {
                                logger.warn("Starting connector...FAIL [message:{}]", asynchResult.cause().getMessage());
                                if (!startFuture.isComplete()) {
                                    startFuture.fail(asynchResult.cause());
                                }
                                notifyConnectionLost();
                            }
                        });
                    } else {
                        logger.error("Cannot register ActiveMQ connection! ", asynchResult.cause().getCause());
                        if (!startFuture.isComplete()) {
                            startFuture.fail(asynchResult.cause());
                        }
                        notifyConnectionLost();
                    }
                });

    }

    @Override
    public void stopInternal(Future<Void> stopFuture) {
        disconnect(stopFuture);
    }

    @Override
    protected void disconnect(Future<Void> stopFuture) {
        logger.info("Closing broker connection...");
        if (connection != null) {
            connection.close();
            connection = null;
            stopFuture.complete();
        }
    }

    private void registerConsumer(ProtonConnection connection, Future<Object> future) {
        try {
            String queue = String.format(QUEUE_PATTERN,
                    ConnectorActiveMQSettings.getInstance().getString(ConnectorActiveMQSettingsKey.BROKER_CLIENT_ID));
            logger.info("Register consumer for queue {}...", queue);
            connection.open();

            // The client ID is set implicitly into the queue subscribed
            connection.createReceiver(queue).handler(this::handleInternalMessage).open();
            logger.info("Register consumer for queue {}... DONE", queue);
            future.complete();
        }
        catch(Exception e) {
            future.fail(e);
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
