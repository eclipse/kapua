/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;

/**
 * Role creator service implementation.
 *
 * @since 1.0
 *
 */
public class RolePermissionCreatorImpl extends AbstractKapuaEntityCreator<RolePermission> implements RolePermissionCreator {

    private static final long serialVersionUID = 972154225756734130L;

    private KapuaId roleId;
    private Permission permission;

    /**
     * Costructor
     *
     * @param scopeId
     */
    public RolePermissionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public KapuaId getRoleId() {
        return roleId;
    }

    public void setRoleId(KapuaId roleId) {
        this.roleId = roleId;
    }

    @SuppressWarnings("unchecked")
    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
