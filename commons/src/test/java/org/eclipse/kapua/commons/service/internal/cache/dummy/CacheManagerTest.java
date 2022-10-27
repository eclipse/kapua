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
package org.eclipse.kapua.commons.service.internal.cache.dummy;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;


@Category(JUnitTests.class)
public class CacheManagerTest {

    @Test
    public void constructorTest() throws Exception {
        Constructor<CacheManager> testConstructor = CacheManager.class.getDeclaredConstructor();
        testConstructor.setAccessible(true);
        testConstructor.newInstance();
    }

    @Test
    public void getInstanceTest() {
        Assert.assertNotNull("Null not expected", CacheManager.getInstance());
    }

    @Test
    public void getCachingProviderTest() {
        Assert.assertNull("Expected null", CacheManager.getInstance().getCachingProvider());
    }

    @Test
    public void getURITest() {
        Assert.assertNull("Expected null", CacheManager.getInstance().getURI());
    }

    @Test
    public void getClassLoaderTest() {
        Assert.assertNull("Expected null", CacheManager.getInstance().getClassLoader());
    }

    @Test
    public void getPropertiesTest() {
        Assert.assertNull("Expected null", CacheManager.getInstance().getProperties());
    }

    @Test
    public void createCacheTest() {
        String cacheName = "Cache Name";
        Assert.assertNotNull("New Cache object expected", CacheManager.getInstance().createCache(cacheName, null));
    }

    @Test
    public void getCacheTest() {
        String cacheName = "Cache Name";
        Class[] keyType = new Class[]{Integer.class, Character.class, String.class, Long.class, Double.class, Byte.class, Boolean.class};
        Class[] valueType = new Class[]{Integer.class, Character.class, String.class, Long.class, Double.class, Byte.class, Boolean.class};

        for (int i = 0; i < keyType.length; i++) {
            for (int j = 0; j < valueType.length; j++) {
                Assert.assertNull("Expected null", CacheManager.getInstance().getCache(cacheName, keyType[i], valueType[j]));
            }
        }
    }

    @Test
    public void getCacheOnlyNameTest() {
        String cacheName = "Cache Name";
        Assert.assertNull("Expected null", CacheManager.getInstance().getCache(cacheName));
    }

    @Test
    public void getCacheNameTest() {
        Assert.assertNull("Expected null", CacheManager.getInstance().getCacheNames());
    }

    @Test
    public void destroyCacheTest() {
        String cacheName = "Cache Name";
        CacheManager.getInstance().destroyCache(cacheName);
    }

    @Test
    public void enableManagementTest() {
        String cacheName = "Cache Name";
        boolean[] enabled = new boolean[]{true, false};
        for (int i = 0; i < enabled.length; i++) {
            CacheManager.getInstance().enableManagement(cacheName, enabled[i]);
        }
    }

    @Test
    public void enableStatisticsTest() {
        String cacheName = "Cache Name";
        boolean[] enabled = new boolean[]{true, false};
        for (int i = 0; i < enabled.length; i++) {
            CacheManager.getInstance().enableStatistics(cacheName, enabled[i]);
        }
    }

    @Test
    public void closeTest() {
        CacheManager.getInstance().close();
    }

    @Test
    public void isClosedTest() {
        Assert.assertFalse("False expected", CacheManager.getInstance().isClosed());
    }

    @Test
    public void unwrapTest() {
        Class[] clazz = new Class[]{Integer.class, Character.class, String.class, Long.class, Double.class, Byte.class, Boolean.class};
        for (int i = 0; i < clazz.length; i++) {
            Assert.assertNull("Null expected", CacheManager.getInstance().unwrap(clazz[i]));
        }
    }
}
