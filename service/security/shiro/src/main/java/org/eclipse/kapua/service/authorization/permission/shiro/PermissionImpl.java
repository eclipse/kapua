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
package org.eclipse.kapua.service.authorization.permission.shiro;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link Permission} implementation.
 *
 * @since 1.0.0
 */
@Embeddable
public class PermissionImpl extends WildcardPermission implements Permission, org.apache.shiro.authz.Permission, Serializable {

    private static final long serialVersionUID = 1480557438886065675L;

    @Basic
    @Column(name = "domain", nullable = true, updatable = false)
    private String domain;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true, updatable = false)
    private Actions action;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "target_scope_id", nullable = true, updatable = false))
    })
    private KapuaEid targetScopeId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "group_id", nullable = true, updatable = false))
    })
    private KapuaEid groupId;

    /**
     * Constructor
     */
    protected PermissionImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param permission
     *            The {@link Permission} to parse.
     * @since 1.0.0
     */
    public PermissionImpl(Permission permission) {
        this(
                permission.getDomain(),
                permission.getAction(),
                permission.getTargetScopeId(),
                permission.getGroupId());
    }

    /**
     * Constructor.
     *
     * @param domain
     * @param action
     * @param targetScopeId
     * @param groupId
     *
     * @since 1.0.0
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {

        setDomain(domain);
        setAction(action);
        setTargetScopeId(targetScopeId);
        setGroupId(groupId);

        setParts(toString());
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setAction(Actions action) {
        this.action = action;
    }

    @Override
    public Actions getAction() {
        return action;
    }

    @Override
    public void setTargetScopeId(KapuaId targetScopeId) {
        this.targetScopeId = targetScopeId != null ? targetScopeId instanceof KapuaEid ? (KapuaEid) targetScopeId : new KapuaEid(targetScopeId) : null;
    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = groupId != null ? groupId instanceof KapuaEid ? (KapuaEid) groupId : new KapuaEid(groupId) : null;
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    /**
     * This methods needs to be overridden to support Access {@link Group} feature.<br>
     *
     * {@link KapuaEntityService}s that access a specific {@link KapuaEntity} (i.e. {@link KapuaEntityService#create(KapuaEntityCreator)}, {@link KapuaEntityService#delete(KapuaId, KapuaId)})
     * can make the control taking in consideration of the {@link Group#getId()} parameter as it is known.<br>
     *
     * Instead, methods that access multiple {@link KapuaEntity}s (i.e. {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)})
     * cannot make a direct control of the {@link Group#getId()} parameter as it is not known and they can be a lot.<br>
     * The access control then, is performed by hiding the data that a {@link Subject} cannot see instead of throwing {@link UnauthorizedException}.
     *
     * The access control for {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)}) must specify that {@link Group#ANY} group assigned to the permission is
     * enough to pass the {@link AuthorizationService#checkPermission(Permission)}.
     *
     */
    @Override
    public boolean implies(org.apache.shiro.authz.Permission p) {

        Permission permission = (Permission) p;

        if (Group.ANY.equals(permission.getGroupId())) {
            setGroupId(null);
        }

        setParts(toString());
        return super.implies(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(domain != null ? domain : Permission.WILDCARD) //
                .append(Permission.SEPARATOR) //
                .append(action != null ? action.name() : Permission.WILDCARD) //
                .append(Permission.SEPARATOR) //
                .append(targetScopeId != null ? targetScopeId.getId() : Permission.WILDCARD) //
                .append(Permission.SEPARATOR) //
                .append(groupId != null ? groupId.getId() : Permission.WILDCARD);

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (action == null ? 0 : action.hashCode());
        result = prime * result + (domain == null ? 0 : domain.hashCode());
        result = prime * result + (targetScopeId == null ? 0 : targetScopeId.hashCode());
        result = prime * result + (groupId == null ? 0 : groupId.hashCode());
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
        PermissionImpl other = (PermissionImpl) obj;
        if (action != other.action) {
            return false;
        }
        if (domain == null) {
            if (other.domain != null) {
                return false;
            }
        } else if (!domain.equals(other.domain)) {
            return false;
        }
        if (targetScopeId == null) {
            if (other.targetScopeId != null) {
                return false;
            }
        } else if (!targetScopeId.equals(other.targetScopeId)) {
            return false;
        }
        if (groupId == null) {
            if (other.groupId != null) {
                return false;
            }
        } else if (!groupId.equals(other.groupId)) {
            return false;
        }
        return true;
    }
}
