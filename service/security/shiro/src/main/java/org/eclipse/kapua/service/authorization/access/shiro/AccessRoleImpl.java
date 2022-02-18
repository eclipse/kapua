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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * {@link AccessRole} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "AccessRole")
@Table(name = "athz_access_role")
public class AccessRoleImpl extends AbstractKapuaEntity implements AccessRole {

    private static final long serialVersionUID = 8400951097610833058L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "access_info_id"))
    })
    private KapuaEid accessInfoId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "role_id"))
    })
    private KapuaEid roleId;

    /**
     * Empty constructor required by JPA.
     *
     * @since 1.0.0
     */
    protected AccessRoleImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set for this {@link AccessRole}.
     * @since 1.0.0
     */
    public AccessRoleImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param accessRole The {@link AccessRole} to clone.
     * @throws KapuaException If the given {@link AccessRole} is incompatible with the implementation-specific type.
     * @since 1.0.0
     */
    public AccessRoleImpl(AccessRole accessRole) throws KapuaException {
        super(accessRole);

        setAccessInfoId(accessRole.getAccessInfoId());
        setRoleId(accessRole.getRoleId());
    }

    @Override
    public void setAccessInfoId(KapuaId accessInfoId) {
        this.accessInfoId = KapuaEid.parseKapuaId(accessInfoId);
    }

    @Override
    public KapuaId getAccessInfoId() {
        return accessInfoId;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (accessInfoId == null ? 0 : accessInfoId.hashCode());
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
        AccessRoleImpl other = (AccessRoleImpl) obj;
        if (accessInfoId == null) {
            if (other.accessInfoId != null) {
                return false;
            }
        } else if (!accessInfoId.equals(other.accessInfoId)) {
            return false;
        }
        if (roleId == null) {
            if (other.roleId != null) {
                return false;
            }
        } else if (!roleId.equals(other.roleId)) {
            return false;
        }
        return true;
    }
}
