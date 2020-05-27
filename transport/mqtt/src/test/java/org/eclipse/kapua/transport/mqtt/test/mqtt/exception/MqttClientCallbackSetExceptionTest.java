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
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCallbackSetException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class MqttClientCallbackSetExceptionTest extends Assert {

    MqttTopic mqttTopic;
    Throwable throwable;
    MqttClientCallbackSetException exception;

    @Before
    public void createInstancesOfClasses() {
        mqttTopic = new MqttTopic("mqttTopic");
        throwable = new Throwable();
        exception = new MqttClientCallbackSetException(throwable, "clientName", mqttTopic);
    }

    @Test
    public void constructorValidTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(throwable, "clientId", mqttTopic);
        assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test
    public void constructorCauseNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(null, "clientId", mqttTopic);
        assertNull("Null expected!", exception.getCause());
        assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test
    public void constructorClientIdNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(throwable, null, mqttTopic);
        assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        assertNull("Null expected!", exception.getClientId());
        assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test (expected = NullPointerException.class)
    public void constructorMqttTopicNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(throwable, "clientId", null);
        assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        assertNull("Null expected!", exception.getTopic());
    }

    @Test (expected = NullPointerException.class)
    public void constructorAllNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(null, null, null);
        assertNull("Null expected!", exception.getCause());
        assertNull("Null expected!", exception.getClientId());
        assertNull("Null expected!", exception.getTopic());
    }

    @Test (expected = MqttClientCallbackSetException.class)
    public void throwingExceptionTest() throws MqttClientCallbackSetException {
        throw exception;
    }
}
