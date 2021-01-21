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

import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class ObjectTypeXmlAdapterTest extends Assert {

    ObjectTypeXmlAdapter objectTypeXmlAdapter;

    @Before
    public void createInstanceOfClass() {
        objectTypeXmlAdapter = new ObjectTypeXmlAdapter();
    }

    @Test
    public void marshalTest() {
        Class[] classes = {String.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, byte[].class, Byte[].class, KapuaIdImpl.class};
        String[] expectedString = {"string", "integer", "long", "float", "double", "boolean", "date", "binary", "binary", "org.eclipse.kapua.model.id.KapuaIdImpl"};

        for (int i = 0; i < classes.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedString[i], objectTypeXmlAdapter.marshal(classes[i]));
        }
    }

    @Test
    public void marshalNullTest() {
        assertNull("Null expected.", objectTypeXmlAdapter.marshal(null));
    }

    @Test
    public void unmarshalTest() throws ClassNotFoundException {
        String[] stringValue = {"string", "integer", "long", "float", "double", "boolean", "date", "binary", "org.eclipse.kapua.model.id.KapuaIdImpl"};
        Class[] expectedClasses = {String.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, byte[].class, KapuaIdImpl.class};

        for (int i = 0; i < stringValue.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedClasses[i], objectTypeXmlAdapter.unmarshal(stringValue[i]));
        }
    }

    @Test
    public void unmarshalNullTest() throws ClassNotFoundException {
        assertNull("Null expected.", objectTypeXmlAdapter.unmarshal(null));
    }

    @Test(expected = ClassNotFoundException.class)
    public void unmarshalInvalidStringValueTest() throws ClassNotFoundException {
        String invalidStringValue = "Non-exciting class";
        objectTypeXmlAdapter.unmarshal(invalidStringValue);
    }
}
