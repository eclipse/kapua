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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RolePermission;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;

/**
 * {@link RolePermission} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "RolePermission")
@Table(name = "athz_role_permission")
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

    /**
     * Constructor
     *
     * @param rolePermission
     */
    public RolePermissionImpl(RolePermission rolePermission) {
        super(rolePermission);

        setId(rolePermission.getId());
        setRoleId(rolePermission.getRoleId());
        setPermission(rolePermission.getPermission());
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public RolePermissionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     *
     * @param scopeId
     * @param permission
     */
    public RolePermissionImpl(KapuaId scopeId, Permission permission) {
        this(scopeId);
        setPermission(permission);
    }

    @Override
    public void setRoleId(KapuaId roleId) {
        this.roleId = KapuaEid.parseKapuaId(roleId);
    }

    @Override
    public KapuaId getRoleId() {
        return roleId;
    }

    @Override
    public void setPermission(Permission permission) {
        PermissionImpl permissionImpl = null;
        if (permission != null) {
            permissionImpl = permission instanceof PermissionImpl ? (PermissionImpl) permission : new PermissionImpl(permission);
        }
        this.permission = permissionImpl;
    }

    @Override
    public Permission getPermission() {
        return permission != null ? permission : new PermissionImpl(null, null, null, null);
    }

    @Override
    public String toString() {
        return getPermission().toString();
    }

    @PreUpdate
    protected void preUpdateAction() {
        if (getCreatedBy() == null) {
            setCreatedBy(KapuaSecurityUtils.getSession().getUserId());
        }

        if (getCreatedOn() == null) {
            setCreatedOn(new Date());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (permission == null ? 0 : permission.hashCode());
        result = prime * result + (roleId == null ? 0 : roleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RolePermissionImpl other = (RolePermissionImpl) obj;
        if (roleId == null) {
            if (other.roleId != null) {
                return false;
            }
        } else if (!roleId.equals(other.roleId)) {
            return false;
        }

        return getPermission().equals(other.getPermission());
    }
}
