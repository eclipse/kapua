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
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AccessTokenCredentialsImplTest extends Assert {

    @Test(expected = NullPointerException.class)
    public void accessTokenCredentialsImplCloneConstructorNullTest() {
        new AccessTokenCredentialsImpl((AccessTokenCredentials) null);
    }

    @Test
    public void accessTokenCredentialsImplCloneConstructorImplTest() {
        AccessTokenCredentialsImpl first = new AccessTokenCredentialsImpl("anAccessToken");

        AccessTokenCredentialsImpl second = new AccessTokenCredentialsImpl(first);

        assertNotEquals("AccessTokenCredentialImpl", first, second);
        assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }

    @Test
    public void accessTokenCredentialsImplCloneConstructorAnotherTest() {
        AccessTokenCredentials first = new AccessTokenCredentialAnother("anAccessToken");

        AccessTokenCredentialsImpl second = new AccessTokenCredentialsImpl(first);

        assertNotEquals("AccessTokenCredentialImpl", first, second);
        assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }

    @Test
    public void accessTokenCredentialsImplParseNullTest() {
        AccessTokenCredentialsImpl first = null;

        AccessTokenCredentialsImpl second = AccessTokenCredentialsImpl.parse(null);

        assertNull("Parsed AccessTokenCredentialsImpl", second);
        assertEquals("AccessTokenCredentialImpl", first, second);
    }

    @Test
    public void accessTokenCredentialsImplParseImplTest() {
        AccessTokenCredentialsImpl first = new AccessTokenCredentialsImpl("anAccessToken");

        AccessTokenCredentialsImpl second = AccessTokenCredentialsImpl.parse(first);

        assertEquals("AccessTokenCredentialImpl", first, second);
        assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }

    @Test
    public void accessTokenCredentialsImplParseAnotherTest() {
        AccessTokenCredentials first = new AccessTokenCredentialAnother("anAccessToken");

        AccessTokenCredentialsImpl second = AccessTokenCredentialsImpl.parse(first);

        assertNotEquals("AccessTokenCredentialImpl", first, second);
        assertEquals("AccessTokenCredential.tokenId", first.getTokenId(), second.getTokenId());
    }


    @Test
    public void accessTokenCredentialsImplTokenIdParameterTest() {
        String[] tokenIds = {null, "", "!!tokenID-1", "#1(TOKEN.,/token id)9--99", "!$$ 1-2 ID//", "id_tokeN(....)<00>"};

        for (String tokenId : tokenIds) {
            AccessTokenCredentialsImpl accessTokenCredentialsImpl = new AccessTokenCredentialsImpl(tokenId);
            assertEquals("Expected and actual values should be the same.", tokenId, accessTokenCredentialsImpl.getTokenId());
            assertEquals("Expected and actual values should be the same.", tokenId, accessTokenCredentialsImpl.getPrincipal());
            assertEquals("Expected and actual values should be the same.", tokenId, accessTokenCredentialsImpl.getCredentials());
        }
    }

    @Test
    public void setAndGetTokenIdPrincipalAndCredentialsTest() {
        String[] newTokenIds = {null, "", "!!NEWtokenID-1", "#1(newTOKEN.,/token id)9--99", "!$$ 1-2new ID//", "id_tokeN NEW ID(....)<00>"};
        AccessTokenCredentialsImpl accessTokenCredentialsImpl = new AccessTokenCredentialsImpl("token id");

        for (String newTokenId : newTokenIds) {
            accessTokenCredentialsImpl.setTokenId(newTokenId);
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenCredentialsImpl.getTokenId());
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenCredentialsImpl.getPrincipal());
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenCredentialsImpl.getCredentials());
        }
    }
}

class AccessTokenCredentialAnother implements AccessTokenCredentials {
    private String tokenId;

    public AccessTokenCredentialAnother(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}