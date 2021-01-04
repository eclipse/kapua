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
package org.eclipse.kapua.model.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class XmlAdaptedTypeValueObjectTest extends Assert {

    @Test
    public void xmlAdaptedNameTypeValueObjectTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValueType());
        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValue());
    }

    @Test
    public void setAndGetValueTypeTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        Class[] classes = {String.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, Byte.class};

        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValueType());

        for (Class clazz : classes) {
            xmlAdaptedNameTypeValueObject.setValueType(clazz);
            assertEquals("Expected and actual values should be the same.", clazz, xmlAdaptedNameTypeValueObject.getValueType());
        }
    }

    @Test
    public void setAndGetValueTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        String[] values = {"Name", null};

        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValue());

        for (String value : values) {
            xmlAdaptedNameTypeValueObject.setValue(value);
            assertEquals("Expected and actual values should be the same.", value, xmlAdaptedNameTypeValueObject.getValue());
        }
    }
}
