/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import org.checkerframework.checker.nullness.qual.Nullable;


@Category(JUnitTests.class)
public class TransportExceptionTest {

    TransportErrorCodes[] transportErrorCodes;
    String kapuaErrorMessage;
    String[] expectedMessageWithoutObject;
    String[] expectedMessageWithObject;
    Object object, stringObject, intObject;
    Throwable[] throwable;

    @Before
    public void initialize() {
        transportErrorCodes = new TransportErrorCodes[]{TransportErrorCodes.SEND_ERROR, TransportErrorCodes.TIMEOUT, TransportErrorCodes.CLIENT_GET};
        kapuaErrorMessage = "transport-client-error-messages";
        object = new Object();
        stringObject = "String Object";
        intObject = 11;
        expectedMessageWithoutObject = new String[]{"An error occurred when sending the message: {0}", "The request has not received a response within the timeout of: {0}ms", "Cannot get an instance of the transport client to connect to host: {0}"};
        expectedMessageWithObject = new String[]{"An error occurred when sending the message: " + object, "The request has not received a response within the timeout of: " + object + "ms", "Cannot get an instance of the transport client to connect to host: " + object};
        throwable = new Throwable[]{new Throwable(), null};
    }

    private class TransportExceptionImpl extends TransportException {

        protected TransportExceptionImpl(TransportErrorCodes code) {
            super(code);
        }

        protected TransportExceptionImpl(TransportErrorCodes code, @Nullable Object... arguments) {
            super(code, arguments);
        }

        protected TransportExceptionImpl(TransportErrorCodes code, Throwable cause, @Nullable Object... arguments) {
            super(code, cause, arguments);
        }
    }

    @Test
    public void transportExceptionCodeTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            TransportException transportException = new TransportExceptionImpl(transportErrorCodes[i]);
            Assert.assertEquals("Expected and actual values should be the same.", transportErrorCodes[i], transportException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutObject[i], transportException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
            Assert.assertNull("Null expected.", transportException.getCause());
        }
    }

    @Test
    public void transportExceptionNullCodeTest() {
        TransportException transportException = new TransportExceptionImpl(null);
        Assert.assertNull("Null expected.", transportException.getCode());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
        Assert.assertNull("Null expected.", transportException.getCause());
        try {
            transportException.getMessage();
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void transportExceptionCodeArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            TransportException transportException = new TransportExceptionImpl(transportErrorCodes[i], object, stringObject, intObject);
            Assert.assertEquals("Expected and actual values should be the same.", transportErrorCodes[i], transportException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithObject[i], transportException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
            Assert.assertNull("Null expected.", transportException.getCause());
        }
    }

    @Test
    public void transportExceptionCodeNullArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            TransportException transportException = new TransportExceptionImpl(transportErrorCodes[i], null);
            Assert.assertEquals("Expected and actual values should be the same.", transportErrorCodes[i], transportException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutObject[i], transportException.getMessage());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
            Assert.assertNull("Null expected.", transportException.getCause());
        }
    }

    @Test
    public void transportExceptionNullCodeArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            TransportException transportException = new TransportExceptionImpl(null, object, stringObject, intObject);
            Assert.assertNull("Null expected.", transportException.getCode());
            Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
            Assert.assertNull("Null expected.", transportException.getCause());
            try {
                transportException.getMessage();
            } catch (Exception e) {
                Assert.assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void transportExceptionCodeCauseArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            for (Throwable cause : throwable) {
                TransportException transportException = new TransportExceptionImpl(transportErrorCodes[i], cause, object, stringObject, intObject);
                Assert.assertEquals("Expected and actual values should be the same.", transportErrorCodes[i], transportException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithObject[i], transportException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
                Assert.assertEquals("Expected and actual values should be the same.", cause, transportException.getCause());
            }
        }
    }

    @Test
    public void transportExceptionCodeCauseNullArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            for (Throwable cause : throwable) {
                TransportException transportException = new TransportExceptionImpl(transportErrorCodes[i], cause, null);
                Assert.assertEquals("Expected and actual values should be the same.", transportErrorCodes[i], transportException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", expectedMessageWithoutObject[i], transportException.getMessage());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
                Assert.assertEquals("Expected and actual values should be the same.", cause, transportException.getCause());
            }
        }
    }

    @Test
    public void transportExceptionNullCodeCauseArgumentsTest() {
        for (int i = 0; i < transportErrorCodes.length; i++) {
            for (Throwable cause : throwable) {
                TransportException transportException = new TransportExceptionImpl(null, cause, object, stringObject, intObject);
                Assert.assertNull("Null expected.", transportException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", kapuaErrorMessage, transportException.getKapuaErrorMessagesBundle());
                Assert.assertEquals("Expected and actual values should be the same.", cause, transportException.getCause());
                try {
                    transportException.getMessage();
                } catch (Exception e) {
                    Assert.assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
                }
            }
        }
    }

    @Test(expected = TransportException.class)
    public void throwingTransportExceptionCodeTest() throws TransportExceptionImpl {
        for (TransportErrorCodes code : transportErrorCodes) {
            throw new TransportExceptionImpl(code);
        }
    }

    @Test(expected = TransportException.class)
    public void throwingTransportExceptionCodeArgumentsTest() throws TransportExceptionImpl {
        for (TransportErrorCodes code : transportErrorCodes) {
            throw new TransportExceptionImpl(code, object, stringObject, intObject);
        }
    }

    @Test(expected = TransportException.class)
    public void throwingTransportExceptionCodeCauseArgumentsTest() throws TransportExceptionImpl {
        for (TransportErrorCodes code : transportErrorCodes) {
            for (Throwable cause : throwable) {
                throw new TransportExceptionImpl(code, cause, object, stringObject, intObject);
            }
        }
    }
}
