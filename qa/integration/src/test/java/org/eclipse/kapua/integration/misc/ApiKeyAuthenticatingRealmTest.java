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
package org.eclipse.kapua.integration.misc;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authentication.shiro.ApiKeyCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.ApiKeyAuthenticatingRealm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(Categories.junitTests.class)
public class ApiKeyAuthenticatingRealmTest extends Assert {

    ApiKeyAuthenticatingRealm apiKeyAuthenticatingRealm;

    @Before
    public void initialize() throws KapuaException {
        apiKeyAuthenticatingRealm = new ApiKeyAuthenticatingRealm();
    }

    @Test
    public void apiKeyAuthenticatingRealmTest() {
        assertEquals("Expected and actual values should be the same.", "apiKeyAuthenticatingRealm", apiKeyAuthenticatingRealm.getName());
        assertNotNull("Null not expected", apiKeyAuthenticatingRealm.getCredentialsMatcher());
    }

    @Test
    public void supportsTrueTest() {
        ApiKeyCredentialsImpl authenticationToken = new ApiKeyCredentialsImpl("api key");
        assertTrue("True expected.", apiKeyAuthenticatingRealm.supports(authenticationToken));
    }

    @Test
    public void supportsFalseTest() {
        AuthenticationToken authenticationToken = Mockito.mock(AuthenticationToken.class);
        assertFalse("False expected.", apiKeyAuthenticatingRealm.supports(authenticationToken));
    }
}