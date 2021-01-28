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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class AccessTokenImplTest extends Assert {

    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] tokenIds, refreshTokens;
    Date[] expiresOnDates, refreshExpiresOnDates;
    AccessToken accessToken;
    Date expiresOn, refreshExpiresOn, invalidatedOn, modifiedOn, createdOn;
    AccessTokenImpl accessTokenImpl1, accessTokenImpl2, accessTokenImpl3, accessTokenImpl4;

    @Before
    public void initialize() throws KapuaException {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid()};
        tokenIds = new String[]{null, "", "!!tokenID-1", "#1(TOKEN.,/token id)9--99", "!$$ 1-2 ID//", "id_tokeN(....)<00>"};
        expiresOnDates = new Date[]{null, new Date()};
        refreshTokens = new String[]{null, "", "!!refreshToken-1", "#1(TOKEN.,/refresh token id)9--99", "!$$ 1-2 REFREsh//", "refresh_tokeN(....)<00>"};
        refreshExpiresOnDates = new Date[]{null, new Date()};
        accessToken = Mockito.mock(AccessToken.class);
        expiresOn = new Date();
        refreshExpiresOn = new Date();
        invalidatedOn = new Date();
        modifiedOn = new Date();
        createdOn = new Date();

        Mockito.when(accessToken.getId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessToken.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(accessToken.getTokenId()).thenReturn("token id");
        Mockito.when(accessToken.getExpiresOn()).thenReturn(expiresOn);
        Mockito.when(accessToken.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessToken.getRefreshExpiresOn()).thenReturn(refreshExpiresOn);
        Mockito.when(accessToken.getInvalidatedOn()).thenReturn(invalidatedOn);
        Mockito.when(accessToken.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(accessToken.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessToken.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getOptlock()).thenReturn(10);

        accessTokenImpl1 = new AccessTokenImpl();
        accessTokenImpl2 = new AccessTokenImpl(KapuaId.ONE, new KapuaEid(KapuaId.ONE), "tokenId", expiresOn, "refreshToken", refreshExpiresOn);
        accessTokenImpl3 = new AccessTokenImpl(KapuaId.ONE);
        accessTokenImpl4 = new AccessTokenImpl(accessToken);
    }

    @Test
    public void accessTokenImplWithoutParametersTest() {
        AccessTokenImpl accessTokenImpl = new AccessTokenImpl();
        assertNull("Null expected.", accessTokenImpl.getScopeId());
        assertNull("Null expected.", accessTokenImpl.getUserId());
        assertNull("Null expected.", accessTokenImpl.getTokenId());
        assertNull("Null expected.", accessTokenImpl.getExpiresOn());
        assertNull("Null expected.", accessTokenImpl.getRefreshToken());
        assertNull("Null expected.", accessTokenImpl.getRefreshExpiresOn());
    }

    @Test
    public void accessTokenImplMultipleParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId userId : userIds) {
                for (String tokenId : tokenIds) {
                    for (Date expiresOnDate : expiresOnDates) {
                        for (String refreshToken : refreshTokens) {
                            for (Date refreshExpiresOnDate : refreshExpiresOnDates) {
                                AccessTokenImpl accessTokenImpl = new AccessTokenImpl(scopeId, userId, tokenId, expiresOnDate, refreshToken, refreshExpiresOnDate);
                                assertEquals("Expected and actual values should be the same.", scopeId, accessTokenImpl.getScopeId());
                                assertEquals("Expected and actual values should be the same.", userId, accessTokenImpl.getUserId());
                                assertEquals("Expected and actual values should be the same.", tokenId, accessTokenImpl.getTokenId());
                                assertEquals("Expected and actual values should be the same.", expiresOnDate, accessTokenImpl.getExpiresOn());
                                assertEquals("Expected and actual values should be the same.", refreshToken, accessTokenImpl.getRefreshToken());
                                assertEquals("Expected and actual values should be the same.", refreshExpiresOnDate, accessTokenImpl.getRefreshExpiresOn());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void accessTokenImplScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            AccessTokenImpl accessTokenImpl = new AccessTokenImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, accessTokenImpl.getScopeId());
            assertNull("Null expected.", accessTokenImpl.getUserId());
            assertNull("Null expected.", accessTokenImpl.getTokenId());
            assertNull("Null expected.", accessTokenImpl.getExpiresOn());
            assertNull("Null expected.", accessTokenImpl.getRefreshToken());
            assertNull("Null expected.", accessTokenImpl.getRefreshExpiresOn());
        }
    }

    @Test
    public void accessTokenImplAccessTokenParameterTest() throws KapuaException {
        AccessTokenImpl accessTokenImpl = new AccessTokenImpl(accessToken);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ONE), accessTokenImpl.getUserId());
        assertEquals("Expected and actual values should be the same.", "token id", accessTokenImpl.getTokenId());
        assertEquals("Expected and actual values should be the same.", expiresOn, accessTokenImpl.getExpiresOn());
        assertEquals("Expected and actual values should be the same.", "refresh token", accessTokenImpl.getRefreshToken());
        assertEquals("Expected and actual values should be the same.", refreshExpiresOn, accessTokenImpl.getRefreshExpiresOn());
        assertEquals("Expected and actual values should be the same.", invalidatedOn, accessTokenImpl.getInvalidatedOn());
        assertEquals("Expected and actual values should be the same.", modifiedOn, accessTokenImpl.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", 10, accessTokenImpl.getOptlock());
    }

    @Test
    public void setAndGetUserIdTest() {
        KapuaId[] newUserIds = {null, KapuaId.ANY};
        for (KapuaId newUserId : newUserIds) {
            accessTokenImpl1.setUserId(newUserId);
            accessTokenImpl2.setUserId(newUserId);
            accessTokenImpl3.setUserId(newUserId);
            accessTokenImpl4.setUserId(newUserId);

            assertEquals("Expected and actual values should be the same.", newUserId, accessTokenImpl1.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, accessTokenImpl2.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, accessTokenImpl3.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, accessTokenImpl4.getUserId());
        }
    }

    @Test
    public void setAndGetTokenIdTest() {
        String[] newTokenIds = {null, "", "!!NEWtokenID-1", "#1(TOKEN.,/token NEW id)9--99", "!$$ 1-2 newID//", "id_tokeN(newID)<00>"};
        for (String newTokenId : newTokenIds) {
            accessTokenImpl1.setTokenId(newTokenId);
            accessTokenImpl2.setTokenId(newTokenId);
            accessTokenImpl3.setTokenId(newTokenId);
            accessTokenImpl4.setTokenId(newTokenId);

            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenImpl1.getTokenId());
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenImpl2.getTokenId());
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenImpl3.getTokenId());
            assertEquals("Expected and actual values should be the same.", newTokenId, accessTokenImpl4.getTokenId());
        }
    }

    @Test
    public void setAndGetExpiresOnTest() {
        Date[] newExpiresOnDates = {null, new Date()};
        for (Date newExpiresOnDate : newExpiresOnDates) {
            accessTokenImpl1.setExpiresOn(newExpiresOnDate);
            accessTokenImpl2.setExpiresOn(newExpiresOnDate);
            accessTokenImpl3.setExpiresOn(newExpiresOnDate);
            accessTokenImpl4.setExpiresOn(newExpiresOnDate);

            assertEquals("Expected and actual values should be the same.", newExpiresOnDate, accessTokenImpl1.getExpiresOn());
            assertEquals("Expected and actual values should be the same.", newExpiresOnDate, accessTokenImpl2.getExpiresOn());
            assertEquals("Expected and actual values should be the same.", newExpiresOnDate, accessTokenImpl3.getExpiresOn());
            assertEquals("Expected and actual values should be the same.", newExpiresOnDate, accessTokenImpl4.getExpiresOn());
        }
    }

    @Test
    public void prePersistsActionTest() {
        accessTokenImpl1.setUserId(KapuaId.ANY);
        assertNull("Null expected.", accessTokenImpl1.getId());
        assertNull("Null expected.", accessTokenImpl1.getCreatedBy());
        assertNull("Null expected.", accessTokenImpl1.getCreatedOn());
        assertNull("Null expected.", accessTokenImpl1.getModifiedBy());
        assertNull("Null expected.", accessTokenImpl1.getModifiedOn());

        accessTokenImpl1.prePersistsAction();
        assertNotNull("NotNull expected.", accessTokenImpl1.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessTokenImpl1.getCreatedBy());
        assertNotNull("NotNullExpected", accessTokenImpl1.getCreatedOn());
        assertNotNull("NotNullExpected", accessTokenImpl1.getModifiedBy());
        assertNotNull("NotNullExpected", accessTokenImpl1.getModifiedOn());

        accessTokenImpl2.setUserId(KapuaId.ANY);
        assertNull("Null expected.", accessTokenImpl2.getId());
        assertNull("Null expected.", accessTokenImpl2.getCreatedBy());
        assertNull("Null expected.", accessTokenImpl2.getCreatedOn());
        assertNull("Null expected.", accessTokenImpl2.getModifiedBy());
        assertNull("Null expected.", accessTokenImpl2.getModifiedOn());

        accessTokenImpl2.prePersistsAction();
        assertNotNull("NotNull expected.", accessTokenImpl2.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessTokenImpl2.getCreatedBy());
        assertNotNull("NotNullExpected", accessTokenImpl2.getCreatedOn());
        assertNotNull("NotNullExpected", accessTokenImpl2.getModifiedBy());
        assertNotNull("NotNullExpected", accessTokenImpl2.getModifiedOn());

        accessTokenImpl3.setUserId(KapuaId.ANY);
        assertNull("Null expected.", accessTokenImpl3.getId());
        assertNull("Null expected.", accessTokenImpl3.getCreatedBy());
        assertNull("Null expected.", accessTokenImpl3.getCreatedOn());
        assertNull("Null expected.", accessTokenImpl3.getModifiedBy());
        assertNull("Null expected.", accessTokenImpl3.getModifiedOn());

        accessTokenImpl3.prePersistsAction();
        assertNotNull("NotNull expected.", accessTokenImpl3.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessTokenImpl3.getCreatedBy());
        assertNotNull("NotNullExpected", accessTokenImpl3.getCreatedOn());
        assertNotNull("NotNullExpected", accessTokenImpl3.getModifiedBy());
        assertNotNull("NotNullExpected", accessTokenImpl3.getModifiedOn());

        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessTokenImpl4.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl4.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, accessTokenImpl4.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl4.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", modifiedOn, accessTokenImpl4.getModifiedOn());

        accessTokenImpl4.prePersistsAction();
        assertNotEquals("Expected and actual values should not be the same.", KapuaId.ANY, accessTokenImpl4.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl4.getCreatedBy());
        assertNotEquals("Expected and actual values should not be the same.", createdOn, accessTokenImpl4.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenImpl4.getModifiedBy());
        assertNotEquals("Expected and actual values should not be the same.", modifiedOn, accessTokenImpl4.getModifiedOn());
    }

    @Test
    public void setAndGetRefreshTokenTest() {
        String[] newRefreshTokens = {null, "", "!!NEW refresh token-1", "#1(TOKEN.REFRESH,/token NEW )9--99", "!$$ 1-2 newRefresh/TOKEN/", "id_tokeN(newrefresh)<00>"};
        for (String newRefreshToken : newRefreshTokens) {
            accessTokenImpl1.setRefreshToken(newRefreshToken);
            accessTokenImpl2.setRefreshToken(newRefreshToken);
            accessTokenImpl3.setRefreshToken(newRefreshToken);
            accessTokenImpl4.setRefreshToken(newRefreshToken);

            assertEquals("Expected and actual values should be the same.", newRefreshToken, accessTokenImpl1.getRefreshToken());
            assertEquals("Expected and actual values should be the same.", newRefreshToken, accessTokenImpl2.getRefreshToken());
            assertEquals("Expected and actual values should be the same.", newRefreshToken, accessTokenImpl3.getRefreshToken());
            assertEquals("Expected and actual values should be the same.", newRefreshToken, accessTokenImpl4.getRefreshToken());
        }
    }

    @Test
    public void setAndGetRefreshExpiresOnTest() {
        Date[] newRefreshExpiresOnDates = {null, new Date()};
        for (Date newRefreshExpiresOnDate : newRefreshExpiresOnDates) {
            accessTokenImpl1.setRefreshExpiresOn(newRefreshExpiresOnDate);
            accessTokenImpl2.setRefreshExpiresOn(newRefreshExpiresOnDate);
            accessTokenImpl3.setRefreshExpiresOn(newRefreshExpiresOnDate);
            accessTokenImpl4.setRefreshExpiresOn(newRefreshExpiresOnDate);

            assertEquals("Expected and actual values should be the same.", newRefreshExpiresOnDate, accessTokenImpl1.getRefreshExpiresOn());
            assertEquals("Expected and actual values should be the same.", newRefreshExpiresOnDate, accessTokenImpl2.getRefreshExpiresOn());
            assertEquals("Expected and actual values should be the same.", newRefreshExpiresOnDate, accessTokenImpl3.getRefreshExpiresOn());
            assertEquals("Expected and actual values should be the same.", newRefreshExpiresOnDate, accessTokenImpl4.getRefreshExpiresOn());
        }
    }

    @Test
    public void setAndGetInvalidatedOnTest() {
        Date[] newInvalidatedOnDates = {null, new Date()};
        for (Date newInvalidatedOnDate : newInvalidatedOnDates) {
            accessTokenImpl1.setInvalidatedOn(newInvalidatedOnDate);
            accessTokenImpl2.setInvalidatedOn(newInvalidatedOnDate);
            accessTokenImpl3.setInvalidatedOn(newInvalidatedOnDate);
            accessTokenImpl4.setInvalidatedOn(newInvalidatedOnDate);

            assertEquals("Expected and actual values should be the same.", newInvalidatedOnDate, accessTokenImpl1.getInvalidatedOn());
            assertEquals("Expected and actual values should be the same.", newInvalidatedOnDate, accessTokenImpl2.getInvalidatedOn());
            assertEquals("Expected and actual values should be the same.", newInvalidatedOnDate, accessTokenImpl3.getInvalidatedOn());
            assertEquals("Expected and actual values should be the same.", newInvalidatedOnDate, accessTokenImpl4.getInvalidatedOn());
        }
    }

    @Test
    public void setAndGetTrustKeyTest() {
        String[] newTrustKeys = {null, "", "!!NEW trust key-1", "#1(TRUST.key,/KEY NEW )9--99", "!$$ 1-2 newTrust/KEY/", "nEwTrUstKEY(0--trustkey)<00>"};
        for (String newTrustKey : newTrustKeys) {
            accessTokenImpl1.setTrustKey(newTrustKey);
            accessTokenImpl2.setTrustKey(newTrustKey);
            accessTokenImpl3.setTrustKey(newTrustKey);
            accessTokenImpl4.setTrustKey(newTrustKey);

            assertEquals("Expected and actual values should be the same.", newTrustKey, accessTokenImpl1.getTrustKey());
            assertEquals("Expected and actual values should be the same.", newTrustKey, accessTokenImpl2.getTrustKey());
            assertEquals("Expected and actual values should be the same.", newTrustKey, accessTokenImpl3.getTrustKey());
            assertEquals("Expected and actual values should be the same.", newTrustKey, accessTokenImpl4.getTrustKey());
        }
    }
}