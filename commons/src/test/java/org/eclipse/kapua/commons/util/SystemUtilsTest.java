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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import java.lang.reflect.Constructor;
import java.net.URI;

@Category(JUnitTests.class)
public class SystemUtilsTest extends Assert {

    @Test
    public void privateConstrutorTest() throws Exception {
        Constructor<SystemUtils> constructor = SystemUtils.class.getDeclaredConstructor();
        assertEquals(constructor.isAccessible(), false);
        constructor.setAccessible(true);
        constructor.newInstance((Object[]) null);
    }

    @Test
    public void getNodeUriTest() throws Exception {
        try {
            URI uri = new URI("tcp://localhost:1893");
            assertEquals(SystemUtils.getNodeURI(), uri);
        } catch (Exception ex) {
            fail("The URI is incorrect!");
        }
    }

    @Test
    public void getNodeUriUdpTest() throws Exception {
        try {
            URI uri = new URI("udp://localhost:1893");
            assertNotEquals(SystemUtils.getNodeURI(), uri);
        } catch (Exception ex) {
            //expected
        }
    }

    @Test
    public void getNodeUriNullTest() throws Exception {
        try {
            URI uri = new URI(null);
            SystemUtils.getNodeURI();
            fail("The URI cannot be null");
        } catch (Exception ex) {
            //expected
        }
    }
}
