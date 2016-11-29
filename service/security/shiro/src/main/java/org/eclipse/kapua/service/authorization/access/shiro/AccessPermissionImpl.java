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
    private KapuaEid accessId;

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
        super((AbstractKapuaEntity) accessPermission);

        setAccessId(accessPermission.getAccessId());
        setPermission(accessPermission.getPermission());
    }

    @Override
    public void setAccessId(KapuaId userId) {
        if (userId != null) {
            this.accessId = new KapuaEid(userId);
        } else {
            this.accessId = null;
        }
    }

    @Override
    public KapuaId getAccessId() {
        return accessId;
    }

    @Override
    public void setPermission(Permission permission) {
        if (permission != null) {
            this.permission = new PermissionImpl(permission);
        } else {
            this.permission = null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Permission getPermission() {
        return permission;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessId == null) ? 0 : accessId.hashCode());
        result = prime * result + ((permission == null) ? 0 : permission.hashCode());
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
        AccessPermissionImpl other = (AccessPermissionImpl) obj;
        if (accessId == null) {
            if (other.accessId != null)
                return false;
        } else if (!accessId.equals(other.accessId))
            return false;
        if (permission == null) {
            if (other.permission != null)
                return false;
        } else if (!permission.equals(other.permission))
            return false;
        return true;
    }
}
