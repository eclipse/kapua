/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class AccessRoleCacheFactoryTest extends Assert {

    @Test
    public void accessRoleCacheFactoryTest() throws Exception {
        Constructor<AccessRoleCacheFactory> accessRoleCacheFactory = AccessRoleCacheFactory.class.getDeclaredConstructor();
        accessRoleCacheFactory.setAccessible(true);
        accessRoleCacheFactory.newInstance();
        assertTrue("True expected.", Modifier.isPrivate(accessRoleCacheFactory.getModifiers()));
    }

    @Test
    public void getInstanceTest() {
        assertTrue("True expected.", AccessRoleCacheFactory.getInstance() instanceof AccessRoleCacheFactory);
        assertEquals("Expected and actual values should be the same.", "AccessRoleId", AccessRoleCacheFactory.getInstance().getEntityIdCacheName());
    }
}