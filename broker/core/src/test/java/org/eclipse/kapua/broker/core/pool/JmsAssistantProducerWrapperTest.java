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
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.JMSException;

@Category(JUnitTests.class)
public class JmsAssistantProducerWrapperTest extends Assert {

    Session session;
    Connection connection;
    String destination;
    String[] topics;
    String[] messages;
    boolean[] transacted;
    boolean[] start;
    MessageProducer producer;
    ActiveMQConnectionFactory activeMQConnectionFactory;
    KapuaSecurityContext kapuaSecurityContext;
    TextMessage textMessage;
    JmsAssistantProducerWrapper jmsAssistantProducerWrapper;

    @Before
    public void initialize() throws JMSException {
        session = Mockito.mock(Session.class);
        connection = Mockito.mock(Connection.class);
        destination = "Destination";
        topics = new String[]{"", "Topic", "Topic123", "Topic!@#$%^&*(?>)_+"};
        messages = new String[]{"", "Message", "Message123", "Message!@#$%^&*(?>)_+"};
        transacted = new boolean[]{true, false};
        start = new boolean[]{true, false};
        producer = Mockito.mock(MessageProducer.class);
        activeMQConnectionFactory = Mockito.mock(ActiveMQConnectionFactory.class);
        kapuaSecurityContext = Mockito.mock(KapuaSecurityContext.class);
        textMessage = Mockito.mock(TextMessage.class);
        Mockito.when(activeMQConnectionFactory.createConnection()).thenReturn(connection);
        Mockito.when(session.createProducer(session.createQueue(destination))).thenReturn(producer);
        Mockito.when(session.createTextMessage()).thenReturn(textMessage);
        Mockito.when(kapuaSecurityContext.getBrokerId()).thenReturn("Broker Id");
    }

    @Test
    public void jmsAssistantProducerWrapperTest() throws KapuaException, JMSException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, destination, transactedValue, startValue);

                assertEquals("Expected and actual values should be the same.", "Destination", jmsAssistantProducerWrapper.destination);
                assertEquals("Expected and actual values should be the same.", session, jmsAssistantProducerWrapper.session);
                assertEquals("Expected and actual values should be the same.", connection, jmsAssistantProducerWrapper.connection);
                assertEquals("Expected and actual values should be the same.", producer, jmsAssistantProducerWrapper.producer);
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void setJmsAssistantProducerWrapperNullFactoryTest() throws KapuaException, JMSException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                new JmsAssistantProducerWrapper(null, destination, transactedValue, startValue);
            }
        }
    }

    @Test
    public void setJmsAssistantProducerWrapperNullDestinationTest() throws KapuaException, JMSException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                JmsAssistantProducerWrapper jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, null, transactedValue, startValue);

                assertNull("Null expected.", jmsAssistantProducerWrapper.destination);
                assertEquals("Expected and actual values should be the same.", session, jmsAssistantProducerWrapper.session);
                assertEquals("Expected and actual values should be the same.", connection, jmsAssistantProducerWrapper.connection);
                assertEquals("Expected and actual values should be the same.", producer, jmsAssistantProducerWrapper.producer);
            }
        }
    }

    @Test
    public void setJmsAssistantProducerWrapperEmptyDestinationTest() throws KapuaException, JMSException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                JmsAssistantProducerWrapper jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "", transactedValue, startValue);

                assertEquals("Expected and actual values should be the same.", "", jmsAssistantProducerWrapper.destination);
                assertEquals("Expected and actual values should be the same.", session, jmsAssistantProducerWrapper.session);
                assertEquals("Expected and actual values should be the same.", connection, jmsAssistantProducerWrapper.connection);
                assertEquals("Expected and actual values should be the same.", producer, jmsAssistantProducerWrapper.producer);
            }
        }
    }

    @Test
    public void sendTest() {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                for (String topic : topics) {
                    for (String message : messages) {
                        try {
                            Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                            jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "Destination", transactedValue, startValue);
                            jmsAssistantProducerWrapper.send(topic, message, kapuaSecurityContext);
                        } catch (Exception e) {
                            fail("Exception not expected.");
                        }
                    }
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void sendNullTest() throws JMSException, KapuaException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "Destination", transactedValue, startValue);
                jmsAssistantProducerWrapper.send(null, null, null);
            }
        }
    }

    @Test
    public void sendNullTopicTest() {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                for (String message : messages) {
                    try {
                        Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                        jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "Destination", transactedValue, startValue);
                        jmsAssistantProducerWrapper.send(null, message, kapuaSecurityContext);
                    } catch (Exception e) {
                        fail("Exception not expected.");
                    }
                }
            }
        }
    }

    @Test
    public void sendNullMessageTest() {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                for (String topic : topics) {
                    try {
                        Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                        jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "Destination", transactedValue, startValue);
                        jmsAssistantProducerWrapper.send(topic, null, kapuaSecurityContext);
                    } catch (Exception e) {
                        fail("Exception not expected.");
                    }
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void sendNullKapuaSecurityContextTest() throws JMSException, KapuaException {
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                for (String topic : topics) {
                    for (String message : messages) {
                        Mockito.when(activeMQConnectionFactory.createConnection().createSession(transactedValue, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
                        jmsAssistantProducerWrapper = new JmsAssistantProducerWrapper(activeMQConnectionFactory, "Destination", transactedValue, startValue);
                        jmsAssistantProducerWrapper.send(topic, message, null);
                    }
                }
            }
        }
    }
}