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
package org.eclipse.kapua.eclipseiot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.hono.client.HonoClient;
import org.eclipse.hono.client.MessageConsumer;
import org.eclipse.hono.client.impl.HonoClientImpl;
import org.eclipse.hono.config.ClientConfigProperties;
import org.eclipse.hono.util.MessageHelper;
import org.eclipse.hono.util.MessageTap;
import org.eclipse.hono.util.TimeUntilDisconnectNotification;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.connector.AbstractConnectorVerticle;
import org.eclipse.kapua.connector.KapuaConnectorException;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.eclipseiot.setting.EclipseiotSetting;
import org.eclipse.kapua.eclipseiot.setting.EclipseiotSettingKey;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportQos;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClientOptions;

public class EclipseIotVerticle extends AbstractConnectorVerticle<byte[], TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(EclipseIotVerticle.class);

    //TODO set the constant according to Hono (does Hono set the QoS in the message properties?)
    private final static String ACTIVEMQ_QOS = "ActiveMQ.MQTT.QoS";

    private String username = EclipseiotSetting.getInstance().getString(EclipseiotSettingKey.HONO_USERNAME);
    private String password = EclipseiotSetting.getInstance().getString(EclipseiotSettingKey.HONO_PASSWORD);
    private String host = EclipseiotSetting.getInstance().getString(EclipseiotSettingKey.HONO_HOST);
    private int port = EclipseiotSetting.getInstance().getInt(EclipseiotSettingKey.HONO_PORT);
    private List<String> tenantId = EclipseiotSetting.getInstance().getList(String.class, EclipseiotSettingKey.HONO_TENANT_ID);
    private String trustStoreFile = EclipseiotSetting.getInstance().getString(EclipseiotSettingKey.HONO_TRUSTSTORE_FILE);

    private HonoClient honoClient;

    public EclipseIotVerticle(Vertx vertx, Converter<byte[], TransportMessage> converter, Processor<TransportMessage> processor) {
        super(converter, processor);
        this.vertx = vertx;
    }

    @Override
    protected void startInternal(Future<Void> startFuture) throws KapuaConnectorException {
        honoClient = new HonoClientImpl(vertx, getClientConfigProperties());
        final Future<MessageConsumer> consumerFuture = Future.future();
        consumerFuture.setHandler(result -> {
            if (!result.succeeded()) {
                logger.error("Hono client - cannot not create telemetry consumer for {}:{} - {}", host, port, result.cause());
            }
        });
        //TODO how to handle subscription to multiple tenants ids?
        honoClient.connect(getProtonClientOptions()).compose(connectedClient -> {
                // create the telemetryHandler by using the helper functionality for demultiplexing messages to callbacks
                final Consumer<Message> telemetryHandler = MessageTap.getConsumer(
                        this::handleTelemetryMessage, this::handleCommandReadinessNotification);
                Future<MessageConsumer> futureConsumer = connectedClient.createTelemetryConsumer(tenantId.get(0),
                        telemetryHandler, closeHook -> logger.error("remotely detached consumer link"));
                if (!startFuture.isComplete()) {
                    startFuture.complete();
                }
                return futureConsumer;
        }).setHandler(consumerFuture.completer());
    }

    private void handleTelemetryMessage(Message message) {
        logger.info("handleProtonMessage...");
        logTelemetryMessage(message);
        try {

            // build the message properties
            Map<String, Object> convertedMsgProperties = new HashMap<String, Object>();

            // extract original MQTT topic
            String mqttTopic = message.getProperties().getTo();
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
        } catch (KapuaConnectorException | KapuaConverterException | KapuaProcessorException e) {
            logger.warn("Error processing message: {}", e.getMessage(), e);
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
        logger.info("received telemetry message:\n    message id '{}' userId '{}' destination '{}' original destination '{}' adapter '{}' tenant '{}' - device '{}' - content-type '{}' - content {}", 
            messageId, userId, to, origAddress, adapter, tenantId, deviceId, msg.getContentType(), ((Data) msg.getBody()).getValue().toString());
    }

    private void handleCommandReadinessNotification(final TimeUntilDisconnectNotification notification) {
    }

    private ClientConfigProperties getClientConfigProperties() {
        ClientConfigProperties props = new ClientConfigProperties();
        props.setHost(host);
        props.setPort(port);
        props.setUsername(username);
        props.setPassword(password);
        try {
            props.setTrustStorePath(KapuaFileUtils.getAsFile(trustStoreFile).toString());
        } catch (KapuaSettingException e) {
            logger.warn("Cannot find truststore configuration file: {}", e.getMessage(), e);
        }
        props.setHostnameVerificationRequired(false);
        return props;
    }

    public ProtonClientOptions getProtonClientOptions() {
        ProtonClientOptions opts = new ProtonClientOptions();
        //TODO do we need to set some parameters?
        return opts;
    }

    @Override
    protected void stopInternal(Future<Void> stopFuture) throws KapuaConnectorException {
        honoClient.shutdown(event -> {
            logger.info("Closing connection {}", event);
            if (!stopFuture.isComplete()) {
                stopFuture.complete();
            }
        }
        );
    }

}
