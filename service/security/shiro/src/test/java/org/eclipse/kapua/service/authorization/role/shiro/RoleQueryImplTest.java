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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class RoleQueryImplTest extends Assert {

    @Test
    public void rolePermissionQueryImplWithoutParametersTest() {
        RoleQueryImpl roleQueryImpl = new RoleQueryImpl();
        assertNull("Null expected.", roleQueryImpl.getScopeId());
        assertNotNull("NotNull expected.", roleQueryImpl.getSortCriteria());
    }

    @Test
    public void rolePermissionQueryImpScopeIdTest() {
        KapuaId[] scopeIds = {null, KapuaId.ANY};

        for (KapuaId scopeId : scopeIds) {
            RoleQueryImpl roleQueryImpl = new RoleQueryImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, roleQueryImpl.getScopeId());
            assertNotNull("NotNull expected.", roleQueryImpl.getSortCriteria());
        }
    }
}