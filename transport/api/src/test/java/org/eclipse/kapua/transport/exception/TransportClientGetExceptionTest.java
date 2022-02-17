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
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class TransportClientGetExceptionTest extends Assert {

    @Test
    public void transportClientGetExceptionNullServerIpTest() {
        TransportClientGetException transportClientGetException = new TransportClientGetException(null);
        assertNull("Null expected.", transportClientGetException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
        assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: null", transportClientGetException.getMessage());
        assertNull("Null expected.", transportClientGetException.getCause());
    }

    @Test
    public void transportClientGetExceptionServerIpTest() {
        String[] randomStrings = {"a", "0.0.0.0", "asdfasfasfaf", "{@}]~ˇ^°˘˘˛˘`", "test", "127.0.0.0"};
        for (String randomString : randomStrings) {
            TransportClientGetException transportClientGetException = new TransportClientGetException(randomString);
            assertEquals("Expected and actual values should be the same.", randomString, transportClientGetException.getRequestMessage());
            assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
            assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: " + randomString, transportClientGetException.getMessage());
            assertNull("Null expected.", transportClientGetException.getCause());
        }
    }

    @Test
    public void transportClientGetExceptionCauseServerIpNullTest() {
        TransportClientGetException transportClientGetException = new TransportClientGetException(null, null);
        assertNull("Null expected.", transportClientGetException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
        assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: null", transportClientGetException.getMessage());
        assertNull("Null expected.", transportClientGetException.getCause());
    }

    @Test
    public void transportClientGetExceptionCauseServerIpTest() {
        Throwable throwable = new Throwable();
        String[] randomStrings = {"a", "0.0.0.0", "asdfasfasfaf", "{@}]~ˇ^°˘˘˛˘`", "test", "127.0.0.0"};
        for (String randomString : randomStrings) {
            TransportClientGetException transportClientGetException = new TransportClientGetException(throwable, randomString);
            assertEquals("Expected and actual values should be the same.", randomString, transportClientGetException.getRequestMessage());
            assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
            assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: " + randomString, transportClientGetException.getMessage());
            assertEquals("Expected and actual values should be the same.", throwable, transportClientGetException.getCause());
        }
    }

    @Test
    public void transportClientGetExceptionNullCauseServerIpTest() {
        String[] randomStrings = {"a", "0.0.0.0", "asdfasfasfaf", "{@}]~ˇ^°˘˘˛˘`", "test", "127.0.0.0"};
        for (String randomString : randomStrings) {
            TransportClientGetException transportClientGetException = new TransportClientGetException(null, randomString);
            assertEquals("Expected and actual values should be the same.", randomString, transportClientGetException.getRequestMessage());
            assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
            assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: " + randomString, transportClientGetException.getMessage());
            assertNull("Null expected.", transportClientGetException.getCause());
        }
    }

    @Test
    public void transportClientGetExceptionCauseNullServerIpTest() {
        Throwable throwable = new Throwable();
        TransportClientGetException transportClientGetException = new TransportClientGetException(throwable, null);
        assertNull("Null expected.", transportClientGetException.getRequestMessage());
        assertEquals("Expected and actual values should be the same.", TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
        assertEquals("Expected and actual values should be the same.", "Cannot get an instance of the transport client to connect to host: null", transportClientGetException.getMessage());
        assertEquals("Expected and actual values should be the same.", throwable, transportClientGetException.getCause());
    }

    @Test(expected = TransportClientGetException.class)
    public void throwingExceptionWithServerIpTest() throws TransportClientGetException {
        String[] randomStrings = {"a", "0.0.0.0", "asdfasfasfaf", "{@}]~ˇ^°˘˘˛˘`", "test", "127.0.0.0"};
        for (String randomString : randomStrings) {
            throw new TransportClientGetException(randomString);
        }
    }

    @Test(expected = TransportClientGetException.class)
    public void throwingExceptionWithCauseAndServerIpTest() throws TransportClientGetException {
        String[] randomStrings = {"a", "0.0.0.0", "asdfasfasfaf", "{@}]~ˇ^°˘˘˛˘`", "test", "127.0.0.0"};
        for (String randomString : randomStrings) {
            throw new TransportClientGetException(new Throwable(), randomString);
        }
    }
}
