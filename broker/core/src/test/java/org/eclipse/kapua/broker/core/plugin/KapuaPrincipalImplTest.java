/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaPrincipalImplTest extends Assert {

    KapuaPrincipalImpl kapuaPrincipal;

    @Before
    public void start() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        kapuaPrincipal = new KapuaPrincipalImpl(token, "user1", "client1", "192.168.1.2");
    }

    @Test
    public void gettersTest() {
        assertEquals("user1", kapuaPrincipal.getName());
        assertEquals("192.168.1.2", kapuaPrincipal.getClientIp());
        assertEquals("client1", kapuaPrincipal.getClientId());
        assertEquals("1", kapuaPrincipal.getTokenId());
        assertEquals(KapuaId.ONE, kapuaPrincipal.getUserId());
        assertEquals(KapuaId.ONE, kapuaPrincipal.getAccountId());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(111580519, kapuaPrincipal.hashCode());
    }

    @Test
    public void hashCodeWithNullAccountTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(null);
        KapuaPrincipalImpl kapuaPrincipal = new KapuaPrincipalImpl(token, "user1", "client1", "192.168.1.2");
        assertEquals(111579527, kapuaPrincipal.hashCode());
    }

    @Test
    public void hashCodeWithNullUsernameTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        KapuaPrincipalImpl kapuaPrincipal = new KapuaPrincipalImpl(token, null, "client1", "192.168.1.2");
        assertEquals(1953, kapuaPrincipal.hashCode());
    }

    @Test
    public void hashCodeWithNullUsernameAndScopeIdTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(null);
        KapuaPrincipalImpl kapuaPrincipal = new KapuaPrincipalImpl(token, null, "client1", "192.168.1.2");
        assertEquals(961, kapuaPrincipal.hashCode());
    }

    @Test
    public void equalsDifferentValuesTest() {
        assertTrue(kapuaPrincipal.equals(kapuaPrincipal));
        assertFalse(kapuaPrincipal.equals(null));
        assertFalse(kapuaPrincipal.equals("test"));
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        KapuaPrincipalImpl nullPrincipal = new KapuaPrincipalImpl(token, null, null, null);
        assertFalse(kapuaPrincipal.equals(nullPrincipal));
        assertFalse(nullPrincipal.equals(kapuaPrincipal));

    }

    @Test
    public void equalsWithSameAccountTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        KapuaPrincipalImpl nullPrincipal = new KapuaPrincipalImpl(token, null, null, null);
        assertFalse(kapuaPrincipal.equals(nullPrincipal));
        assertFalse(nullPrincipal.equals(kapuaPrincipal));
    }

    @Test
    public void equalsWithDifferentNameTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        KapuaPrincipalImpl nullPrincipal = new KapuaPrincipalImpl(token, "differentName", "client1", "192.168.1.2");
        assertFalse(nullPrincipal.equals(kapuaPrincipal));
    }

    @Test
    public void equalsWithNullNameTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        KapuaPrincipalImpl nullPrincipal = new KapuaPrincipalImpl(token, null, "client1", "192.168.1.2");
        assertFalse(nullPrincipal.equals(kapuaPrincipal));
    }

    @Test
    public void equalsWithIdenticalPrincipalsTest() {
        AccessToken token = new AccessTokenImpl();
        token.setTokenId("1");
        token.setUserId(KapuaId.ONE);
        token.setScopeId(KapuaId.ONE);
        KapuaPrincipalImpl nullPrincipal = new KapuaPrincipalImpl(token, "user1", "client1", "192.168.1.2");
        assertTrue(nullPrincipal.equals(kapuaPrincipal));
    }
}
