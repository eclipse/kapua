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

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.connector.AbstractAmqpSource;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.MessageFilter;
import org.eclipse.kapua.connector.MessageHandler;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.message.transport.TransportQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonDelivery;
import io.vertx.proton.ProtonHelper;

/**
 * AMQP ActiveMQ connector implementation.<br>
 * This connector implicitly transform the incoming messages to {@link TransportMessage} with incoming message body (byte[]) as content
 */
public class AmqpTransportActiveMQSource extends AbstractAmqpSource<byte[]> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpTransportActiveMQSource.class);

    private final static String ACTIVEMQ_QOS = "ActiveMQ.MQTT.QoS";
    private final static String TOPIC_SEPARATOR = "/";
    private final static String CLASSIFIER_TOPIC_PREFIX = SystemSetting.getInstance().getMessageClassifier() + TOPIC_SEPARATOR;

    private AmqpConsumer consumer;

    public static AmqpTransportActiveMQSource create(Vertx vertx, AmqpConsumer consumer) {
        return new AmqpTransportActiveMQSource(vertx, consumer);
    }

    private AmqpTransportActiveMQSource(Vertx vertx, AmqpConsumer consumer) {
        super(vertx);
        this.consumer = consumer;        
        this.consumer.messageHandler(this::handleMessage);
    }

    @Override
    public void start(Future<Void> startFuture) {
        // TODO move client options from contructor to init and build from JsonObject
        connect(startFuture);
    }

    private void handleMessage(ProtonDelivery delivery, Message message) {
        try {
            MessageContext<byte[]> msg = convert(message);
            if (isProcessMessage(msg)) {
                messageHandler.handle(msg, result -> {
                    if (result.succeeded()) {
                        ProtonHelper.accepted(delivery, true);
                    }
                    else {
                        ProtonHelper.released(delivery, true);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Exception while processing message: {}", e.getMessage(), e);
            ProtonHelper.released(delivery, true);
        }
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        disconnect(stopFuture);
    }

    @Override
    protected void connect(Future<Void> connectFuture) {
        logger.info("Opening broker connection...");
        consumer.connect(connectFuture);
    }

    @Override
    protected void disconnect(Future<Void> disconnectFuture) {
        logger.info("Closing broker connection...");
        consumer.disconnect(disconnectFuture);
    }

    protected MessageContext<byte[]> convert(Message message) throws KapuaException {
        //this cast is safe since this implementation is using the AMQP connector
        return new MessageContext<byte[]>(
                extractBytePayload(message.getBody()),
                getMessageParameters(message));

        // By default, the receiver automatically accepts (and settles) the delivery
        // when the handler returns, if no other disposition has been applied.
        // To change this and always manage dispositions yourself, use the
        // setAutoAccept method on the receiver.
    }

    @Override
    protected Map<String, Object> getMessageParameters(Message message) throws KapuaException {
        Map<String, Object> parameters = new HashMap<>();
        // extract original MQTT topic
        //TODO restore it once the ActiveMQ issue will be fixed
        //String mqttTopic = message.getProperties().getTo(); // topic://VirtualTopic.kapua-sys.02:42:AC:11:00:02.heater.data
        String mqttTopic = (String)message.getApplicationProperties().getValue().get("originalTopic");
        mqttTopic = mqttTopic.replace(".", "/");
        // process prefix and extract message type
        // FIXME: pluggable message types and dialects
        if (mqttTopic!=null && mqttTopic.startsWith(CLASSIFIER_TOPIC_PREFIX)) {
            parameters.put(Properties.MESSAGE_TYPE, TransportMessageType.CONTROL);
            mqttTopic = mqttTopic.substring(CLASSIFIER_TOPIC_PREFIX.length());
        } else {
            parameters.put(Properties.MESSAGE_TYPE, TransportMessageType.TELEMETRY);
        }
        parameters.put(Properties.MESSAGE_DESTINATION, mqttTopic);

        //extract connection id
        try {
            //Base 64 encoded String (by the broker plugin)
            Object tmp = message.getApplicationProperties().getValue().get(Properties.HEADER_KAPUA_CONNECTION_ID);
            if (tmp!=null && !(tmp instanceof String)) {
                throw KapuaException.internalError(String.format("Invalid connection id instance type. Found %s instead of String", tmp.getClass()));
            }
            String connectionId = (String)tmp;
            if (connectionId!=null) {
                parameters.put(Properties.CONNECTION_ID, connectionId);
            }
        }
        catch(NullPointerException | IllegalArgumentException e) {
            //cannot get connection id so it may be not available at publish time
            logger.debug("Cannot get Kapua connection Id: {}", e.getMessage(), e);
        }

        // extract the original QoS
        Object activeMqQos = message.getApplicationProperties().getValue().get(ACTIVEMQ_QOS);
        if (activeMqQos != null && activeMqQos instanceof Integer) {
            int activeMqQosInt = (int) activeMqQos;
            switch (activeMqQosInt) {
            case 0:
                parameters.put(Properties.MESSAGE_QOS, TransportQos.AT_MOST_ONCE);
                break;
            case 1:
                parameters.put(Properties.MESSAGE_QOS, TransportQos.AT_LEAST_ONCE);
                break;
            case 2:
                parameters.put(Properties.MESSAGE_QOS, TransportQos.EXACTLY_ONCE);
                break;
            }
        }
        return parameters;
    }

    private MessageHandler<byte[]> messageHandler;

    @Override
    public void messageHandler(MessageHandler<byte[]> handler) {
        this.messageHandler = handler;
    }

    MessageFilter<byte[]> filter;

    @Override
    public void messageFilter(MessageFilter<byte[]> filter) {
        this.filter = filter;
    }

    protected boolean isProcessMessage(MessageContext<byte[]> message) {
        return filter.matches(message);
    }
}
