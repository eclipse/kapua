/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class TransportSendExceptionTest extends Assert {

    TransportMessage transportMessage;
    Throwable throwable;

    @Before
    public void initialize() {
        transportMessage = new TransportMessage() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
        throwable = new Throwable();
    }

    @Test
    public void transportSendExceptionWithMessageTest() {
        TransportSendException transportSendException = new TransportSendException(transportMessage);
        assertEquals("Expected and actual values should be the same.", transportMessage, transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: " + transportMessage, transportSendException.getMessage());
        assertNull("Null expected.", transportSendException.getCause());
    }

    @Test
    public void transportSendExceptionWithNullMessageTest() {
        TransportSendException transportSendException = new TransportSendException(null);
        assertNull("Null expected.", transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: null", transportSendException.getMessage());
        assertNull("Null expected.", transportSendException.getCause());
    }

    @Test
    public void transportSendExceptionWithCauseAndMessageTest() {
        TransportSendException transportSendException = new TransportSendException(throwable, transportMessage);
        assertEquals("Expected and actual values should be the same.", transportMessage, transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: " + transportMessage, transportSendException.getMessage());
        assertEquals("Expected and actual values should be the same.", throwable, transportSendException.getCause());
    }

    @Test
    public void transportSendExceptionWithCauseAndMessageNullTest() {
        TransportSendException transportSendException = new TransportSendException(null, null);
        assertNull("Null expected.", transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: null", transportSendException.getMessage());
        assertNull("Null expected.", transportSendException.getCause());
    }

    @Test
    public void transportSendExceptionWithNullCauseAndMessageTest() {
        TransportSendException transportSendException = new TransportSendException(null, transportMessage);
        assertEquals("Expected and actual values should be the same.", transportMessage, transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: " + transportMessage, transportSendException.getMessage());
        assertNull("Null expected.", transportSendException.getCause());
    }

    @Test
    public void transportSendExceptionWithCauseAndNullMessageTest() {
        TransportSendException transportSendException = new TransportSendException(throwable, null);
        assertNull("Null expected.", transportSendException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        assertEquals("Expected and actual values should be the same.", "An error occurred when sending the message: null", transportSendException.getMessage());
        assertEquals("Expected and actual values should be the same.", throwable, transportSendException.getCause());
    }

    @Test(expected = TransportSendException.class)
    public void throwingTransportSendExceptionWithMessageTest() throws TransportSendException {
        throw new TransportSendException(transportMessage);
    }

    @Test(expected = TransportSendException.class)
    public void throwingTransportSendExceptionWithCauseAndMessageTest() throws TransportSendException {
        throw new TransportSendException(throwable, transportMessage);
    }
}