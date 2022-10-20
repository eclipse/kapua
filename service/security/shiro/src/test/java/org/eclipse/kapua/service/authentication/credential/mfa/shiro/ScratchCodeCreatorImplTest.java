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
public class ScratchCodeCreatorImplTest {

    KapuaId[] scopeIds, mfaOptionIds;
    String[] codes;

    @Before
    public void initialize() {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        mfaOptionIds = new KapuaId[]{null, KapuaId.ONE};
        codes = new String[]{null, "", "!!code-1", "#1(code-a.,/codeey)9--99", "!$$ 1-2 code//", "code(....)<00>", "co..,,000de@"};
    }

    @Test
    public void scratchCodeCreatorImplScopeIdMfaOptionIdCodeParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId mfaOptionId : mfaOptionIds) {
                for (String code : codes) {
                    ScratchCodeCreatorImpl scratchCodeCreatorImpl = new ScratchCodeCreatorImpl(scopeId, mfaOptionId, code);
                    Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeCreatorImpl.getScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", mfaOptionId, scratchCodeCreatorImpl.getMfaOptionId());
                    Assert.assertEquals("Expected and actual values should be the same.", code, scratchCodeCreatorImpl.getCode());
                }
            }
        }
    }

    @Test
    public void scratchCodeCreatorImplScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            ScratchCodeCreatorImpl scratchCodeCreatorImpl = new ScratchCodeCreatorImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeCreatorImpl.getScopeId());
            Assert.assertNull("Null expected.", scratchCodeCreatorImpl.getMfaOptionId());
            Assert.assertNull("Null expected.", scratchCodeCreatorImpl.getCode());
        }
    }

    @Test
    public void setAndGetMfaOptionIdTest() {
        KapuaId[] newMfaOptionIds = {null, KapuaId.ANY};

        ScratchCodeCreatorImpl scratchCodeCreatorImpl1 = new ScratchCodeCreatorImpl(KapuaId.ONE);
        for (KapuaId newMfaOptionId : newMfaOptionIds) {
            scratchCodeCreatorImpl1.setMfaOptionId(newMfaOptionId);
            Assert.assertEquals("Expected and actual values should be the same.", newMfaOptionId, scratchCodeCreatorImpl1.getMfaOptionId());
        }

        ScratchCodeCreatorImpl scratchCodeCreatorImpl2 = new ScratchCodeCreatorImpl(KapuaId.ONE, KapuaId.ANY, "code");
        for (KapuaId newMfaOptionId : newMfaOptionIds) {
            scratchCodeCreatorImpl2.setMfaOptionId(newMfaOptionId);
            Assert.assertEquals("Expected and actual values should be the same.", newMfaOptionId, scratchCodeCreatorImpl2.getMfaOptionId());
        }
    }

    @Test
    public void setAndGetCodeTest() {
        String[] newCodes = {null, "", "new...!!code-1", "#1(new-a.,/codeey)9--99", "NEW!$$ 1-2 code//", "code(....)newCODE<00>", "co..,,000de@"};

        ScratchCodeCreatorImpl scratchCodeCreatorImpl1 = new ScratchCodeCreatorImpl(KapuaId.ONE);
        for (String newCode : newCodes) {
            scratchCodeCreatorImpl1.setCode(newCode);
            Assert.assertEquals("Expected and actual values should be the same.", newCode, scratchCodeCreatorImpl1.getCode());
        }

        ScratchCodeCreatorImpl scratchCodeCreatorImpl2 = new ScratchCodeCreatorImpl(KapuaId.ONE, KapuaId.ANY, "code");
        for (String newCode : newCodes) {
            scratchCodeCreatorImpl2.setCode(newCode);
            Assert.assertEquals("Expected and actual values should be the same.", newCode, scratchCodeCreatorImpl2.getCode());
        }
    }
}