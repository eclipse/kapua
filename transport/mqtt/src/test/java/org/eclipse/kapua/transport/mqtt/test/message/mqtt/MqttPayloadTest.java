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
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


@Category(JUnitTests.class)
public class MqttPayloadTest {

    MqttPayload mqttPayload;

    @Before
    public void createInstanceOfClass() {
        mqttPayload = new MqttPayload(body);
    }

    byte[] body = "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇqwertyuiopasdfghjklzxcvbnm1234567890".getBytes();

    @Test
    public void constructorTest() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<MqttPayload> mqttPayloadConstructor = MqttPayload.class.getDeclaredConstructor();
        mqttPayloadConstructor.setAccessible(true);
        mqttPayloadConstructor.newInstance();
    }

    @Test
    public void mqttPayloadSetAndGetTest() {
        mqttPayload.setBody(body);
        Assert.assertEquals("Expected and actual values should be the same!", body, mqttPayload.getBody());
    }

    @Test
    public void mqttPayloadSetAndGetNullTest() {
        mqttPayload.setBody(null);
        Assert.assertNull("Null expected!", mqttPayload.getBody());
    }

    @Test
    public void hasBodyTest() {
        mqttPayload.setBody(body);
        Assert.assertTrue("Should contain body value!", mqttPayload.hasBody());
        Assert.assertEquals("Expected and actual values should be the same!", body, mqttPayload.getBody());
    }

    @Test
    public void hasBodyNullTest() {
        mqttPayload.setBody(null);
        Assert.assertFalse("Should not contain body value!", mqttPayload.hasBody());
        Assert.assertNull("Null expected!", mqttPayload.getBody());
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("ISMkJSYnKCk9P+KBhEDigLnigLrigqzCsMK34oCaLC4tOzpfw4jLh8K/PD7Cq+KAmOKAneKAmcOJw5jiiI97fQ==", mqttPayload.toString());
    }

    @Test
    public void toStringEmptyTest() {
        mqttPayload.setBody("".getBytes());
        Assert.assertEquals("Empty string expected!", "", mqttPayload.toString());
    }
}
