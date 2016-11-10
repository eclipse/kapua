/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.role.shiro;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RolePermission;

@Entity(name = "RolePermission")
@Table(name = "athz_role_permission")
/**
 * Role permission implementation.
 * 
 * @since 1.0
 *
 */
public class RolePermissionImpl extends AbstractKapuaEntity implements RolePermission {

    private static final long serialVersionUID = -4107313856966377197L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "role_id"))
    })
    private KapuaEid roleId;

    @Embedded
    private PermissionImpl permission;

    protected RolePermissionImpl() {
        super();
    }

    public RolePermissionImpl(RolePermission rolePermission) {
        super(rolePermission.getScopeId());

        setId(rolePermission.getId());
        setRoleId(rolePermission.getRoleId());
        setPermission(rolePermission.getPermission());
    }

    /**
     * Constructor
     * 
     * @param scopeId
     * @param domain
     * @param action
     * @param targetScopeId
     */
    public RolePermissionImpl(KapuaId scopeId, String domain, Actions action, KapuaId targetScopeId) {
        super(scopeId);
        setPermission(new PermissionImpl(domain, action, targetScopeId));
    }

    @Override
    public void setRoleId(KapuaId roleId) {
        if (roleId != null) {
            this.roleId = new KapuaEid(roleId.getId());
        }
    }

    @Override
    public KapuaId getRoleId() {
        return roleId;
    }

    @Override
    public void setPermission(Permission permission) {
        this.permission = new PermissionImpl(permission.getDomain(),
                permission.getAction(),
                permission.getTargetScopeId());
    }

    @Override
    public Permission getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return permission.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((permission == null) ? 0 : permission.hashCode());
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RolePermissionImpl other = (RolePermissionImpl) obj;
        if (permission == null) {
            if (other.permission != null)
                return false;
        } else if (!permission.equals(other.permission))
            return false;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        return true;
    }
}
