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

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class MfaOptionFactoryImplTest extends Assert {

    MfaOptionFactoryImpl mfaOptionFactoryImpl;
    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] mfaSecretKeys;
    MfaOption mfaOption;
    Date trustExpirationDate;
    Date modifiedOn;

    @Before
    public void initialize() {
        mfaOptionFactoryImpl = new MfaOptionFactoryImpl();
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid()};
        mfaSecretKeys = new String[]{null, "", "!!mfaSecretKey-1", "#1(newMfa.,/SecretKey)9--99", "!$$ 1-2 key//", "mfa_key(....)<00>"};
        mfaOption = Mockito.mock(MfaOption.class);
        trustExpirationDate = new Date();
        modifiedOn = new Date();
    }

    @Test
    public void newCreatorScopeIdUserIdMfaSecretKeyParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid userId : userIds) {
                for (String mfaSecretKey : mfaSecretKeys) {
                    MfaOptionCreatorImpl mfaOptionCreatorImpl = mfaOptionFactoryImpl.newCreator(scopeId, userId, mfaSecretKey);
                    assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionCreatorImpl.getScopeId());
                    assertEquals("Expected and actual values should be the same.", userId, mfaOptionCreatorImpl.getUserId());
                    assertEquals("Expected and actual values should be the same.", mfaSecretKey, mfaOptionCreatorImpl.getMfaSecretKey());
                }
            }
        }
    }

    @Test
    public void newListResultTest() {
        assertTrue("Instance of MfaOptionListResult expected.", mfaOptionFactoryImpl.newListResult() instanceof MfaOptionListResult);
    }

    @Test
    public void newEntityTest() {
        for (KapuaId scopeId : scopeIds) {
            MfaOption mfaOption = mfaOptionFactoryImpl.newEntity(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, mfaOption.getScopeId());
        }
    }

    @Test
    public void newMfaOptionTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId userId : userIds) {
                for (String mfaSecretKey : mfaSecretKeys) {
                    MfaOption mfaOption = mfaOptionFactoryImpl.newMfaOption(scopeId, userId, mfaSecretKey);
                    assertEquals("Expected and actual values should be the same.", scopeId, mfaOption.getScopeId());
                    assertEquals("Expected and actual values should be the same.", userId, mfaOption.getUserId());
                    assertEquals("Expected and actual values should be the same.", mfaSecretKey, mfaOption.getMfaSecretKey());
                }
            }
        }
    }

    @Test
    public void newQueryTest() {
        for (KapuaId scopeId : scopeIds) {
            MfaOptionQuery mfaOptionQuery = mfaOptionFactoryImpl.newQuery(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionQuery.getScopeId());
        }
    }

    @Test
    public void newCreatorScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            MfaOptionCreator mfaOptionCreator = mfaOptionFactoryImpl.newCreator(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionCreator.getScopeId());
        }
    }

    @Test
    public void cloneTest() {
        Mockito.when(mfaOption.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOption.getTrustKey()).thenReturn("thrust key");
        Mockito.when(mfaOption.getTrustExpirationDate()).thenReturn(trustExpirationDate);
        Mockito.when(mfaOption.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(mfaOption.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getOptlock()).thenReturn(10);

        MfaOption mfaOptionResult = mfaOptionFactoryImpl.clone(mfaOption);

        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionResult.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionResult.getUserId());
        assertEquals("Expected and actual values should be the same.", "mfa secret key", mfaOptionResult.getMfaSecretKey());
        assertEquals("Expected and actual values should be the same.", "thrust key", mfaOptionResult.getTrustKey());
        assertEquals("Expected and actual values should be the same.", trustExpirationDate, mfaOptionResult.getTrustExpirationDate());
        assertEquals("Expected and actual values should be the same.", modifiedOn, mfaOptionResult.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionResult.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", 10, mfaOptionResult.getOptlock());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        mfaOptionFactoryImpl.clone(null);
    }
}