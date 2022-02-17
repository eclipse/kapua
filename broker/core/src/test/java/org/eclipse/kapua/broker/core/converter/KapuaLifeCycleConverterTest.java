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

import com.codahale.metrics.Counter;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang.SerializationUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProvider;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptorProviders;
import org.eclipse.kapua.broker.core.utils.Utils;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.cache.TranslatorCache;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Base64;
import java.util.Date;

@Category(JUnitTests.class)
public class KapuaLifeCycleConverterTest extends Assert {

    KapuaLifeCycleConverter kapuaLifeCycleConverter;
    Exchange exchange;
    Object value;
    Message message;
    Counter converterAppMessage, converterBirthMessage, converterDcMessage, converterMissingMessage, converterNotifyMessage;
    ConnectorDescriptorProvider provider;
    ConnectorDescriptor connectorDescriptor;
    byte[] connectorDescriptorBytes, byteArray, kapuaIdBytes;
    String connectorDescriptorString, kapuaIdString;
    KapuaMessage kapuaMessage;
    Translator translator1, translator2;
    KapuaId kapuaId;
    DefaultMessage defaultMessage;
    org.eclipse.kapua.transport.message.jms.JmsMessage jmsMessage;
    org.eclipse.kapua.message.Message messageKapua;

    @Before
    public void initialize() {
        kapuaLifeCycleConverter = new KapuaLifeCycleConverter();
        exchange = Mockito.mock(Exchange.class);
        value = new Object();
        message = Mockito.mock(Message.class);
        converterAppMessage = MetricServiceFactory.getInstance().getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_APP, ConverterMetrics.METRIC_COUNT);
        converterBirthMessage = MetricServiceFactory.getInstance().getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_BIRTH, ConverterMetrics.METRIC_COUNT);
        converterDcMessage = MetricServiceFactory.getInstance().getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_DC, ConverterMetrics.METRIC_COUNT);
        converterMissingMessage = MetricServiceFactory.getInstance().getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_MISSING, ConverterMetrics.METRIC_COUNT);
        converterNotifyMessage = MetricServiceFactory.getInstance().getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_NOTIFY, ConverterMetrics.METRIC_COUNT);
        Utils.initCounter(converterAppMessage, converterBirthMessage, converterDcMessage, converterMissingMessage, converterNotifyMessage);
        provider = ConnectorDescriptorProviders.getInstance();
        connectorDescriptor = provider.getDescriptor("foo");
        connectorDescriptorBytes = SerializationUtils.serialize(connectorDescriptor);
        connectorDescriptorString = Base64.getEncoder().encodeToString(connectorDescriptorBytes);
        kapuaMessage = Mockito.mock(KapuaMessage.class);
        translator1 = Mockito.mock(Translator.class);
        translator2 = Mockito.mock(Translator.class);
        byteArray = new byte[]{1, 2, 3, 4, 5, 6};
        kapuaId = KapuaId.ONE;
        kapuaIdBytes = SerializationUtils.serialize(kapuaId);
        kapuaIdString = Base64.getEncoder().encodeToString(kapuaIdBytes);
        defaultMessage = Mockito.mock(DefaultMessage.class);
    }

    @After
    public void tearDown() {
        Utils.initCounter(converterAppMessage, converterBirthMessage, converterDcMessage, converterMissingMessage, converterNotifyMessage);
    }

    @Test
    public void convertToAppsTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class, translator2);

        jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        messageKapua = translator2.translate(jmsMessage);

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

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToApps(exchange, byteArray);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertEquals("Expected and actual values should be the same.", 1L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToAppsObjectValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToApps(exchange, value);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 1L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToAppsNullExchangeTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToApps(null, byteArray);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 1L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToAppsNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToApps(exchange, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 1L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToBirthTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class, translator2);

        jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        messageKapua = translator2.translate(jmsMessage);

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

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToBirth(exchange, byteArray);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToBirthObjectValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToBirth(exchange, value);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToBirthNullExchangeTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToBirth(null, byteArray);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToBirthNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToBirth(exchange, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToDisconnectTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage.class, translator2);

        jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        messageKapua = translator2.translate(jmsMessage);

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

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToDisconnect(exchange, byteArray);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToDisconnectObjectValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToDisconnect(exchange, value);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToDisconnectNullExchangeTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToDisconnect(null, byteArray);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToDisconnectNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToDisconnect(exchange, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToMissingTest() throws TranslateException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage.class, org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage.class, translator2);

        jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        messageKapua = translator2.translate(jmsMessage);

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

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToMissing(exchange, byteArray);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToMissingObjectValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToMissing(exchange, value);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToMissingNullExchangeTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToMissing(null, byteArray);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToMissingNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToMissing(exchange, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToNotifyTest() throws KapuaException {
        TranslatorCache.cacheTranslator(org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage.class, org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage.class, translator1);
        TranslatorCache.cacheTranslator(org.eclipse.kapua.transport.message.jms.JmsMessage.class, org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage.class, translator2);

        jmsMessage = new org.eclipse.kapua.transport.message.jms.JmsMessage(new JmsTopic("topic"), new Date(10L), new JmsPayload(byteArray));
        messageKapua = translator2.translate(jmsMessage);

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

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToNotify(exchange, byteArray);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToNotifyObjectValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToNotify(exchange, value);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToNotifyNullExchangeTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToNotify(null, byteArray);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterNotifyMessage.getCount());

    }

    @Test
    public void convertToNotifyNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);

        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterNotifyMessage.getCount());
        try {
            kapuaLifeCycleConverter.convertToNotify(exchange, null);
            fail("KapuaException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, "Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass()).toString(), e.toString());
        }
        assertEquals("Expected and actual values should be the same.", 0L, converterAppMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterBirthMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterDcMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 0L, converterMissingMessage.getCount());
        assertEquals("Expected and actual values should be the same.", 1L, converterNotifyMessage.getCount());

    }
}
