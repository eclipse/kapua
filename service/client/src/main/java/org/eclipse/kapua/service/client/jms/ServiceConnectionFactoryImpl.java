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
package org.eclipse.kapua.service.client.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceConnectionFactoryImpl extends ActiveMQConnectionFactory {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceConnectionFactoryImpl.class);

    private final static AtomicInteger INDEX = new AtomicInteger();

    private String clientId;

    public ServiceConnectionFactoryImpl(String url, String username, String password, String clientId) {
        super(url, username, password);
        this.clientId = clientId;
        logger.info("From service - Created connection factory with client id: {} - min large: {}", this.clientId, getMinLargeMessageSize());
    }

    @Override
    public Connection createConnection(String username, String password) throws JMSException {
        Connection connection = super.createConnection(username, password);
        String generatedClient = generateClientId();
        logger.info("From service - Created connection for generated client id: {} - min large: {}", generatedClient, getMinLargeMessageSize());
        connection.setClientID(generatedClient);
        return connection;
    }

    protected String generateClientId() {
        return clientId + "-" + INDEX.incrementAndGet();
    }

}
