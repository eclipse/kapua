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
package org.eclipse.kapua.commons.event.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceConnectionFactoryImpl extends ActiveMQConnectionFactory {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceConnectionFactoryImpl.class);

    private final static AtomicInteger INDEX = new AtomicInteger();

    private String url;
    private String clientId;

    public ServiceConnectionFactoryImpl(String host, int port, String username, String password, String clientId) {
        super("tcp://" + host + ":" + port + "?minLargeMessageSize=999123&amqpMinLargeMessageSize=999123", username, password);
        url = "tcp://" + host + ":" + port + "?minLargeMessageSize=999123&amqpMinLargeMessageSize=999123";
        this.clientId = clientId;
        setMinLargeMessageSize(1124000);
        logger.info("Created connection factory with client id: {} - {} - min large: {}", url, this.clientId, getMinLargeMessageSize());
    }

    @Override
    public Connection createConnection(String username, String password) throws JMSException {
        String generatedClient = generateClientId();
        logger.info("Connecting to {} - Creating connection for generated client id: {} - min large: {}", url, generatedClient, getMinLargeMessageSize());
        Connection connection = super.createConnection(username, password);
        connection.setClientID(generatedClient);
        return connection;
    }

    protected String generateClientId() {
        return clientId + "-" + INDEX.incrementAndGet();
    }

}
