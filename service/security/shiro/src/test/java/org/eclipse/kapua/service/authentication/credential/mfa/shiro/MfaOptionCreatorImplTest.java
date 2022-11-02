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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MfaOptionCreatorImplTest {

    KapuaId[] scopeIds;
    KapuaId[] userIds;
    KapuaId[] newUserIds;
    String[] mfaSecretKeys;
    String[] newMfaSecretKeys;
    MfaOptionCreatorImpl mfaOptionCreatorImpl1;
    MfaOptionCreatorImpl mfaOptionCreatorImpl2;

    @Before
    public void initialize() {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaId[]{null, KapuaId.ONE};
        newUserIds = new KapuaId[]{null, KapuaId.ANY};
        mfaSecretKeys = new String[]{null, "", "!!mfaSecretKey-1", "#1(newMfa.,/SecretKey)9--99", "!$$ 1-2 key//", "mfa_key(....)<00>"};
        newMfaSecretKeys = new String[]{null, "", "new_mfaSecret,Key-1", "#1(new...MfaSecretKey)999", "!$$ 1-2 key", "mfa_key<00>"};
        mfaOptionCreatorImpl1 = new MfaOptionCreatorImpl(KapuaId.ONE, KapuaId.ONE, "mfaSecretKey");
        mfaOptionCreatorImpl2 = new MfaOptionCreatorImpl(KapuaId.ONE);
    }

    @Test
    public void mfaOptionCreatorImplScopeIdUserIdSecretKeyParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId userId : userIds) {
                for (String mfaSecretKey : mfaSecretKeys) {
                    MfaOptionCreatorImpl mfaOptionCreatorImpl = new MfaOptionCreatorImpl(scopeId, userId, mfaSecretKey);
                    Assert.assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionCreatorImpl.getScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", userId, mfaOptionCreatorImpl.getUserId());
                    Assert.assertEquals("Expected and actual values should be the same.", mfaSecretKey, mfaOptionCreatorImpl.getMfaSecretKey());
                }
            }
        }
    }

    @Test
    public void mfaOptionCreatorImplScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            MfaOptionCreatorImpl mfaOptionCreatorImpl = new MfaOptionCreatorImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionCreatorImpl.getScopeId());
            Assert.assertNull("Null expected.", mfaOptionCreatorImpl.getUserId());
            Assert.assertNull("Null expected.", mfaOptionCreatorImpl.getMfaSecretKey());
        }
    }

    @Test
    public void setAndGetUserIdTest() {
        for (KapuaId newUserId : newUserIds) {
            mfaOptionCreatorImpl1.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionCreatorImpl1.getUserId());

            mfaOptionCreatorImpl2.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionCreatorImpl2.getUserId());
        }
    }

    @Test
    public void setAndGetMfaSecretKeyTest() {
        for (String newMfaSecretKey : newMfaSecretKeys) {
            mfaOptionCreatorImpl1.setMfaSecretKey(newMfaSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newMfaSecretKey, mfaOptionCreatorImpl1.getMfaSecretKey());

            mfaOptionCreatorImpl2.setMfaSecretKey(newMfaSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newMfaSecretKey, mfaOptionCreatorImpl2.getMfaSecretKey());
        }
    }
}