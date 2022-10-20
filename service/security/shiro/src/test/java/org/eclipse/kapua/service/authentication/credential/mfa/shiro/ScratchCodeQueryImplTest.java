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
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ScratchCodeQueryImplTest {

    @Test
    public void scratchCodeQueryImplWithoutParameterTest() {
        ScratchCodeQueryImpl scratchCodeQueryImpl = new ScratchCodeQueryImpl();
        Assert.assertNull("Null expected.", scratchCodeQueryImpl.getScopeId());
        Assert.assertNull("scratchCodeQueryImpl.sortCriteria", scratchCodeQueryImpl.getSortCriteria());
        Assert.assertNotNull("scratchCodeQueryImpl.defaultSortCriteria", scratchCodeQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void scratchCodeQueryImplScopeIdParameterTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};
        for (KapuaId scopeId : scopeIds) {
            ScratchCodeQueryImpl scratchCodeQueryImpl = new ScratchCodeQueryImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, scratchCodeQueryImpl.getScopeId());
            Assert.assertNull("scratchCodeQueryImpl.sortCriteria", scratchCodeQueryImpl.getSortCriteria());
            Assert.assertNotNull("scratchCodeQueryImpl.defaultSortCriteria", scratchCodeQueryImpl.getDefaultSortCriteria());
        }
    }
}
