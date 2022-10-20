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
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;


@Category(JUnitTests.class)
public class ComposedKeyTest {

    @Test
    public void constructorTest() {
        KapuaId kapuaId = new KapuaIdImpl(BigInteger.ONE);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey = new ComposedKey(kapuaId, key);
        Assert.assertNotNull(composedKey);
    }

    @Test
    public void hasCodeTest() {
        KapuaId[] kapuaIdList = {new KapuaIdImpl(BigInteger.ONE), new KapuaIdImpl(BigInteger.TEN), new KapuaIdImpl(BigInteger.ZERO),};
        KapuaId[] kapuaIdForKeyList = {new KapuaIdImpl(BigInteger.ONE), new KapuaIdImpl(BigInteger.TEN), new KapuaIdImpl(BigInteger.ZERO),};

        for (int i = 0; i < kapuaIdList.length; i++) {
            for (int j = 0; j < kapuaIdForKeyList.length; j++) {
                Serializable key = new ComposedKey(kapuaIdForKeyList[j], null);
                ComposedKey composedKey1 = new ComposedKey(kapuaIdList[i], key);
                ComposedKey composedKey2 = new ComposedKey(null, key);
                ComposedKey composedKey3 = new ComposedKey(kapuaIdList[i], null);
                ComposedKey composedKey4 = new ComposedKey(null, null);
                Assert.assertEquals("Exception not expected", Objects.hash(kapuaIdList[i], key), composedKey1.hashCode());
                Assert.assertEquals("Exception not expected", Objects.hash(null, key), composedKey2.hashCode());
                Assert.assertEquals("Exception not expected", Objects.hash(kapuaIdList[i], null), composedKey3.hashCode());
                Assert.assertEquals("Exception not expected", Objects.hash(null, null), composedKey4.hashCode());
            }
        }
    }

    @Test
    public void equalsTest() {
        KapuaId kapuaId1 = new KapuaIdImpl(BigInteger.ONE);
        KapuaId kapuaId2 = new KapuaIdImpl(BigInteger.TEN);
        Serializable key1 = new ComposedKey(kapuaId1, null);
        Serializable key2 = new ComposedKey(kapuaId2, null);
        ComposedKey composedKey1 = new ComposedKey(kapuaId1, key1);
        ComposedKey composedKey2 = new ComposedKey(null, null);
        ComposedKey composedKey3 = new ComposedKey(kapuaId2, key1);
        ComposedKey composedKey4 = new ComposedKey(kapuaId1, key2);
        ComposedKey composedKey5 = new ComposedKey(kapuaId1, key1);
        String stringObject = "String";

        Assert.assertTrue("Expected true", composedKey1.equals(composedKey1));
        Assert.assertFalse("Expected false", composedKey1.equals(null));
        Assert.assertFalse("Expected false", composedKey1.equals(stringObject));
        Assert.assertFalse("Expected false", composedKey1.equals(composedKey2));
        Assert.assertFalse("Expected false", composedKey1.equals(composedKey3));
        Assert.assertFalse("Expected false", composedKey1.equals(composedKey4));
        Assert.assertTrue("Expected true", composedKey1.equals(composedKey5));
    }

    @Test
    public void getScopeIdTest() {
        KapuaId kapuaId = new KapuaIdImpl(BigInteger.ONE);
        ComposedKey composedKey1 = new ComposedKey(kapuaId, null);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey2 = new ComposedKey(kapuaId, key);
        ComposedKey composedKey3 = new ComposedKey(null, null);

        Assert.assertEquals("Exception not expected", kapuaId, composedKey1.getScopeId());
        Assert.assertEquals("Exception not expected", kapuaId, composedKey2.getScopeId());
        Assert.assertNull("Expected null", composedKey3.getScopeId());
    }

    @Test
    public void getKeyTest() {
        KapuaId kapuaId = new KapuaIdImpl(BigInteger.ONE);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey1 = new ComposedKey(kapuaId, key);
        ComposedKey composedKey2 = new ComposedKey(null, null);
        ComposedKey composedKey3 = new ComposedKey(null, key);

        Assert.assertEquals("Exception not expected", key, composedKey1.getKey());
        Assert.assertNull("Expected null", composedKey2.getKey());
        Assert.assertEquals("Exception not expected", key, composedKey3.getKey());
    }
} 
