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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.message.Message;
import org.eclipse.hono.client.HonoClient;
import org.eclipse.hono.client.MessageConsumer;
import org.eclipse.hono.client.impl.HonoClientImpl;
import org.eclipse.hono.config.ClientConfigProperties;
import org.eclipse.hono.util.MessageHelper;
import org.eclipse.hono.util.MessageTap;
import org.eclipse.hono.util.TimeUntilDisconnectNotification;
import org.eclipse.kapua.connector.AmqpAbstractConnector;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.hono.settings.ConnectorHonoSetting;
import org.eclipse.kapua.connector.hono.settings.ConnectorHonoSettingKey;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.message.transport.TransportQos;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClientOptions;

/**
 * Amqp Hono connector implementation
 *
 */
public class AmqpHonoConnector extends AmqpAbstractConnector<TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpHonoConnector.class);

    private final static String CONTROL_PREFIX = "c/";
    private final static String TELEMETRY_PREFIX = "t/";

    private String username = ConnectorHonoSetting.getInstance().getString(ConnectorHonoSettingKey.HONO_USERNAME);
    private String password = ConnectorHonoSetting.getInstance().getString(ConnectorHonoSettingKey.HONO_PASSWORD);
    private String host = ConnectorHonoSetting.getInstance().getString(ConnectorHonoSettingKey.HONO_HOST);
    private int port = ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.HONO_PORT);
    private int maxReconnectAttempts = ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.HONO_PROTON_MAX_RECONNECT_ATTEMPTS);
    private long waitBetweenReconnect = ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.HONO_PROTON_WAIT_BETWEEN_RECONNECT);
    private int connectTimeout = ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.HONO_PROTON_CONNECT_TIMEOUT);
    private int idleTimeout = ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.HONO_PROTON_IDLE_TIMEOUT);
    private List<String> tenantId = ConnectorHonoSetting.getInstance().getList(String.class, ConnectorHonoSettingKey.HONO_TENANT_ID);
    private String trustStoreFile = ConnectorHonoSetting.getInstance().getString(ConnectorHonoSettingKey.HONO_TRUSTSTORE_FILE);

    private HonoClient honoClient;

    public AmqpHonoConnector(Vertx vertx, Converter<byte[], TransportMessage> converter, Processor<TransportMessage> processor) {
        super(vertx, converter, processor);
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(AmqpAbstractConnector.KEY_MAX_RECONNECTION_ATTEMPTS, ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.MAX_RECONNECTION_ATTEMPTS));
        configuration.put(AmqpAbstractConnector.KEY_EXIT_CODE, ConnectorHonoSetting.getInstance().getInt(ConnectorHonoSettingKey.EXIT_CODE));
        setConfiguration(configuration);
    }

    @Override
    protected void startInternal(final Future<Void> startFuture) {
        connect(startFuture);
    }

    @Override
    protected void stopInternal(final Future<Void> stopFuture) {
        disconnect(stopFuture);
    }

    protected void connect(final Future<Void> connectFuture) {
        logger.info("Hono client - Connecting to {}:{}", host, port);
        if (honoClient != null) {
            //try to disconnect the client
            Future<Void> tmpFuture = Future.future();
            tmpFuture.setHandler(result -> {
                if (!result.succeeded()) {
                    logger.warn("Hono client - Cannot close connection... may be the connection was already closed!", result.cause());
                }
                else {
                    logger.debug("Hono client - Connection closed");
                }
            });
            disconnect(tmpFuture);
        }
        honoClient = new HonoClientImpl(vertx, getClientConfigProperties());
        //TODO handle subscription to multiple tenants ids
        honoClient.connect(
                getProtonClientOptions(),
                protonConnection -> notifyConnectionLost()
                ).compose(connectedClient -> {
                final Consumer<Message> telemetryHandler = MessageTap.getConsumer(
                        this::handleTelemetryMessage, this::handleCommandReadinessNotification);
                Future<MessageConsumer> futureConsumer = connectedClient.createTelemetryConsumer(tenantId.get(0),
                        telemetryHandler, closeHook -> {
                            String errorMesssage = "Hono client - remotely detached consumer link";
                            logger.error(errorMesssage);
                            if (!connectFuture.isComplete()) {
                                connectFuture.fail(errorMesssage);
                            }
                            notifyConnectionLost();
                            }
                        );
                return futureConsumer;
        }).setHandler(result -> {
            if (!result.succeeded()) {
                logger.error("Hono client - cannot create telemetry consumer for {}:{} - {}", host, port, result.cause());
                if (!connectFuture.isComplete()) {
                    connectFuture.fail(result.cause());
                }
                notifyConnectionLost();
            }
            else {
                logger.info("Hono client - Established connection to {}:{}", host, port);
                connectFuture.complete();
            }
        });
    }

    protected void disconnect(final Future<Void> closeFuture) {
        if(honoClient!=null) {
            honoClient.shutdown(event -> {
                logger.info("Hono client - closing connection {}", event);
                if (!closeFuture.isComplete()) {
                    closeFuture.complete();
                }
            }
            );
        }
    }

    private void handleTelemetryMessage(final Message message) {
        logTelemetryMessage(message);
        try {
            super.handleMessage(new MessageContext<Message>(message));
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

    private void handleCommandReadinessNotification(final TimeUntilDisconnectNotification notification) {
    }

    protected ClientConfigProperties getClientConfigProperties() {
        ClientConfigProperties props = new ClientConfigProperties();
        props.setHost(host);
        props.setPort(port);
        props.setUsername(username);
        props.setPassword(password);
        props.setTrustStorePath(trustStoreFile);
        props.setHostnameVerificationRequired(false);
        return props;
    }

    protected ProtonClientOptions getProtonClientOptions() {
        ProtonClientOptions opts = new ProtonClientOptions();
        opts.setConnectTimeout(connectTimeout);
        //check if zero disables the timeout and heartbeat
        opts.setIdleTimeout(idleTimeout);//no activity for t>idleTimeout will close the connection (in seconds)
        opts.setHeartbeat(idleTimeout * 1000 / 2);//no activity for t>2*heartbeat will close connection (in milliseconds)
        opts.setReconnectAttempts(maxReconnectAttempts);
        opts.setReconnectInterval(waitBetweenReconnect);
        //TODO do we need to set some other parameter?
        return opts;
    }

    @Override
    protected Map<String, Object> getMessageParameters(Message message) throws KapuaConverterException {
        Map<String, Object> parameters = new HashMap<>();
        // build the message properties
        // extract original MQTT topic
        String mqttTopic = (String)message.getApplicationProperties().getValue().get("orig_address");
        mqttTopic = mqttTopic.replace(".", "/");
        if (mqttTopic.startsWith(TELEMETRY_PREFIX)) {
            parameters.put(Converter.MESSAGE_TYPE, TransportMessageType.TELEMETRY);
            mqttTopic = mqttTopic.substring(TELEMETRY_PREFIX.length());
        }
        else if (mqttTopic.startsWith(CONTROL_PREFIX)) {
            parameters.put(Converter.MESSAGE_TYPE, TransportMessageType.CONTROL);
            mqttTopic = mqttTopic.substring(CONTROL_PREFIX.length());
        }
        //TODO handle alerts, ... messages types
        parameters.put(Converter.MESSAGE_DESTINATION, mqttTopic);

        // extract the original QoS
        //TODO
        parameters.put(Converter.MESSAGE_QOS, TransportQos.AT_MOST_ONCE);
        return parameters;
    }

}
