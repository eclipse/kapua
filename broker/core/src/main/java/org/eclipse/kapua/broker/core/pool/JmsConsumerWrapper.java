/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jms consumer wrapper.<BR>
 * This class wrap a single session per connection and manage the close operation of connection on session close.<BR>
 * The connection is taken from a connection pool ({@link org.apache.activemq.ActiveMQConnectionFactory})
 *
 * @since 1.0
 */
public class JmsConsumerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(JmsConsumerWrapper.class);

    protected String destination;
    protected Connection connection;
    protected Session session;
    protected MessageConsumer consumer;

    /**
     *
     * @param destination
     * @param transacted
     * @param start
     *            start activeMQ connection
     * @throws JMSException
     * @throws KapuaException
     */
    public JmsConsumerWrapper(String destination, boolean transacted, boolean start, MessageListener messageListener) throws JMSException, KapuaException {
        if (StringUtils.isEmpty(destination)) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, "Invalid destination (empty)!");
        }
        connection = JmsConnectionFactory.VM_CONN_FACTORY.createConnection();
        if (start == true) {
            connection.start();
        }
        session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(session.createTopic(destination));
        consumer.setMessageListener(messageListener);
        this.destination = destination;
    }

    public void close() {
        try {
            connection.close();
        } catch (JMSException e) {
            logger.error("Exception on connection close {}", e.getMessage(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}
