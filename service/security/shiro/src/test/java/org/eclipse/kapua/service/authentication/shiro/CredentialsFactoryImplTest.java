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
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class CredentialsFactoryImplTest {

    CredentialsFactoryImpl credentialsFactoryImpl;
    String[] usernames, passwords, apiKeys, idTokens, accessTokens, refreshTokens;

    @Before
    public void initialize() {
        credentialsFactoryImpl = new CredentialsFactoryImpl();
        usernames = new String[]{null, "", "user_name123!!", "user#999username", "USERNAME_9", "user,,,,name", "... us_er%%67na*(me"};
        passwords = new String[]{null, "", "pass-word0000@!!,,,#", "!@#00PaSSwOrD.", " password ---44<>", "pA_ss0###woE**9()", "    pass0wo-rd  12344*&^%"};
        apiKeys = new String[]{null, "", "api_key1122#$%", "   aPi)(..,,KEY", "apiKEYYY ./??)_)*", "<> 1111      ", "keyyy&^$$##Z||'", "%%%KEY api-key11"};
        idTokens = new String[]{null, "", "   ID tokenID 747.,.,,,82(*&%<> ", "   token((11@-", "id)__.,TOKen65", "TOKENid543$#%&t   oken", "to-ken_id++=,", "id,,,,id3$^&"};
        accessTokens = new String[]{null, "", "  j_w=t110.,<> jwt", "(!!)432j&^w$#3t", "##<>/.JWT    ", "__J!#W(-8T    ", "jw&* 990t  ", "jwt987)_=;'''     .", "jwt JWT-123"};
        refreshTokens = new String[]{null, "", "   refresh tokenREFRESH 747.,.,,,82(*&%<> ", "   token((11@-", "REFresh)__.,TOKen65", "TOKENrefresh543$#%&t   oken", "to-ken_++rE=fresh,", "refresh,,,,id3$^&"};
    }

    @Test
    public void newUsernamePasswordCredentialsTest() {
        for (String username : usernames) {
            for (String password : passwords) {
                UsernamePasswordCredentials usernamePasswordCredentials = credentialsFactoryImpl.newUsernamePasswordCredentials(username, password);
                Assert.assertEquals("Expected and actual values should be the same.", username, usernamePasswordCredentials.getUsername());
                Assert.assertEquals("Expected and actual values should be the same.", password, usernamePasswordCredentials.getPassword());
            }
        }
    }

    @Test
    public void newApiKeyCredentialsTest() {
        for (String apiKey : apiKeys) {
            ApiKeyCredentials apiKeyCredentials = credentialsFactoryImpl.newApiKeyCredentials(apiKey);
            Assert.assertEquals("Expected and actual values should be the same.", apiKey, apiKeyCredentials.getApiKey());
        }
    }

    @Test
    public void newJwtCredentialsTest() {
        for (String accessToken : accessTokens) {
            for (String idToken : idTokens) {
                JwtCredentials jwtCredentials = credentialsFactoryImpl.newJwtCredentials(accessToken, idToken);
                Assert.assertEquals("Expected and actual values should be the same.", accessToken, jwtCredentials.getAccessToken());
                Assert.assertEquals("Expected and actual values should be the same.", idToken, jwtCredentials.getIdToken());
            }
        }
    }

    @Test
    public void newAccessTokenCredentialsTest() {
        for (String idToken : idTokens) {
            AccessTokenCredentials accessTokenCredentials = credentialsFactoryImpl.newAccessTokenCredentials(idToken);
            Assert.assertEquals("Expected and actual values should be the same.", idToken, accessTokenCredentials.getTokenId());
        }
    }

    @Test
    public void newRefreshTokenCredentialsTest() {
        for (String idToken : idTokens) {
            for (String refreshToken : refreshTokens) {
                RefreshTokenCredentials refreshTokenCredentials = credentialsFactoryImpl.newRefreshTokenCredentials(idToken, refreshToken);
                Assert.assertEquals("Expected and actual values should be the same.", idToken, refreshTokenCredentials.getTokenId());
                Assert.assertEquals("Expected and actual values should be the same.", refreshToken, refreshTokenCredentials.getRefreshToken());
            }
        }
    }
}
