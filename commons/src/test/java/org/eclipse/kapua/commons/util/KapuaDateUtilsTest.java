/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.lang.reflect.Constructor;
import java.util.Date;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaDateUtilsTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        Constructor<KapuaDateUtils> dateUtils = KapuaDateUtils.class.getDeclaredConstructor();
        dateUtils.setAccessible(true);
        dateUtils.newInstance();
    }

    @Test
    public void testGetKapuaSysDate() {
        Assert.assertNotNull(KapuaDateUtils.getKapuaSysDate());

    }

    @Test
    public void testGetTimeZone() {
        Assert.assertNotNull(KapuaDateUtils.getTimeZone());

    }

    @Test
    public void testParseDate() throws Exception {
        String[] listOfFalseStrings = new String[]{null,"2017/12/12T11:22:10.999Z","2017:12:12T:12:12:12.000Z",
                "2017_12_12T_12_12_12_000Z","2017;12;12T11;11;11;000Z","2017-12-12T12-12-12-000Z","2017:12:12T12/12/12/000Z",
                "2017.12.12T12.12.12.000Z","2017/12/12T12:12:12:000X"};
        int sizeOfFalseStrings = listOfFalseStrings.length;
        String[] listOfPermittedStrings = new String[] { "2017-12-12T11:22:10.999Z" };
        int sizeOfPermittedStrings = listOfPermittedStrings.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            if (listOfFalseStrings[i] == null) {
                try {
                    Assert.assertNull(KapuaDateUtils.parseDate(listOfFalseStrings[i]));
                } catch (Exception ex) {
                    fail("No exception expected for Null");
                }
            } else {
                try {
                    KapuaDateUtils.parseDate(listOfFalseStrings[i]);
                    fail("Exception expected for: " + listOfFalseStrings[i]);
                } catch (Exception ex) {
                    // Expected
                }
            }
        }
        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                KapuaDateUtils.parseDate(listOfPermittedStrings[i]);
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStrings[i]);
            }
        }
    }

    @Test
    public void testFormatDate() {
        Date falseDate = null;
        Date permittedDate = new Date();
        // Negative tests
        try {
            Assert.assertNull(KapuaDateUtils.formatDate(falseDate));
        } catch (Exception ex) {
            fail("No exception expected for Null");
        }
        // Positive tests
        try {
            KapuaDateUtils.formatDate(permittedDate);
        } catch (Exception ex) {
            fail("No exception expected for: " + permittedDate);
        }
    }
}
