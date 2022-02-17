/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.mqtt;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.eclipse.kapua.client.gateway.BinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.spi.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract base class for implementing MQTT based channel
 */
public abstract class AbstractMqttChannel implements Channel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMqttChannel.class);

    public abstract static class Builder<T extends Builder<T>> {

        private MqttNamespace namespace;
        private BinaryPayloadCodec codec;
        private UserAndPassword userAndPassword;
        private String clientId;
        private URI broker;

        protected abstract T builder();

        public T codec(final BinaryPayloadCodec codec) {
            this.codec = codec;
            return builder();
        }

        public BinaryPayloadCodec codec() {
            return this.codec;
        }

        public T namespace(final MqttNamespace namespace) {
            this.namespace = namespace;
            return builder();
        }

        public MqttNamespace namespace() {
            return this.namespace;
        }

        public T clientId(final String clientId) {
            this.clientId = clientId;
            return builder();
        }

        public String clientId() {
            return this.clientId;
        }

        public T credentials(final UserAndPassword userAndPassword) {
            this.userAndPassword = userAndPassword;
            return builder();
        }

        public T broker(final String broker) throws URISyntaxException {
            Objects.requireNonNull(broker);
            this.broker = new URI(broker);
            return builder();
        }

        public T broker(final URI broker) throws URISyntaxException {
            Objects.requireNonNull(broker);
            this.broker = broker;
            return builder();
        }

        public URI broker() {
            return this.broker;
        }

        public Object credentials() {
            return this.userAndPassword;
        }

        public abstract Channel build() throws Exception;
    }

    private final MqttModuleContext mqttAdapter = new MqttModuleContext() {

        @Override
        public CompletionStage<?> publishMqtt(final String topic, final ByteBuffer payload) {
            return AbstractMqttChannel.this.publishMqtt(topic, payload);
        }

        @Override
        public String getMqttClientId() {
            return AbstractMqttChannel.this.getMqttClientId();
        }
    };

    private final String clientId;
    private final BinaryPayloadCodec codec;
    private final MqttNamespace namespace;

    public AbstractMqttChannel(final BinaryPayloadCodec codec, final MqttNamespace namespace, final String clientId) {
        this.clientId = clientId;
        this.codec = codec;
        this.namespace = namespace;
    }

    protected abstract CompletionStage<?> publishMqtt(String topic, ByteBuffer payload);

    protected abstract CompletionStage<?> subscribeMqtt(String topic, MqttMessageHandler messageHandler);

    protected abstract void unsubscribeMqtt(Set<String> mqttTopics) throws Exception;

    @Override
    public <T> Optional<T> adapt(Class<T> clazz) {

        if (clazz.equals(MqttModuleContext.class)) {
            return Optional.of(clazz.cast(mqttAdapter));
        }

        return Optional.empty();
    }

    protected CompletionStage<?> publish(final String applicationId, final Topic topic, final ByteBuffer buffer) {
        final String mqttTopic = namespace.dataTopic(clientId, applicationId, topic);
        return publishMqtt(mqttTopic, buffer);
    }

    @Override
    public CompletionStage<?> handlePublish(final String applicationId, final Topic topic, final Payload payload) {
        logger.debug("Publishing values - {} -> {}", topic, payload.getValues());

        try {
            final ByteBuffer buffer = codec.encode(payload, null);
            buffer.flip();

            return publish(applicationId, topic, buffer);
        } catch (final Exception e) {
            final CompletableFuture<?> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }

    }

    @Override
    public CompletionStage<?> handleSubscribe(final String applicationId, final Topic topic, final MessageHandler handler, final ErrorHandler<? extends Throwable> errorHandler) {
        return subscribe(applicationId, topic, (messageTopic, payload) -> {
            logger.debug("Received message for: {}", topic);
            try {
                handleMessage(handler, payload);
            } catch (final Exception e) {
                try {
                    errorHandler.handleError(e, null);
                } catch (final Exception e1) {
                    throw e1;
                } catch (final Throwable e1) {
                    throw new Exception(e1);
                }
            }
        });
    }

    protected void handleMessage(final MessageHandler handler, final ByteBuffer buffer) throws Exception {
        final Payload payload = codec.decode(buffer);
        logger.debug("Received: {}", payload);
        handler.handleMessage(payload);
    }

    protected CompletionStage<?> subscribe(final String applicationId, final Topic topic, final MqttMessageHandler messageHandler) {
        final String mqttTopic = namespace.dataTopic(clientId, applicationId, topic);
        return subscribeMqtt(mqttTopic, messageHandler);
    }

    @Override
    public void handleUnsubscribe(final String applicationId, final Collection<Topic> topics) throws Exception {
        final Set<String> mqttTopics = topics.stream()
                .map(topic -> namespace.dataTopic(clientId, applicationId, topic))
                .collect(Collectors.toSet());
        unsubscribeMqtt(mqttTopics);
    }

    protected String getMqttClientId() {
        return clientId;
    }

    protected BinaryPayloadCodec getCodec() {
        return codec;
    }

}
