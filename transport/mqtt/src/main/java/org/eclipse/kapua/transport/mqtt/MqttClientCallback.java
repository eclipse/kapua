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
 *
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * Generic implementation of {@link MqttCallback} interface.
 * <p>
 * This generic implementation si meant to be used in the transport layer of Kapua
 * to receive responses from the device when a request is sent to the device.
 * It offers the capability of receive one or more responses.
 * </p>
 * 
 * @since 1.0.0
 */
public class MqttClientCallback implements MqttCallback {

    /**
     * List of received messages.
     * 
     * @since 1.0.0
     */
    private List<MqttMessage> responses;

    /**
     * Number of response messages expected
     * 
     * @since 1.0.0
     */
    private int expectedResponses;

    /**
     * Construct a callback with the given response container and 1 as expected response messages
     * 
     * @param responses
     *            The container in which put the received messages
     * @since 1.0.0
     */
    public MqttClientCallback(List<MqttMessage> responses) {
        this(responses, 1);
    }

    /**
     * Construct a callback with the given response container and the given number of expected responses
     * 
     * @param responses
     *            The container in which put the received messages
     * @param expectedResponses
     *            The number of the expected responses to wait before notify observers.
     * @since 1.0.0
     */
    public MqttClientCallback(List<MqttMessage> responses, int expectedResponses) {
        this.responses = responses;
        this.expectedResponses = expectedResponses;
    }

    /**
     * Implements the API signature {@link MqttCallback#messageArrived(String, org.eclipse.paho.client.mqttv3.MqttMessage)}.
     * <p>
     * Each {@link org.eclipse.paho.client.mqttv3.MqttMessage} arrived is converted into a
     * {@link MqttMessage} and added to the list of received messages.
     * </p>
     * <p>
     * If the total number of received reaches the limit set for expected response messages
     * the {@code notifyAll()} is invoked
     * </p>
     * 
     * @since 1.0.0
     */
    @Override
    public void messageArrived(String stringTopic, org.eclipse.paho.client.mqttv3.MqttMessage message)
            throws Exception {
        MqttTopic mqttTopic = new MqttTopic(stringTopic);
        MqttPayload mqttPayload = new MqttPayload(message.getPayload());
        MqttMessage mqttMessage = new MqttMessage(mqttTopic,
                new Date(),
                mqttPayload);

        //
        // Add to the received responses
        if (responses == null) {
            responses = new ArrayList<MqttMessage>();
        }

        //
        // Convert MqttMessage to the given device-levelMessage
        responses.add(mqttMessage);

        //
        // notify if all expected responses arrived
        if (expectedResponses == responses.size()) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Implements the API signature {@link MqttCallback#deliveryComplete(IMqttDeliveryToken)} with an empty method
     * 
     * @since 1.0.0
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * Implements the API signature {@link MqttCallback#connectionLost(Throwable)}.
     * <p>
     * The {@link Throwable} parameters is wrapped into a {@link MqttClientException} and logged.
     * {@code notifyAll()} is invoked.
     * </p>
     * 
     * @since 1.0.0
     */
    @Override
    public void connectionLost(Throwable cause) {
        //
        // Call the KapuaClientCallback
        try {
            throw new MqttClientException(MqttClientErrorCodes.CLIENT_CONNECTION_LOST, cause, (Object[]) null);
        } catch (KapuaException e) {
            // FIXME: What to do here?? Wrap the exceptionin a Runtime and throw it??
            e.printStackTrace();

            notifyAll();
        }
    }
}
