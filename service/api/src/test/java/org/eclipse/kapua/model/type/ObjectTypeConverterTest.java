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

import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Date;


@Category(JUnitTests.class)
public class ObjectTypeConverterTest {

    @Test
    public void objectTypeConverterTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ObjectTypeConverter> objectTypeConverter = ObjectTypeConverter.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(objectTypeConverter.getModifiers()));
        objectTypeConverter.setAccessible(true);
        objectTypeConverter.newInstance();
    }

    @Test
    public void toStringTest() {
        Class[] classes = {String.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, byte[].class, Byte[].class, KapuaIdImpl.class};
        String[] expectedString = {"string", "integer", "long", "float", "double", "boolean", "date", "binary", "binary", "org.eclipse.kapua.model.id.KapuaIdImpl"};
        for (int i = 0; i < classes.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", expectedString[i], ObjectTypeConverter.toString(classes[i]));
        }
    }

    @Test
    public void toStringNullParameterTest() {
        Assert.assertNull("Null expected.", ObjectTypeConverter.toString(null));
    }

    @Test
    public void fromStringTest() throws ClassNotFoundException {
        String[] stringValue = {"string", "integer", "int", "long", "float", "double", "boolean", "date", "binary", "org.eclipse.kapua.model.id.KapuaIdImpl"};
        Class[] expectedClasses = {String.class, Integer.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, byte[].class, KapuaIdImpl.class};
        for (int i = 0; i < stringValue.length; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", expectedClasses[i].toString(), ObjectTypeConverter.fromString(stringValue[i]).toString());
        }
    }

    @Test
    public void fromStringNullParameterTest() throws ClassNotFoundException {
        Assert.assertNull("Null expected.", ObjectTypeConverter.fromString(null));
    }

    @Test(expected = ClassNotFoundException.class)
    public void fromStringInvalidStringValueTest() throws ClassNotFoundException {
        String invalidStringValue = "Non-exciting class";
        ObjectTypeConverter.fromString(invalidStringValue);
    }
}
