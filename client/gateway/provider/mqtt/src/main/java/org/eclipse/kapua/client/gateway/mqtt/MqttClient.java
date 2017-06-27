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
package org.eclipse.kapua.client.gateway.mqtt;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.BinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.spi.AbstractApplication;
import org.eclipse.kapua.client.gateway.spi.AbstractClient;
import org.eclipse.kapua.client.gateway.Module;
import org.eclipse.kapua.client.gateway.Topic;

public abstract class MqttClient extends AbstractClient {

    public abstract static class Builder<T extends Builder<T>> extends AbstractClient.Builder<T> {

        private MqttNamespace namespace;
        private BinaryPayloadCodec codec;
        private UserAndPassword userAndPassword;
        private String clientId;
        private URI broker;

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
    }

    private final String clientId;
    private final BinaryPayloadCodec codec;
    private final MqttNamespace namespace;

    public MqttClient(final ScheduledExecutorService executor, final BinaryPayloadCodec codec, final MqttNamespace namespace, final String clientId, final Set<Module> modules) {
        super(executor, modules);
        this.clientId = clientId;
        this.codec = codec;
        this.namespace = namespace;
    }

    protected void publish(String applicationId, Topic topic, ByteBuffer buffer) throws Exception {
        final String mqttTopic = namespace.dataTopic(clientId, applicationId, topic);
        publishMqtt(mqttTopic, buffer);
    }

    public abstract void publishMqtt(String topic, ByteBuffer payload) throws Exception;

    protected abstract CompletionStage<?> subscribeMqtt(String topic, MqttMessageHandler messageHandler) throws Exception;

    protected CompletionStage<?> subscribe(final String applicationId, final Topic topic, final MqttMessageHandler messageHandler) throws Exception {
        final String mqttTopic = namespace.dataTopic(clientId, applicationId, topic);
        return subscribeMqtt(mqttTopic, messageHandler);
    }

    @Override
    protected void internalUnsubscribe(final String applicationId, final Collection<Topic> topics) throws Exception {
        Set<String> mqttTopics = topics.stream().map(topic -> namespace.dataTopic(clientId, applicationId, topic)).collect(Collectors.toSet());
        unsubscribeMqtt(mqttTopics);
    }

    protected abstract void unsubscribeMqtt(Set<String> mqttTopics) throws Exception;

    public String getMqttClientId() {
        return clientId;
    }

    @Override
    protected AbstractApplication internalCreateApplication(final Application.Builder builder, final String applicationId) {
        return new MqttApplication(this, applicationId, executor);
    }

    protected BinaryPayloadCodec getCodec() {
        return codec;
    }

}
