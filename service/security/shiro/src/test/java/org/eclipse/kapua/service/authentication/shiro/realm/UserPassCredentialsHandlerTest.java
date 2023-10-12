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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.model.NotProcessableCredentials;
import org.eclipse.kapua.service.authentication.shiro.realm.model.NotProcessableCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.model.UsernamePasswordCredentialsAnotherImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserPassCredentialsHandlerTest {

    UserPassCredentialsHandler instance;

    @Before
    public void setUp() {
        instance = new UserPassCredentialsHandler();
    }

    @Test
    public void usernamePasswordCredentialsImplCanProcessNullTest() {
        Assert.assertFalse(instance.canProcess(null));
    }

    @Test
    public void usernamePasswordCredentialsImplCanProcessImplTest() throws KapuaAuthenticationException {
        UsernamePasswordCredentials usernamePasswordCredentialsImpl = new UsernamePasswordCredentialsImpl("aUsernamePassword", "anIdToken");
        UsernamePasswordCredentials usernamePasswordCredentialsAnother = new UsernamePasswordCredentialsAnotherImpl("aUsernamePassword", "anIdToken");
        NotProcessableCredentials notProcessableCredentials = new NotProcessableCredentialsImpl();

        Assert.assertTrue(instance.canProcess(usernamePasswordCredentialsImpl));
        Assert.assertTrue(instance.canProcess(usernamePasswordCredentialsAnother));
        Assert.assertFalse(instance.canProcess(notProcessableCredentials));
    }

    @Test
    public void usernamePasswordCredentialsImplMapToShiroImplTest() throws KapuaAuthenticationException {
        UsernamePasswordCredentialsImpl first = new UsernamePasswordCredentialsImpl("aUsername", "aPassword");
        first.setAuthenticationCode("123456");
        first.setTrustKey("aTrustKey");
        first.setTrustMe(true);

        UsernamePasswordCredentialsImpl second = (UsernamePasswordCredentialsImpl) instance.mapToShiro(first);

        Assert.assertNotNull(second);
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.getUsername(), second.getUsername());
        Assert.assertEquals(first.getPassword(), second.getPassword());
        Assert.assertEquals(first.getAuthenticationCode(), second.getAuthenticationCode());
        Assert.assertEquals(first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals(first.getTrustMe(), second.getTrustMe());
    }

    @Test
    public void usernamePasswordCredentialsImplMapToShiroAnotherTest() throws KapuaAuthenticationException {
        UsernamePasswordCredentials first = new UsernamePasswordCredentialsAnotherImpl("aAccessToken", "anIdToken");
        first.setAuthenticationCode("123456");
        first.setTrustKey("aTrustKey");
        first.setTrustMe(true);

        UsernamePasswordCredentialsImpl second = (UsernamePasswordCredentialsImpl) instance.mapToShiro(first);

        Assert.assertNotNull(second);
        Assert.assertNotEquals(first, second);
        Assert.assertEquals(first.getUsername(), second.getUsername());
        Assert.assertEquals(first.getPassword(), second.getPassword());
        Assert.assertEquals(first.getAuthenticationCode(), second.getAuthenticationCode());
        Assert.assertEquals(first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals(first.getTrustMe(), second.getTrustMe());
    }

    @Test(expected = NullPointerException.class)
    public void usernamePasswordCredentialsImplMapToShiroNullTest() throws KapuaAuthenticationException {
        instance.mapToShiro(null);
    }

    @Test(expected = KapuaAuthenticationException.class)
    public void usernamePasswordCredentialsImplMapToShiroEmptyTest() throws KapuaAuthenticationException {
        UsernamePasswordCredentialsImpl first = new UsernamePasswordCredentialsImpl(null, null);

        Assert.assertNotNull(first);

        instance.mapToShiro(first);
    }
}