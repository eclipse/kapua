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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class MfaOptionQueryImplTest extends Assert {

    @Test
    public void mfaOptionQueryImplTest() {
        MfaOptionQueryImpl mfaOptionQueryImpl = new MfaOptionQueryImpl();
        assertNotNull("NotNull expected.", mfaOptionQueryImpl.getSortCriteria());
        assertNull("Null expected.", mfaOptionQueryImpl.getScopeId());
    }

    @Test
    public void mfaOptionQueryImplScopeIdParameterTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};

        for (KapuaId scopeId : scopeIds) {
            MfaOptionQueryImpl mfaOptionQueryImpl = new MfaOptionQueryImpl(scopeId);
            assertNotNull("NotNull expected.", mfaOptionQueryImpl.getSortCriteria());
            assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionQueryImpl.getScopeId());
        }
    }
}