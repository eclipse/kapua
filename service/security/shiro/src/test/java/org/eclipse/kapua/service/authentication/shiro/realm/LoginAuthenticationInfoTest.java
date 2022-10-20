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
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class LoginAuthenticationInfoTest {

    String[] realmNames;
    Account account;
    User user;
    Credential credentials;
    Map<String, Object> credentialServiceConfig;

    @Before
    public void initialize() {
        realmNames = new String[]{"", "!!realm NAME-1", "#1(REALM.,/name realm)9--99", "!$$ 1-2 name//", "RE_ALM naME(....)<00>", "na_me=223RE   65ALM     "};
        account = Mockito.mock(Account.class);
        user = Mockito.mock(User.class);
        credentials = Mockito.mock(Credential.class);
        credentialServiceConfig = new HashMap<>();
    }

    @Test
    public void loginAuthenticationInfoTest() {
        for (String realmName : realmNames) {
            LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(realmName, account, user, credentials, credentialServiceConfig);
            Assert.assertEquals("Expected and actual values should be the same.", user, loginAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, loginAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, loginAuthenticationInfo.getRealmName());
            Assert.assertFalse("False expected.", loginAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertEquals("Expected and actual values should be the same.", credentials, loginAuthenticationInfo.getCredentials());
            Assert.assertEquals("Expected and actual values should be the same.", credentialServiceConfig, loginAuthenticationInfo.getCredentialServiceConfig());
        }
    }

    @Test
    public void loginAuthenticationInfoNullNameTest() {
        LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(null, account, user, credentials, credentialServiceConfig);
        Assert.assertEquals("Expected and actual values should be the same.", user, loginAuthenticationInfo.getUser());
        Assert.assertEquals("Expected and actual values should be the same.", account, loginAuthenticationInfo.getAccount());
        Assert.assertNull("Null expected.", loginAuthenticationInfo.getRealmName());
        Assert.assertEquals("Expected and actual values should be the same.", credentials, loginAuthenticationInfo.getCredentials());
        Assert.assertEquals("Expected and actual values should be the same.", credentialServiceConfig, loginAuthenticationInfo.getCredentialServiceConfig());
        try {
            loginAuthenticationInfo.getPrincipals();
            Assert.fail("IllegalArgumentException expected.");
        } catch (Exception e) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException("realmName argument cannot be null.").toString(), e.toString());
        }
    }

    @Test
    public void loginAuthenticationInfoNullAccountTest() {
        for (String realmName : realmNames) {
            LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(realmName, null, user, credentials, credentialServiceConfig);
            Assert.assertEquals("Expected and actual values should be the same.", user, loginAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, loginAuthenticationInfo.getRealmName());
            Assert.assertNull("Null expected.", loginAuthenticationInfo.getAccount());
            Assert.assertFalse("False expected.", loginAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertEquals("Expected and actual values should be the same.", credentials, loginAuthenticationInfo.getCredentials());
            Assert.assertEquals("Expected and actual values should be the same.", credentialServiceConfig, loginAuthenticationInfo.getCredentialServiceConfig());
        }
    }

    @Test
    public void loginAuthenticationInfoNullUserTest() {
        for (String realmName : realmNames) {
            LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(realmName, account, null, credentials, credentialServiceConfig);
            Assert.assertNull("Null expected.", loginAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, loginAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, loginAuthenticationInfo.getRealmName());
            Assert.assertEquals("Expected and actual values should be the same.", credentials, loginAuthenticationInfo.getCredentials());
            Assert.assertEquals("Expected and actual values should be the same.", credentialServiceConfig, loginAuthenticationInfo.getCredentialServiceConfig());
            try {
                loginAuthenticationInfo.getPrincipals();
                Assert.fail("IllegalArgumentException expected.");
            } catch (Exception e) {
                Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException("principal argument cannot be null.").toString(), e.toString());
            }
        }
    }

    @Test
    public void loginAuthenticationInfoNullCredentialsTest() {
        for (String realmName : realmNames) {
            LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(realmName, account, user, null, credentialServiceConfig);
            Assert.assertEquals("Expected and actual values should be the same.", user, loginAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, loginAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, loginAuthenticationInfo.getRealmName());
            Assert.assertFalse("False expected.", loginAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertNull("Null expected.", loginAuthenticationInfo.getCredentials());
            Assert.assertEquals("Expected and actual values should be the same.", credentialServiceConfig, loginAuthenticationInfo.getCredentialServiceConfig());
        }
    }

    @Test
    public void loginAuthenticationInfoNullCredentialServiceConfigTest() {
        for (String realmName : realmNames) {
            LoginAuthenticationInfo loginAuthenticationInfo = new LoginAuthenticationInfo(realmName, account, user, credentials, null);
            Assert.assertEquals("Expected and actual values should be the same.", user, loginAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, loginAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, loginAuthenticationInfo.getRealmName());
            Assert.assertFalse("False expected.", loginAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertEquals("Expected and actual values should be the same.", credentials, loginAuthenticationInfo.getCredentials());
            Assert.assertNull("Null expected.", loginAuthenticationInfo.getCredentialServiceConfig());
        }
    }
}