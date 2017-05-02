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
package org.eclipse.kapua.service.authorization.access.shiro;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;

/**
 * User permission implementation.
 *
 * @since 1.0
 *
 */
@Entity(name = "AccessPermission")
@Table(name = "athz_access_permission")
public class AccessPermissionImpl extends AbstractKapuaEntity implements AccessPermission {

    private static final long serialVersionUID = -3760818776351242930L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "access_info_id", nullable = false, updatable = false))
    })
    private KapuaEid accessInfoId;

    @Embedded
    private PermissionImpl permission;

    /**
     * Constructor
     */
    protected AccessPermissionImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public AccessPermissionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     *
     * @param accessPermission
     */
    public AccessPermissionImpl(AccessPermission accessPermission) {
        super(accessPermission);

        setAccessInfoId(accessPermission.getAccessInfoId());
        setPermission(accessPermission.getPermission());
    }

    @Override
    public void setAccessInfoId(KapuaId accessInfo) {
        accessInfoId = accessInfo != null ? accessInfo instanceof KapuaEid ? (KapuaEid) accessInfo : new KapuaEid(accessInfo) : null;
    }

    @Override
    public KapuaId getAccessInfoId() {
        return accessInfoId;
    }

    @Override
    public void setPermission(Permission permission) {
        this.permission = permission != null ? permission instanceof PermissionImpl ? (PermissionImpl) permission : new PermissionImpl(permission) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Permission getPermission() {
        return permission != null ? permission : new PermissionImpl(null, null, null, null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (accessInfoId == null ? 0 : accessInfoId.hashCode());
        result = prime * result + (permission == null ? 0 : permission.hashCode());
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
        AccessPermissionImpl other = (AccessPermissionImpl) obj;
        if (accessInfoId == null) {
            if (other.accessInfoId != null) {
                return false;
            }
        } else if (!accessInfoId.equals(other.accessInfoId)) {
            return false;
        }
        if (getPermission() == null) {
            if (other.getPermission() != null) {
                return false;
            }
        } else if (!getPermission().equals(other.getPermission())) {
            return false;
        }
        return true;
    }
}
