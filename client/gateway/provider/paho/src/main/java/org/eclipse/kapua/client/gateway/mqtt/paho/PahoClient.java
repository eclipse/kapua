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
package org.eclipse.kapua.client.gateway.mqtt.paho;

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
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.kapua.client.gateway.BinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.mqtt.MqttClient;
import org.eclipse.kapua.client.gateway.mqtt.MqttMessageHandler;
import org.eclipse.kapua.client.gateway.mqtt.MqttNamespace;
import org.eclipse.kapua.client.gateway.mqtt.paho.internal.Listeners;
import org.eclipse.kapua.client.gateway.utils.Buffers;
import org.eclipse.kapua.client.gateway.Module;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PahoClient extends MqttClient {

    private static final Logger logger = LoggerFactory.getLogger(PahoClient.class);

    public static class Builder extends MqttClient.Builder<Builder> {

        private Supplier<MqttClientPersistence> persistenceProvider = MemoryPersistence::new;

        @Override
        protected Builder builder() {
            return this;
        }

        public Builder persistentProvider(final Supplier<MqttClientPersistence> provider) {
            if (provider != null) {
                persistenceProvider = provider;
            } else {
                persistenceProvider = MemoryPersistence::new;
            }
            return builder();
        }

        public Supplier<MqttClientPersistence> persistentProvider() {
            return persistenceProvider;
        }

        @Override
        public PahoClient build() throws Exception {

            final URI broker = requireNonNull(broker(), "Broker must be set");
            final String clientId = nonEmptyText(clientId(), "clientId");

            final MqttClientPersistence persistence = requireNonNull(persistenceProvider.get(), "Persistence provider returned 'null' persistence");
            final MqttNamespace namespace = requireNonNull(namespace(), "Namespace must be set");
            final BinaryPayloadCodec codec = requireNonNull(codec(), "Codec must be set");

            MqttAsyncClient client = new MqttAsyncClient(broker.toString(), clientId, persistence);
            ScheduledExecutorService executor = createExecutor(clientId);
            try {
                final PahoClient result = new PahoClient(modules(), clientId, executor, namespace, codec, client, persistence, createConnectOptions(this));
                client = null;
                executor = null;
                return result;
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
                if (client != null) {
                    try {
                        client.disconnectForcibly(0);
                    } finally {
                        client.close();
                    }
                }
            }
        }
    }

    private static ScheduledExecutorService createExecutor(final String clientId) {
        return Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, clientId));
    }

    private static MqttConnectOptions createConnectOptions(final Builder builder) {
        final MqttConnectOptions result = new MqttConnectOptions();

        final Object credentials = builder.credentials();
        if (credentials instanceof UserAndPassword) {
            final UserAndPassword userAndPassword = (UserAndPassword) credentials;
            result.setUserName(userAndPassword.getUsername());
            result.setPassword(userAndPassword.getPassword());
        } else if (credentials == null) {
            // ignore
        } else {
            throw new IllegalArgumentException(String.format("Unsupported credentials type: %s", credentials.getClass().getName()));
        }

        return result;
    }

    private final MqttConnectOptions connectOptions;
    private MqttAsyncClient client;

    private final Map<String, MqttMessageHandler> subscriptions = new HashMap<>();

    private PahoClient(final Set<Module> modules, final String clientId, final ScheduledExecutorService executor, final MqttNamespace namespace, final BinaryPayloadCodec codec,
            final MqttAsyncClient client, final MqttClientPersistence persistence, final MqttConnectOptions connectOptions) {

        super(executor, codec, namespace, clientId, modules);

        this.connectOptions = connectOptions;
        this.client = client;

        this.client.setCallback(new MqttCallback() {

            @Override
            public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                handleMessageArrived(topic, message);
            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {
            }

            @Override
            public void connectionLost(final Throwable cause) {
                handleDisconnected();
            }
        });

        this.executor.execute(this::connect);
    }

    protected void connect() {
        try {
            client.connect(connectOptions, null, new IMqttActionListener() {

                @Override
                public void onSuccess(final IMqttToken asyncActionToken) {
                    handleConnected();
                }

                @Override
                public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                    handleDisconnected();
                }
            });
        } catch (final MqttException e) {
            logger.warn("Failed to call connect", e);
        }
    }

    @Override
    public void close() {

        final MqttAsyncClient client;

        synchronized (this) {
            client = this.client;
            if (client == null) {
                return;
            }
            this.client = null;
        }

        try {
            // disconnect first

            try {
                client.disconnect().waitForCompletion();
            } catch (final MqttException e) {
            }

            // now try to close (and free the resources)

            try {
                client.close();
            } catch (final MqttException e) {
            }
        } finally {
            executor.shutdown();
        }
    }

    @Override
    protected void handleConnected() {
        synchronized (this) {
            super.handleConnected();
            handleResubscribe();
        }
    }

    private void handleResubscribe() {
        for (final Map.Entry<String, MqttMessageHandler> entry : subscriptions.entrySet()) {
            try {
                internalSubscribe(entry.getKey());
            } catch (final MqttException e) {
                logger.warn("Failed to re-subscribe to '{}'", entry.getKey());
            }
        }
    }

    @Override
    protected void handleDisconnected() {
        synchronized (this) {
            try {
                super.handleDisconnected();
            } finally {
                executor.schedule(this::connect, 1, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void publishMqtt(final String topic, final ByteBuffer payload) throws Exception {
        publish(topic, payload);
    }

    protected void publish(final String topic, final ByteBuffer payload) throws MqttException {
        logger.debug("Publishing {} - {}", topic, payload);
        client.publish(topic, Buffers.toByteArray(payload), 1, false);
    }

    @Override
    protected CompletionStage<?> subscribeMqtt(String topic, MqttMessageHandler messageHandler) throws MqttException {
        synchronized (this) {
            subscriptions.put(topic, messageHandler);
            return internalSubscribe(topic);
        }
    }

    @Override
    protected void unsubscribeMqtt(final Set<String> mqttTopics) throws MqttException {
        logger.info("Unsubscribe from: {}", mqttTopics);

        final List<String> topics = new ArrayList<>(mqttTopics.size());

        synchronized (this) {
            for (String topic : mqttTopics) {
                if (subscriptions.remove(topic) != null) {
                    topics.add(topic);
                }
            }
        }

        client.unsubscribe(topics.toArray(new String[topics.size()]));
    }

    protected void handleMessageArrived(final String topic, final MqttMessage message) throws Exception {
        final ByteBuffer buffer = Buffers.wrap(message.getPayload());
        buffer.flip();

        logger.debug("Received message - mqtt-topic: {}, payload: {}", topic, buffer);

        final MqttMessageHandler handler = subscriptions.get(topic);
        if (handler != null) {
            handler.handleMessage(topic, buffer);
        }
    }

    private CompletionStage<?> internalSubscribe(final String topic) throws MqttException {
        final CompletableFuture<?> future = new CompletableFuture<>();
        client.subscribe(topic, 1, null, Listeners.toListener(future));
        return future;
    }

}
