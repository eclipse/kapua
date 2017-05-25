/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Class that wrap a {@link org.eclipse.paho.client.mqttv3.MqttClient} and
 * adds some utility methods to manage the operations at the transport level
 * in Kapua.
 * 
 * @since 1.0.0
 */
public class MqttClient {

    /**
     * The wrapped Paho client.
     */
    private org.eclipse.paho.client.mqttv3.MqttClient pahoMqttClient;

    /**
     * List of all subscribed topics by the {@link MqttClient#pahoMqttClient}.
     */
    private List<MqttTopic> subscribedTopics = new ArrayList<>();

    //
    // Connection management
    //
    /**
     * Connects the {@link MqttClient#pahoMqttClient} according to the given {@link TransportClientConnectOptions}.
     * 
     * @param options
     *            The connection options to use
     * @throws MqttClientException
     *             When connect fails.
     * @since 1.0.0
     */
    public void connectClient(TransportClientConnectOptions options)
            throws KapuaException {
        try {
            if (pahoMqttClient != null) {
                throw new MqttClientException(MqttClientErrorCodes.CLIENT_ALREADY_CONNECTED,
                        null,
                        (Object[]) null);

            }

            pahoMqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(options.getEndpointURI().toString(),
                    options.getClientId(),
                    new MemoryPersistence());

            MqttConnectOptions pahoConnectOptions = new MqttConnectOptions();
            pahoConnectOptions.setUserName(options.getUsername());
            pahoConnectOptions.setPassword(options.getPassword());
            pahoConnectOptions.setCleanSession(true);
            // FIXME: Set other connect options!

            pahoMqttClient.connect(pahoConnectOptions);
        } catch (MqttException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_CONNECT_ERROR,
                    e,
                    new Object[] { options.getEndpointURI().toString(),
                            options.getClientId(),
                            options.getUsername() });
        }
    }

    /**
     * Disconnects the {@link MqttClient#pahoMqttClient}.
     * <p>
     * Before disconnecting cleaning of subscriptions is attempted.
     * </p>
     * 
     * @throws KapuaException
     *             When disconnect fails.
     * 
     * @since 1.0.0
     */
    public void disconnectClient()
            throws KapuaException {
        try {
            unsubscribeAll();

            if (getPahoClient() != null) {
                getPahoClient().disconnect();
            }
        } catch (MqttException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_DISCONNECT_ERROR,
                    e,
                    (Object[]) null);
        }
    }

    /**
     * Disconnects the {@link MqttClient#pahoMqttClient}.
     * <p>
     * Before termination {@link MqttClient#disconnectClient()} is invoked to clean up appended subscriptions and close connection.
     * </p>
     * 
     * @throws KapuaException
     *             Whne close fails.
     * @since 1.0.0
     */
    public void terminateClient()
            throws KapuaException {
        try {
            if (getPahoClient() != null) {
                if (getPahoClient().isConnected()) {
                    disconnectClient();
                }

                getPahoClient().close();
                pahoMqttClient = null;
            }
        } catch (MqttException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_TERMINATE_ERROR,
                    e,
                    (Object[]) null);
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
        } catch (KapuaException e) {
            // FIXME: add log
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
     * @param mqttMessage
     *            The {@link MqttMessage} to publish.
     * 
     * @throws KapuaException
     *             When publish fails.
     * @since 1.0.0
     */
    public void publish(MqttMessage mqttMessage)
            throws KapuaException {
        MqttTopic mqttTopic = mqttMessage.getRequestTopic();
        MqttPayload mqttPayload = mqttMessage.getPayload();
        try {

            getPahoClient().publish(mqttTopic.getTopic(),
                    mqttPayload.getBody(),
                    0,
                    false);
        } catch (MqttException | KapuaException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_SUBSCRIBE_ERROR,
                    e,
                    new Object[] { mqttTopic.toString() });
        }
    }

    /**
     * Subscribes this client to the given {@link MqttTopic}.
     * 
     * @param mqttTopic
     *            The {@link MqttTopic} to subscribe to.
     * @throws KapuaException
     *             When subscribe fails.
     * @since 1.0.0
     */
    public void subscribe(MqttTopic mqttTopic)
            throws KapuaException {
        try {
            getPahoClient().subscribe(mqttTopic.getTopic());
            subscribedTopics.add(mqttTopic);
        } catch (MqttException | KapuaException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_SUBSCRIBE_ERROR,
                    e,
                    new Object[] { mqttTopic.getTopic() });
        }
    }

    /**
     * Unsubscribes this client from the given {@link MqttTopic}.
     * 
     * @param mqttTopic
     *            The {@link MqttTopic} to unsubscribe to.
     * @throws KapuaException
     *             When unsubscribe fails.
     * @since 1.0.0
     */
    private void unsubscribe(MqttTopic mqttTopic)
            throws KapuaException {
        try {
            getPahoClient().unsubscribe(mqttTopic.getTopic());
        } catch (MqttException | KapuaException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_UNSUBSCRIBE_ERROR,
                    e,
                    new Object[] { mqttTopic.toString() });
        }
    }

    /**
     * Unsubscribes this client from all topics subscribed.
     * 
     * @throws KapuaException
     *             When any of the unsubscribe fails.
     * @since 1.0.0
     */
    public synchronized void unsubscribeAll()
            throws KapuaException {
        Iterator<MqttTopic> subscribptionIterator = subscribedTopics.iterator();

        while (subscribptionIterator.hasNext()) {
            MqttTopic mqttTopic = subscribptionIterator.next();
            unsubscribe(mqttTopic);

        }

        subscribedTopics.clear();
    }

    /**
     * Sets a {@link MqttClientCallback} to this client.
     * 
     * @param mqttClientCallback
     *            The {@link MqttClientCallback} to use.
     * @throws KapuaException
     *             When set the callback fails.
     */
    public void setCallback(MqttClientCallback mqttClientCallback)
            throws KapuaException {
        try {
            getPahoClient().setCallback(mqttClientCallback);
        } catch (KapuaException e) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_CALLBACK_ERROR,
                    e,
                    (Object[]) null);
        }

    }

    /**
     * Cleans this client from any callback set and unsubscribes from all {@link MqttTopic} subscribed.
     * 
     * @throws KapuaException
     *             When any of the clean operations fails.
     */
    public void clean()
            throws KapuaException {
        try {
            getPahoClient().setCallback(null);
            unsubscribeAll();
        } catch (KapuaException e) {
            terminateClient();
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_CLEAN_ERROR,
                    e,
                    (Object[]) null);
        }

    }

    //
    // Utilty
    //
    /**
     * Gets the clientId of the wrapped {@link MqttClient#pahoMqttClient}.
     * 
     * @return The clientId of the wrapped {@link MqttClient#pahoMqttClient}.
     */
    public String getClientId() {
        try {
            return getPahoClient().getClientId();
        } catch (KapuaException e) {
            return null;
        }
    }

    /**
     * Gets the reference to the wrapped {@link MqttClient#pahoMqttClient} ready to be used.
     * 
     * @return The wrapped {@link MqttClient#pahoMqttClient}.
     * @throws KapuaException
     *             If client has never been connected using {@link MqttClient#connectClient(TransportClientConnectOptions)}.
     */
    private synchronized org.eclipse.paho.client.mqttv3.MqttClient getPahoClient()
            throws KapuaException {
        if (pahoMqttClient == null) {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_NOT_CONNECTED,
                    null,
                    (Object[]) null);
        }

        return pahoMqttClient;
    }
}
