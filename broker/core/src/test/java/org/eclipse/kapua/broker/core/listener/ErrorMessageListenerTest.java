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
package org.eclipse.kapua.broker.core.listener;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import com.codahale.metrics.Counter;

import java.util.Base64;

@Category(JUnitTests.class)
public class ErrorMessageListenerTest extends Assert {

    ErrorMessageListener errorMessageListener;
    Exchange exchange;
    Message exchangeMessage;
    Object[] messages;
    Counter error;
    Counter errorLifeCycleMessage;
    Throwable throwable;
    byte[] throwableBytes;
    String stringThrowable;

    @Before
    public void initialize() {
        errorMessageListener = new ErrorMessageListener();
        exchange = Mockito.mock(Exchange.class);
        exchangeMessage = Mockito.mock(Message.class);
        messages = new Object[]{null, new Object()};
        error = errorMessageListener.registerCounter("messages", "generic", "count");
        errorLifeCycleMessage = errorMessageListener.registerCounter("messages", "life_cycle", "count");
        throwable = new Throwable("message");
        throwableBytes = SerializationUtils.serialize(throwable);
        stringThrowable = Base64.getEncoder().encodeToString(throwableBytes);
    }

    @Test
    public void errorMessageListenerNameTest() {
        assertEquals("Expected and actual values should be the same.", "error", errorMessageListener.name);
    }

    @Test
    public void processMessageNullThrowableNullExchangeTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(null);
        Mockito.when(exchange.getException()).thenReturn(null);
        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            errorMessageListener.processMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 1L, error.getCount());

            error.dec();
        }
    }

    @Test
    public void processMessageNullExchangeTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(new Throwable());
        Mockito.when(exchange.getException()).thenReturn(null);

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.processMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 1L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());

            error.dec();
        }
    }

    @Test
    public void processMessageNullThrowableTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(null);
        Mockito.when(exchange.getException()).thenReturn(new Exception());

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.processMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 1L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());

            error.dec();
        }
    }

    @Test
    public void processMessageTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(stringThrowable);

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.processMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 1L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());

            error.dec();
        }
    }

    @Test
    public void lifeCycleMessageNullThrowableNullExchangeTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(null);
        Mockito.when(exchange.getException()).thenReturn(null);

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.lifeCycleMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 1L, errorLifeCycleMessage.getCount());

            errorLifeCycleMessage.dec();
        }
    }

    @Test
    public void lifeCycleMessageNullExchangeTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(new Throwable());
        Mockito.when(exchange.getException()).thenReturn(null);

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.lifeCycleMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 1L, errorLifeCycleMessage.getCount());

            errorLifeCycleMessage.dec();
        }
    }

    @Test
    public void lifeCycleMessageNullThrowableTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(null);
        Mockito.when(exchange.getProperty(CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION)).thenReturn(null);
        Mockito.when(exchange.getException()).thenReturn(new Exception());

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.lifeCycleMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 1L, errorLifeCycleMessage.getCount());

            errorLifeCycleMessage.dec();
        }
    }

    @Test
    public void lifeCycleMessageTest() throws KapuaException {
        Mockito.when(exchange.getIn()).thenReturn(exchangeMessage);
        Mockito.when(exchangeMessage.getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION, String.class)).thenReturn(stringThrowable);

        for (Object message : messages) {
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 0L, errorLifeCycleMessage.getCount());
            errorMessageListener.lifeCycleMessage(exchange, message);
            assertEquals("Expected and actual values should be the same.", 0L, error.getCount());
            assertEquals("Expected and actual values should be the same.", 1L, errorLifeCycleMessage.getCount());

            errorLifeCycleMessage.dec();
        }
    }
}