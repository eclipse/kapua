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
package org.eclipse.kapua.broker.core.pool;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jms producer wrapper.<BR>
 * This class wrap a single session per connection and manage the close operation of connection on session close.<BR>
 * The connection is taken from a connection pool ({@link org.apache.activemq.ActiveMQConnectionFactory})
 *
 * @since 1.0
 */
public abstract class JmsProducerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(JmsProducerWrapper.class);

    protected String destination;
    protected Connection connection;
    protected Session session;
    protected MessageProducer producer;

    /**
     *
     * @param vmconnFactory
     * @param destination
     *            if it's null the producer will not be bound to any destination so it can sends messages to the whole topic space.<BR>
     *            Otherwise if it isn't null the producer will be bound to a queue destination as specified by the parameter.
     * @param transacted
     * @param start
     *            start activeMQ connection
     * @throws JMSException
     * @throws KapuaException
     */
    protected JmsProducerWrapper(ActiveMQConnectionFactory vmconnFactory, String destination, boolean transacted, boolean start) throws JMSException, KapuaException {
        connection = vmconnFactory.createConnection();
        if (start == true) {
            connection.start();
        }
        session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
        if (destination != null && destination.trim().length() > 0) {
            producer = session.createProducer(session.createQueue(destination));
        } else {
            producer = session.createProducer(null);
        }
        this.destination = destination;
    }

    public void close() {
        try {
            connection.close();
        } catch (JMSException e) {
            logger.error("Exception on connection close {}", e.getMessage(), e);
        }
    }

    public String getDestination() {
        return destination;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}
