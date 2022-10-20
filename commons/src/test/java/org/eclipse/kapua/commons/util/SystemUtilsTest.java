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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import java.lang.reflect.Constructor;
import java.net.URI;


@Category(JUnitTests.class)
public class SystemUtilsTest {

    @Test
    public void privateConstrutorTest() throws Exception {
        Constructor<SystemUtils> constructor = SystemUtils.class.getDeclaredConstructor();
        Assert.assertEquals(constructor.isAccessible(), false);
        constructor.setAccessible(true);
        constructor.newInstance((Object[]) null);
    }

    @Test
    public void getNodeUriTest() throws Exception {
        try {
            URI uri = new URI("tcp://localhost:1893");
            Assert.assertEquals(SystemUtils.getNodeURI(), uri);
        } catch (Exception ex) {
            Assert.fail("The URI is incorrect!");
        }
    }

    @Test
    public void getNodeUriUdpTest() throws Exception {
        try {
            URI uri = new URI("udp://localhost:1893");
            Assert.assertNotEquals(SystemUtils.getNodeURI(), uri);
        } catch (Exception ex) {
            //expected
        }
    }

    @Test
    public void getNodeUriNullTest() throws Exception {
        try {
            URI uri = new URI(null);
            SystemUtils.getNodeURI();
            Assert.fail("The URI cannot be null");
        } catch (Exception ex) {
            //expected
        }
    }
}
