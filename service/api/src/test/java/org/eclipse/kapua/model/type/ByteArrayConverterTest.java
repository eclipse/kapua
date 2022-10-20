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
package org.eclipse.kapua.model.type;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class ByteArrayConverterTest {

    Byte[] byteClassArray;
    byte[] byteArray;
    String expectedString;

    @Before
    public void initialize() {
        byteClassArray = new Byte[]{-128, -10, 0, 1, 10, 127};
        byteArray = new byte[]{-128, -10, 0, 1, 10, 127};
        expectedString = "gPYAAQp/";
    }

    @Test
    public void byteArrayConverterTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ByteArrayConverter> byteArrayConverter = ByteArrayConverter.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(byteArrayConverter.getModifiers()));
        byteArrayConverter.setAccessible(true);
        byteArrayConverter.newInstance();
    }

    @Test
    public void toStringByteClassParameterTest() {
        Assert.assertEquals("Expected and actual values should be the same.", expectedString, ByteArrayConverter.toString(byteClassArray));
    }

    @Test(expected = NullPointerException.class)
    public void toStringNullByteClassParameterTest() {
        ByteArrayConverter.toString((Byte[]) null);
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("Expected and actual values should be the same.", expectedString, ByteArrayConverter.toString(byteArray));
    }

    @Test(expected = NullPointerException.class)
    public void toStringNullParameterTest() {
        ByteArrayConverter.toString((byte[]) null);
    }

    @Test
    public void fromStringTest() {
        String stringValue = "String Value";
        Assert.assertThat("Instance of byte[] expected.", ByteArrayConverter.fromString(stringValue), IsInstanceOf.instanceOf(byte[].class));
    }

    @Test(expected = NullPointerException.class)
    public void fromStringNullParameterTest() {
        ByteArrayConverter.fromString(null);
    }
}
