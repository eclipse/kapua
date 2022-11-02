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
package org.eclipse.kapua.transport.mqtt.test.mqtt.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientDisconnectException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MqttClientDisconnectExceptionTest {

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
        Assert.assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", clientId, exception.getClientId());
        Assert.assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
    }

    @Test
    public void constructorCauseNullTest() {
        try {
            MqttClientDisconnectException exception = new MqttClientDisconnectException(null, clientId);
            Assert.assertNull("Null expected!", exception.getCause());
            Assert.assertEquals("Expected and actual values should be the same!", clientId, exception.getClientId());
            Assert.assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            Assert.fail("No exception expected!");
        }
    }

    @Test
    public void constructorClientIdNullTest() {
        try {
            MqttClientDisconnectException exception = new MqttClientDisconnectException(cause, null);
            Assert.assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
            Assert.assertNull("Null expected!", exception.getClientId());
            Assert.assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            Assert.fail("No exception expected!");
        }
    }

    @Test
    public void constructorAllNullTest() {
        MqttClientDisconnectException exception = new MqttClientDisconnectException(null, null);
        Assert.assertNull("Null expected!", exception.getCause());
        Assert.assertNull("Null expected!", exception.getClientId());
        Assert.assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", exception.getCode().toString());
    }

    @Test (expected = MqttClientDisconnectException.class)
    public void throwingExceptionUsingConstructor() throws MqttClientDisconnectException {
        throw exception;
    }
}
