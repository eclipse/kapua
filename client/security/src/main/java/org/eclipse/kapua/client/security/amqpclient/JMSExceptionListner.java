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

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSExceptionListner implements ExceptionListener {

    protected static final Logger logger = LoggerFactory.getLogger(JMSExceptionListner.class);

    private ConnectionStatus connectionStatus;
    private String clientId;

    public JMSExceptionListner(ConnectionStatus connectionStatus, String clientId) {
        this.connectionStatus = connectionStatus;
        this.clientId = clientId;
    }

    @Override
    public void onException(JMSException e) {
        connectionStatus.setConnectionFault();
        logger.warn("Client: {} - Error: {} ", clientId, e.getMessage());
    }
}