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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaConfigurableServiceSchemaUtilsTest {

    @Test(expected = NullPointerException.class)
    public void scriptSessionWithNullArgumentsTest() {
        KapuaConfigurableServiceSchemaUtils.scriptSession(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void scriptSessionWithNullPathTest() {
        KapuaConfigurableServiceSchemaUtils.scriptSession(null, KapuaConfigurableServiceSchemaUtils.DEFAULT_FILTER);
    }

    @Test(expected = NullPointerException.class)
    public void scriptSessionWithNullFilterTest() {
        KapuaConfigurableServiceSchemaUtils.scriptSession(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH, null);
    }

    @Test
    public void scriptSessionWithDefaultValuesTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.scriptSession(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH, KapuaConfigurableServiceSchemaUtils.DEFAULT_FILTER);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void scriptSessionWithEmptyPathValueTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.scriptSession("", KapuaConfigurableServiceSchemaUtils.DEFAULT_FILTER);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void scriptSessionWithWrongPathValueTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.scriptSession("wrong/path", KapuaConfigurableServiceSchemaUtils.DEFAULT_FILTER);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void scriptSessionWithEmptyFilterValueTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.scriptSession(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH, "");
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void scriptSessionWithWrongFilterValueTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.scriptSession(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH, "*.wrong");
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void createSchemaObjectsWithDefaultPathTest() throws KapuaException {
        try {
            KapuaConfigurableServiceSchemaUtils.createSchemaObjects(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void createSchemaObjectsWithWrongPathTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.createSchemaObjects("wrong/path");
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void createSchemaObjectsWithRandomPathTest() {
        String[] paths = {"", "asdf", "wqeqw@123", "@123ˇ^ˇ°", "c", "www.test.asd"};
        for (String path : paths) {
            try {
                KapuaConfigurableServiceSchemaUtils.createSchemaObjects(path);
            } catch (Exception e) {
                Assert.fail("Exception should not be thrown");
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void createSchemaObjectsWithNullPathTest() throws KapuaException {
        KapuaConfigurableServiceSchemaUtils.createSchemaObjects(null);

    }

    @Test
    public void dropSchemaObjectsWithDefaultPathTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(KapuaConfigurableServiceSchemaUtils.DEFAULT_PATH);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void dropSchemaObjectsWithRandomPathTest() {
        String[] paths = {"asd", "w/qww", "123214", "asdasd13213", "!#@{}"};
        for (String path : paths) {
            try {
                KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(path);
            } catch (Exception e) {
                Assert.fail("Exception should not be thrown");
            }
        }
    }

    @Test
    public void dropSchemaObjectsWithWrongPathTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.dropSchemaObjects("wrong/path");
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void dropSchemaObjectsWithEmptyPathTest() {
        try {
            KapuaConfigurableServiceSchemaUtils.dropSchemaObjects("");
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test(expected = NullPointerException.class)
    public void dropSchemaObjectsWithNullPathTest() {
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(null);
    }
}
