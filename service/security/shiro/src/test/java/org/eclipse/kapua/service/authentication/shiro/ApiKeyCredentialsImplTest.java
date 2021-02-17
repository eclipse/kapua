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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ApiKeyCredentialsImplTest extends Assert {

    @Test(expected = NullPointerException.class)
    public void apiKeyCredentialsImplCloneConstructorNullTest() {
        new ApiKeyCredentialsImpl((ApiKeyCredentials) null);
    }

    @Test
    public void apiKeyCredentialsImplCloneConstructorImplTest() {
        ApiKeyCredentialsImpl first = new ApiKeyCredentialsImpl("anApiKey");

        ApiKeyCredentialsImpl second = new ApiKeyCredentialsImpl(first);

        assertNotEquals("ApiKeyCredentialImpl", first, second);
        assertEquals("ApiKeyCredential.apiKey", first.getApiKey(), second.getApiKey());
    }

    @Test
    public void apiKeyCredentialsImplCloneConstructorAnotherTest() {
        ApiKeyCredentials first = new ApiKwyCredentialsAnother("anApiKey");

        ApiKeyCredentialsImpl second = new ApiKeyCredentialsImpl(first);

        assertNotEquals("ApiKeyCredentialImpl", first, second);
        assertEquals("ApiKeyCredential.apiKey", first.getApiKey(), second.getApiKey());
    }

    @Test
    public void apiKeyCredentialsImplParseNullTest() {
        ApiKeyCredentialsImpl first = null;

        ApiKeyCredentialsImpl second = ApiKeyCredentialsImpl.parse(null);

        assertNull("Parsed ApiKeyCredentialsImpl", second);
        assertEquals("ApiKeyCredentialImpl", first, second);
    }

    @Test
    public void apiKeyCredentialsImplParseImplTest() {
        ApiKeyCredentialsImpl first = new ApiKeyCredentialsImpl("anApiKey");

        ApiKeyCredentialsImpl second = ApiKeyCredentialsImpl.parse(first);

        assertEquals("ApiKeyCredentialImpl", first, second);
        assertEquals("ApiKeyCredential.apiKey", first.getApiKey(), second.getApiKey());
    }

    @Test
    public void apiKeyCredentialsImplParseAnotherTest() {
        ApiKeyCredentials first = new ApiKwyCredentialsAnother("anApiKey");

        ApiKeyCredentialsImpl second = ApiKeyCredentialsImpl.parse(first);

        assertNotEquals("ApiKeyCredentialImpl", first, second);
        assertEquals("ApiKeyCredential.apiKey", first.getApiKey(), second.getApiKey());
    }

    @Test
    public void apiKeyCredentialsImplTest() {
        String[] apiKeys = {null, "", "!!api key-1", "#1(API KEY.,/api key)9--99", "!$$ 1-2 KEY//", "APIkey(....)<00>"};

        for (String apiKey : apiKeys) {
            ApiKeyCredentialsImpl apiKeyCredentialsImpl = new ApiKeyCredentialsImpl(apiKey);
            assertEquals("Expected and actual values should be the same.", apiKey, apiKeyCredentialsImpl.getApiKey());
            assertEquals("Expected and actual values should be the same.", apiKey, apiKeyCredentialsImpl.getPrincipal());
            assertEquals("Expected and actual values should be the same.", apiKey, apiKeyCredentialsImpl.getCredentials());
        }
    }

    @Test
    public void setAndGetApiKeyPrincipalAndCredentialsTest() {
        String[] newApiKeys = {null, "", "!!api key-1NEW", "#1(new API KEY.,/api key)9--99", "!$$ 1-2 newKEY//", "NEwAPIkey(....)<00>"};
        ApiKeyCredentialsImpl apiKeyCredentialsImpl = new ApiKeyCredentialsImpl("apiKey");

        for (String newApiKey : newApiKeys) {
            apiKeyCredentialsImpl.setApiKey(newApiKey);
            assertEquals("Expected and actual values should be the same.", newApiKey, apiKeyCredentialsImpl.getApiKey());
            assertEquals("Expected and actual values should be the same.", newApiKey, apiKeyCredentialsImpl.getPrincipal());
            assertEquals("Expected and actual values should be the same.", newApiKey, apiKeyCredentialsImpl.getCredentials());
        }
    }
}

class ApiKwyCredentialsAnother implements ApiKeyCredentials {
    private String apiKey;

    public ApiKwyCredentialsAnother(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}