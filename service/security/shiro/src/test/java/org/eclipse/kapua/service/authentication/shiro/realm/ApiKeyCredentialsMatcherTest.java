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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class ApiKeyCredentialsMatcherTest {

    ApiKeyCredentialsMatcher apiKeyCredentialsMatcher;
    JwtCredentialsImpl authenticationToken;
    LoginAuthenticationInfo authenticationInfo;
    Credential credential;

    @Before
    public void initialize() {
        apiKeyCredentialsMatcher = new ApiKeyCredentialsMatcher();
        authenticationToken = Mockito.mock(JwtCredentialsImpl.class);
        authenticationInfo = Mockito.mock(LoginAuthenticationInfo.class);
        credential = Mockito.mock(Credential.class);
    }

    @Test(expected = NullPointerException.class)
    public void doCredentialsMatchNullAuthenticationTokenTest() {
        apiKeyCredentialsMatcher.doCredentialsMatch(null, authenticationInfo);
    }

    @Test(expected = NullPointerException.class)
    public void doCredentialsMatchNullAuthenticationInfoTest() {
        Mockito.when(authenticationToken.getCredentials()).thenReturn("tokenApiFullKey");

        apiKeyCredentialsMatcher.doCredentialsMatch(authenticationToken, null);
    }

    @Test
    public void doCredentialsMatchDifferentCredentialTypesTest() {
        Mockito.when(authenticationToken.getCredentials()).thenReturn("tokenApiFullKey");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.PASSWORD);

        Assert.assertFalse("False expected.", apiKeyCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchDifferentTokenAndInfoPreTest() {
        Mockito.when(authenticationToken.getCredentials()).thenReturn("tokenApiFullKey");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credential.getCredentialKey()).thenReturn("FullApiK:Credential");

        Assert.assertFalse("False expected.", apiKeyCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchEqualTokenAndInfoPreFalseCheckPwTest() {
        Mockito.when(authenticationToken.getCredentials()).thenReturn("FullApiKCredential");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credential.getCredentialKey()).thenReturn("FullApiK:$2a$12$2AZYOAvilJyNvG8b6rBDaOSIcM3mKc6iyNQUYIXOF4ZFEAYdzM7Jm");

        Assert.assertFalse("False expected.", apiKeyCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }

    @Test
    public void doCredentialsMatchTest() {
        Mockito.when(authenticationToken.getCredentials()).thenReturn("FullApiKplainValue");
        Mockito.when(authenticationInfo.getCredentials()).thenReturn(credential);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credential.getCredentialKey()).thenReturn("FullApiK:$2a$12$2AZYOAvilJyNvG8b6rBDaOSIcM3mKc6iyNQUYIXOF4ZFEAYdzM7Jm");

        Assert.assertTrue("True expected.", apiKeyCredentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo));
    }
}