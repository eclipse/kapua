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
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class RolePermissionCreatorImplTest {

    @Test
    public void rolePermissionCreatorImplTest() {
        KapuaId[] scopeIds = {null, KapuaId.ANY};
        for (KapuaId scopeId : scopeIds) {
            RolePermissionCreatorImpl rolePermissionCreatorImpl = new RolePermissionCreatorImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, rolePermissionCreatorImpl.getScopeId());
            Assert.assertNull("Null expected.", rolePermissionCreatorImpl.getRoleId());
            Assert.assertNull("Null expected.", rolePermissionCreatorImpl.getPermission());
        }
    }

    @Test
    public void setAndGetRoleIdTest() {
        KapuaId[] roleIds = {null, KapuaId.ONE};
        RolePermissionCreatorImpl rolePermissionCreatorImpl = new RolePermissionCreatorImpl(KapuaId.ONE);

        for (KapuaId roleId : roleIds) {
            rolePermissionCreatorImpl.setRoleId(roleId);
            Assert.assertEquals("Expected and actual values should be the same.", roleId, rolePermissionCreatorImpl.getRoleId());
        }
    }

    @Test
    public void setAndGetPermissionTest() {
        Permission[] permissions = {null, Mockito.mock(Permission.class)};
        RolePermissionCreatorImpl rolePermissionCreatorImpl = new RolePermissionCreatorImpl(KapuaId.ONE);

        for (Permission permission : permissions) {
            rolePermissionCreatorImpl.setPermission(permission);
            Assert.assertEquals("Expected and actual values should be the same.", permission, rolePermissionCreatorImpl.getPermission());
        }
    }
}