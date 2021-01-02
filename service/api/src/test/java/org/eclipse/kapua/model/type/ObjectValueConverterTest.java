/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.type;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class ObjectValueConverterTest extends Assert {

    @Test
    public void objectValueConverterTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ObjectValueConverter> objectValueConverter = ObjectValueConverter.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(objectValueConverter.getModifiers()));
        objectValueConverter.setAccessible(true);
        objectValueConverter.newInstance();
    }

    @Test
    public void toStringTest() {
        Byte[] byteArray1 = {-128, -10, 0, 1, 10, 127};
        byte[] byteArray2 = {-128, -10, 0, 1, 10, 127};
        Object[] objects = {byteArray1, byteArray2, 0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, 10.10d, true, false};
        String[] expectedString = {"gPYAAQp/", "gPYAAQp/", "0", "10", "100000", "String", "c", "-10", "-1000000000", "-100000000000", "10", "10.0", "10.1", "true", "false"};
        Object object = new Object();

        for (int i = 0; i < objects.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedString[i], ObjectValueConverter.toString(objects[i]));
        }
        assertNull("Null expected.", ObjectValueConverter.toString(null));
        assertEquals("Expected and actual values should be the same.", object.toString(), ObjectValueConverter.toString(object));
    }

    @Test
    public void fromStringTest() {
        String[] stringValue = {"String", "-2147483648", "11", "-9223372036854775808", "9223372036854775807", "10F", "10.10d", "true", "false", "Object"};
        Class[] classes = {String.class, Integer.class, Integer.class, Long.class, Long.class, Float.class, Double.class, Boolean.class, Boolean.class, Object.class};
        Object[] expectedObjects = {"String", -2147483648, 11, -9223372036854775808L, 9223372036854775807L, 10F, 10.1d, true, false, "Object"};
        Class[] byteClasses = {byte[].class, Byte[].class};

        for (int i = 0; i < stringValue.length; i++) {
            assertEquals(expectedObjects[i], ObjectValueConverter.fromString(stringValue[i], classes[i]));
        }

        for (Class byteClass : byteClasses) {
            assertThat("Instance of byte[] expected.", ObjectValueConverter.fromString("byteArray", byteClass), IsInstanceOf.instanceOf(byte[].class));
        }

        for (Class clazz : classes) {
            assertNull(ObjectValueConverter.fromString(null, clazz));
        }
    }

    @Test(expected = NumberFormatException.class)
    public void fromStringInvalidStringValueTest() {
        String[] invalidStringValue = {"-2147483649", "2147483648", "-9223372036854775809", "9223372036854775808", "10F",};
        Class[] invalidClasses = {Integer.class, Integer.class, Long.class, Long.class, Integer.class};

        for (int i = 0; i < invalidStringValue.length; i++) {
            ObjectValueConverter.fromString(invalidStringValue[i], invalidClasses[i]);
        }
    }
}
