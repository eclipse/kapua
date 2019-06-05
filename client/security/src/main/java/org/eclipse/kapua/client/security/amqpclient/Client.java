/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.amqpclient;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle request/reply through AMQP messaging layer
 *
 */
public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    private ConnectionFactory connectionFactory;//is this reference needed?
    private Connection connection;//keep to implement cleanup (and object lifecycle)
    private Session session;
    private MessageConsumer consumer;
    private MessageProducer producer;

    private ConnectionStatus connectionStatus;

    public Client(String username, String password, String host, int port, String clientId,
            String requestAddress, String replyAddress, ClientMessageListener clientMessageListener) throws JMSException {
        connectionStatus = new ConnectionStatus();
        connectionFactory = new JmsConnectionFactory(username, password, "amqp://" + host + ":" + port);
        connection = connectionFactory.createConnection();
        connection.setExceptionListener(new JMSExceptionListner(connectionStatus, clientId));
        connection.setClientID(clientId);
        connection.start();
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        consumer = session.createConsumer(session.createQueue(replyAddress));
        consumer.setMessageListener(clientMessageListener);
        producer = session.createProducer(session.createQueue(requestAddress));
        clientMessageListener.init(session, producer);
        connectionStatus.setConnectionAlive();
    }

    public void checkAuthServiceConnection() {
        if (!isConnected()) {
            //TODO throw exception and deny operations
        }
    }

    public void sendMessage(TextMessage message) throws JMSException {
        producer.send(message);
        message.acknowledge();
    }

    public TextMessage createTextMessage() throws JMSException {
        return session.createTextMessage();
    }

    public boolean isConnected() {
        try {
            return connection!=null && connection.getClientID()!=null && connectionStatus.isAlive();
        } catch (JMSException e) {
            logger.warn("Error while validating connection: {}", e.getMessage(), e);
            return false;
        }
    }
}