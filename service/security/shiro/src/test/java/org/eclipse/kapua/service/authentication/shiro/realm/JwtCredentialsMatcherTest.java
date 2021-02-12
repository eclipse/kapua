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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class JwtCredentialsMatcherTest extends Assert {

    JwtProcessor jwtProcessor;
    JwtCredentialsImpl authenticationToken;
    AuthenticationInfo authenticationInfo;
    Credential credential;
    JwtCredentialsMatcher jwtCredentialsMatcher;

    @Before
    public void initialize() {
        jwtProcessor = Mockito.mock(JwtProcessor.class);
        authenticationToken = Mockito.mock(JwtCredentialsImpl.class);
        authenticationInfo = Mockito.mock(AuthenticationInfo.class);
        credential = Mockito.mock(Credential.class);
        jwtCredentialsMatcher = new JwtCredentialsMatcher(jwtProcessor);
    }

    @Test
    public void jwtCredentialsMatcherNullTest() {
        try {
            new JwtCredentialsMatcher(null);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void doCredentialsMatchNullAuthenticationTokenTest() {
        jwtCredentialsMatcher.doCredentialsMatch(null, authenticationInfo);
    }

    @Test
    public void doCredentialsMatchNullAuthenticationInfoNullJwtTest() {
        Mockito.when(authenticationToken.getJwt()).thenReturn(null);
        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, null));
    }

    @Test(expected = NullPointerException.class)
    public void doCredentialsMatchNullAuthenticationInfoTest() {
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, null);
    }

    @Test
    public void doCredentialsMatchNullJwtTest() {
        Mockito.when(authenticationToken.getJwt()).thenReturn(null);

        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchStringCredentialTest() {
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn("credential");

        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchDifferentKeysTest() {
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialKey()).thenReturn("credential key");

        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchEqualKeysFalseTest() throws OpenIDException {
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialKey()).thenReturn("jwt");
        Mockito.when(jwtProcessor.validate("jwt")).thenReturn(false);

        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchEqualKeysTrueTest() throws OpenIDException {
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialKey()).thenReturn("jwt");
        Mockito.when(jwtProcessor.validate("jwt")).thenReturn(true);

        assertTrue("True expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchEqualKeysOpenIDExceptionTest() throws OpenIDException {
        JwtCredentialsMatcher jwtCredentialsMatcher = new JwtCredentialsMatcher(jwtProcessor);
        Mockito.when(authenticationToken.getJwt()).thenReturn("jwt");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialKey()).thenReturn("jwt");
        Mockito.when(jwtProcessor.validate("jwt")).thenThrow(Mockito.mock(OpenIDException.class));

        assertFalse("False expected.", jwtCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }
}