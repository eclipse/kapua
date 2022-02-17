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
package org.eclipse.kapua.broker.core.pool;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

@Category(JUnitTests.class)
public class JmsAssistantProducerWrapperFactoryTest extends Assert {

    String[] destinations;
    JmsAssistantProducerWrapper producerWrapper;
    PooledObject<JmsAssistantProducerWrapper> pooledProducerWrapper;
    Session session;
    ActiveMQSession activeMQSession;
    Connection connection;
    MessageProducer producer;
    ActiveMQConnectionFactory activeMQConnectionFactory;

    @Before
    public void initialize() {
        destinations = new String[]{"", "destination", "destination1234567890", "dEsTiNaTion-1pr23", "De-12!stinatioN   123#$%", "  dest0-=,...,,ination!123#", "() _ + ?><|/.des-=44tinat,,ion'!@#$ % ^&*"};
        producerWrapper = Mockito.mock(JmsAssistantProducerWrapper.class);
        pooledProducerWrapper = Mockito.mock(PooledObject.class);
        session = Mockito.mock(Session.class);
        activeMQSession = Mockito.mock(ActiveMQSession.class);
        connection = Mockito.mock(Connection.class);
        producer = Mockito.mock(MessageProducer.class);
        activeMQConnectionFactory = Mockito.mock(ActiveMQConnectionFactory.class);
    }

    @Test
    public void jmsAssistantProducerWrapperFactoryTest() {
        for (String destination : destinations) {
            try {
                new JmsAssistantProducerWrapperFactory(destination);
            } catch (Exception e) {
                fail("No exception should be thrown");
            }
        }
    }

    @Test
    public void wrapTest() {
        for (String destination : destinations) {
            JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory(destination);
            assertEquals("Expected and actual values should be the same.", PooledObjectState.IDLE, wrapperFactory.wrap(producerWrapper).getState());
            assertTrue("True expected.", wrapperFactory.wrap(producerWrapper) instanceof PooledObject);
        }
    }

    @Test
    public void wrapNullTest() {
        for (String destination : destinations) {
            JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory(destination);
            assertEquals("Expected and actual values should be the same.", PooledObjectState.IDLE, wrapperFactory.wrap(null).getState());
            assertTrue("True expected.", wrapperFactory.wrap(null) instanceof PooledObject);
        }
    }

    @Test
    public void validateObjectTest() throws KapuaException, JMSException {
        Mockito.when(activeMQConnectionFactory.createConnection()).thenReturn(connection);
        Mockito.when(session.createProducer(session.createQueue("destination"))).thenReturn(producer);
        Mockito.when(activeMQConnectionFactory.createConnection().createSession(true, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        JmsAssistantProducerWrapper producerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "destination", true, true);
        Mockito.when(pooledProducerWrapper.getObject()).thenReturn(producerWrapper);
        JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory("destination");

        assertTrue("True expected.", wrapperFactory.validateObject(pooledProducerWrapper));
    }

    @Test
    public void validateObjectActiveMQSessionTrueTest() throws KapuaException, JMSException {
        Mockito.when(activeMQConnectionFactory.createConnection()).thenReturn(connection);
        Mockito.when(activeMQSession.createProducer(activeMQSession.createQueue("destination"))).thenReturn(producer);
        Mockito.when(activeMQConnectionFactory.createConnection().createSession(true, Session.AUTO_ACKNOWLEDGE)).thenReturn(activeMQSession);
        JmsAssistantProducerWrapper producerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "destination", true, true);
        Mockito.when(pooledProducerWrapper.getObject()).thenReturn(producerWrapper);
        JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory("destination");

        assertTrue("True expected.", wrapperFactory.validateObject(pooledProducerWrapper));
    }

    @Test
    public void validateObjectActiveMQSessionFalseTest() throws KapuaException, JMSException {
        Mockito.when(activeMQConnectionFactory.createConnection()).thenReturn(connection);
        Mockito.when(activeMQSession.createProducer(activeMQSession.createQueue("destination"))).thenReturn(producer);
        Mockito.when(activeMQConnectionFactory.createConnection().createSession(true, Session.AUTO_ACKNOWLEDGE)).thenReturn(activeMQSession);
        JmsAssistantProducerWrapper producerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "destination", true, true);
        Mockito.when(pooledProducerWrapper.getObject()).thenReturn(producerWrapper);
        JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory("destination");
        Mockito.when(activeMQSession.isClosed()).thenReturn(true);

        assertFalse("False expected.", wrapperFactory.validateObject(pooledProducerWrapper));
    }


    @Test(expected = NullPointerException.class)
    public void validateNullTest() {
        JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory("destination");
        wrapperFactory.validateObject(null);
    }

    @Test
    public void destroyObjectTest() {
        for (String destination : destinations) {
            JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory(destination);
            Mockito.when(pooledProducerWrapper.getObject()).thenReturn(producerWrapper);
            try {
                wrapperFactory.destroyObject(pooledProducerWrapper);
            } catch (Exception e) {
                fail("Exception not expected.");
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void destroyObjectNullTest() throws Exception {
        for (String destination : destinations) {
            JmsAssistantProducerWrapperFactory wrapperFactory = new JmsAssistantProducerWrapperFactory(destination);
            wrapperFactory.destroyObject(null);
        }
    }
}