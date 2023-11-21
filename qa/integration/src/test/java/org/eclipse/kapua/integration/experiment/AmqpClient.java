/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.experiment;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    String host;
    int port;
    String clientId;
    Connection connection;
    Session session;
    MessageProducer producer;

    AmqpClient(String host, int port, String clientId) {
        this.host = host;
        this.port = port;
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public String getClientId() {
        return clientId;
    }

    public void send(int messageNumber) throws JMSException {
        producer.send(getTextMessage(clientId + " | (" + messageNumber + ")"));
    }

    public void send(String address, int messageNumber) throws JMSException {
        session.createProducer(session.createQueue(address)).send(getTextMessage(clientId + " | (" + messageNumber + ")"));
    }

    public void sendToQueue(String address, byte[] message) throws JMSException {
        session.createProducer(session.createQueue(address)).send(getByteMessage(message));
    }

    public void sendToTopic(String address, byte[] message) throws JMSException {
        session.createProducer(session.createTopic(address)).send(getByteMessage(message));
    }

    public TextMessage getTextMessage(String message) throws JMSException {
        return session.createTextMessage(message);
    }

    public BytesMessage getByteMessage(byte[] message) throws JMSException {
        BytesMessage bytesMessage = session.createBytesMessage();
        bytesMessage.writeBytes(message);
        bytesMessage.setStringProperty("KAPUA_MESSAGE_TYPE", "TEL");
        return bytesMessage;
    }

    public void close() {
        close(producer);
        close(session);
        close(connection);
    }

    private void close(AutoCloseable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception e) {
                logger.warn("Error:", e);
            }
        }
    }

    public void init(String username, String password, String publishAdress, boolean durable, String[] addresses, MessageListener messageListener) throws Exception {
        session = createClientAndSubscribe(host, port, username, password, clientId, addresses, durable, 1000, messageListener);
        producer = session.createProducer(session.createQueue(publishAdress));
    }

    private Session createClientAndSubscribe(String host, int port, String username, String password, String clientId, String[] addresses, boolean durable, int timeout, MessageListener messageListener) throws Exception {
        connection = new JmsConnectionFactory(username, password, "amqp://" + host + ":" + port).createConnection();
        connection.setClientID(clientId);
        connection.start();
        session = connection.createSession();
        for (String str : addresses) {
            subscribe(str, durable, messageListener);
        }
        return session;
    }

    private void subscribe(String address, boolean durable, MessageListener messageListener) throws JMSException {
        logger.info("Subscribing {} from client {} to broker {}", address, clientId, port);
        MessageConsumer messageConsumer = durable ? session.createDurableConsumer(session.createTopic(address), address) : session.createConsumer(session.createTopic(address));
        messageConsumer.setMessageListener(messageListener);
    }

    public int getIndexFromReply(String reply) {
        return Integer.valueOf(reply.substring(reply.indexOf('-') + 1, reply.indexOf(' ')));
    }

}