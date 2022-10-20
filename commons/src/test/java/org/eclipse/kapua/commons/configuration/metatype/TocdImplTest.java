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

import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
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
public class TocdImplTest {

    @Before
    public void createInstanceOfClasses() {

        tocdImpl = new TocdImpl();
        tadImpl = new TadImpl();
        tadImpl2 = new TadImpl();
        tadImpl3 = new TadImpl();
        ticon = new TiconImpl();
        ticon2 = new TiconImpl();
    }

    TocdImpl tocdImpl;
    TadImpl tadImpl;
    TadImpl tadImpl2;
    TadImpl tadImpl3;
    TiconImpl ticon;
    TiconImpl ticon2;

    @Test
    public void getADTest() {
        Assert.assertTrue(tocdImpl.getAD().isEmpty());
    }

    @Test
    public void setAndGetADTest() {
        List<KapuaTad> expectedValues = new ArrayList<>();
        expectedValues.add(tadImpl);
        tocdImpl.setAD(expectedValues);
        Assert.assertEquals("tocdImpl.ad", expectedValues, tocdImpl.getAD());
    }

    @Test
    public void addAndGetADTest() {
        tocdImpl.addAD(tadImpl3);
        tocdImpl.addAD(tadImpl2);
        tocdImpl.getAD();
        Assert.assertTrue(tocdImpl.getAD().contains(tadImpl3) && tocdImpl.getAD().contains(tadImpl2));
    }

    @Test
    public void getIconTest(){
        Assert.assertTrue(tocdImpl.getIcon().isEmpty());
    }

    @Test
    public void setAndGetIconTest() {
        List<KapuaTicon> listOfObj = new ArrayList<>();
        listOfObj.add(ticon);
        tocdImpl.setIcon(listOfObj);
        Assert.assertEquals("tocdImpl.icon", listOfObj, tocdImpl.getIcon());
    }

    @Test
    public void addAndGetIconTest() {
        tocdImpl.addIcon(ticon);
        tocdImpl.addIcon(ticon2);
        tocdImpl.getIcon();
        Assert.assertTrue(tocdImpl.getIcon().contains(ticon) && tocdImpl.getIcon().contains(ticon2));
    }

    @Test
    public void getAnyTest() {
        Assert.assertTrue(tocdImpl.getAny().isEmpty());
    }

    @Test
    public void setAndGetAnyTest() {
        List<Object> options = new ArrayList<>();
        options.add(ticon);
        options.add(ticon2);
        tocdImpl.setAny(options);
        Assert.assertEquals("tocdImpl.any", options, tocdImpl.getAny());
    }

    @Test
    public void addAndGetAnyTest() {
        tocdImpl.addAny(ticon);
        tocdImpl.addAny(ticon2);
        Assert.assertTrue(tocdImpl.getAny().contains(ticon) && tocdImpl.getAny().contains(ticon2));
    }

    @Test
    public void setAndGetNameToNullTest() {
        tocdImpl.setName(null);
        Assert.assertNull(tocdImpl.getName());
    }

    @Test
    public void setAndGetNameTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularName", "regular Name", "49", "regularName49", "REGULAR", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tocdImpl.setName(value);
            Assert.assertTrue(tocdImpl.getName().contains(value));
        }
    }

    @Test
    public void setAndGetDescriptionToNullTest() {
        tocdImpl.setDescription(null);
        Assert.assertNull(tocdImpl.getDescription());
    }

    @Test
    public void setAndGetDescriptionTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regular Description", "49", "regularDescription49", "DESCRIPTION", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tocdImpl.setDescription(value);
            Assert.assertTrue(tocdImpl.getDescription().contains(value));
        }
    }

    @Test
    public void setAndGetIdToNullTest() {
        tocdImpl.setId(null);
        Assert.assertNull(tocdImpl.getId());
    }

    @Test
    public void setAndGetIdTest() {
        String[] permittedValues = {"", "regularId", "49", "regular Id", "regular id with spaces", "!@#$%&*()_+/->,<", "ID", "id123"};
        for (String value : permittedValues) {
            tocdImpl.setId(value);
            Assert.assertTrue(tocdImpl.getId().contains(value));
        }
    }

    @Test
    public void putAndGetOtherAttributeTest() {
        Map<QName, String> expectedValues = new HashMap<>();

        expectedValues.put(QName.valueOf("1"), "a");
        expectedValues.put(QName.valueOf("2"), "b");
        expectedValues.put(QName.valueOf("3"), "c");

        tocdImpl.putOtherAttribute(QName.valueOf("1"),"a");
        tocdImpl.putOtherAttribute(QName.valueOf("2"),"b");
        tocdImpl.putOtherAttribute(QName.valueOf("3"),"c");

        Assert.assertEquals("tocdImpl.otherAttributes", expectedValues, tocdImpl.getOtherAttributes());
    }

    @Test
    public void getOtherAttributeTest() {
        Assert.assertTrue(tocdImpl.getOtherAttributes().isEmpty());
    }

    @Test
    public void testSetAndGetOtherAttribute() {
        Map<QName, String> values = new HashMap<>();

        values.put(QName.valueOf("1"), "a");
        values.put(QName.valueOf("2"), "b");
        values.put(QName.valueOf("3"), "c");

        tocdImpl.setOtherAttributes(values);
        Assert.assertEquals("tocdImpl.otherAttributes", values, tocdImpl.getOtherAttributes());
    }
}
