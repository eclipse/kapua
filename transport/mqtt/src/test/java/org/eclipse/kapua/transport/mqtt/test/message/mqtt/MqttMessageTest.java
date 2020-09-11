/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt.test.message.mqtt;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class MqttMessageTest extends Assert {

    MqttTopic requestTopic, responseTopic;
    MqttPayload mqttPayload;
    Date date;

    @Before
    public void createInstancesOfClasses() {
        requestTopic = new MqttTopic("requestTopic");
        responseTopic = new MqttTopic("responseTopic");
        mqttPayload = new MqttPayload("payload.code".getBytes());
        date = new Date();
    }

    @Test
    public void mqttMessageConstructorTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, mqttPayload);
        assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructorRequestTopicNullTest() {
        MqttMessage mqttMessage = new MqttMessage(null, responseTopic, mqttPayload);
        assertNull("Null expected!", mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructorRequestPayloadNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, null);
        assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        assertEquals("Expected and actual value should be the same!", "", mqttMessage.getPayload().toString());
    }

    @Test
    public void mqttMessageConstructorResponseTopicNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, (MqttTopic) null, mqttPayload);
        assertEquals("Expected and actual value should be the same!", requestTopic , mqttMessage.getRequestTopic());
        assertNull("Null expected!", mqttMessage.getResponseTopic());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2Test() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, date, mqttPayload);
        assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2MqttRequestNullTest() {
        MqttMessage mqttMessage = new MqttMessage(null, date, mqttPayload);
        assertNull("Null expected!", mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2RequestPayloadNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, date, null);
        assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        assertEquals("Expected and actual value should be the same!", "", mqttMessage.getPayload().toString());
    }

    @Test
    public void mqttMessageConstructor2DateNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, (Date) null, mqttPayload);
        assertEquals("Expected and actual value should be the same!", requestTopic , mqttMessage.getRequestTopic());
        assertNull("Null expected!", mqttMessage.getResponseTopic());
        assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void expectResponseTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, mqttPayload);
        assertTrue("The response should not be null!", mqttMessage.expectResponse());
    }

    @Test
    public void toStringTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, mqttPayload);
        assertEquals("null, requestTopic, cGF5bG9hZC5jb2Rl", mqttMessage.toString());
    }
}