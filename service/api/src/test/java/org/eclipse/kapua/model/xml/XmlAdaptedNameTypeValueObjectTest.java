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
package org.eclipse.kapua.model.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class XmlAdaptedNameTypeValueObjectTest {

    @Test
    public void xmlAdaptedNameTypeValueObjectTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        Assert.assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getName());
        Assert.assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValueType());
        Assert.assertNull("Null expected.", xmlAdaptedNameTypeValueObject.getValue());
    }

    @Test
    public void setAndGetNameTest() {
        XmlAdaptedNameTypeValueObject xmlAdaptedNameTypeValueObject = new XmlAdaptedNameTypeValueObject();
        String[] names = {"Name", null};

        for (String name : names) {
            xmlAdaptedNameTypeValueObject.setName(name);
            Assert.assertEquals("Expected and actual values should be the same.", name, xmlAdaptedNameTypeValueObject.getName());
        }
    }
}
