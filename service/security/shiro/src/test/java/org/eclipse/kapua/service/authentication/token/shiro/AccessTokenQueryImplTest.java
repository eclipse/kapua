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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class AccessTokenQueryImplTest {

    @Test
    public void accessTokenQueryImplTest() throws Exception {
        Constructor<AccessTokenQueryImpl> accessTokenQueryImpl = AccessTokenQueryImpl.class.getDeclaredConstructor();
        accessTokenQueryImpl.setAccessible(true);
        accessTokenQueryImpl.newInstance();
        Assert.assertTrue("True expected.", Modifier.isPrivate(accessTokenQueryImpl.getModifiers()));
    }

    @Test
    public void accessTokenQueryImplTestScopeIdParameterTest() {
        AccessTokenQueryImpl accessTokenQueryImpl = new AccessTokenQueryImpl(KapuaId.ONE);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenQueryImpl.getScopeId());
    }

    @Test
    public void accessTokenQueryImplTestNullScopeIdParameterTest() {
        AccessTokenQueryImpl accessTokenQueryImpl = new AccessTokenQueryImpl(null);
        Assert.assertNull("Null expected.", accessTokenQueryImpl.getScopeId());
    }
}