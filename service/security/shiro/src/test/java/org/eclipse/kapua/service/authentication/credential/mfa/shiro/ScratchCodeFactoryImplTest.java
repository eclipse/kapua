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

import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;


@Category(JUnitTests.class)
public class ScratchCodeFactoryImplTest {

    ScratchCodeFactoryImpl scratchCodeFactoryImpl;
    KapuaId[] scopeIds;
    KapuaEid[] mfaOptionIds;
    String[] codes;
    ScratchCode scratchCode;
    Date modifiedOn;

    @Before
    public void initialize() {
        scratchCodeFactoryImpl = new ScratchCodeFactoryImpl();
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        mfaOptionIds = new KapuaEid[]{null, new KapuaEid()};
        codes = new String[]{null, "", "!!code-1", "#1(newMfa.,/code code)9--99", "!$$ 1-2 co de//", "co_de secret--(....)<00>"};
        scratchCode = Mockito.mock(ScratchCode.class);
        modifiedOn = new Date();
    }


    @Test
    public void newCreatorScopeIdMfaOptionIdCodeParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid mfaOptionId : mfaOptionIds) {
                for (String code : codes) {
                    ScratchCodeCreatorImpl scratchCodeCreatorImpl = scratchCodeFactoryImpl.newCreator(scopeId, mfaOptionId, code);
                    Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeCreatorImpl.getScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", mfaOptionId, scratchCodeCreatorImpl.getMfaOptionId());
                    Assert.assertEquals("Expected and actual values should be the same.", code, scratchCodeCreatorImpl.getCode());
                }
            }
        }
    }

    @Test
    public void newListResultTest() {
        Assert.assertTrue("True expected.", scratchCodeFactoryImpl.newListResult() instanceof ScratchCodeListResult);
    }

    @Test
    public void newEntityTest() {
        for (KapuaId scopeId : scopeIds) {
            ScratchCode scratchCode = scratchCodeFactoryImpl.newEntity(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCode.getScopeId());
        }
    }

    @Test
    public void newScratchCodeTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId mfaOptionId : mfaOptionIds) {
                for (String code : codes) {
                    ScratchCode scratchCode = scratchCodeFactoryImpl.newScratchCode(scopeId, mfaOptionId, code);
                    Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCode.getScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", mfaOptionId, scratchCode.getMfaOptionId());
                    Assert.assertEquals("Expected and actual values should be the same.", code, scratchCode.getCode());
                }
            }
        }
    }

    @Test
    public void newQueryTest() {
        for (KapuaId scopeId : scopeIds) {
            ScratchCodeQuery scratchCodeQuery = scratchCodeFactoryImpl.newQuery(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeQuery.getScopeId());
        }
    }

    @Test
    public void newCreatorScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            ScratchCodeCreator scratchCodeCreator = scratchCodeFactoryImpl.newCreator(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeCreator.getScopeId());
        }
    }

    @Test
    public void cloneTest() {
        Mockito.when(scratchCode.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(scratchCode.getCode()).thenReturn("code");
        Mockito.when(scratchCode.getMfaOptionId()).thenReturn(KapuaId.ONE);
        Mockito.when(scratchCode.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(scratchCode.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(scratchCode.getOptlock()).thenReturn(10);

        ScratchCode scratchCodeResult = scratchCodeFactoryImpl.clone(scratchCode);

        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, scratchCodeResult.getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", "code", scratchCodeResult.getCode());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, scratchCodeResult.getMfaOptionId());
        Assert.assertEquals("Expected and actual values should be the same.", modifiedOn, scratchCodeResult.getModifiedOn());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, scratchCodeResult.getModifiedBy());
        Assert.assertEquals("Expected and actual values should be the same.", 10, scratchCodeResult.getOptlock());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        scratchCodeFactoryImpl.clone(null);
    }
}