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
package org.eclipse.kapua.broker.core.message;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProvider;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProviders;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.cache.TranslatorCache;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Topic;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Date;

@Category(JUnitTests.class)
public class JmsUtilTest extends Assert {

    ActiveMQMessage activeMQMessage;
    ActiveMQDestination activeMQDestination;
    Topic topic;
    ConnectorDescriptorProvider provider;
    ConnectorDescriptor connectorDescriptor;
    ConnectorDescriptor.MessageType messageType;
    BytesMessage jmsMessage;
    KapuaId connectionId;
    String clientId;
    KapuaMessage kapuaMessage;
    Translator translator1;
    Translator translator2;
    org.eclipse.kapua.transport.message.jms.JmsMessage jmsMessageForKapua;
    JmsPayload jmsPayload;
    byte[] messageBody;
    String jmsTopic = "topic";
    Date queuedOn = new Date();
    DeviceMessage deviceMessage;
    org.eclipse.kapua.transport.message.jms.JmsMessage returnJmsMessage;

    @Before
    public void initialize() throws JMSException {
        activeMQMessage = Mockito.mock(ActiveMQMessage.class);
        activeMQDestination = Mockito.mock(ActiveMQDestination.class);
        topic = Mockito.mock(Topic.class);
        provider = ConnectorDescriptorProviders.getInstance();
        connectorDescriptor = provider.getDescriptor("foo");
        messageType = ConnectorDescriptor.MessageType.BIRTH;
        jmsMessage = Mockito.mock(BytesMessage.class);
        connectionId = KapuaId.ONE;
        clientId = "clientId";
        kapuaMessage = Mockito.mock(KapuaMessage.class);
        translator1 = Mockito.mock(Translator.class);
        translator2 = Mockito.mock(Translator.class);
        jmsMessageForKapua = Mockito.mock(JmsMessage.class);
        jmsPayload = new JmsPayload(new byte[(int) jmsMessage.getBodyLength()]);
        messageBody = new byte[]{127, 110, 1, 0, 11, -1, -10, -128};
        deviceMessage = Mockito.mock(DeviceMessage.class);
        returnJmsMessage = Mockito.mock(JmsMessage.class);
    }

    @Test
    public void jmsUtilTest() throws Exception {
        Constructor<JmsUtil> jmsUtil = JmsUtil.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(jmsUtil.getModifiers()));
        jmsUtil.setAccessible(true);
        jmsUtil.newInstance();
    }

    @Test(expected = NullPointerException.class)
    public void getJmsTopicNullTest() throws JMSException {
        JmsUtil.getJmsTopic(null);
    }

    @Test
    public void getJmsTopicTopicDestinationTest() throws JMSException {
        Mockito.when(activeMQMessage.getDestination()).thenReturn(activeMQDestination);
        Mockito.when(activeMQDestination.isTopic()).thenReturn(true);
        Mockito.when(activeMQMessage.getJMSDestination()).thenReturn(topic);
        Mockito.when(topic.getTopicName()).thenReturn("VirtualTopic.name");

        assertEquals("Expected and actual values should be the same.", "name", JmsUtil.getJmsTopic(activeMQMessage));
    }

    @Test
    public void getJmsTopicQueueDestinationTest() throws JMSException {
        Mockito.when(activeMQMessage.getDestination()).thenReturn(activeMQDestination);
        Mockito.when(activeMQDestination.isTopic()).thenReturn(false);
        Mockito.when(activeMQDestination.isQueue()).thenReturn(true);
        Mockito.when(activeMQMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("queue");

        assertEquals("Expected and actual values should be the same.", "queue", JmsUtil.getJmsTopic(activeMQMessage));
    }

    @Test
    public void getNullJmsTopicTest() throws JMSException {
        Mockito.when(activeMQMessage.getDestination()).thenReturn(activeMQDestination);
        Mockito.when(activeMQDestination.isTopic()).thenReturn(false);
        Mockito.when(activeMQDestination.isQueue()).thenReturn(false);

        assertNull("Null expected.", JmsUtil.getJmsTopic(activeMQMessage));
    }

    @Test
    public void convertToKapuaMessageTest() throws KapuaException, JMSException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(jmsMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("original topic");
        Mockito.when(jmsMessage.getLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP)).thenReturn(10L);
        Mockito.when(jmsMessage.getBodyLength()).thenReturn(10L);
        Mockito.when(kapuaMessage.getClientId()).thenReturn("id");

        assertTrue("True expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToKapuaMessageEmptyClientIdTest() throws KapuaException, JMSException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(jmsMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("original topic");
        Mockito.when(jmsMessage.getLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP)).thenReturn(10L);
        Mockito.when(jmsMessage.getBodyLength()).thenReturn(10L);
        Mockito.when(kapuaMessage.getClientId()).thenReturn("");

        assertTrue("True expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToKapuaMessageZeroBodyLengthTest() throws KapuaException, JMSException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), null));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(jmsMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("original topic");
        Mockito.when(jmsMessage.getLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP)).thenReturn(10L);
        Mockito.when(jmsMessage.getBodyLength()).thenReturn(0L);
        Mockito.when(kapuaMessage.getClientId()).thenReturn("id");

        assertTrue("True expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, clientId).getConnectorDescriptor());
    }

    @Test(expected = NullPointerException.class)
    public void convertToKapuaMessageNullConnectorDescriptorTest() throws KapuaException, JMSException {
        JmsUtil.convertToKapuaMessage(null, messageType, jmsMessage, connectionId, clientId);
    }

    @Test(expected = NullPointerException.class)
    public void convertToKapuaMessageNullMessageTypeTest() throws KapuaException, JMSException {
        JmsUtil.convertToKapuaMessage(connectorDescriptor, null, jmsMessage, connectionId, clientId);
    }

    @Test(expected = NullPointerException.class)
    public void convertToKapuaMessageNullJmsMessageTest() throws KapuaException, JMSException {
        JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, null, connectionId, clientId);
    }

    @Test
    public void convertJmsWildCardToMqttNullTest() {
        assertNull("Null expected.", JmsUtil.convertJmsWildCardToMqtt(null));
    }

    @Test
    public void convertToKapuaMessageNullConnectionIdTest() throws KapuaException, JMSException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(jmsMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("original topic");
        Mockito.when(jmsMessage.getLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP)).thenReturn(10L);
        Mockito.when(jmsMessage.getBodyLength()).thenReturn(10L);
        Mockito.when(kapuaMessage.getClientId()).thenReturn("id");

        assertTrue("True expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, null, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, null, clientId).getMessage());
        assertNull("Null expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, null, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, null, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToKapuaMessageNullClientIdTest() throws KapuaException, JMSException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(jmsMessage.getStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC)).thenReturn("original topic");
        Mockito.when(jmsMessage.getLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP)).thenReturn(10L);
        Mockito.when(jmsMessage.getBodyLength()).thenReturn(10L);

        assertTrue("True expected.", JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, null) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, null).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, null).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToKapuaMessage(connectorDescriptor, messageType, jmsMessage, connectionId, null).getConnectorDescriptor());
    }

    @Test
    public void convertToCamelKapuaMessageTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), queuedOn, jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, clientId).getConnectorDescriptor());
    }

    @Test(expected = NullPointerException.class)
    public void convertToCamelKapuaMessageNullConnectorDescriptorTest() throws KapuaException {
        JmsUtil.convertToCamelKapuaMessage(null, messageType, messageBody, jmsTopic, queuedOn, connectionId, clientId);
    }

    @Test(expected = NullPointerException.class)
    public void convertToCamelKapuaMessageNullMessageTypeTest() throws KapuaException {
        JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, null, messageBody, jmsTopic, queuedOn, connectionId, clientId);
    }

    @Test
    public void convertToCamelKapuaMessageNullMessageBodyTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), queuedOn, null));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, null, jmsTopic, queuedOn, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, null, jmsTopic, queuedOn, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, null, jmsTopic, queuedOn, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, null, jmsTopic, queuedOn, connectionId, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToCamelKapuaMessageNullTopicTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic((String) null), queuedOn, jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, null, queuedOn, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, null, queuedOn, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, null, queuedOn, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, null, queuedOn, connectionId, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToCamelKapuaMessageNullQueuedOnTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), null, jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, null, connectionId, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, null, connectionId, clientId).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, null, connectionId, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, null, connectionId, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToCamelKapuaMessageNullConnectionIdTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), queuedOn, jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, null, clientId) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, null, clientId).getMessage());
        assertNull("Null expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, null, clientId).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, null, clientId).getConnectorDescriptor());
    }

    @Test
    public void convertToCamelKapuaMessageNullClientIdTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessageForKapua);

        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), queuedOn, jmsPayload));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);

        assertTrue("True expected.", JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, null) instanceof CamelKapuaMessage);
        assertEquals("Expected and actual values should be the same.", kapuaMessage, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, null).getMessage());
        assertEquals("Expected and actual values should be the same.", connectionId, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, null).getConnectionId());
        assertEquals("Expected and actual values should be the same.", connectorDescriptor, JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageBody, jmsTopic, queuedOn, connectionId, null).getConnectorDescriptor());
    }

    @Test
    public void convertToJmsMessageTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.transport.message.jms.JmsMessage.class, translator2);

        Mockito.when(translator1.translate(kapuaMessage)).thenReturn(deviceMessage);
        Mockito.when(translator2.translate(deviceMessage)).thenReturn(returnJmsMessage);

        assertEquals("Expected and actual values should be the same.", returnJmsMessage, JmsUtil.convertToJmsMessage(connectorDescriptor, messageType, kapuaMessage));
    }

    @Test(expected = NullPointerException.class)
    public void convertToJmsMessageNullConnectorDescriptorTest() throws KapuaException {
        JmsUtil.convertToJmsMessage(null, messageType, kapuaMessage);
    }

    @Test(expected = NullPointerException.class)
    public void convertToJmsMessageNullMessageTypeTest() throws KapuaException {
        JmsUtil.convertToJmsMessage(connectorDescriptor, null, kapuaMessage);
    }

    @Test
    public void convertToJmsMessageNullKapuaMessageTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.transport.message.jms.JmsMessage.class, translator2);

        Mockito.when(translator1.translate(null)).thenReturn(deviceMessage);
        Mockito.when(translator2.translate(deviceMessage)).thenReturn(returnJmsMessage);

        assertEquals("Expected and actual values should be the same.", returnJmsMessage, JmsUtil.convertToJmsMessage(connectorDescriptor, messageType, null));
    }

    @Test
    public void convertJmsWildCardToMqttTest() {
        String[] jmsTopic = {"topic", "jms.topic", "jms/topic", ".topic", "..topic", "t..opic", "topic.", "topic..", "/topic", "topic/", "t.o.p.i.c", "t.o/p.i/c"};
        String[] expectedValue = {"topic", "jms/topic", "jms.topic", "/topic", "//topic", "t//opic", "topic/", "topic//", ".topic", "topic.", "t/o/p/i/c", "t/o.p/i.c"};
        for (int i = 0; i < jmsTopic.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedValue[i], JmsUtil.convertJmsWildCardToMqtt(jmsTopic[i]));
        }
    }

    @Test
    public void convertMqttWildCardToJmsNullTest() {
        assertNull("Null expected.", JmsUtil.convertMqttWildCardToJms(null));
    }

    @Test
    public void convertMqttWildCardToJmsTest() {
        String[] mqttTopic = {"topic", "mqtt.topic", "mqtt/topic", ".topic", "..topic", "t..opic", "topic.", "topic..", "/topic", "topic/", "t.o.p.i.c", "t.o/p.i/c"};
        String[] expectedValue = {"topic", "mqtt/topic", "mqtt.topic", "/topic", "//topic", "t//opic", "topic/", "topic//", ".topic", "topic.", "t/o/p/i/c", "t/o.p/i.c"};
        for (int i = 0; i < mqttTopic.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedValue[i], JmsUtil.convertMqttWildCardToJms(mqttTopic[i]));
        }
    }
}