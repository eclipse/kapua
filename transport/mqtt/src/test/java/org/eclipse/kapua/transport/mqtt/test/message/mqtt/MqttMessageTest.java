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
public class MqttMessageTest {

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
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructorRequestTopicNullTest() {
        MqttMessage mqttMessage = new MqttMessage(null, responseTopic, mqttPayload);
        Assert.assertNull("Null expected!", mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructorRequestPayloadNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, null);
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", responseTopic, mqttMessage.getResponseTopic());
        Assert.assertEquals("Expected and actual value should be the same!", "", mqttMessage.getPayload().toString());
    }

    @Test
    public void mqttMessageConstructorResponseTopicNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, (MqttTopic) null, mqttPayload);
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic , mqttMessage.getRequestTopic());
        Assert.assertNull("Null expected!", mqttMessage.getResponseTopic());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2Test() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, date, mqttPayload);
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2MqttRequestNullTest() {
        MqttMessage mqttMessage = new MqttMessage(null, date, mqttPayload);
        Assert.assertNull("Null expected!", mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void mqttMessageConstructor2RequestPayloadNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, date, null);
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic, mqttMessage.getRequestTopic());
        Assert.assertEquals("Expected and actual value should be the same!", date, mqttMessage.getTimestamp());
        Assert.assertEquals("Expected and actual value should be the same!", "", mqttMessage.getPayload().toString());
    }

    @Test
    public void mqttMessageConstructor2DateNullTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, (Date) null, mqttPayload);
        Assert.assertEquals("Expected and actual value should be the same!", requestTopic , mqttMessage.getRequestTopic());
        Assert.assertNull("Null expected!", mqttMessage.getResponseTopic());
        Assert.assertEquals("Expected and actual value should be the same!", mqttPayload, mqttMessage.getPayload());
    }

    @Test
    public void expectResponseTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, mqttPayload);
        Assert.assertTrue("The response should not be null!", mqttMessage.expectResponse());
    }

    @Test
    public void toStringTest() {
        MqttMessage mqttMessage = new MqttMessage(requestTopic, responseTopic, mqttPayload);
        Assert.assertEquals("null, requestTopic, cGF5bG9hZC5jb2Rl", mqttMessage.toString());
    }
}
