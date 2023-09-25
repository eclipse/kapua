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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.cache.Cache;
import java.lang.reflect.Constructor;


@Category(JUnitTests.class)
public class KapuaCacheManagerTest {

    @Before
    public void setFakeLocator() {
        System.setProperty(org.eclipse.kapua.locator.KapuaLocator.LOCATOR_CLASS_NAME_SYSTEM_PROPERTY, MockitoLocator.class.getName());
    }

    @Test
    public void kapuaCacheManagerTest() throws Exception {
        Constructor<StaticKapuaCacheManager> kapuaCacheManager = StaticKapuaCacheManager.class.getDeclaredConstructor();
        kapuaCacheManager.setAccessible(true);
        kapuaCacheManager.newInstance();
    }

    @Test
    public void getCacheTest() {
        String cacheName = "cacheName";
        NullPointerException nullPointerException = new NullPointerException();

        Assert.assertNotNull("Null not expected.", StaticKapuaCacheManager.getCache(cacheName));
        Assert.assertThat("Cache object expected.", StaticKapuaCacheManager.getCache(cacheName), IsInstanceOf.instanceOf(Cache.class));

        try {
            StaticKapuaCacheManager.getCache(null);
        } catch (Exception e) {
            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
        }
    }

    @Test
    public void invalidateAllTest() {
        StaticKapuaCacheManager.invalidateAll();
    }
}
