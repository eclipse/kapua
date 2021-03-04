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
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdStatic;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Category(Categories.junitTests.class)
public class ComposedKeyTest extends Assert {

    @Test
    public void constructorTest() {
        KapuaId kapuaId = new KapuaIdStatic(BigInteger.ONE);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey = new ComposedKey(kapuaId, key);
        assertNotNull(composedKey);
    }

    @Test
    public void hasCodeTest() {
        KapuaId[] kapuaIdList = {new KapuaIdStatic(BigInteger.ONE), new KapuaIdStatic(BigInteger.TEN), new KapuaIdStatic(BigInteger.ZERO),};
        KapuaId[] kapuaIdForKeyList = {new KapuaIdStatic(BigInteger.ONE), new KapuaIdStatic(BigInteger.TEN), new KapuaIdStatic(BigInteger.ZERO),};

        for (int i = 0; i < kapuaIdList.length; i++) {
            for (int j = 0; j < kapuaIdForKeyList.length; j++) {
                Serializable key = new ComposedKey(kapuaIdForKeyList[j], null);
                ComposedKey composedKey1 = new ComposedKey(kapuaIdList[i], key);
                ComposedKey composedKey2 = new ComposedKey(null, key);
                ComposedKey composedKey3 = new ComposedKey(kapuaIdList[i], null);
                ComposedKey composedKey4 = new ComposedKey(null, null);
                assertEquals("Exception not expected", Objects.hash(kapuaIdList[i], key), composedKey1.hashCode());
                assertEquals("Exception not expected", Objects.hash(null, key), composedKey2.hashCode());
                assertEquals("Exception not expected", Objects.hash(kapuaIdList[i], null), composedKey3.hashCode());
                assertEquals("Exception not expected", Objects.hash(null, null), composedKey4.hashCode());
            }
        }
    }

    @Test
    public void equalsTest() {
        KapuaId kapuaId1 = new KapuaIdStatic(BigInteger.ONE);
        KapuaId kapuaId2 = new KapuaIdStatic(BigInteger.TEN);
        Serializable key1 = new ComposedKey(kapuaId1, null);
        Serializable key2 = new ComposedKey(kapuaId2, null);
        ComposedKey composedKey1 = new ComposedKey(kapuaId1, key1);
        ComposedKey composedKey2 = new ComposedKey(null, null);
        ComposedKey composedKey3 = new ComposedKey(kapuaId2, key1);
        ComposedKey composedKey4 = new ComposedKey(kapuaId1, key2);
        ComposedKey composedKey5 = new ComposedKey(kapuaId1, key1);
        String stringObject = "String";

        assertTrue("Expected true", composedKey1.equals(composedKey1));
        assertFalse("Expected false", composedKey1.equals(null));
        assertFalse("Expected false", composedKey1.equals(stringObject));
        assertFalse("Expected false", composedKey1.equals(composedKey2));
        assertFalse("Expected false", composedKey1.equals(composedKey3));
        assertFalse("Expected false", composedKey1.equals(composedKey4));
        assertTrue("Expected true", composedKey1.equals(composedKey5));
    }

    @Test
    public void getScopeIdTest() {
        KapuaId kapuaId = new KapuaIdStatic(BigInteger.ONE);
        ComposedKey composedKey1 = new ComposedKey(kapuaId, null);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey2 = new ComposedKey(kapuaId, key);
        ComposedKey composedKey3 = new ComposedKey(null, null);

        assertEquals("Exception not expected", kapuaId, composedKey1.getScopeId());
        assertEquals("Exception not expected", kapuaId, composedKey2.getScopeId());
        assertNull("Expected null", composedKey3.getScopeId());
    }

    @Test
    public void getKeyTest() {
        KapuaId kapuaId = new KapuaIdStatic(BigInteger.ONE);
        Serializable key = new ComposedKey(kapuaId, null);
        ComposedKey composedKey1 = new ComposedKey(kapuaId, key);
        ComposedKey composedKey2 = new ComposedKey(null, null);
        ComposedKey composedKey3 = new ComposedKey(null, key);

        assertEquals("Exception not expected", key, composedKey1.getKey());
        assertNull("Expected null", composedKey2.getKey());
        assertEquals("Exception not expected", key, composedKey3.getKey());
    }
} 
