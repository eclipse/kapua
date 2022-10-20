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
public class RoleQueryImplTest {

    @Test
    public void rolePermissionQueryImplWithoutParametersTest() {
        RoleQueryImpl roleQueryImpl = new RoleQueryImpl();
        Assert.assertNull("Null expected.", roleQueryImpl.getScopeId());
        Assert.assertNull("roleQueryImpl.sortCriteria", roleQueryImpl.getSortCriteria());
        Assert.assertNotNull("roleQueryImpl.defaultSortCriteria", roleQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void rolePermissionQueryImpScopeIdTest() {
        KapuaId[] scopeIds = {null, KapuaId.ANY};

        for (KapuaId scopeId : scopeIds) {
            RoleQueryImpl roleQueryImpl = new RoleQueryImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, roleQueryImpl.getScopeId());
            Assert.assertNull("roleQueryImpl.sortCriteria", roleQueryImpl.getSortCriteria());
            Assert.assertNotNull("roleQueryImpl.defaultSortCriteria", roleQueryImpl.getDefaultSortCriteria());
        }
    }
}