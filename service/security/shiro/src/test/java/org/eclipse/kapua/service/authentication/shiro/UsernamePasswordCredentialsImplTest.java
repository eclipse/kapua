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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class UsernamePasswordCredentialsImplTest {

    String[] usernames, passwords, newUsernames, newPasswords, trustKeys, authenticationCodes;
    UsernamePasswordCredentialsImpl usernamePasswordCredentialsImpl;

    @Before
    public void initialize() {
        usernames = new String[]{null, "", "user_name123!!", "user#999username", "USERNAME_9", "user,,,,name", "... us_er%%67na*(me"};
        passwords = new String[]{null, "", "pass-word0000@!!,,,#", "!@#00PaSSwOrD.", " password ---44<>", "pA_ss0###woE**9()", "    pass0wo-rd  12344*&^%"};
        newUsernames = new String[]{null, "", "NEW---user_name123!!", "user#999username,.,@#NEW", "1111USERNAME_9", "   new--^%4user,,,,name", "... us_er%%67na*(me   NEW  "};
        newPasswords = new String[]{null, "", "pass-word0000@!!,new password,,#", "!@#00PaSSwOrD._@#new", "new    password ---44<>", "   new#@$pA_ss0###woE**9()", "    pass0wo-rd  12344*NEW&^%"};
        trustKeys = new String[]{null, "", "!!trust key-1", "#1(TRUST KEY.,/trust key)9--99", "!$$ 1-2 KEY//", "trust 99key(....)<00>"};
        authenticationCodes = new String[]{null, "", "  authentication@#$%Code=t110.,<> code", "(!!)432j&^authenti)(&%cation-Code$#3t", "##<>/.CODE    ", "__J!#W(-8T    ", "authenticatioN&* 99code0t  ", "jwt987)_=;'''     .", "jwt CODE-123"};
        usernamePasswordCredentialsImpl = new UsernamePasswordCredentialsImpl("username", "password");
    }

    @Test(expected = NullPointerException.class)
    public void usernamePasswordCredentialsImplCloneConstructorNullTest() {
        new UsernamePasswordCredentialsImpl(null);
    }

    @Test
    public void usernamePasswordCredentialsImplCloneConstructorImplTest() {
        UsernamePasswordCredentialsImpl first = new UsernamePasswordCredentialsImpl("aUsername", "aPassword");
        first.setTrustKey("aTrustKey");
        first.setAuthenticationCode("aAuthCode");

        UsernamePasswordCredentialsImpl second = new UsernamePasswordCredentialsImpl(first);

        Assert.assertNotEquals("UsernamePasswordCredentialImpl", first, second);
        Assert.assertEquals("UsernamePasswordCredential.username", first.getUsername(), second.getUsername());
        Assert.assertEquals("UsernamePasswordCredential.password", first.getPassword(), second.getPassword());
        Assert.assertEquals("UsernamePasswordCredential.trustKey", first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals("UsernamePasswordCredential.authenticationCode", first.getAuthenticationCode(), second.getAuthenticationCode());

    }

    @Test
    public void usernamePasswordCredentialsImplCloneConstructorAnotherTest() {
        UsernamePasswordCredentials first = new UsernamePasswordCredentialsAnother("aUsername", "aPassword");
        first.setTrustKey("aTrustKey");
        first.setAuthenticationCode("aAuthCode");

        UsernamePasswordCredentialsImpl second = new UsernamePasswordCredentialsImpl(first);

        Assert.assertNotEquals("UsernamePasswordCredentialImpl", first, second);
        Assert.assertEquals("UsernamePasswordCredential.username", first.getUsername(), second.getUsername());
        Assert.assertEquals("UsernamePasswordCredential.password", first.getPassword(), second.getPassword());
        Assert.assertEquals("UsernamePasswordCredential.trustKey", first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals("UsernamePasswordCredential.authenticationCode", first.getAuthenticationCode(), second.getAuthenticationCode());
    }

    @Test
    public void usernamePasswordCredentialsImplParseNullTest() {
        UsernamePasswordCredentialsImpl first = null;

        UsernamePasswordCredentialsImpl second = UsernamePasswordCredentialsImpl.parse(null);

        Assert.assertNull("Parsed UsernamePasswordCredentialsImpl", second);
        Assert.assertEquals("UsernamePasswordCredentialImpl", first, second);
    }

    @Test
    public void usernamePasswordCredentialsImplParseImplTest() {
        UsernamePasswordCredentialsImpl first = new UsernamePasswordCredentialsImpl("aUsername", "aPassword");
        first.setTrustKey("aTrustKey");
        first.setAuthenticationCode("aAuthCode");

        UsernamePasswordCredentialsImpl second = UsernamePasswordCredentialsImpl.parse(first);

        Assert.assertEquals("UsernamePasswordCredentialImpl", first, second);
        Assert.assertEquals("UsernamePasswordCredential.username", first.getUsername(), second.getUsername());
        Assert.assertEquals("UsernamePasswordCredential.password", first.getPassword(), second.getPassword());
        Assert.assertEquals("UsernamePasswordCredential.trustKey", first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals("UsernamePasswordCredential.authenticationCode", first.getAuthenticationCode(), second.getAuthenticationCode());
    }

    @Test
    public void usernamePasswordCredentiaslImplParseAnotherTest() {
        UsernamePasswordCredentials first = new UsernamePasswordCredentialsAnother("aUsername", "aPassword");
        first.setTrustKey("aTrustKey");
        first.setAuthenticationCode("aAuthCode");

        UsernamePasswordCredentialsImpl second = UsernamePasswordCredentialsImpl.parse(first);

        Assert.assertNotEquals("UsernamePasswordCredentialImpl", first, second);
        Assert.assertEquals("UsernamePasswordCredential.username", first.getUsername(), second.getUsername());
        Assert.assertEquals("UsernamePasswordCredential.password", first.getPassword(), second.getPassword());
        Assert.assertEquals("UsernamePasswordCredential.trustKey", first.getTrustKey(), second.getTrustKey());
        Assert.assertEquals("UsernamePasswordCredential.authenticationCode", first.getAuthenticationCode(), second.getAuthenticationCode());
    }

    @Test
    public void usernamePasswordCredentialsImplTest() {
        for (String username : usernames) {
            for (String password : passwords) {
                UsernamePasswordCredentialsImpl usernamePasswordCredentialsImpl = new UsernamePasswordCredentialsImpl(username, password);
                Assert.assertEquals("Expected and actual values should be the same.", username, usernamePasswordCredentialsImpl.getUsername());
                Assert.assertEquals("Expected and actual values should be the same.", username, usernamePasswordCredentialsImpl.getPrincipal());
                Assert.assertEquals("Expected and actual values should be the same.", password, usernamePasswordCredentialsImpl.getPassword());
                Assert.assertEquals("Expected and actual values should be the same.", password, usernamePasswordCredentialsImpl.getCredentials());
                Assert.assertNull("Null expected.", usernamePasswordCredentialsImpl.getAuthenticationCode());
                Assert.assertNull("Null expected.", usernamePasswordCredentialsImpl.getTrustKey());
            }
        }
    }

    @Test
    public void setAndGetUsernameAndPrincipalTest() {
        for (String newUsername : newUsernames) {
            usernamePasswordCredentialsImpl.setUsername(newUsername);
            Assert.assertEquals("Expected and actual values should be the same.", newUsername, usernamePasswordCredentialsImpl.getUsername());
            Assert.assertEquals("Expected and actual values should be the same.", newUsername, usernamePasswordCredentialsImpl.getPrincipal());
        }
    }

    @Test
    public void setAndGetPasswordAndCredentialsTest() {
        for (String newPassword : newPasswords) {
            usernamePasswordCredentialsImpl.setPassword(newPassword);
            Assert.assertEquals("Expected and actual values should be the same.", newPassword, usernamePasswordCredentialsImpl.getPassword());
            Assert.assertEquals("Expected and actual values should be the same.", newPassword, usernamePasswordCredentialsImpl.getCredentials());
        }
    }

    @Test
    public void setAndGetAuthenticationCodeTest() {
        for (String authenticationCode : authenticationCodes) {
            usernamePasswordCredentialsImpl.setAuthenticationCode(authenticationCode);
            Assert.assertEquals("Expected and actual values should be the same.", authenticationCode, usernamePasswordCredentialsImpl.getAuthenticationCode());
        }
    }

    @Test
    public void setAndGetTrustKeyTest() {
        for (String trustKey : trustKeys) {
            usernamePasswordCredentialsImpl.setTrustKey(trustKey);
            Assert.assertEquals("Expected and actual values should be the same.", trustKey, usernamePasswordCredentialsImpl.getTrustKey());
        }
    }
}

class UsernamePasswordCredentialsAnother implements UsernamePasswordCredentials {

    private String username;
    private String password;
    private String authenticationCode;
    private String trustKey;
    private boolean trustMe;

    public UsernamePasswordCredentialsAnother(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    @Override
    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    @Override
    public String getTrustKey() {
        return trustKey;
    }

    @Override
    public void setTrustKey(String trustKey) {
        this.trustKey = trustKey;
    }

    @Override
    public boolean getTrustMe() {
        return trustMe;
    }

    @Override
    public void setTrustMe(boolean trustMe) {
        this.trustMe = trustMe;
    }
}