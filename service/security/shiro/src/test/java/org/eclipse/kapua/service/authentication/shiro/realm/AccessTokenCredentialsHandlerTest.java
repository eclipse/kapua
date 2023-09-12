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

import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialAnother;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccessTokenCredentialsHandlerTest {

    AccessTokenCredentialsHandler instance;

    @Before
    void setUp() {
        instance = new AccessTokenCredentialsHandler();
    }

    @Test
    public void accessTokenCredentialsImplParseImplTest() throws KapuaAuthenticationException {
        AccessTokenCredentialsImpl first = new AccessTokenCredentialsImpl("anAccessToken");

        AccessTokenCredentialsImpl second = (AccessTokenCredentialsImpl) instance.mapToShiro(first);

        Assert.assertEquals("AccessTokenCredentialImpl", first, second);
        Assert.assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }

    @Test
    public void accessTokenCredentialsImplParseAnotherTest() throws KapuaAuthenticationException {
        AccessTokenCredentials first = new AccessTokenCredentialAnother("anAccessToken");

        AccessTokenCredentialsImpl second = (AccessTokenCredentialsImpl) instance.mapToShiro(first);

        Assert.assertNotEquals("AccessTokenCredentialImpl", first, second);
        Assert.assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }
}