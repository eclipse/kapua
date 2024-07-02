/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.model.AccessTokenCredentialsAnotherImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.model.NotProcessableCredentials;
import org.eclipse.kapua.service.authentication.shiro.realm.model.NotProcessableCredentialsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AccessTokenCredentialsConverterTest {

    AccessTokenCredentialsConverter instance;

    @Before
    public void setUp() {
        instance = new AccessTokenCredentialsConverter();
    }

    @Test
    public void accessTokenCredentialsImplCanProcessNullTest() {
        Assert.assertFalse(instance.canProcess(null));
    }

    @Test
    public void accessTokenCredentialsImplCanProcessImplTest() throws KapuaAuthenticationException {
        AccessTokenCredentials accessTokenCredentialsImpl = new AccessTokenCredentialsImpl("anAccessToken");
        AccessTokenCredentials accessTokenCredentialsAnother = new AccessTokenCredentialsAnotherImpl("anAccessToken");
        NotProcessableCredentials notProcessableCredentials = new NotProcessableCredentialsImpl();

        Assert.assertTrue(instance.canProcess(accessTokenCredentialsImpl));
        Assert.assertTrue(instance.canProcess(accessTokenCredentialsAnother));
        Assert.assertFalse(instance.canProcess(notProcessableCredentials));
    }

    @Test
    public void accessTokenCredentialsImplMapToShiroImplTest() throws KapuaAuthenticationException {
        AccessTokenCredentialsImpl first = new AccessTokenCredentialsImpl("anAccessToken");

        AccessTokenCredentialsImpl second = (AccessTokenCredentialsImpl) instance.convertToShiro(first);

        Assert.assertEquals(first, second);
        Assert.assertEquals(first.getTokenId(), second.getTokenId());
    }

    @Test
    public void accessTokenCredentialsImplMapToShiroAnotherTest() throws KapuaAuthenticationException {
        AccessTokenCredentials first = new AccessTokenCredentialsAnotherImpl("anAccessToken");

        AccessTokenCredentialsImpl second = (AccessTokenCredentialsImpl) instance.convertToShiro(first);

        Assert.assertNotNull(second);
        Assert.assertNotEquals(first, second);
        Assert.assertEquals(first.getTokenId(), second.getTokenId());
    }

    @Test(expected = NullPointerException.class)
    public void accessTokenCredentialsImplMapToShiroNullTest() throws KapuaAuthenticationException {
        instance.convertToShiro(null);
    }

    @Test(expected = KapuaAuthenticationException.class)
    public void accessTokenCredentialsImplMapToShiroEmptyTest() throws KapuaAuthenticationException {
        AccessTokenCredentialsImpl first = new AccessTokenCredentialsImpl((String) null);

        Assert.assertNotNull(first);

        instance.convertToShiro(first);
    }
}