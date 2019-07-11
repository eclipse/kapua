/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link MqttCallback} interface.
 * <p>
 * This implementation si meant to be used in the transport layer of Kapua
 * to receive responses from the device when a request is sent to the device.
 * It offers the capability of receive one or more responses.
 * </p>
 *
 * @since 1.0.0
 */
public class MqttResponseCallback implements MqttCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MqttResponseCallback.class);

    /**
     * {@link List} of received {@link MqttMessage}s.
     *
     * @since 1.0.0
     */
    private final List<MqttMessage> responses;

    /**
     * Number of response {@link MqttMessage}s expected
     *
     * @since 1.0.0
     */
    private int expectedResponses;

    /**
     * Construct a {@link MqttCallback} with the given response container and 1 as expected response {@link MqttMessage}.
     *
     * @param responses The container in which put the received {@link MqttMessage}s.
     * @since 1.0.0
     */
    public MqttResponseCallback(@NotNull List<MqttMessage> responses) {
        this(responses, 1);
    }

    /**
     * Construct a {@link MqttCallback} with the given response container and the given number of expected {@link MqttMessage}.
     *
     * @param responses         The container in which put the received messages
     * @param expectedResponses The number of the expected responses to wait before notify observers.
     * @since 1.0.0
     */
    public MqttResponseCallback(@NotNull List<MqttMessage> responses, int expectedResponses) {
        this.responses = responses;
        this.expectedResponses = expectedResponses;
    }

    /**
     * Implements the API signature {@link MqttCallback#messageArrived(String, org.eclipse.paho.client.mqttv3.MqttMessage)}.
     * <p>
     * Each {@link org.eclipse.paho.client.mqttv3.MqttMessage} arrived is converted into a
     * {@link MqttMessage} and added to the list of received messages.
     * <p>
     * If the total number of received reaches the limit set for expected response messages
     * the {@code notifyAll()} is invoked
     *
     * @since 1.0.0
     */
    @Override
    public void messageArrived(String stringTopic, org.eclipse.paho.client.mqttv3.MqttMessage message) {
        // Synchronized access to the response container
        synchronized (responses) {

            // Convert MqttMessage to the given device-levelMessage
            MqttTopic mqttTopic = new MqttTopic(stringTopic);
            MqttPayload mqttPayload = new MqttPayload(message.getPayload());
            MqttMessage mqttMessage = new MqttMessage(mqttTopic, new Date(), mqttPayload);

            // Add the MqttMessage to the container.
            responses.add(mqttMessage);

            // Notify if all expected responses arrived
            if (expectedResponses >= responses.size()) {
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }

    /**
     * Implements the API signature {@link MqttCallback#deliveryComplete(IMqttDeliveryToken)} logging the event.
     *
     * @since 1.0.0
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOG.debug("Message delivery completed for messageId: {}", token.getMessageId());
    }

    /**
     * Implements the API signature {@link MqttCallback#connectionLost(Throwable)}.
     * <p>
     * The {@link Throwable} parameters is logged and {@link #notifyAll()}} is invoked.
     *
     * @since 1.0.0
     */
    @Override
    public void connectionLost(Throwable cause) {
        LOG.warn("Connection lost detected! {}", cause.getMessage());
        LOG.error("Connection lost detected!", cause);
        synchronized (this) {
            notifyAll();
        }
    }
}
