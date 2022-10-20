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
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;


@Category(JUnitTests.class)
public class KapuaIdImplTest {

    @Test
    public void kapuaIdImplTest() {
        KapuaIdImpl kapuaIdImpl = new KapuaIdImpl(BigInteger.ONE);
        Assert.assertEquals("Expected and actual values should be the same.", BigInteger.ONE, kapuaIdImpl.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void kapuaIdImplNullParameterTest() {
        KapuaIdImpl kapuaIdImpl = new KapuaIdImpl(null);
    }

    @Test
    public void hashCodeTest() {
        BigInteger[] bigInteger = {BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN};
        int[] expectedResult = {31, 32, 41};

        for (int i = 0; i < bigInteger.length; i++) {
            KapuaIdImpl kapuaIdImpl = new KapuaIdImpl(bigInteger[i]);
            Assert.assertEquals("Expected and actual values should be the same.", expectedResult[i], kapuaIdImpl.hashCode());
        }
    }

    @Test
    public void equalsTest() {
        KapuaIdImpl kapuaIdImpl1 = new KapuaIdImpl(BigInteger.ONE);
        KapuaIdImpl kapuaIdImpl2 = new KapuaIdImpl(BigInteger.TEN);
        KapuaIdImpl kapuaIdImpl3 = new KapuaIdImpl(BigInteger.ONE);
        Object[] objects = {0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false};

        Assert.assertEquals("True expected.", kapuaIdImpl1, kapuaIdImpl1);
        Assert.assertNotEquals("False expected", null, kapuaIdImpl1);
        for (Object object : objects) {
            Assert.assertNotEquals("False expected", kapuaIdImpl1, object);
        }
        Assert.assertNotEquals("False expected", kapuaIdImpl1, kapuaIdImpl2);
        Assert.assertEquals("True expected", kapuaIdImpl1, kapuaIdImpl3);
    }
}
