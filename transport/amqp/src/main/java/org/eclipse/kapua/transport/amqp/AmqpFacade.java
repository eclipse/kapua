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
package org.eclipse.kapua.transport.amqp;

import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.AmqpClient;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.DestinationTranslator;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.eclipse.kapua.transport.amqp.setting.AmqpClientSetting;
import org.eclipse.kapua.transport.amqp.setting.AmqpClientSettingKeys;
import org.eclipse.kapua.transport.utils.ClientIdGenerator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of {@link TransportFacade} API for AMQP transport facade.
 *
 * @since 1.0.0
 */
public class AmqpFacade implements TransportFacade<AmqpTopic, AmqpPayload, AmqpMessage, AmqpMessage> {

    private final static String VT_PREFIX_PATTERN = "topic://VirtualTopic.%s";

    /**
     * The client to use to make requests.
     *
     * @since 1.0.0
     */
    private AmqpClient client;

    /**
     * The client callback for this set of requests.
     *
     * @since 1.0.0
     */
    private AmqpClientConsumerHandler amqpClientCallback;

    /**
     * Initialize a transport facade to be used to send requests to devices.
     * 
     * @param nodeUri
     * @throws KapuaException When AMQP client is not available.
     */
    public AmqpFacade(String nodeUri, Map<String, Object> configParameters) throws KapuaException {
        try {
            AmqpClientSetting amqpClientSettings = AmqpClientSetting.getInstance();
            String username = amqpClientSettings.getString(AmqpClientSettingKeys.TRANSPORT_CREDENTIAL_USERNAME);
            char[] password = amqpClientSettings.getString(AmqpClientSettingKeys.TRANSPORT_CREDENTIAL_PASSWORD).toCharArray();
            String clientId = ClientIdGenerator.getInstance().next(amqpClientSettings.getString(AmqpClientSettingKeys.CLIENT_ID_PREFIX));
            AmqpClientConnectionOptions connectionOptions = new AmqpClientConnectionOptions();
            connectionOptions.setClientId(clientId);
            connectionOptions.setUsername(username);
            connectionOptions.setPassword(password);
            connectionOptions.setEndpointURI(URI.create(nodeUri));
            client = AmqpSessionFactory.getInstance(nodeUri, wrapOptions(connectionOptions));
        } catch (Exception e) {
            // FIXME use appropriate exception for this
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, (Object[]) null);
        }
    }

    private ClientOptions wrapOptions(TransportClientConnectOptions options) {
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.put(AmqpClientOptions.BROKER_HOST, "127.0.0.1");
        //the connection port is a deployment parameter so read it from configuration
        clientOptions.put(AmqpClientOptions.BROKER_PORT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECTION_PORT));
        clientOptions.put(AmqpClientOptions.PASSWORD, options.getPassword());
        clientOptions.put(AmqpClientOptions.USERNAME, options.getUsername());
        clientOptions.put(AmqpClientOptions.CLIENT_ID, options.getClientId());
        clientOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECT_TIMEOUT));
        clientOptions.put(AmqpClientOptions.EXIT_CODE, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.EXIT_CODE));
        clientOptions.put(AmqpClientOptions.IDLE_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
        clientOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
        clientOptions.put(AmqpClientOptions.WAIT_BETWEEN_RECONNECT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_WAIT_BETWEEN_RECONNECT));
        if (AmqpClientSetting.getInstance().getBoolean(AmqpClientSettingKeys.TRANSPORT_MQTT_VT_ENABLED)) {
            //enable it if the device is connected through ActiveMQ MQTT connector with VirtualTopic enabled
            clientOptions.put(AmqpClientOptions.DESTINATION_TRANSLATOR, new DestinationTranslator() {

                @Override
                public String translate(String destination) {
                    return String.format(VT_PREFIX_PATTERN, destination.replaceAll("/", "."));
                }
            });
        }
        return clientOptions;
    }

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
                throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_TIMEOUT_EXCEPTION, null, (amqpMessage.getRequestTopic() != null ? amqpMessage.getRequestTopic().getTopic() : "N/A"));
            }
            AmqpMessage response = responses.get(0);
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
            // Subscribe if necessary
            if (amqpMessage.getResponseTopic() != null) {
                amqpClientCallback = new AmqpClientConsumerHandler(responses);
                client.subscribe(amqpMessage.getResponseTopic().getTopic(), amqpClientCallback);
            }
            // Publish message
            AmqpTopic amqpTopic = amqpMessage.getRequestTopic();
            AmqpPayload amqpPayload = amqpMessage.getPayload();
            client.send(createMesage(amqpPayload.getBody(), amqpTopic.getTopic()), amqpTopic.getTopic());

            // Wait if required
            if (amqpMessage.getResponseTopic() != null &&
                    timeout != null) {
                String timerName = new StringBuilder().append(AmqpFacade.class.getSimpleName())
                        .append("-TimeoutTimer-")
                        .append(client.getClientId())
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
        return client.getClientId();
    }

    @Override
    public Class<AmqpMessage> getMessageClass() {
        return AmqpMessage.class;
    }

    @Override
    public void clean() {
        client.clean();
        client = null;
    }

    private Message createMesage(byte[] payload, String destination) {
        Message msg = Proton.message();
        Section s = new Data(new Binary(payload));
        msg.setBody(s);
        msg.setAddress(destination);
        return msg;
    }
}
