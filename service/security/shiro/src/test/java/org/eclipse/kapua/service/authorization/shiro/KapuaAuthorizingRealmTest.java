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
package org.eclipse.kapua.service.authorization.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class KapuaAuthorizingRealmTest extends Assert {

    KapuaAuthorizingRealm kapuaAuthorizingRealm;
    AuthenticationToken authenticationToken;

    @Before
    public void initialize() throws KapuaException {
        kapuaAuthorizingRealm = new KapuaAuthorizingRealm();
        authenticationToken = Mockito.mock(AuthenticationToken.class);
    }

    @Test
    public void kapuaAuthorizingRealmTest() {
        assertEquals("Expected and actual values should be the same.", "kapuaAuthorizingRealm", kapuaAuthorizingRealm.getName());
    }

    @Test
    public void supportsTest() {
        assertFalse("False expected.", kapuaAuthorizingRealm.supports(authenticationToken));
    }

    @Test
    public void supportsNullTest() {
        assertFalse("False expected.", kapuaAuthorizingRealm.supports(null));
    }

    @Test
    public void doGetAuthenticationInfoTest() {
        assertNull("Null expected.", kapuaAuthorizingRealm.doGetAuthenticationInfo(authenticationToken));
    }

    @Test
    public void doGetAuthenticationInfoNullTest() {
        assertNull("Null expected.", kapuaAuthorizingRealm.doGetAuthenticationInfo(authenticationToken));
    }
}