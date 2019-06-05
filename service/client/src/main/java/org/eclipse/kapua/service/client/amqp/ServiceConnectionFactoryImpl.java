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
package org.eclipse.kapua.service.client.amqp;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;

public class ServiceConnectionFactoryImpl extends JmsConnectionFactory {

    private final static AtomicInteger INDEX = new AtomicInteger();

    private String clientId;

    public ServiceConnectionFactoryImpl(String host, int port, String username, String password, String clientId) {
        super(username, password, "amqp://" + host + ":" + port);
        this.clientId = clientId;
    }

    @Override
    public Connection createConnection(String username, String password) throws JMSException {
        Connection connection = super.createConnection(username, password);
        connection.setClientID(generateClientId());
        return connection;
    }

    protected String generateClientId() {
        return clientId + "-" + INDEX.incrementAndGet();
    }

}
