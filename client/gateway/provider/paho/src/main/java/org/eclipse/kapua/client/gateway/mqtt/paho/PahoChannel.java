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

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.kapua.client.gateway.BinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.TransmissionException;
import org.eclipse.kapua.client.gateway.mqtt.AbstractMqttChannel;
import org.eclipse.kapua.client.gateway.mqtt.MqttMessageHandler;
import org.eclipse.kapua.client.gateway.mqtt.MqttNamespace;
import org.eclipse.kapua.client.gateway.mqtt.paho.internal.Listeners;
import org.eclipse.kapua.client.gateway.spi.util.Buffers;
import org.eclipse.kapua.client.gateway.spi.util.Strings;

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

public class PahoChannel extends AbstractMqttChannel {

    private static final Logger logger = LoggerFactory.getLogger(PahoChannel.class);

    public static class Builder extends AbstractMqttChannel.Builder<Builder> {

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
        public PahoChannel build() throws Exception {

            final URI broker = Objects.requireNonNull(broker(), "Broker must be set");
            final String clientId = Strings.nonEmptyText(clientId(), "clientId");

            final MqttClientPersistence persistence = Objects.requireNonNull(persistenceProvider.get(), "Persistence provider returned 'null' persistence");
            final MqttNamespace namespace = Objects.requireNonNull(namespace(), "Namespace must be set");
            final BinaryPayloadCodec codec = Objects.requireNonNull(codec(), "Codec must be set");

            MqttAsyncClient client = new MqttAsyncClient(broker.toString(), clientId, persistence);
            try {
                final PahoChannel result = new PahoChannel(clientId, namespace, codec, client, persistence, createConnectOptions(this));
                client = null;
                return result;
            } finally {
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
    private Context context;

    private PahoChannel(final String clientId, final MqttNamespace namespace, final BinaryPayloadCodec codec,
            final MqttAsyncClient client, final MqttClientPersistence persistence, final MqttConnectOptions connectOptions) {

        super(codec, namespace, clientId);

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
    }

    @Override
    public void handleInit(final Context context) {
        this.context = context;
        this.context.executor().execute(this::connect);
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
    public void handleClose(final Context context) {

        final MqttAsyncClient client;

        synchronized (this) {
            client = this.client;
            if (client == null) {
                return;
            }
            this.client = null;
        }

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
    }

    protected void handleConnected() {
        synchronized (this) {
            context.notifyConnected();
            handleResubscribe();
        }
    }

    protected void handleDisconnected() {
        synchronized (this) {
            try {
                context.notifyDisconnected();
            } finally {
                context.executor().schedule(this::connect, 1, TimeUnit.SECONDS);
            }
        }
    }

    private void handleResubscribe() {
        for (final Map.Entry<String, MqttMessageHandler> entry : subscriptions.entrySet()) {
            internalSubscribe(entry.getKey()).whenComplete((value, ex) -> {
                logger.warn("Failed to re-subscribe to '{}'", entry.getKey(), ex);
            });
        }
    }

    @Override
    public CompletionStage<?> publishMqtt(final String topic, final ByteBuffer payload) {
        logger.debug("Publishing {} - {}", topic, payload);

        final CompletableFuture<?> future = new CompletableFuture<>();
        try {
            client.publish(topic, Buffers.toByteArray(payload), 1, false, null,
                    Listeners.toListener(
                            () -> future.complete(null),
                            error -> handlePublishError(future, error)));
        } catch (MqttException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    private void handlePublishError(final CompletableFuture<?> future, final Throwable error) {
        if (!(error instanceof MqttException)) {
            // unknown exception type, simply forward
            future.completeExceptionally(error);
            return;
        }

        // check for error code

        final MqttException e = (MqttException) error;
        switch (e.getReasonCode()) {
        case MqttException.REASON_CODE_CLIENT_EXCEPTION: //$FALL-THROUGH$
        case MqttException.REASON_CODE_UNEXPECTED_ERROR:
            // consider this non-temporary
            future.completeExceptionally(error);
            return;
        default:
            // consider this temporary and recoverable
            future.completeExceptionally(new TransmissionException(error));
            return;
        }
    }

    @Override
    protected CompletionStage<?> subscribeMqtt(String topic, MqttMessageHandler messageHandler) {
        synchronized (this) {
            subscriptions.put(topic, messageHandler);
            return internalSubscribe(topic);
        }
    }

    private CompletionStage<?> internalSubscribe(final String topic) {
        final CompletableFuture<?> future = new CompletableFuture<>();
        try {
            client.subscribe(topic, 1, null, Listeners.toListener(future));
        } catch (final MqttException e) {
            future.completeExceptionally(e);
        }
        return future;
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

}
