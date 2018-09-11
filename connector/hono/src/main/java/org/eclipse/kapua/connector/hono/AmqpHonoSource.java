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
package org.eclipse.kapua.connector.hono;

import java.util.HashMap;
import java.util.Map;

import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.message.Message;
import org.eclipse.hono.util.MessageHelper;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.hono.HonoClient;
import org.eclipse.kapua.connector.AbstractAmqpSource;
import org.eclipse.kapua.connector.KapuaConverterException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.MessageFilter;
import org.eclipse.kapua.connector.MessageHandler;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.message.transport.TransportQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Amqp Hono connector implementation
 *
 */
public class AmqpHonoSource extends AbstractAmqpSource<byte[]> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpHonoSource.class);

    private final static String CONTROL_PREFIX = "c/";
    private final static String TELEMETRY_PREFIX = "t/";

    private HonoClient honoClient;

    public AmqpHonoSource(Vertx vertx) {
        super(vertx);
        honoClient = new HonoClient(vertx, this::handleTelemetryMessage);
    }

    @Override
    public void start(Future<Void> startFuture) {
        connect(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        disconnect(stopFuture);
    }

    @Override
    protected void connect(Future<Void> connectFuture) {
        logger.info("Opening broker connection...");
        honoClient.connect(connectFuture);
    }

    @Override
    protected void disconnect(Future<Void> disconnectFuture) {
        logger.info("Closing broker connection...");
        honoClient.disconnect(disconnectFuture);
    }

    private void handleTelemetryMessage(final Message message) {
        logTelemetryMessage(message);
        try {
            //TODO fix me!
            try {
                MessageContext<byte[]> msg = convert(message);
                if (isProcessMessage(msg)) {
                    messageHandler.handle(msg, result -> {
                    if (result.succeeded()) {
                        //TODO handle ProtonHelper.accepted
                    }
                    else {
                        //TODO handle ProtonHelper.released
                    }
                });
                }
            } catch (Exception e) {
                logger.error("Exception while processing message: {}", e.getMessage(), e);
                //TODO handle ProtonHelper.released
            }
        } catch (Exception e) {
            logger.error("Exception while processing message: {}", e.getMessage(), e);
        }
    }

    private void logTelemetryMessage(final Message msg) {
        String messageId, userId, to, adapter, origAddress;
        messageId = userId = to = adapter = origAddress = null;
        if (msg.getProperties()!=null) {
            messageId = msg.getProperties().getMessageId() != null ? msg.getProperties().getMessageId().toString() : null;
            userId = msg.getProperties().getUserId() != null ? msg.getProperties().getUserId().toString() : null;
            to = msg.getProperties().getTo();
        }
        if (msg.getApplicationProperties()!=null && msg.getApplicationProperties().getValue()!=null) {
            adapter = (String)msg.getApplicationProperties().getValue().get("orig_adapter");
            origAddress = (String)msg.getApplicationProperties().getValue().get("orig_address");
        }
        String deviceId = MessageHelper.getDeviceId(msg);
        String tenantId = MessageHelper.getTenantId(msg);
        if (tenantId==null && msg.getMessageAnnotations() != null && msg.getMessageAnnotations().getValue() != null) {
            tenantId = msg.getMessageAnnotations().getValue().get(Symbol.getSymbol("tenant_id")).toString();
        }
        logger.info("received telemetry message:\n\tmessage id '{}' userId '{}' destination '{}' original destination '{}' adapter '{}' tenant '{}' - device '{}' - content-type '{}' - content {}", 
            messageId, userId, to, origAddress, adapter, tenantId, deviceId, msg.getContentType(), ((Data) msg.getBody()).getValue().toString());
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
    protected Map<String, Object> getMessageParameters(Message message) throws KapuaConverterException {
        Map<String, Object> parameters = new HashMap<>();
        // build the message properties
        // extract original MQTT topic
        String mqttTopic = (String)message.getApplicationProperties().getValue().get("orig_address");
        mqttTopic = mqttTopic.replace(".", "/");
        if (mqttTopic.startsWith(TELEMETRY_PREFIX)) {
            parameters.put(Properties.MESSAGE_TYPE, TransportMessageType.TELEMETRY);
            mqttTopic = mqttTopic.substring(TELEMETRY_PREFIX.length());
        }
        else if (mqttTopic.startsWith(CONTROL_PREFIX)) {
            parameters.put(Properties.MESSAGE_TYPE, TransportMessageType.CONTROL);
            mqttTopic = mqttTopic.substring(CONTROL_PREFIX.length());
        }
        //TODO handle alerts, ... messages types
        parameters.put(Properties.MESSAGE_DESTINATION, mqttTopic);

        // extract the original QoS
        //TODO
        parameters.put(Properties.MESSAGE_QOS, TransportQos.AT_MOST_ONCE);
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
