/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt;

import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientAlreadyConnectedException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCallbackSetException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCleanException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientConnectException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientDisconnectException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientNotConnectedException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientPublishException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientSubscribeException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientTerminateException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientUnsubscribeException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that wraps a {@link org.eclipse.paho.client.mqttv3.MqttClient} and
 * adds some utility methods to manage the operations at the transport level
 * in Kapua.
 *
 * @since 1.0.0
 */
public class MqttClient {

    private static final Logger LOG = LoggerFactory.getLogger(MqttClient.class);

    /**
     * The wrapped Paho client.
     *
     * @since 1.0.0
     */
    private org.eclipse.paho.client.mqttv3.MqttClient pahoMqttClient;

    /**
     * List of all subscribed topics by the {@link MqttClient#pahoMqttClient}.
     *
     * @since 1.0.0
     */
    private final List<MqttTopic> subscribedTopics = new ArrayList<>();

    /**
     * The clientId of the {@link MqttClient}
     *
     * @since 1.2.0
     */
    private String clientId;

    //
    // Connection management
    //

    /**
     * Connects the {@link MqttClient#pahoMqttClient} according to the given {@link TransportClientConnectOptions}.
     *
     * @param options The {@link TransportClientConnectOptions} to use.
     * @throws MqttClientAlreadyConnectedException if this {@link MqttClient} is already connected.
     * @throws MqttClientConnectException          When connect fails.
     * @since 1.0.0
     */
    public void connectClient(TransportClientConnectOptions options) throws MqttClientConnectException, MqttClientAlreadyConnectedException {

        if (options.getEndpointURI() == null) {
            throw new MqttClientConnectException(options.getClientId(), options.getUsername(), options.getEndpointURI());
        }

        try {
            if (pahoMqttClient != null) {
                throw new MqttClientAlreadyConnectedException(getClientId());
            }
            pahoMqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(options.getEndpointURI().toString(),
                    options.getClientId(),
                    new MemoryPersistence());

            MqttConnectOptions pahoConnectOptions = new MqttConnectOptions();
            pahoConnectOptions.setUserName(options.getUsername());
            pahoConnectOptions.setPassword(options.getPassword());
            pahoConnectOptions.setCleanSession(true);
            pahoConnectOptions.setAutomaticReconnect(true);

            clientId = options.getClientId();

            pahoMqttClient.connect(pahoConnectOptions);
        } catch (MqttException e) {
            throw new MqttClientConnectException(e,
                    options.getClientId(),
                    options.getUsername(),
                    options.getEndpointURI());
        }
    }

    /**
     * Disconnects the {@link MqttClient#pahoMqttClient}.
     * <p>
     * Before disconnecting {@link #unsubscribeAll()} invoked.
     * </p>
     *
     * @throws MqttClientDisconnectException When disconnect fails.
     * @since 1.0.0
     */
    public void disconnectClient() throws MqttClientDisconnectException {
        try {
            unsubscribeAll();

            getPahoClient().disconnect();
        } catch (MqttException | MqttClientNotConnectedException e) {
            throw new MqttClientDisconnectException(e, getClientId());
        }
    }

    /**
     * Disconnects the {@link MqttClient#pahoMqttClient}.
     * <p>
     * Before termination {@link MqttClient#disconnectClient()} is invoked to clean up appended subscriptions and close connection.
     * </p>
     *
     * @throws MqttClientTerminateException Whne close fails.
     * @since 1.0.0
     */
    public void terminateClient() throws MqttClientTerminateException {
        try {
            if (getPahoClient().isConnected()) {
                disconnectClient();
            }

            getPahoClient().close();

            pahoMqttClient = null;
        } catch (MqttException | MqttClientNotConnectedException | MqttClientDisconnectException e) {
            throw new MqttClientTerminateException(e, getClientId());
        }
    }

    /**
     * Checks if this {@link MqttClient} is connected.
     *
     * @return {@code true} if connected, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean isConnected() {
        try {
            return getPahoClient().isConnected();
        } catch (MqttClientNotConnectedException e) {
            return false;
        }
    }

    //
    // Message management
    //

    /**
     * Publish a {@link MqttMessage}.
     * <p>
     * QoS of publishing is set to 0.
     * </p>
     *
     * @param mqttMessage The {@link MqttMessage} to publish.
     * @throws MqttClientPublishException When publish fails.
     * @since 1.0.0
     */
    public void publish(MqttMessage mqttMessage) throws MqttClientPublishException {
        MqttTopic mqttTopic = mqttMessage.getRequestTopic();
        MqttPayload mqttPayload = mqttMessage.getPayload();
        try {
            getPahoClient().publish(mqttTopic.getTopic(),
                    mqttPayload.getBody(),
                    0,
                    false);
        } catch (MqttException | MqttClientNotConnectedException e) {
            throw new MqttClientPublishException(e, getClientId(), mqttTopic.toString(), mqttMessage);
        }
    }

    /**
     * Subscribes this client to the given {@link MqttTopic}.
     *
     * @param mqttTopic The {@link MqttTopic} to subscribe to.
     * @throws MqttClientSubscribeException When subscribe fails.
     * @since 1.0.0
     */
    public void subscribe(MqttTopic mqttTopic) throws MqttClientSubscribeException {
        try {
            getPahoClient().subscribe(mqttTopic.getTopic());
            subscribedTopics.add(mqttTopic);
        } catch (MqttException | MqttClientNotConnectedException e) {
            throw new MqttClientSubscribeException(e, getClientId(), mqttTopic);
        }
    }

    /**
     * Unsubscribes this client from the given {@link MqttTopic}.
     *
     * @param mqttTopic The {@link MqttTopic} to unsubscribe to.
     * @throws MqttClientUnsubscribeException When unsubscribe fails.
     * @since 1.0.0
     */
    private void unsubscribe(MqttTopic mqttTopic) throws MqttClientUnsubscribeException {
        try {
            getPahoClient().unsubscribe(mqttTopic.getTopic());
        } catch (MqttException | MqttClientNotConnectedException e) {
            throw new MqttClientUnsubscribeException(e, getClientId(), mqttTopic);
        }

    }

    /**
     * Unsubscribes this {@link MqttClient} from all {@link MqttTopic} subscribed.
     *
     * @since 1.0.0
     */
    public synchronized void unsubscribeAll() {
        for (MqttTopic mqttTopic : subscribedTopics) {
            try {
                unsubscribe(mqttTopic);
            } catch (MqttClientUnsubscribeException mcue) {
                LOG.warn(mcue.getMessage());
            }
        }

        subscribedTopics.clear();
    }

    /**
     * Sets a {@link MqttResponseCallback} to this client.
     *
     * @param mqttClientCallback The {@link MqttResponseCallback} to use.
     * @throws MqttClientCallbackSetException When set the callback fails.
     * @since 1.0.0
     */
    public void setCallback(MqttResponseCallback mqttClientCallback) throws MqttClientCallbackSetException {
        try {
            getPahoClient().setCallback(mqttClientCallback);
        } catch (MqttClientNotConnectedException e) {
            throw new MqttClientCallbackSetException(e, getClientId(), mqttClientCallback.getResponseTopic());
        }

    }

    /**
     * Cleans this client from any callback set and unsubscribes from all {@link MqttTopic} subscribed.
     *
     * @throws MqttClientCleanException When any of the clean operations fails.
     * @since 1.0.0
     */
    public void clean() throws MqttClientCleanException {
        try {
            getPahoClient().setCallback(null);
            unsubscribeAll();
        } catch (MqttClientNotConnectedException e) {
            throw new MqttClientCleanException(e, getClientId());
        }

    }

    //
    // Utilities
    //

    /**
     * Gets the clientId of the wrapped {@link MqttClient#pahoMqttClient}.
     *
     * @return The clientId of the wrapped {@link MqttClient#pahoMqttClient}.
     * @since 1.0.0
     */
    public String getClientId() {
        return pahoMqttClient != null ? pahoMqttClient.getClientId() : clientId;
    }

    /**
     * Gets the reference to the wrapped {@link MqttClient#pahoMqttClient} ready to be used.
     *
     * @return The wrapped {@link MqttClient#pahoMqttClient}.
     * @throws MqttClientNotConnectedException If client has never been connected using {@link MqttClient#connectClient(TransportClientConnectOptions)}.
     * @since 1.0.0
     */
    private synchronized org.eclipse.paho.client.mqttv3.MqttClient getPahoClient() throws MqttClientNotConnectedException {
        if (pahoMqttClient == null) {
            throw new MqttClientNotConnectedException(getClientId());
        }

        return pahoMqttClient;
    }
}
