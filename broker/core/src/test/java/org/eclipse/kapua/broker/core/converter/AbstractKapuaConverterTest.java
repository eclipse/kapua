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
package org.eclipse.kapua.broker.core.converter;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProvider;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProviders;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.cache.TranslatorCache;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.BytesMessage;
import javax.jms.Message;
import java.util.Base64;
import java.util.Date;

@Category(JUnitTests.class)
public class AbstractKapuaConverterTest extends Assert {

    private class AbstractKapuaConverterImpl extends AbstractKapuaConverter {

    }

    AbstractKapuaConverter abstractKapuaConverter;
    Exchange exchange;
    byte[] byteArray, kapuaIdBytes, connectorDescriptorBytes;
    DefaultMessage defaultMessage;
    ConnectorDescriptor.MessageType[] messageTypes;
    KapuaId kapuaId;
    ConnectorDescriptor connectorDescriptor;
    String kapuaIdString, connectorDescriptorString;
    BytesMessage byteMessage;
    JmsMessage jmsMessage;
    Message message;

    @Before
    public void initialize() {
        abstractKapuaConverter = new AbstractKapuaConverterImpl();
        exchange = Mockito.mock(Exchange.class);
        byteArray = new byte[]{1, 2, 3, 4, 5, 6};
        defaultMessage = Mockito.mock(DefaultMessage.class);
        messageTypes = new ConnectorDescriptor.MessageType[]{ConnectorDescriptor.MessageType.APP, ConnectorDescriptor.MessageType.DATA, ConnectorDescriptor.MessageType.BIRTH,
                ConnectorDescriptor.MessageType.DISCONNECT, ConnectorDescriptor.MessageType.MISSING, ConnectorDescriptor.MessageType.NOTIFY, ConnectorDescriptor.MessageType.DATA};
        kapuaId = KapuaId.ONE;
        connectorDescriptor = Mockito.mock(ConnectorDescriptor.class);
        kapuaIdBytes = SerializationUtils.serialize(kapuaId);
        connectorDescriptorBytes = SerializationUtils.serialize(connectorDescriptor);
        kapuaIdString = Base64.getEncoder().encodeToString(kapuaIdBytes);
        connectorDescriptorString = Base64.getEncoder().encodeToString(connectorDescriptorBytes);
        byteMessage = Mockito.mock(BytesMessage.class);
        jmsMessage = Mockito.mock(JmsMessage.class);
        message = Mockito.mock(Message.class);
    }

    @Test
    public void convertToNullTest() {
        try {
            abstractKapuaConverter.convertTo(null, null, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void convertToNullExchangeTest() {
        for (ConnectorDescriptor.MessageType type : messageTypes) {
            try {
                abstractKapuaConverter.convertTo(null, byteArray, type);
                fail("NullPointerException expected.");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void convertToNullObjectTest() {
        for (ConnectorDescriptor.MessageType type : messageTypes) {
            Mockito.doReturn(Mockito.mock(JmsMessage.class)).when(exchange).getIn();

            try {
                abstractKapuaConverter.convertTo(exchange, null, type);
                fail("KapuaException expected.");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
            }
        }
    }

    @Test
    public void convertToNullTypeTest() {
        Mockito.when(exchange.getIn()).thenReturn(Mockito.mock(org.apache.camel.Message.class));

        try {
            abstractKapuaConverter.convertTo(exchange, byteArray, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
    }

    @Test
    public void convertToObjectValueTest() {
        for (ConnectorDescriptor.MessageType type : messageTypes) {
            Mockito.doReturn(Mockito.mock(JmsMessage.class)).when(exchange).getIn();

            try {
                abstractKapuaConverter.convertTo(exchange, new Object(), type);
                fail("KapuaException expected.");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
            }
        }
    }

    @Test
    public void convertToMessageTypeTest() {
        for (ConnectorDescriptor.MessageType type : messageTypes) {
            Mockito.when(exchange.getIn()).thenReturn(Mockito.mock(org.apache.camel.Message.class));

            try {
                abstractKapuaConverter.convertTo(exchange, byteArray, type);
                fail("KapuaException expected.");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
            }
        }
    }

    @Test
    public void convertToDefaultMessageExceptionTest() {
        for (ConnectorDescriptor.MessageType type : messageTypes) {
            Mockito.when(exchange.getIn()).thenReturn(defaultMessage);
            Mockito.when(defaultMessage.getHeader(CamelConstants.JMS_HEADER_TIMESTAMP, Long.class)).thenReturn(10L);
            Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CONNECTION_ID, String.class)).thenReturn(kapuaIdString);
            Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL, String.class)).thenReturn(connectorDescriptorString);
            Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CLIENT_ID, String.class)).thenReturn("clientid");

            try {
                abstractKapuaConverter.convertTo(exchange, byteArray, type);
                fail("KapuaException expected.");
            } catch (Exception e) {
                assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message type " + exchange.getIn().getClass()).toString(), e.toString());
            }
        }
    }

    @Test
    public void convertToTest() throws Exception {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        ConnectorDescriptor connectorDescriptor = provider.getDescriptor("foo");
        byte[] connectorDescriptorBytes = SerializationUtils.serialize(connectorDescriptor);
        String connectorDescriptorString = Base64.getEncoder().encodeToString(connectorDescriptorBytes);
        KapuaMessage kapuaMessage = Mockito.mock(KapuaMessage.class);
        Translator translator1 = Mockito.mock(Translator.class);
        Translator translator2 = Mockito.mock(Translator.class);

        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);

        org.eclipse.kapua.transport.message.jms.JmsMessage jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        org.eclipse.kapua.message.Message messageKapua = translator2.translate(jmsMessage);

        System.setProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, "topic");
        Mockito.when(exchange.getIn()).thenReturn(defaultMessage);
        Mockito.when(defaultMessage.getHeader(CamelConstants.JMS_HEADER_TIMESTAMP, Long.class)).thenReturn(10L);
        Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CONNECTION_ID, String.class)).thenReturn(kapuaIdString);
        Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CLIENT_ID, String.class)).thenReturn("clientid");
        Mockito.when(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL, String.class)).thenReturn(connectorDescriptorString);
        Mockito.when(defaultMessage.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("topic");
        Mockito.doReturn(messageKapua).when(translator2).translate(new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray)));
        Mockito.when(translator1.translate(messageKapua)).thenReturn(kapuaMessage);
        Mockito.when(kapuaMessage.getClientId()).thenReturn("id");

        try {
            assertTrue("Instance of CamelKapuaMessage expected.", abstractKapuaConverter.convertTo(exchange, byteArray, ConnectorDescriptor.MessageType.BIRTH) instanceof CamelKapuaMessage);
        } catch (Exception e) {
            fail("KapuaException expected.");
        }
    }

    @Test
    public void convertToJmsMessageNullTest() {
        try {
            abstractKapuaConverter.convertToJmsMessage(null, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void convertToJmsMessageNullValueTest() {
        Mockito.when(exchange.getIn(JmsMessage.class)).thenReturn(jmsMessage);
        Mockito.when(exchange.getIn(JmsMessage.class).getJmsMessage()).thenReturn(byteMessage);

        try {
            assertTrue("Instance of Message expected.", abstractKapuaConverter.convertToJmsMessage(exchange, null) instanceof Message);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void convertToJmsMessageExceptionNullExchangeTest() {
        try {
            abstractKapuaConverter.convertToJmsMessage(null, new Object());
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void convertToJmsBytesMessageTest() {
        Mockito.when(exchange.getIn(JmsMessage.class)).thenReturn(jmsMessage);
        Mockito.when(exchange.getIn(JmsMessage.class).getJmsMessage()).thenReturn(byteMessage);

        try {
            assertTrue("Instance of Message expected.", abstractKapuaConverter.convertToJmsMessage(exchange, byteArray) instanceof Message);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void convertToJmsMessageExceptionTest() {
        Mockito.when(exchange.getIn(JmsMessage.class)).thenReturn(jmsMessage);
        Mockito.when(exchange.getIn(JmsMessage.class).getJmsMessage()).thenReturn(message);
        Mockito.doReturn(Mockito.mock(JmsMessage.class)).when(exchange).getIn();

        try {
            abstractKapuaConverter.convertToJmsMessage(exchange, byteArray);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
    }
}