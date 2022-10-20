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

import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class AccessInfoCacheFactoryTest {

    AccessInfoCacheFactory accessInfoCacheFactory;

    @Before
    public void initialize() {
        accessInfoCacheFactory = new AccessInfoCacheFactory();
    }

    @Test
    public void accessInfoCacheFactoryTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "AccessInfoId", accessInfoCacheFactory.getEntityIdCacheName());
    }

    @Test
    public void createCacheTest() {
        Assert.assertTrue("True expected.", accessInfoCacheFactory.createCache() instanceof EntityCache);
    }

    @Test
    public void getInstanceTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "AccessInfoId", AccessInfoCacheFactory.getInstance().getEntityIdCacheName());
    }
}