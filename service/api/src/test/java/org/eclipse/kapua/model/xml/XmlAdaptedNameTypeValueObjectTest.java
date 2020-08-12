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
package org.eclipse.kapua.model.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class XmlAdaptedNameTypeValueObjectTest extends Assert {

    @Test
    public void xmlAdaptedNameTypeValueObjectTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getName());
        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValueType());
        assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValue());
    }

    @Test
    public void setAndGetNameTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        String[] names = {"Name", null};

        for (String name : names) {
            xmlAdaptedNameTypeValueObject.setName(name);
            assertEquals("Expected and actual values should be the same.", name, xmlAdaptedNameTypeValueObject.getName());
        }
    }
}