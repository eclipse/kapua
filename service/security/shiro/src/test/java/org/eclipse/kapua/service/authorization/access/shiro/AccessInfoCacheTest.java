/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.access.shiro;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.service.internal.cache.CacheManagerProvider;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class AccessInfoCacheTest {

    String[] idCacheNames, nameCacheNames;
    AccessInfo kapuaEntity;
    private KapuaCacheManager kapuaCacheManager;
    private CommonsMetric commonsMetric;

    @Before
    public void initialize() {
        idCacheNames = new String[]{"", " id 123<> cache(*&% NAME", ")(87CASHE name ^%$id", "98ID <id>     name%$^#62522", ",, #@IDcacheNAME-09", "cache_ID 0998@#$", "C12_...cache==_NAME ID   "};
        nameCacheNames = new String[]{"", "name &^5CACHE-name;'...,,,   ", "!@@@name123CACHE ;,.,,name", "cache--987name,*(NAME", "CACHE      32%$#$%^ name", "CaChE 098)  (name     "};
        kapuaEntity = Mockito.mock(AccessInfo.class);
        System.setProperty(org.eclipse.kapua.locator.KapuaLocator.LOCATOR_CLASS_NAME_SYSTEM_PROPERTY, MockitoLocator.class.getName());
        commonsMetric = Mockito.mock(CommonsMetric.class);
        Mockito.when(commonsMetric.getRegisteredCache()).thenReturn(new Counter());
        Mockito.when(commonsMetric.getCacheError()).thenReturn(new Counter());
        Mockito.when(commonsMetric.getCacheHit()).thenReturn(new Counter());
        Mockito.when(commonsMetric.getCacheMiss()).thenReturn(new Counter());
        final CacheManagerProvider cacheManagerProvider = new CacheManagerProvider(commonsMetric, SystemSetting.getInstance());
        kapuaCacheManager = new KapuaCacheManager(cacheManagerProvider.get(), commonsMetric, SystemSetting.getInstance());
    }

    @Test
    public void accessInfoCacheTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                Assert.assertNotNull("NotNull expected.", accessInfoCache.accessInfoByUserIdCache);
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void accessInfoCacheNullIdCacheNameTest() {
        for (String nameCacheName : nameCacheNames) {
            new AccessInfoCache(kapuaCacheManager, commonsMetric, null, nameCacheName);
        }
    }

    @Test(expected = NullPointerException.class)
    public void accessInfoCacheNullNameCacheNameTest() {
        for (String idCacheName : idCacheNames) {
            new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, null);
        }
    }

    @Test
    public void getByUserIdTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                //COMMENT: This method always returns null, due to method get(Object key) in Cache.java which always returns null
                Assert.assertNull("Null expected.", accessInfoCache.getByUserId(KapuaId.ONE, KapuaId.ONE));
            }
        }
    }

    @Test
    public void getByUserIdNullScopeIdTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                //COMMENT: This method always returns null, due to method get(Object key) in Cache.java which always returns null
                Assert.assertNull("Null expected.", accessInfoCache.getByUserId(null, KapuaId.ONE));
            }
        }
    }

    @Test
    public void getByNullUserIdTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                Assert.assertNull("Null expected.", accessInfoCache.getByUserId(KapuaId.ONE, null));
            }
        }
    }

    @Test
    public void putTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                try {
                    accessInfoCache.put(kapuaEntity);
                } catch (Exception e) {
                    Assert.fail("Exception not expected.");
                }
            }
        }
    }

    @Test
    public void putNullEntityTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                try {
                    accessInfoCache.put(null);
                } catch (Exception e) {
                    Assert.fail("Exception not expected.");
                }
            }
        }
    }

    @Test
    public void removeTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                //COMMENT: kapuaEntity is always null(see method get(Object key) in Cache.java which always returns null)
                // Due to that reason the following part of code could not be tested in AccessInfoCache.java:
                //          if (kapuaEntity != null) {
                //            accessInfoByUserIdCache.remove(((AccessInfo) kapuaEntity).getUserId());
                //        }
                //and this method always returns null
                Assert.assertNull("Null expected.", accessInfoCache.remove(KapuaId.ONE, KapuaId.ONE));
            }
        }
    }

    @Test
    public void removeNullScopeIdTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                //COMMENT: kapuaEntity is always null(see method get(Object key) in Cache.java which always returns null)
                // Due to that reason the following part of code could not be tested in AccessInfoCache.java:
                //          if (kapuaEntity != null) {
                //            accessInfoByUserIdCache.remove(((AccessInfo) kapuaEntity).getUserId());
                //        }
                //and this method always returns null
                Assert.assertNull("Null expected.", accessInfoCache.remove(null, KapuaId.ONE));
            }
        }
    }

    @Test
    public void removeNullKapuaIdTest() {
        for (String idCacheName : idCacheNames) {
            for (String nameCacheName : nameCacheNames) {
                AccessInfoCache accessInfoCache = new AccessInfoCache(kapuaCacheManager, commonsMetric, idCacheName, nameCacheName);
                Assert.assertNull("Null expected.", accessInfoCache.remove(KapuaId.ONE, (KapuaId) null));
            }
        }
    }
}