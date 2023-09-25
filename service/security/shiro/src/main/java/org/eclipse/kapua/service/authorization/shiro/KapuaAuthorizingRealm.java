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
package org.eclipse.kapua.service.authorization.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
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
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JPA-based application's one and only configured Apache Shiro Realm.
 */
public class KapuaAuthorizingRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(KapuaAuthorizingRealm.class);

    public static final String REALM_NAME = "kapuaAuthorizingRealm";

    public KapuaAuthorizingRealm() throws KapuaException {
        setName(REALM_NAME);
    }

    /**
     * Authorization.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
            throws AuthenticationException {
        // Extract principal
        String username = ((User) principals.getPrimaryPrincipal()).getName();
        logger.debug("Getting authorization info for: {}", username);
        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();

        UserService userService = locator.getService(UserService.class);
        AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(username));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new ShiroException("Error while find user!", e);
        }
        // Check existence
        if (user == null) {
            SecurityUtils.getSubject().logout();

            throw new AuthenticationException();
        }
        // Get user access infos
        AccessInfoQuery accessInfoQuery = accessInfoFactory.newQuery(user.getScopeId());
        accessInfoQuery.setPredicate(accessInfoQuery.attributePredicate(AccessInfoAttributes.USER_ID, user.getId()));

        final KapuaListResult<AccessInfo> accessInfos;
        try {
            accessInfos = KapuaSecurityUtils.doPrivileged(() -> accessInfoService.query(accessInfoQuery));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new ShiroException("Error while find access info!", e);
        }
        // Check existence
        if (accessInfos == null) {
            throw new UnknownAccountException();
        }
        // Create SimpleAuthorizationInfo with principals permissions
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // Get user roles set and related permissions
        for (AccessInfo accessInfo : accessInfos.getItems()) {

            // Access Permissions
            AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
            AccessPermissionListResult accessPermissions;
            try {
                accessPermissions = KapuaSecurityUtils.doPrivileged(() -> accessPermissionService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));
            } catch (AuthenticationException e) {
                throw e;
            } catch (Exception e) {
                throw new ShiroException("Error while find access permissions!", e);
            }

            for (AccessPermission accessPermission : accessPermissions.getItems()) {
                PermissionImpl p = accessPermission.getPermission();
                logger.trace("User: {} has permission: {}", username, p);
                info.addObjectPermission(mapPermission(p));
            }

            // Access Role Id
            AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
            AccessRoleListResult accessRoles;
            try {
                accessRoles = KapuaSecurityUtils.doPrivileged(() -> accessRoleService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));
            } catch (AuthenticationException e) {
                throw e;
            } catch (Exception e) {
                throw new ShiroException("Error while find access role ids!", e);
            }

            RoleService roleService = locator.getService(RoleService.class);
            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            for (AccessRole accessRole : accessRoles.getItems()) {

                KapuaId roleId = accessRole.getRoleId();

                Role role;
                try {
                    role = KapuaSecurityUtils.doPrivileged(() -> roleService.find(accessRole.getScopeId(), roleId));
                } catch (AuthenticationException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ShiroException("Error while find role ids!", e);
                }

                info.addRole(role.getName());
                final RolePermissionListResult rolePermissions;
                try {
                    rolePermissions = KapuaSecurityUtils.doPrivileged(() -> rolePermissionService.findByRoleId(role.getScopeId(), role.getId()));
                } catch (Exception e) {
                    throw new ShiroException("Error while find role permission!", e);
                }

                for (RolePermission rolePermission : rolePermissions.getItems()) {

                    PermissionImpl p = rolePermission.getPermission();
                    logger.trace("Role: {} has permission: {}", role, p);
                    info.addObjectPermission(mapPermission(p));
                }
            }
        }
        // Return authorization info
        return info;
    }

    private final DomainRegistryService domainService = KapuaLocator.getInstance().getComponent(DomainRegistryService.class);
    private final AccountService accountService = KapuaLocator.getInstance().getComponent(AccountService.class);

    private Permission mapPermission(PermissionImpl permission) {
        return new MyWildcardPermission(permission.getDomain(), permission.getAction(), permission.getTargetScopeId(), permission.getGroupId(), permission.getForwardable());
    }

    /**
     * This method always returns false as it works only as AuthorizingReam.
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return false;
    }

    /**
     * This method can always return null as it does not support any authentication token.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        return null;
    }

    private class MyWildcardPermission extends WildcardPermission implements org.eclipse.kapua.service.authorization.permission.Permission, Permission {
        private String domain;
        private Actions action;
        private KapuaId targetScopeId;
        private KapuaId groupId;
        private boolean forwardable;

        public MyWildcardPermission(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable) {
            this.domain = domain;
            this.action = action;
            this.targetScopeId = targetScopeId;
            this.groupId = groupId;
            this.forwardable = forwardable;
            setParts(toString());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(domain != null ? domain : org.eclipse.kapua.service.authorization.permission.Permission.WILDCARD)
                    .append(org.eclipse.kapua.service.authorization.permission.Permission.SEPARATOR)
                    .append(action != null ? action.name() : org.eclipse.kapua.service.authorization.permission.Permission.WILDCARD)
                    .append(org.eclipse.kapua.service.authorization.permission.Permission.SEPARATOR)
                    .append(targetScopeId != null ? targetScopeId.getId() : org.eclipse.kapua.service.authorization.permission.Permission.WILDCARD)
                    .append(org.eclipse.kapua.service.authorization.permission.Permission.SEPARATOR)
                    .append(groupId != null ? groupId.getId() : org.eclipse.kapua.service.authorization.permission.Permission.WILDCARD);

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
            MyWildcardPermission other = (MyWildcardPermission) obj;
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
                return other.groupId == null;
            } else {
                return groupId.equals(other.groupId);
            }
        }

        /**
         * This method needs to be overridden to support Access {@link Group} feature.
         * <p>
         * {@link KapuaEntityService}s that access a specific {@link KapuaEntity} (i.e. {@link KapuaEntityService#create(KapuaEntityCreator)}, {@link KapuaEntityService#delete(KapuaId, KapuaId)})
         * can make the control taking in consideration of the {@link Group#getId()} parameter as it is known.<br>
         * <p>
         * Instead, methods that access multiple {@link KapuaEntity}s (i.e. {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)})
         * cannot make a direct control of the {@link Group#getId()} parameter as it is not known and they can be a lot.<br>
         * The access control then, is performed by hiding the data that a {@link Subject} cannot see instead of throwing {@link UnauthorizedException}.
         * </p>
         * <p>
         * The access control for {@link KapuaEntityService#query(KapuaQuery)}, {@link KapuaEntityService#count(KapuaQuery)}) must specify that {@link Group#ANY} group assigned to the permission is
         * enough to pass the {@link AuthorizationService#checkPermission(org.eclipse.kapua.service.authorization.permission.Permission)}.
         * </p>
         * <p>
         * In case of the {@link org.eclipse.kapua.service.authorization.permission.Permission#getForwardable()} equals to {@code true}, more lookup is required.<br>
         * If a parent account access the resources of one of its child accounts it won't have the direct permission to access it.
         * A lookup of {@link Account#getParentAccountPath()} will be required to search if the current user scope id is
         * one of the parent of the given {@link org.eclipse.kapua.service.authorization.permission.Permission#getTargetScopeId()}
         * </p>
         *
         * @since 1.0.0
         */
        @Override
        public boolean implies(Permission shiroPermission) {

            org.eclipse.kapua.service.authorization.permission.Permission targetPermission = (org.eclipse.kapua.service.authorization.permission.Permission) shiroPermission;

            // Check target Permission domain
            checkTargetPermissionIsGroupable(targetPermission);

            // If checked Permission ask for ANY targetScopeId, promote this Permission.targetScopeId to `null` (a.k.a. ALL scopes).
            if (KapuaId.ANY.equals(targetPermission.getTargetScopeId())) {
                this.setTargetScopeId(null);
            }

            // If checked Permission ask for ANY groupId, promote this Permission.groupId to `null` (a.k.a. ALL groups).
            if (Group.ANY.equals(targetPermission.getGroupId())) {
                this.setGroupId(null);
            }

            // Set part of the Shiro Permission to then run 'implies' with the target Permission
            this.setParts(this.toString());

            boolean implies = super.implies(shiroPermission);

            // If it fails try forward permission if this Permission is forwardable
            if (!implies && targetPermission.getTargetScopeId() != null && this.getForwardable()) {
                implies = forwardPermission(shiroPermission);
            }

            // Return result
            return implies;
        }

        /**
         * Checks whether the given {@link org.eclipse.kapua.service.authorization.permission.Permission#getDomain()} is {@link org.eclipse.kapua.service.authorization.domain.Domain#getGroupable()}.
         * <p>
         * If it is, promotes this {@link org.eclipse.kapua.service.authorization.permission.Permission#getGroupId()} to {@code null} (a.k.a. ALL groups).
         *
         * @param targetPermission The target {@link Permission} to check.
         * @since 2.0.0
         */
        private void checkTargetPermissionIsGroupable(org.eclipse.kapua.service.authorization.permission.Permission targetPermission) {
            if (targetPermission.getDomain() != null) {
                try {
                    org.eclipse.kapua.service.authorization.domain.Domain domainDefinition = KapuaSecurityUtils.doPrivileged(() -> domainService.findByName(targetPermission.getDomain()));

                    if (!domainDefinition.getGroupable()) {
                        this.setGroupId(null);
                    }
                } catch (Exception e) {
                    throw KapuaRuntimeException.internalError(e, "Error while resolving target Permission.domain: " + targetPermission.getDomain());
                }
            }
        }

        /**
         * Checks {@code this} Permission against the given {@link Permission} parameter.
         * <p>
         * It tries to forward {@code this} Permission to the {@link #getTargetScopeId()} of the given {@link Permission} parameter.<br>
         * This means that if the required permission has scope id 'B' and {@code this} {@link Permission} has scope id 'A',
         * this methods search the {@link Account#getParentAccountPath()} of the scope id 'B' and checks the {@link Permission} forwarding {@code this} Permission
         * to the same level of the given {@link Permission}.
         * </p>
         * <p>
         * <h3>Example:</h3>
         * User 'A' in account 'A' has scopeId 'A' and this permission (A) "*:*:A:*".
         * Account 'A' has a child account 'B', then 'B' has this parent account path: '/A/B';
         * User 'A' tries to access a resource of account 'B' an the direct check {@link Permission#implies(Permission)} fails.
         * So this method searches the parent account path of account 'B', found that 'A' is a parent of 'B'
         * so then {@code this} {@link Permission} is checked again with 'B' as scopeId.
         * </p>
         *
         * @param shiroPermission The permission to check against.
         * @return {@code true} if this permission is forward-able and is valid when forwarded, {@code false otherwise}
         * @since 1.0.0
         */
        private boolean forwardPermission(Permission shiroPermission) {
            org.eclipse.kapua.service.authorization.permission.Permission targetPermission = (org.eclipse.kapua.service.authorization.permission.Permission) shiroPermission;

            try {
                Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(targetPermission.getTargetScopeId()));

                if (account != null && account.getScopeId() != null) {
                    String parentAccountPath = account.getParentAccountPath();

                    // If it doesn't contain the scope id in the parent, don't even try to check against
                    if (parentAccountPath.contains("/" + getTargetScopeId().toStringId() + "/")) {
                        this.setTargetScopeId(targetPermission.getTargetScopeId());
                        this.setParts(this.toString());

                        return super.implies(shiroPermission);
                    }
                }
            } catch (KapuaException e) {
                throw KapuaRuntimeException.internalError(e, "Error while forwarding target Permission: " + shiroPermission);
            }

            return false;
        }


        @Override
        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getDomain() {
            return domain;
        }

        public void setAction(Actions action) {
            this.action = action;
        }

        public Actions getAction() {
            return action;
        }

        public void setTargetScopeId(KapuaId targetScopeId) {
            this.targetScopeId = KapuaEid.parseKapuaId(targetScopeId);
        }

        public KapuaId getTargetScopeId() {
            return targetScopeId;
        }

        public void setGroupId(KapuaId groupId) {
            this.groupId = KapuaEid.parseKapuaId(groupId);
        }

        public KapuaId getGroupId() {
            return groupId;
        }

        public boolean getForwardable() {
            return forwardable;
        }

        public void setForwardable(boolean forwardable) {
            this.forwardable = forwardable;
        }

    }
}
