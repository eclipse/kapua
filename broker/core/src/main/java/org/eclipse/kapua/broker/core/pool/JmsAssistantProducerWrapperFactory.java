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

import javax.jms.Session;

import org.apache.activemq.ActiveMQSession;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BasePooledObjectFactory} assistant broker wrapper factory implementation.
 *
 * @since 1.0
 */
public class JmsAssistantProducerWrapperFactory extends BasePooledObjectFactory<JmsAssistantProducerWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(JmsAssistantProducerWrapperFactory.class);

    private final String destination;

    public JmsAssistantProducerWrapperFactory(String destination) {
        this.destination = destination;
    }

    @Override
    public JmsAssistantProducerWrapper create() throws Exception {
        return new JmsAssistantProducerWrapper(JmsConnectionFactory.VM_CONN_FACTORY, destination, false, false);
    }

    @Override
    public PooledObject<JmsAssistantProducerWrapper> wrap(JmsAssistantProducerWrapper producerWrapper) {
        return new DefaultPooledObject<JmsAssistantProducerWrapper>(producerWrapper);
    }

    /**
     * Check if the session is still active
     */
    @Override
    public boolean validateObject(PooledObject<JmsAssistantProducerWrapper> p) {
        Session session = p.getObject().session;
        if (session instanceof ActiveMQSession) {
            return !((ActiveMQSession) session).isClosed();
        } else {
            logger.warn("Wrong session object type {}", session.getClass());
            return true;
        }
    }

    @Override
    public void destroyObject(PooledObject<JmsAssistantProducerWrapper> pooledProducerWrapper) throws Exception {
        JmsAssistantProducerWrapper producerWrapper = pooledProducerWrapper.getObject();
        logger.info("Close jms broker assistant producer wrapper: {}", producerWrapper.toString());
        producerWrapper.close();
        super.destroyObject(pooledProducerWrapper);
    }

}
