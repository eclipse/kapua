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

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.permission.Permission;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * {@link Permission} implementation.
 *
 * @since 1.0.0
 */
@Embeddable
public class PermissionImpl extends WildcardPermission implements Permission, org.apache.shiro.authz.Permission, Serializable {

    private static final long serialVersionUID = 1480557438886065675L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);

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

    @Basic
    @Column(name = "forwardable", nullable = false, updatable = false)
    private boolean forwardable;

    /**
     * Constructor
     */
    protected PermissionImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param permission The {@link Permission} to parse.
     * @since 1.0.0
     */
    public PermissionImpl(Permission permission) {
        this(
                permission.getDomain(),
                permission.getAction(),
                permission.getTargetScopeId(),
                permission.getGroupId(),
                permission.getForwardable());
    }

    /**
     * Constructor.
     *
     * @param domain
     * @param action
     * @param targetScopeId
     * @param groupId
     * @since 1.0.0
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {
        this(domain, action, targetScopeId, groupId, false);
    }

    /**
     * Constructor.
     *
     * @param domain
     * @param action
     * @param targetScopeId
     * @param groupId
     * @since 1.0.0
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable) {

        setDomain(domain);
        setAction(action);
        setTargetScopeId(targetScopeId);
        setGroupId(groupId);
        setForwardable(forwardable);

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
        this.targetScopeId = KapuaEid.parseKapuaId(targetScopeId);
    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = KapuaEid.parseKapuaId(groupId);
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public boolean getForwardable() {
        return forwardable;
    }

    @Override
    public void setForwardable(boolean forwardable) {
        this.forwardable = forwardable;
    }

    /**
     * This methods needs to be overridden to support Access {@link Group} feature.<br>
     * <p>
     * <p>
     * {@link KapuaEntityService}s that access a specific {@link KapuaEntity} (i.e. {@link KapuaEntityService#create(KapuaEntityCreator)}, {@link KapuaEntityService#delete(KapuaId, KapuaId)})
     * can make the control taking in consideration of the {@link Group#getId()} parameter as it is known.<br>
     * <p>
     * Instead, methods that access multiple {@link KapuaEntity}s (i.e. {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)})
     * cannot make a direct control of the {@link Group#getId()} parameter as it is not known and they can be a lot.<br>
     * The access control then, is performed by hiding the data that a {@link Subject} cannot see instead of throwing {@link UnauthorizedException}.
     * </p>
     * <p>
     * <p>
     * The access control for {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)}) must specify that {@link Group#ANY} group assigned to the permission is
     * enough to pass the {@link AuthorizationService#checkPermission(Permission)}.
     * </p>
     * <p>
     * <p>
     * In case of the {@link Permission#getForwardable()} equals to {@code true}, more lookup is required.<br>
     * If a parent account access the resources of one of its child accounts it won't have the direct permission to access it.
     * A lookup of {@link Account#getParentAccountPath()} will be required to search if the current user scope id is
     * one of the parent of the given {@link Permission#getTargetScopeId()}
     * </p>
     *
     * @since 1.0.0
     */
    @Override
    public boolean implies(org.apache.shiro.authz.Permission p) {

        Permission permission = (Permission) p;

        if (KapuaId.ANY.equals(permission.getTargetScopeId())) {
            setTargetScopeId(null);
        }

        if (Group.ANY.equals(permission.getGroupId())) {
            setGroupId(null);
        }

        setParts(toString());

        boolean implies = super.implies(p);

        if (!implies && permission.getTargetScopeId() != null && getForwardable()) {
            implies = forwardPermission(p);
        }

        return implies;
    }

    /**
     * Checks {@code this} Permission against the given {@link Permission} parameter.<br>
     * <p>
     * <p>
     * It tries to forward {@code this} Permission to the {@link #getTargetScopeId()} of the given {@link org.apache.shiro.authz.Permission} parameter.<br>
     * This means that if the required permission has scope id 'B' and {@code this} {@link Permission} has scope id 'A',
     * this methods search the {@link Account#getParentAccountPath()} of the scope id 'B' and checks the {@link Permission} forwarding {@code this} Permission
     * to the same level of the given {@link org.apache.shiro.authz.Permission}.
     * </p>
     * <p>
     * <p>
     * <b>Example:</b>
     * User 'A' in account 'A' has scopeId 'A' and this permission (A) "*:*:A:*".<br>
     * Account 'A' has a child account 'B', then 'B' has this parent account path: '/A/B';<br>
     * <br>
     * User 'A' tries to access a resource of account 'B' an the direct check {@link org.apache.shiro.authz.Permission#implies(org.apache.shiro.authz.Permission)} fails.
     * So this method searches the parent account path of account 'B', found that 'A' is a parent of 'B'
     * so then {@code this} {@link Permission} is checked again with 'B' as scopeId.
     * </p>
     *
     * @param p The permission to check against.
     * @return {@code true} if this permission is forward-able and is valid when forwarded, {@code false otherwise}
     * @since 1.0.0
     */
    private boolean forwardPermission(org.apache.shiro.authz.Permission p) {
        Permission permission = (Permission) p;

        try {
            Account account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(permission.getTargetScopeId()));

            if (account != null && account.getScopeId() != null) {
                String parentAccountPath = account.getParentAccountPath();

                // If it doesn't contain the scope id in the parent, don't even try to check against
                if (parentAccountPath.contains("/" + getTargetScopeId().toStringId() + "/")) {
                    setTargetScopeId(permission.getTargetScopeId());
                    setParts(toString());

                    return super.implies(p);
                }
            }
        } catch (KapuaException e) {
            throw KapuaRuntimeException.internalError(e, "Error while forwarding permission: " + p.toString());
        }

        return false;
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
        int prime = 31;
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
