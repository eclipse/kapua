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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class RolePermissionQueryImplTest {

    @Test
    public void rolePermissionQueryImplWithoutParametersTest() {
        RolePermissionQueryImpl rolePermissionQueryImpl = new RolePermissionQueryImpl();
        Assert.assertNull("Null expected.", rolePermissionQueryImpl.getScopeId());
        Assert.assertNull("rolePermissionQueryImpl.sortCriteria", rolePermissionQueryImpl.getSortCriteria());
        Assert.assertNotNull("rolePermissionQueryImpl.defaultSortCriteria", rolePermissionQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void rolePermissionQueryImpScopeIdTest() {
        KapuaId[] scopeIds = {null, KapuaId.ANY};

        for (KapuaId scopeId : scopeIds) {
            RolePermissionQueryImpl rolePermissionQueryImpl = new RolePermissionQueryImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, rolePermissionQueryImpl.getScopeId());
            Assert.assertNull("rolePermissionQueryImpl.sortCriteria", rolePermissionQueryImpl.getSortCriteria());
            Assert.assertNotNull("rolePermissionQueryImpl.defaultSortCriteria", rolePermissionQueryImpl.getDefaultSortCriteria());
        }
    }
}