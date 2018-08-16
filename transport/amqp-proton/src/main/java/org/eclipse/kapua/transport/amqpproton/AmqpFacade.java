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
package org.eclipse.kapua.transport.amqpproton;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.amqpproton.message.AmqpMessage;
import org.eclipse.kapua.transport.amqpproton.message.AmqpPayload;
import org.eclipse.kapua.transport.amqpproton.message.AmqpTopic;
import org.eclipse.kapua.transport.amqpproton.pooling.AmqpClientPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of {@link TransportFacade} API for AMQP transport facade.
 *
 * @since 1.0.0
 */
public class AmqpFacade implements TransportFacade<AmqpTopic, AmqpPayload, AmqpMessage, AmqpMessage> {

    private final static String VT_TOPIC_PREFIX = "topic://VirtualTopic.";

    /**
     * The client to use to make requests.
     *
     * @since 1.0.0
     */
    private AmqpClient borrowedClient;

    /**
     * The client callback for this set of requests.
     *
     * @since 1.0.0
     */
    private AmqpClientConsumerHandler amqpClientCallback;

    private final String nodeUri;

    /**
     * Initialize a transport facade to be used to send requests to devices.
     * 
     * @param nodeUri
     * @throws KapuaException When AMQP client is not available.
     */
    public AmqpFacade(String nodeUri) throws KapuaException {
        this.nodeUri = nodeUri;

        //
        // Get the client form the pool
        try {
            borrowedClient = AmqpClientPool.getInstance(nodeUri).borrowObject();
        } catch (Exception e) {
            // FIXME use appropriate exception for this
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, (Object[]) null);
        }
    }

    //
    // Message management
    //

    /**
     *
     */
    @Override
    public void sendAsync(AmqpMessage amqpMessage)
            throws KapuaException {
        sendSync(amqpMessage, null);
    }

    @Override
    public AmqpMessage sendSync(AmqpMessage amqpMessage, Long timeout)
            throws KapuaException {
        List<AmqpMessage> responses = new ArrayList<>();

        sendInternal(amqpMessage, responses, timeout);

        if (timeout != null) {
            if (responses.isEmpty()) {
                throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_TIMEOUT_EXCEPTION, null, amqpMessage.getRequestTopic());
            }
            AmqpMessage response = responses.get(0);
            //TODO FIXME why the message interchanges between AMQP-MQTT connectors (with MQTT virtual topic on) on AtiveMQ doesn't clean the topic://VirtualTopic prefix?
            if (response.getChannel().getTopic().toString().startsWith(VT_TOPIC_PREFIX)) {
                response.setChannel(new AmqpTopic(response.getChannel().getTopic().toString().substring(VT_TOPIC_PREFIX.length()).replaceAll("\\.", "/")));
            }
            return response;
        } else {
            return null;
        }
    }

    /**
     * Actual implementation of the send operations.
     * <p>
     * According to the parameters given, it will make a sync or async request.
     * </p>
     *
     * @param amqpMessage The request to send.
     * @param responses   The container in which load responses received from the device
     * @param timeout     The timeout of waiting the response from the device.
     *                    If {@code null} request will be fired without waiting for the response.
     *                    If amqpMessage has no response message set, timeout will be ignore even if set.
     * @throws KapuaException
     * @see AmqpMessage#getResponseTopic()
     * @since 1.0.0.
     */
    private void sendInternal(AmqpMessage amqpMessage, List<AmqpMessage> responses, Long timeout)
            throws KapuaException {
        try {
            //
            // Subscribe if necessary
            if (amqpMessage.getResponseTopic() != null) {
                try {
                    amqpClientCallback = new AmqpClientConsumerHandler(responses);
                    borrowedClient.subscribe(amqpMessage.getResponseTopic(), amqpClientCallback);
                } catch (KapuaException e) {
                    throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_SUBSCRIBE_ERROR, e, amqpMessage.getResponseTopic().getTopic());
                }
            }

            //
            // Publish message
            try {
                borrowedClient.publish(amqpMessage);
            } catch (KapuaException e) {
                throw new AmqpClientException(
                        AmqpClientErrorCodes.CLIENT_PUBLISH_ERROR,
                        e,
                        amqpMessage.getRequestTopic().getTopic(),
                        amqpMessage.getPayload().getBody());
            }

            //
            // Wait if required
            if (amqpMessage.getResponseTopic() != null &&
                    timeout != null) {
                String timerName = new StringBuilder().append(AmqpFacade.class.getSimpleName())
                        .append("-TimeoutTimer-")
                        .append(borrowedClient.getClientId())
                        .toString();

                Timer timeoutTimer = new Timer(timerName, true);

                timeoutTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (amqpMessage.getResponseTopic() != null) {
                            synchronized (amqpClientCallback) {
                                amqpClientCallback.notifyAll();
                            }
                        }
                    }
                }, timeout);

                try {
                    synchronized (amqpClientCallback) {
                        amqpClientCallback.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_CALLBACK_ERROR, e, (Object) null);
                } finally {
                    timeoutTimer.cancel();
                }
            }
        } catch (Exception e) {
            throw new AmqpClientException(AmqpClientErrorCodes.SEND_ERROR,
                    e,
                    amqpMessage.getRequestTopic().getTopic());
        }
    }

    @Override
    public String getClientId() {
        return borrowedClient.getClientId();
    }

    @Override
    public Class<AmqpMessage> getMessageClass() {
        return AmqpMessage.class;
    }

    @Override
    public void clean() {
        //
        // Return the client form the pool
        try {
            borrowedClient.clean();
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AmqpClientPool.getInstance(nodeUri).returnObject(borrowedClient);
        borrowedClient = null;
    }
}
