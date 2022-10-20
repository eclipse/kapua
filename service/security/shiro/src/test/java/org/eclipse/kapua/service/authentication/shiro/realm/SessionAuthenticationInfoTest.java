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
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class SessionAuthenticationInfoTest {

    String[] realmNames;
    Account account;
    User user;
    AccessToken accessToken;

    @Before
    public void initialize() {
        realmNames = new String[]{"", "!!realm NAME-1", "#1(REALM.,/name realm)9--99", "!$$ 1-2 name//", "RE_ALM naME(....)<00>", "na_me=223RE   65ALM     "};
        account = Mockito.mock(Account.class);
        user = Mockito.mock(User.class);
        accessToken = Mockito.mock(AccessToken.class);
    }

    @Test
    public void sessionAuthenticationInfoTest() {
        for (String realmName : realmNames) {
            SessionAuthenticationInfo sessionAuthenticationInfo = new SessionAuthenticationInfo(realmName, account, user, accessToken);
            Assert.assertEquals("Expected and actual values should be the same.", user, sessionAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, sessionAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, sessionAuthenticationInfo.getRealmName());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getAccessToken());
            Assert.assertFalse("False expected.", sessionAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getCredentials());
        }
    }

    @Test
    public void sessionAuthenticationInfoNullNameTest() {
        SessionAuthenticationInfo sessionAuthenticationInfo = new SessionAuthenticationInfo(null, account, user, accessToken);
        Assert.assertEquals("Expected and actual values should be the same.", user, sessionAuthenticationInfo.getUser());
        Assert.assertEquals("Expected and actual values should be the same.", account, sessionAuthenticationInfo.getAccount());
        Assert.assertNull("Null expected.", sessionAuthenticationInfo.getRealmName());
        Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getAccessToken());
        Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getCredentials());
        try {
            sessionAuthenticationInfo.getPrincipals();
            Assert.fail("IllegalArgumentException expected.");
        } catch (Exception e) {
            Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException("realmName argument cannot be null.").toString(), e.toString());
        }
    }

    @Test
    public void sessionAuthenticationInfoNullAccountTest() {
        for (String realmName : realmNames) {
            SessionAuthenticationInfo sessionAuthenticationInfo = new SessionAuthenticationInfo(realmName, null, user, accessToken);
            Assert.assertEquals("Expected and actual values should be the same.", user, sessionAuthenticationInfo.getUser());
            Assert.assertNull("Null expected.", sessionAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, sessionAuthenticationInfo.getRealmName());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getAccessToken());
            Assert.assertFalse("False expected.", sessionAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getCredentials());
        }
    }

    @Test
    public void sessionAuthenticationInfoNullUserTest() {
        for (String realmName : realmNames) {
            SessionAuthenticationInfo sessionAuthenticationInfo = new SessionAuthenticationInfo(realmName, account, null, accessToken);
            Assert.assertNull("Null expected.", sessionAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, sessionAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, sessionAuthenticationInfo.getRealmName());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getAccessToken());
            Assert.assertEquals("Expected and actual values should be the same.", accessToken, sessionAuthenticationInfo.getCredentials());
            try {
                sessionAuthenticationInfo.getPrincipals();
                Assert.fail("IllegalArgumentException expected.");
            } catch (Exception e) {
                Assert.assertEquals("Expected and actual values should be the same.", new NullPointerException("principal argument cannot be null.").toString(), e.toString());
            }
        }
    }

    @Test
    public void sessionAuthenticationInfoNullAccessTokenTest() {
        for (String realmName : realmNames) {
            SessionAuthenticationInfo sessionAuthenticationInfo = new SessionAuthenticationInfo(realmName, account, user, null);
            Assert.assertEquals("Expected and actual values should be the same.", user, sessionAuthenticationInfo.getUser());
            Assert.assertEquals("Expected and actual values should be the same.", account, sessionAuthenticationInfo.getAccount());
            Assert.assertEquals("Expected and actual values should be the same.", realmName, sessionAuthenticationInfo.getRealmName());
            Assert.assertNull("Null expected.", sessionAuthenticationInfo.getAccessToken());
            Assert.assertFalse("False expected.", sessionAuthenticationInfo.getPrincipals().isEmpty());
            Assert.assertNull("Null expected.", sessionAuthenticationInfo.getCredentials());
        }
    }
}