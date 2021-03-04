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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;

@Category(Categories.junitTests.class)
public class TdesignateImplTest extends Assert {

    @Before
    public void createInstanceOfClass() {

        tdesignate = new TdesignateImpl();
    }

    TdesignateImpl tdesignate;

    @Test
    public void setAndGetObjectToNullTest() {
        tdesignate.setObject(null);
        assertNull(tdesignate.getObject());
    }

    @Test
    public void setAndGetObjectTest() {
        TobjectImpl tobject = new TobjectImpl();
        tdesignate.setObject(tobject);
        assertEquals("tdesignate.object", tobject, tdesignate.getObject());
    }

    @Test
    public void getAnyTest() {
        assertTrue(tdesignate.getAny().isEmpty());
    }

    @Test
    public void setAndGetAnyToNullTest() {
        tdesignate.setAny(null);
        assertTrue(tdesignate.getAny().isEmpty());
    }

    @Test
    public void setAndGetAnyTest() {
        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add("String");
        objectArrayList.add(2L);
        objectArrayList.add(100);
        objectArrayList.add(11122.33);
        objectArrayList.add(true);
        objectArrayList.add(false);
        objectArrayList.add(new ToptionImpl());

        tdesignate.setAny(objectArrayList);
        assertEquals("tdesignate.any", objectArrayList, tdesignate.getAny());
    }

    @Test
    public void setAndGetPidToNullTest() {
        tdesignate.setPid(null);
        assertNull(tdesignate.getPid());
    }

    @Test
    public void setAndGetPidTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularPid", "regular Pid", "49", "regularPid49", "PID", "2 46 465494 13564612 0009 09004 9684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tdesignate.setPid(value);
            assertTrue(tdesignate.getPid().contains(value));
        }
    }

    @Test
    public void setAndGetFactoryPidToNullTest() {
        tdesignate.setFactoryPid(null);
        assertNull(tdesignate.getFactoryPid());
    }

    @Test
    public void setAndGetFactoryPidTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularPid", "regular Pid", "49", "regularPid49", "PID", "2 46 465494 13564612 0009 09004 9684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tdesignate.setFactoryPid(value);
            assertTrue(tdesignate.getFactoryPid().contains(value));
        }
    }

    @Test
    public void setAndGetBundleToNullTest() {
        tdesignate.setBundle(null);
        assertNull(tdesignate.getBundle());
    }

    @Test
    public void setAndGetBundleTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularBundle", "regular Bundle", "49", "regularBundle49", "BUNDLE", "2 46 465494 13564612 0009 09004 9684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tdesignate.setBundle(value);
            assertTrue(tdesignate.getBundle().contains(value));
        }
    }

    @Test
    public void setAndGetOptionalToNullTest() {
        tdesignate.setOptional(null);
        assertFalse(tdesignate.isOptional());
    }

    @Test
    public void setAndIsOptionalTest() {
        boolean[] permittedValues = {false, true};
        for (boolean value : permittedValues) {
            tdesignate.setOptional(value);
            assertEquals("tdesignate.isOptional", value, tdesignate.isOptional());
        }
    }

    @Test
    public void setAndGetMergeToNullTest() {
        tdesignate.setMerge(null);
        assertFalse(tdesignate.isMerge());
    }

    @Test
    public void setAndIsMergeTest() {
        boolean[] permittedValues = {false, true};
        for (boolean value : permittedValues) {
            tdesignate.setMerge(value);
            assertEquals("tdesignate.isMerge", value, tdesignate.isMerge());
        }
    }

    @Test
    public void getOtherAttributesTest() {
        assertTrue(tdesignate.getOtherAttributes().isEmpty());
    }

    @Test
    public void setAndGetOtherAttributesToNullTest() {
        tdesignate.setOtherAttributes(null);
        assertNull(tdesignate.getOtherAttributes());
    }
}
