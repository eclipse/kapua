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
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCleanException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MqttClientCleanExceptionTest {

    MqttClientCleanException exception;
    Throwable cause;

    @Before
    public void createInstanceOfClass() {
        cause = new Throwable();
        exception = new MqttClientCleanException(cause, "clientId");
    }

    @Test
    public void constructorValidTest() {
        try {
            Assert.assertEquals("MqttClient " + exception.getClientId() + " cannot be terminated properly. This can lead to resource leaks.", exception.getMessage());
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown!");
        }
    }

    @Test
    public void constructorRandomStringsTest() {
        try {
            String[] stringValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844ppooqqqqweqrttskjoijjnbvzbhdsjkpk++adasdascdadserfaolkaiw;leqawoejoaidmn,masdnjokjaduiyqhwidbhnaskjfhaskidhnkauidhkauisdjhdhadnjkahduiqhdeihjoljiaolidjpqdjp;qkd';adkpoakdpoqjwoejqwoejqldfkjlasjf"};
            for (String value : stringValues) {
                MqttClientCleanException exception = new MqttClientCleanException(cause, value);
                Assert.assertEquals("MqttClient " + exception.getClientId() + " cannot be terminated properly. This can lead to resource leaks.", exception.getMessage());
            }
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown!");
        }
    }

    @Test
    public void constructorCauseNullTest() {
        try {
            MqttClientCleanException exception = new MqttClientCleanException(null, "clientId");
            Assert.assertNull("Null expected!", exception.getCause());
            Assert.assertEquals("Expected and actual values should be the same!", "clientId", exception.getClientId());
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown!");
        }
    }

    @Test
    public void constructorClientIdNullTest() {
        try {
            MqttClientCleanException exception = new MqttClientCleanException(cause, null);
            Assert.assertNull("Null expected!", exception.getClientId());
            Assert.assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown!");
        }
    }

    @Test
    public void constructorAllNullTest() {
        try {
            MqttClientCleanException exception = new MqttClientCleanException(null, null);
            Assert.assertNull("Null expected!", exception.getCause());
            Assert.assertNull("Null expected!", exception.getClientId());
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown!");
        }
    }

    @Test (expected = MqttClientCleanException.class)
    public void throwingExceptionTest() throws MqttClientCleanException {
        throw exception;
    }
}
