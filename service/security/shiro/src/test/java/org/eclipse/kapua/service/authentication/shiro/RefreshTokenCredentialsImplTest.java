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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class RefreshTokenCredentialsImplTest {

    String[] idsToken, refreshTokens, newIdsToken, newRefreshTokens;
    RefreshTokenCredentialsImpl refreshTokenCredentialsImpl;

    @Before
    public void initialize() {
        idsToken = new String[]{null, "", "   ID tokenID 747.,.,,,82(*&%<> ", "   token((11@-", "id)__.,TOKen65", "TOKENid543$#%&t   oken", "to-ken_id++=,", "id,,,,id3$^&"};
        refreshTokens = new String[]{null, "", "   refresh tokenREFRESH 747.,.,,,82(*&%<> ", "   token((11@-", "REFresh)__.,TOKen65", "TOKENrefresh543$#%&t   oken", "to-ken_++rE=fresh,", "refresh,,,,id3$^&"};
        newIdsToken = new String[]{null, "", "NEW tokenID0000@!!,,,#", "!@#00tokenID new.", " new id TOK --EN-44<>", "pA_ss0###woE**9()", "    tokenID new tokenID  12344*&^%"};
        newRefreshTokens = new String[]{null, "", "new_refresh1122TOKEN#$%", "   JWT)(..,,new", "NEW_token .refresh/??)_)*", "<> 1111      ", "jwttt&^$$##Z||'", "%%%KEY NEW-TOKEN1r-e5f&resh"};
    }

    @Test
    public void refreshTokenCredentialsImplTest() {
        for (String idToken : idsToken) {
            for (String refreshToken : refreshTokens) {
                refreshTokenCredentialsImpl = new RefreshTokenCredentialsImpl(idToken, refreshToken);
                Assert.assertEquals("Expected and actual values should be the same.", idToken, refreshTokenCredentialsImpl.getTokenId());
                Assert.assertEquals("Expected and actual values should be the same.", refreshToken, refreshTokenCredentialsImpl.getRefreshToken());
            }
        }
    }

    @Test
    public void setAndGetTokenIdTest() {
        refreshTokenCredentialsImpl = new RefreshTokenCredentialsImpl("token id", "refresh token");
        for (String newIdToken : newIdsToken) {
            refreshTokenCredentialsImpl.setTokenId(newIdToken);
            Assert.assertEquals("Expected and actual values should be the same.", newIdToken, refreshTokenCredentialsImpl.getTokenId());
        }
    }

    @Test
    public void setAndGetRefreshTokenTest() {
        refreshTokenCredentialsImpl = new RefreshTokenCredentialsImpl("token id", "refresh token");
        for (String newRefreshToken : newRefreshTokens) {
            refreshTokenCredentialsImpl.setRefreshToken(newRefreshToken);
            Assert.assertEquals("Expected and actual values should be the same.", newRefreshToken, refreshTokenCredentialsImpl.getRefreshToken());
        }
    }
}