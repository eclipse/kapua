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
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class KapuaIdStaticTest extends Assert {

    @Test
    public void kapuaIdStaticTest() {
        KapuaIdStatic kapuaIdStatic = new KapuaIdStatic(BigInteger.ONE);
        assertEquals("Expected and actual values should be the same.", BigInteger.ONE, kapuaIdStatic.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void kapuaIdStaticNullParameterTest() {
        KapuaIdStatic kapuaIdStatic = new KapuaIdStatic(null);
    }

    @Test
    public void hashCodeTest() {
        BigInteger[] bigInteger = {BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN};
        int[] expectedResult = {31, 32, 41};

        for (int i = 0; i < bigInteger.length; i++) {
            KapuaIdStatic kapuaIdStatic = new KapuaIdStatic(bigInteger[i]);
            assertEquals("Expected and actual values should be the same.", expectedResult[i], kapuaIdStatic.hashCode());
        }
    }

    @Test
    public void equalsTest() {
        KapuaIdStatic kapuaIdStatic1 = new KapuaIdStatic(BigInteger.ONE);
        KapuaIdStatic kapuaIdStatic2 = new KapuaIdStatic(BigInteger.TEN);
        KapuaIdStatic kapuaIdStatic3 = new KapuaIdStatic(BigInteger.ONE);
        Object[] objects = {0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false};

        assertTrue("True expected.", kapuaIdStatic1.equals(kapuaIdStatic1));
        assertFalse("False expected", kapuaIdStatic1.equals(null));
        for (Object object : objects) {
            assertFalse("False expected", kapuaIdStatic1.equals(object));
        }
        assertFalse("False expected", kapuaIdStatic1.equals(kapuaIdStatic2));
        assertTrue("True expected", kapuaIdStatic1.equals(kapuaIdStatic3));
    }
}
