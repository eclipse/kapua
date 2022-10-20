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
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MqttTopicTest {

    @Test
    public void mqttTopicConstructorValidTest() {
        MqttTopic mqttTopic = new MqttTopic("mqttTopicConstructor");
        Assert.assertEquals("Expected and actual values are not equal!", "mqttTopicConstructor", mqttTopic.getTopic());
    }

    @Test
    public void mqttTopicConstructorCharCheckTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            MqttTopic mqttTopic = new MqttTopic(value);
            Assert.assertEquals("Expected and actual values are not equal!", value, mqttTopic.getTopic());
        }
    }

    @Test
    public void mqttTopicSecondConstructorValidTest() {
        String[] mqttFromParts = new String[]{"mqtt0123456789", "from", "parts!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ"};
        MqttTopic mqttTopic = new MqttTopic(mqttFromParts);
        Assert.assertEquals("Expected and actual values are not equal!", "mqtt0123456789/from/parts!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", mqttTopic.getTopic());
    }

    @Test
    public void getSplittedTopicValidTest() {
        String[] mqttValue = new String[]{"mqttFromParts"};
        MqttTopic mqttTopic = new MqttTopic(mqttValue);
        Assert.assertArrayEquals("Expected and actual values are not equal!", mqttValue, mqttTopic.getSplittedTopic());
    }

    @Test
    public void getSplittedEmptyTopicTest() {
        String[] mqttValue = new String[]{};
        MqttTopic mqttTopic = new MqttTopic(mqttValue);
        Assert.assertArrayEquals("Expected and actual values are not equal!", mqttValue, mqttTopic.getSplittedTopic());
    }

    @Test
    public void toStringTest() {
        MqttTopic mqttTopic = new MqttTopic("mqttFromParts");
        Assert.assertEquals("Expected and actual values are not equal!", "mqttFromParts", mqttTopic.toString());
    }
}
