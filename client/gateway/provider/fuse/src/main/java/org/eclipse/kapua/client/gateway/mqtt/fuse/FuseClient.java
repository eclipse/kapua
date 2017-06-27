/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.mqtt.fuse;

import static java.util.Objects.requireNonNull;
import static org.eclipse.kapua.client.gateway.utils.Strings.nonEmptyText;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.kapua.client.gateway.BinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.mqtt.MqttClient;
import org.eclipse.kapua.client.gateway.mqtt.MqttMessageHandler;
import org.eclipse.kapua.client.gateway.mqtt.MqttNamespace;
import org.eclipse.kapua.client.gateway.mqtt.fuse.internal.Callbacks;
import org.eclipse.kapua.client.gateway.Module;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.ExtendedListener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Promise;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuseClient extends MqttClient {

    private static final Logger logger = LoggerFactory.getLogger(FuseClient.class);

    public static class Builder extends MqttClient.Builder<Builder> {

        @Override
        protected Builder builder() {
            return this;
        }

        @Override
        public FuseClient build() throws Exception {

            final URI broker = requireNonNull(broker(), "Broker must be set");
            final String clientId = nonEmptyText(clientId(), "clientId");

            final MqttNamespace namespace = requireNonNull(namespace(), "Namespace must be set");
            final BinaryPayloadCodec codec = requireNonNull(codec(), "Codec must be set");

            final MQTT mqtt = new MQTT();
            mqtt.setCleanSession(false);
            mqtt.setHost(broker);
            mqtt.setClientId(clientId);

            final Object credentials = credentials();
            if (credentials == null) {
                // none
            } else if (credentials instanceof UserAndPassword) {
                final UserAndPassword userAndPassword = (UserAndPassword) credentials;
                mqtt.setUserName(userAndPassword.getUsername());
                mqtt.setPassword(userAndPassword.getPasswordAsString());
            } else {
                throw new IllegalStateException(
                        String.format("Unknown credentials type: %s", credentials.getClass().getName()));
            }

            CallbackConnection connection = mqtt.callbackConnection();
            ScheduledExecutorService executor = createExecutor(clientId);
            try {
                final FuseClient result = new FuseClient(modules(), clientId, executor, namespace, codec, connection);
                connection = null;
                executor = null;
                return result;
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
            }
        }
    }

    private static ScheduledExecutorService createExecutor(final String clientId) {
        return Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, clientId));
    }

    private final ExtendedListener listener = new ExtendedListener() {

        @Override
        public void onPublish(final UTF8Buffer topic, final Buffer body, final Runnable ack) {
            onPublish(topic, body, new Callback<Callback<Void>>() {

                @Override
                public void onSuccess(Callback<Void> value) {
                    ack.run();
                }

                @Override
                public void onFailure(Throwable value) {
                }

            });
        }

        @Override
        public void onFailure(Throwable value) {
        }

        @Override
        public void onDisconnected() {
            handleDisconnected();
        }

        @Override
        public void onConnected() {
            handleConnected();
        }

        @Override
        public void onPublish(final UTF8Buffer topic, final Buffer body, final Callback<Callback<Void>> ack) {
            handleMessageArrived(topic.toString(), body, ack);
        }
    };

    private final CallbackConnection connection;

    private final Map<String, MqttMessageHandler> subscriptions = new HashMap<>();

    private FuseClient(final Set<Module> modules, final String clientId, final ScheduledExecutorService executor,
            final MqttNamespace namespace, final BinaryPayloadCodec codec, final CallbackConnection connection) {

        super(executor, codec, namespace, clientId, modules);

        this.connection = connection;

        connection.listener(listener);
        connection.connect(new Promise<>());
    }

    @Override
    public void close() {
        connection.disconnect(null);
        executor.shutdown();
    }

    @Override
    public void publishMqtt(final String topic, final ByteBuffer payload) {
        connection.publish(Buffer.utf8(topic), new Buffer(payload), QoS.AT_LEAST_ONCE, false, null);
    }

    @Override
    protected CompletionStage<?> subscribeMqtt(final String topic, final MqttMessageHandler messageHandler) {
        synchronized (this) {
            subscriptions.put(topic, messageHandler);

            final CompletableFuture<byte[]> future = new CompletableFuture<>();
            connection.subscribe(
                    new org.fusesource.mqtt.client.Topic[] {
                            new org.fusesource.mqtt.client.Topic(topic, QoS.AT_LEAST_ONCE) },
                    Callbacks.asCallback(future));

            return future;
        }
    }

    @Override
    protected void unsubscribeMqtt(final Set<String> mqttTopics) {

        logger.info("Unsubscribe from: {}", mqttTopics);

        final List<UTF8Buffer> topics = new ArrayList<>(mqttTopics.size());

        synchronized (this) {
            for (final String topic : mqttTopics) {
                if (subscriptions.remove(topic) != null) {
                    topics.add(new UTF8Buffer(topic));
                }
            }
        }

        connection.unsubscribe(topics.toArray(new UTF8Buffer[topics.size()]), new Promise<>());
    }

    protected void handleMessageArrived(final String topic, final Buffer payload, final Callback<Callback<Void>> ack) {
        final MqttMessageHandler handler;

        synchronized (this) {
            handler = subscriptions.get(topic);
        }

        if (handler != null) {
            try {
                handler.handleMessage(topic, payload.toByteBuffer());
                ack.onSuccess(null);
            } catch (Exception e) {
                ack.onFailure(e);
            }
        }
    }

}
