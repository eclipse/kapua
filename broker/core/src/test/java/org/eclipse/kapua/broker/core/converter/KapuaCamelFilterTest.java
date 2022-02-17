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
import org.apache.camel.Message;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Base64;

@Category(JUnitTests.class)
public class KapuaCamelFilterTest extends Assert {

    Exchange exchange, messageExchange;
    Object value;
    Message message;
    KapuaCamelFilter kapuaCamelFilter;
    KapuaSession kapuaSession;
    String stringKapuaSession;
    KapuaSession kapuaSessionToEncode;
    byte[] byteKapuaSession;
    Exception exception;

    @Before
    public void initialize() {
        exchange = Mockito.mock(Exchange.class);
        value = "Object";
        message = Mockito.mock(Message.class);
        kapuaCamelFilter = new KapuaCamelFilter();
        kapuaSession = new KapuaSession();
        kapuaSessionToEncode = new KapuaSession();
        byteKapuaSession = SerializationUtils.serialize(kapuaSessionToEncode);
        stringKapuaSession = Base64.getEncoder().encodeToString(byteKapuaSession);
        messageExchange = Mockito.mock(Exchange.class);
        exception = new Exception();
    }

    @Test
    public void bindSessionNullExchangeTest() {
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(null, value);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void bindSessionNullTest() {
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(null, null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void bindSessionNullValueTrueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)).thenReturn(true);

        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(exchange, null);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void bindSessionTrueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)).thenReturn(true);

        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(exchange, value);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void bindNoSessionFalseTest() {
        String incorrectHeader = "incorrect header";
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)).thenReturn(false);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_SESSION, String.class)).thenReturn(incorrectHeader);

        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(exchange, value);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void bindSessionFalseTest() {
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)).thenReturn(false);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_SESSION, String.class)).thenReturn(stringKapuaSession);

        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(exchange, value);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertNotNull("NotNull expected.", KapuaSecurityUtils.getSession());
        KapuaSecurityUtils.clearSession();
    }

    @Test
    public void bindSessionNullValueFalseTest() {
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)).thenReturn(false);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getHeader(MessageConstants.HEADER_KAPUA_SESSION, String.class)).thenReturn(stringKapuaSession);

        assertNull("Null expected.", KapuaSecurityUtils.getSession());
        try {
            kapuaCamelFilter.bindSession(exchange, value);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
        assertNotNull("NotNull expected.", KapuaSecurityUtils.getSession());
        KapuaSecurityUtils.clearSession();
    }

    @Test
    public void unbindSessionTest() throws KapuaException {
        KapuaSecurityUtils.setSession(kapuaSession);

        assertEquals("Expected and actual values should be the same.", kapuaSession, KapuaSecurityUtils.getSession());
        kapuaCamelFilter.unbindSession(exchange, value);
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void unbindSessionNullExchangeTest() throws KapuaException {
        KapuaSecurityUtils.setSession(kapuaSession);

        assertEquals("Expected and actual values should be the same.", kapuaSession, KapuaSecurityUtils.getSession());
        kapuaCamelFilter.unbindSession(null, value);
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void unbindSessionNullValueTest() throws KapuaException {
        KapuaSecurityUtils.setSession(kapuaSession);

        assertEquals("Expected and actual values should be the same.", kapuaSession, KapuaSecurityUtils.getSession());
        kapuaCamelFilter.unbindSession(exchange, null);
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test
    public void unbindSessionNullTest() throws KapuaException {
        KapuaSecurityUtils.setSession(kapuaSession);

        assertEquals("Expected and actual values should be the same.", kapuaSession, KapuaSecurityUtils.getSession());
        kapuaCamelFilter.unbindSession(null, null);
        assertNull("Null expected.", KapuaSecurityUtils.getSession());
    }

    @Test(expected = NullPointerException.class)
    public void bridgeErrorNullTest() {
        kapuaCamelFilter.bridgeError(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void bridgeErrorNullExchangeTest() {
        kapuaCamelFilter.bridgeError(null, value);
    }

    @Test
    public void bridgeErrorNullValueTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getExchange()).thenReturn(messageExchange);
        Mockito.when(messageExchange.getException()).thenReturn(exception);

        kapuaCamelFilter.bridgeError(exchange, null);
        try {
            exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void bridgeErrorTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getExchange()).thenReturn(messageExchange);
        Mockito.when(messageExchange.getException()).thenReturn(exception);

        kapuaCamelFilter.bridgeError(exchange, value);
        try {
            exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void bridgeErrorNullMessageTest() {
        Mockito.when(exchange.getIn()).thenReturn(null);
        Mockito.when(exchange.getException()).thenReturn(null);

        kapuaCamelFilter.bridgeError(exchange, value);
        try {
            exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void bridgeErrorNullExceptionTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getExchange()).thenReturn(exchange);
        Mockito.when(exchange.getException()).thenReturn(null);

        kapuaCamelFilter.bridgeError(exchange, value);
        try {
            exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void bridgeErrorNullMessageNullExchangeExceptionTest() {
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(message.getExchange()).thenReturn(messageExchange);
        Mockito.when(exchange.getException()).thenReturn(exception);

        kapuaCamelFilter.bridgeError(exchange, value);
        try {
            exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}