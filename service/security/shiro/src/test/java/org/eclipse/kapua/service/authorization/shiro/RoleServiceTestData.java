/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;

@Singleton
public class RoleServiceTestData {

    Role role;
    Role roleFound;
    RoleListResult roleList;
    Set<Permission> permissions;
    RolePermission rolePermission;
    RolePermission rolePermissionFound;
    RolePermissionListResult permissionList;

    public void clearData() {
        role = null;
        roleFound = null;
        roleList = null;
        permissions = null;
        rolePermission = null;
        rolePermissionFound = null;
        permissionList = null;
    }
}
