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
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCallbackSetException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MqttClientCallbackSetExceptionTest {

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
        Assert.assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        Assert.assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test
    public void constructorCauseNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(null, "clientId", mqttTopic);
        Assert.assertNull("Null expected!", exception.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        Assert.assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test
    public void constructorClientIdNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(throwable, null, mqttTopic);
        Assert.assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        Assert.assertNull("Null expected!", exception.getClientId());
        Assert.assertEquals("Expected and actual values should be the same!", mqttTopic, exception.getTopic());
    }

    @Test (expected = NullPointerException.class)
    public void constructorMqttTopicNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(throwable, "clientId", null);
        Assert.assertEquals("Expected and actual values should be the same!", throwable, exception.getCause());
        Assert.assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        Assert.assertNull("Null expected!", exception.getTopic());
    }

    @Test (expected = NullPointerException.class)
    public void constructorAllNullTest() {
        MqttClientCallbackSetException exception = new MqttClientCallbackSetException(null, null, null);
        Assert.assertNull("Null expected!", exception.getCause());
        Assert.assertNull("Null expected!", exception.getClientId());
        Assert.assertNull("Null expected!", exception.getTopic());
    }

    @Test (expected = MqttClientCallbackSetException.class)
    public void throwingExceptionTest() throws MqttClientCallbackSetException {
        throw exception;
    }
}
