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

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;

@Singleton
public class AccessInfoServiceTestData {

    public AccessInfoCreator accessInfoCreator;
    public AccessInfo accessInfo;
    public AccessInfo accessInfoFound;
    public AccessInfoListResult accessList;
    public UserCreator userCreator;
    public User user;
    public Permission permission;
    public Set<Permission> permissions;
    public AccessPermissionCreator accessPermissionCreator;
    public AccessPermission accessPermission;
    public AccessPermission accessPermissionFound;
    public AccessPermissionListResult accessPermissions;
    public RoleCreator roleCreator;
    public Role role;
    public Role roleFound;
    public AccessRole accessRole;
    public AccessRole accessRoleFound;
    public AccessRoleListResult accessRoles;
    public Set<KapuaId> roleIds;
    
    public void clearData() {
        accessInfoCreator = null;
        accessInfo = null;
        accessInfoFound = null;
        accessList = null;
        userCreator = null;
        user = null;
        permission = null;
        permissions = new HashSet<>();
        accessPermissionCreator = null;
        accessPermission = null;
        accessPermissionFound = null;
        accessPermissions = null;
        roleCreator = null;
        role = null;
        roleFound = null;
        accessRole = null;
        accessRoleFound = null;
        accessRoles = null;
        roleIds = null;
    }
}
