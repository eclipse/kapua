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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.cache.Cache;
import java.io.Serializable;
import java.math.BigInteger;


@Category(JUnitTests.class)
public class NamedEntityCacheTest {

    @Test
    public void namedEntityCacheTest() {
        String idCacheName = "idCacheName";
        String nameCacheName = "nameCacheName";
        NullPointerException nullPointerException = new NullPointerException();

        NamedEntityCache namedEntityCache = new NamedEntityCache(idCacheName, nameCacheName);
        Cache<Serializable, Serializable> expectedNameCache = KapuaCacheManager.getCache(nameCacheName);

        Assert.assertEquals("Expected and actual values should be the same.", expectedNameCache, namedEntityCache.nameCache);

        try {
            NamedEntityCache invalidNamedEntityCache = new NamedEntityCache(idCacheName, null);
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
        }
        try {
            NamedEntityCache invalidNamedEntityCache = new NamedEntityCache(null, nameCacheName);
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
        }
        try {
            NamedEntityCache invalidNamedEntityCache = new NamedEntityCache(null, null);
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
        }
    }

    @Test
    public void getTest() {
        String idCacheName = "idCacheName";
        String nameCacheName = "nameCacheName";
        NamedEntityCache namedEntityCache = new NamedEntityCache(idCacheName, nameCacheName);
        KapuaId scopeId = new KapuaIdImpl(BigInteger.ONE);
        String name = "name";
        String nullName = null;

        //COMMENT: entityId is always null (see Cache.get() method)
        //Due to that reason this method always returns null
        Assert.assertNull("Null expected.", namedEntityCache.get(scopeId, name));
        Assert.assertNull("Null expected.", namedEntityCache.get(null, name));
        Assert.assertNull("Null expected.", namedEntityCache.get(scopeId, nullName));
        Assert.assertNull("Null expected.", namedEntityCache.get(null, nullName));

        // COMMENT: Once the get() method will be changed,
        // we will be able to test other results also.
    }

    @Test
    public void putTest() {
        NamedEntityCache namedEntityCache = new NamedEntityCache("idCacheName", "nameCacheName");
        KapuaId scopeId = new KapuaIdImpl(BigInteger.ONE);
        KapuaId kapuaId = new KapuaIdImpl(BigInteger.ONE);
        KapuaEntity kapuaEntity = namedEntityCache.get(scopeId, kapuaId);

        //COMMENT: Entity is always null (see Cache.get() method)
        // Due to that reason the following part of code could not be tested in NamedEntityCache.java:
        //if (entity != null) {
        //            idCache.put(entity.getId(), entity);
        //            nameCache.put(((KapuaNamedEntity) entity).getName(), entity.getId());
        //        }
        namedEntityCache.put(kapuaEntity);
        namedEntityCache.put(null);

        // COMMENT: Once the get() method will be changed,
        // we will be able to test other results also.
    }

    @Test
    public void removeTest() {
        String idCacheName = "idCacheName";
        String nameCacheName = "nameCacheName";
        NamedEntityCache namedEntityCache = new NamedEntityCache(idCacheName, nameCacheName);
        KapuaId scopeId = new KapuaIdImpl(BigInteger.ONE);
        KapuaId kapuaId = new KapuaIdImpl(BigInteger.ONE);
        KapuaId nullScopeId = null;
        KapuaId nullKapuaId = null;

        //COMMENT: Entity is always null (see Cache.get() method)
        // Due to that reason the following part of code could not be tested in NamedEntityCache.java:
        //if (kapuaEntity != null) {
        //            nameCache.remove(((KapuaNamedEntity) kapuaEntity).getName());
        //        }
        //and this method always returns null
        Assert.assertNull("Null expected.", namedEntityCache.remove(scopeId, kapuaId));
        Assert.assertNull("Null expected.", namedEntityCache.remove(nullScopeId, kapuaId));
        Assert.assertNull("Null expected.", namedEntityCache.remove(scopeId, nullKapuaId));
        Assert.assertNull("Null expected.", namedEntityCache.remove(nullScopeId, nullKapuaId));

        // COMMENT: Once the get() method will be changed,
        // we will be able to test other results also.
    }
} 
