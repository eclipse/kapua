/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle request/reply through AMQP messaging layer
 *
 */
public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    private Connection connection;//keep to implement cleanup (and object lifecycle)
    private Session session;
    private MessageConsumer consumer;
    private MessageProducer producer;

    private ConnectionStatus connectionStatus;

    public Client(ConnectionFactory connectionFactory, String requestAddress, String replyAddress, ClientMessageListener clientMessageListener) throws JMSException {
        connectionStatus = new ConnectionStatus();
        connection = connectionFactory.createConnection();
        connection.setExceptionListener(new JMSExceptionListner(connectionStatus));
        connection.start();
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        logger.info("Client binding request sender to: {}", requestAddress);
        logger.info("Client binding message listener to: {}", replyAddress);
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