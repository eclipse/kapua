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
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class JwtCredentialsTest extends Assert {

    JwtCredentialsImpl jwtCredentialsImpl;
    String[] jwts, idsToken, newJwts, newIdsToken;

    @Before
    public void initialize() {
        jwtCredentialsImpl = new JwtCredentialsImpl("jwt", "idToken");
        idsToken = new String[]{null, "", "   ID tokenID 747.,.,,,82(*&%<> ", "   token((11@-", "id)__.,TOKen65", "TOKENid543$#%&t   oken", "to-ken_id++=,", "id,,,,id3$^&"};
        jwts = new String[]{null, "", "  j_w=t110.,<> jwt", "(!!)432j&^w$#3t", "##<>/.JWT    ", "__J!#W(-8T    ", "jw&* 990t  ", "jwt987)_=;'''     .", "jwt JWT-123"};
        newJwts = new String[]{null, "", "new_Jwt1122#$%", "   JWT)(..,,new", "NEW_jwt ./??)_)*", "<> 1111      ", "jwttt&^$$##Z||'", "%%%KEY NEW-JWT11"};
        newIdsToken = new String[]{null, "", "NEW tokenID0000@!!,,,#", "!@#00tokenID new.", " new id TOK --EN-44<>", "pA_ss0###woE**9()", "    tokenID new tokenID  12344*&^%"};
    }

    @Test(expected = NullPointerException.class)
    public void jwtCredentialsImplCloneConstructorNullTest() {
        new JwtCredentialsImpl(null);
    }

    @Test
    public void jwtCredentialsImplCloneConstructorImplTest() {
        JwtCredentialsImpl first = new JwtCredentialsImpl("aJwt", "anIdToken");

        JwtCredentialsImpl second = new JwtCredentialsImpl(first);

        assertNotEquals("JwtCredentialImpl", first, second);
        assertEquals("JwtCredential.jwt", first.getJwt(), second.getJwt());
        assertEquals("JwtCredential.idToken", first.getIdToken(), second.getIdToken());
    }

    @Test
    public void jwtCredentialsImplCloneConstructorAnotherTest() {
        JwtCredentials first = new JwtCredentialsAnother("aJwt", "anIdToken");

        JwtCredentialsImpl second = new JwtCredentialsImpl(first);

        assertNotEquals("JwtCredentialImpl", first, second);
        assertEquals("JwtCredential.jwt", first.getJwt(), second.getJwt());
        assertEquals("JwtCredential.idToken", first.getIdToken(), second.getIdToken());
    }

    @Test
    public void jwtCredentialsImplParseNullTest() {
        JwtCredentialsImpl first = null;

        JwtCredentialsImpl second = JwtCredentialsImpl.parse(null);

        assertNull("Parsed JwtCredentialsImpl", second);
        assertEquals("JwtCredentialImpl", first, second);
    }

    @Test
    public void jwtCredentialsImplParseImplTest() {
        JwtCredentialsImpl first = new JwtCredentialsImpl("aJwt", "anIdToken");

        JwtCredentialsImpl second = JwtCredentialsImpl.parse(first);

        assertEquals("JwtCredentialImpl", first, second);
        assertEquals("JwtCredential.jwt", first.getJwt(), second.getJwt());
        assertEquals("JwtCredential.idToken", first.getIdToken(), second.getIdToken());
    }

    @Test
    public void jwtCredentialsImplParseAnotherTest() {
        JwtCredentials first = new JwtCredentialsAnother("aJwt", "anIdToken");

        JwtCredentialsImpl second = JwtCredentialsImpl.parse(first);

        assertNotEquals("JwtCredentialImpl", first, second);
        assertEquals("JwtCredential.jwt", first.getJwt(), second.getJwt());
        assertEquals("JwtCredential.idToken", first.getIdToken(), second.getIdToken());
    }


    @Test
    public void jwtCredentialsImplTest() {
        for (String jwt : jwts) {
            for (String idToken : idsToken) {
                JwtCredentialsImpl jwtCredentialsImpl = new JwtCredentialsImpl(jwt, idToken);
                assertEquals("Expected and actual values should be the same.", jwt, jwtCredentialsImpl.getJwt());
                assertEquals("Expected and actual values should be the same.", idToken, jwtCredentialsImpl.getIdToken());
                assertEquals("Expected and actual values should be the same.", jwt, jwtCredentialsImpl.getPrincipal());
                assertEquals("Expected and actual values should be the same.", jwt, jwtCredentialsImpl.getCredentials());
            }
        }
    }

    @Test
    public void setAndGetJwtPrincipalAndCredentialTest() {
        for (String newJwt : newJwts) {
            jwtCredentialsImpl.setJwt(newJwt);
            assertEquals("Expected and actual values should be the same.", newJwt, jwtCredentialsImpl.getJwt());
            assertEquals("Expected and actual values should be the same.", newJwt, jwtCredentialsImpl.getPrincipal());
            assertEquals("Expected and actual values should be the same.", newJwt, jwtCredentialsImpl.getCredentials());
        }
    }

    @Test
    public void setAndGetIdTokenTest() {
        for (String newIdToken : newIdsToken) {
            jwtCredentialsImpl.setIdToken(newIdToken);
            assertEquals("Expected and actual values should be the same.", newIdToken, jwtCredentialsImpl.getIdToken());
        }
    }
}

class JwtCredentialsAnother implements JwtCredentials {
    private String jwt;
    private String idToken;

    public JwtCredentialsAnother(String jwt, String idToken) {
        this.jwt = jwt;
        this.idToken = idToken;
    }

    @Override
    public String getJwt() {
        return jwt;
    }

    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}