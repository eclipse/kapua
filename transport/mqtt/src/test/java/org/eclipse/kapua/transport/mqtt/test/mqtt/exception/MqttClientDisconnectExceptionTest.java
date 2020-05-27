/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt.test.mqtt.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientDisconnectException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class MqttClientDisconnectExceptionTest extends Assert {

    MqttClientDisconnectException exception;
    String clientId = "clientName";
    Throwable cause;

    @Before
    public void createInstancesOfClasses() {
        cause = new Throwable();
        exception = new MqttClientDisconnectException(cause, clientId);
    }

    @Test
    public void constructorValidTest() {
        MqttClientDisconnectException exception = new MqttClientDisconnectException(cause, clientId);
        assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
        assertEquals("Expected and actual values should be the same!", clientId, exception.getClientId());
        assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
    }

    @Test
    public void constructorCauseNullTest() {
        try {
            MqttClientDisconnectException exception = new MqttClientDisconnectException(null, clientId);
            assertNull("Null expected!", exception.getCause());
            assertEquals("Expected and actual values should be the same!", clientId, exception.getClientId());
            assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test
    public void constructorClientIdNullTest() {
        try {
            MqttClientDisconnectException exception = new MqttClientDisconnectException(cause, null);
            assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
            assertNull("Null expected!", exception.getClientId());
            assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test
    public void constructorAllNullTest() {
        MqttClientDisconnectException exception = new MqttClientDisconnectException(null, null);
        assertNull("Null expected!", exception.getCause());
        assertNull("Null expected!", exception.getClientId());
        assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
    }

    @Test (expected = MqttClientDisconnectException.class)
    public void throwingExceptionUsingConstructor() throws MqttClientDisconnectException {
        throw exception;
    }
}
