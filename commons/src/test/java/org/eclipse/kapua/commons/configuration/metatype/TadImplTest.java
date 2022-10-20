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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Category(JUnitTests.class)
public class TadImplTest {

    @Before
    public void createInstanceOfClasses() {

        tadImpl = new TadImpl();
        toption = new ToptionImpl();
        toption2 = new ToptionImpl();
    }

    TadImpl tadImpl;
    ToptionImpl toption;
    ToptionImpl toption2;

    @Test
    public void getOptionToNullTest() {
        tadImpl.getOption();
        Assert.assertTrue(tadImpl.getOption().isEmpty());
    }

    @Test
    public void setAndGetOptionRegularTest() {
        List<KapuaToption> option = new ArrayList<>();
        option.add(toption);
        tadImpl.setOption(option);
        Assert.assertEquals("tadImpl.option", option, tadImpl.getOption());
    }

    @Test
    public void addAndGetOptionTest() {
        List<KapuaToption> options = new ArrayList<>();
        options.add(toption);
        options.add(toption2);

        tadImpl.addOption(toption);
        tadImpl.addOption(toption2);

        Assert.assertEquals("tadImpl.option", options, tadImpl.getOption());
    }

    @Test
    public void getAnyTest() {
        Assert.assertTrue(tadImpl.getAny().isEmpty());
    }

    @Test
    public void setAndGetAnyTest() {
        List<Object> options = new ArrayList<>();
        options.add(toption);
        options.add(toption2);
        options.add(null);
        tadImpl.setAny(options);
        Assert.assertEquals("tadImpl.option", options, tadImpl.getAny());
    }

    @Test
    public void setAndGetNameToNullTest() {
        tadImpl.setName(null);
        Assert.assertNull(tadImpl.getName());
    }

    @Test
    public void setAndGetNameTest() {
        String[] permittedValues = {"", "regularName", "49", "regular Name", "regular name with spaces", "!@#$%&*()_+/->,<", "NAME", "name123"};
        for (String value : permittedValues) {
            tadImpl.setName(value);
            Assert.assertTrue(tadImpl.getName().contains(value));
        }
    }

    @Test
    public void setAndGetDescriptionToNullTest() {
        tadImpl.setDescription(null);
        Assert.assertNull(tadImpl.getDescription());
    }

    @Test
    public void setAndGetDescriptionTest() {
        String[] permittedValues = {"", "regularDescription", "49", "regular Description", "regular description with spaces", "!@#$%&*()_+/->,<", "DESCRIPTION", "description123"};
        for (String value : permittedValues) {
            tadImpl.setDescription(value);
            Assert.assertTrue(tadImpl.getDescription().contains(value));
        }
    }

    @Test
    public void setAndGetIdToNullTest() {
        tadImpl.setId(null);
        Assert.assertNull(tadImpl.getId());
    }

    @Test
    public void setAndGetIdTest() {
        String[] permittedValues = {"", "regularId", "49", "regular Id", "regular id with spaces", "!@#$%&*()_+/->,<", "ID", "id123"};
        for (String value : permittedValues) {
            tadImpl.setId(value);
            Assert.assertTrue(tadImpl.getId().contains(value));
        }
    }

    @Test
    public void setAndGetTypeStringTest() {
        tadImpl.setType(TscalarImpl.STRING);
        Assert.assertEquals("tadImpl.type", TscalarImpl.STRING, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeLongTest() {
        tadImpl.setType(TscalarImpl.LONG);
        Assert.assertEquals("tadImpl.type", TscalarImpl.LONG, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeDoubleTest() {
        tadImpl.setType(TscalarImpl.DOUBLE);
        Assert.assertEquals("tadImpl.type", TscalarImpl.DOUBLE, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeFloatTest() {
        tadImpl.setType(TscalarImpl.FLOAT);
        Assert.assertEquals("tadImpl.type", TscalarImpl.FLOAT, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeIntegerTest() {
        tadImpl.setType(TscalarImpl.INTEGER);
        Assert.assertEquals("tadImpl.type", TscalarImpl.INTEGER, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeByteTest() {
        tadImpl.setType(TscalarImpl.BYTE);
        Assert.assertEquals("tadImpl.type", TscalarImpl.BYTE, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeCharTest() {
        tadImpl.setType(TscalarImpl.CHAR);
        Assert.assertEquals("tadImpl.type", TscalarImpl.CHAR, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeBooleanTest() {
        tadImpl.setType(TscalarImpl.BOOLEAN);
        Assert.assertEquals("tadImpl.type", TscalarImpl.BOOLEAN, tadImpl.getType());
    }

    @Test
    public void setAndGetTypeShortTest() {
        tadImpl.setType(TscalarImpl.SHORT);
        Assert.assertEquals("tadImpl.type", TscalarImpl.SHORT, tadImpl.getType());
    }

    @Test
    public void setAndGetTypePasswordTest() {
        tadImpl.setType(TscalarImpl.PASSWORD);
        Assert.assertEquals("tadImpl.type", TscalarImpl.PASSWORD, tadImpl.getType());
    }

    @Test
    public void setAndGetCardinalityToNullTest() {
        tadImpl.setCardinality(null);
        Assert.assertEquals("tadImpl.cardinality", 0, (int) tadImpl.getCardinality());
    }

    @Test
    public void setAndGetCardinalityRegularTest() {
        int[] permittedValues = {1, 10, 100, 500, -2147483648, 2147483647, 0};
        for (int value : permittedValues) {
            tadImpl.setCardinality(value);
            Assert.assertEquals("tadImpl.cardinality", Integer.valueOf(value), tadImpl.getCardinality());
        }
    }

    @Test
    public void setAndGetMinToNullTest() {
        tadImpl.setMin(null);
        Assert.assertNull(tadImpl.getMin());
    }

    @Test
    public void setAndGetMinTest() {
        String[] permittedValues = {"", "regularMin", "49", "regular Min", "regular min with spaces", "!@#$%&*()_+/->,<", "MIN", "min123"};
        for (String value : permittedValues) {
            tadImpl.setMin(value);
            Assert.assertTrue(tadImpl.getMin().contains(value));
        }
    }

    @Test
    public void setAndGetMaxToNullTest() {
        tadImpl.setMax(null);
        Assert.assertNull(tadImpl.getMax());
    }

    @Test
    public void setAndGetMaxTest() {
        String[] permittedValues = {"", "regularMax", "49", "regular Max", "regular max with spaces", "!@#$%&*()_+/->,<", "MAX", "max123"};
        for (String value : permittedValues) {
            tadImpl.setMax(value);
            Assert.assertTrue(tadImpl.getMax().contains(value));
        }
    }

    @Test
    public void setAndGetDefaultToNullTest() {
        tadImpl.setDefault(null);
        Assert.assertNull(tadImpl.getDefault());
    }

    @Test
    public void setAndGetDefaultTest() {
        String[] permittedValues = {"", "regularDefault", "49", "regular Default", "regular default with spaces", "!@#$%&*()_+/->,<", "DEFAULT", "default123"};
        for (String value : permittedValues) {
            tadImpl.setDefault(value);
            Assert.assertTrue(tadImpl.getDefault().contains(value));
        }
    }

    @Test
    public void setIsRequiredToNullTest() {
        tadImpl.setRequired(null);
        Assert.assertTrue(tadImpl.isRequired());
    }

    @Test
    public void setAndIsRequiredTest() {
        boolean[] permittedValues = {false, true};
        for (boolean value : permittedValues) {
            tadImpl.setRequired(value);
            Assert.assertEquals("tadImpl.value", value, tadImpl.isRequired());
        }
    }

    @Test
    public void putAndGetOtherAttributeTest() {
        Map<QName, String> expectedValues = new HashMap<>();

        expectedValues.put(QName.valueOf("1"), "a");
        expectedValues.put(QName.valueOf("2"), "b");
        expectedValues.put(QName.valueOf("3"), "c");

        tadImpl.putOtherAttribute(QName.valueOf("1"),"a");
        tadImpl.putOtherAttribute(QName.valueOf("2"),"b");
        tadImpl.putOtherAttribute(QName.valueOf("3"),"c");

        Assert.assertEquals("tadImpl.attributes", expectedValues, tadImpl.getOtherAttributes());
    }

    @Test
    public void getOtherAttributeTest() {
        Assert.assertTrue(tadImpl.getOtherAttributes().isEmpty());
    }
}
