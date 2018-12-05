/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionValidator;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleAttributes;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RoleService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class RoleServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Role, RoleCreator, RoleService, RoleListResult, RoleQuery, RoleFactory> implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final RolePermissionFactory ROLE_PERMISSION_FACTORY = LOCATOR.getFactory(RolePermissionFactory.class);

    public RoleServiceImpl() {
        super(RoleService.class.getName(), AuthorizationDomains.ROLE_DOMAIN, AuthorizationEntityManagerFactory.getInstance(), RoleService.class, RoleFactory.class);
    }

    @Override
    public Role create(RoleCreator roleCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(roleCreator, "roleCreator");
        ArgumentValidator.notNull(roleCreator.getScopeId(), "roleCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(roleCreator.getName(), "roleCreator.name");
        ArgumentValidator.notNull(roleCreator.getPermissions(), "roleCreator.permissions");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.write, roleCreator.getScopeId()));

        //
        // Check limits
        if (allowedChildEntities(roleCreator.getScopeId()) <= 0) {
            throw new KapuaMaxNumberOfItemsReachedException("Roles");
        }

        //
        // Check duplicate name
        RoleQuery query = new RoleQueryImpl(roleCreator.getScopeId());
        query.setPredicate(new AttributePredicateImpl<>(RoleAttributes.NAME, roleCreator.getName()));

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(roleCreator.getName());
        }

        //
        // If permission are created out of the role scope, check that the current user has the permission on the external scopeId.
        if (roleCreator.getPermissions() != null) {
            for (Permission p : roleCreator.getPermissions()) {
                if (p.getTargetScopeId() == null || !p.getTargetScopeId().equals(roleCreator.getScopeId())) {
                    AUTHORIZATION_SERVICE.checkPermission(p);
                }
            }
        }

        //
        // Check that the given permission matches the definition of the Domains.
        PermissionValidator.validatePermissions(roleCreator.getPermissions());

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> {
            Role role = RoleDAO.create(em, roleCreator);

            if (!roleCreator.getPermissions().isEmpty()) {
                for (Permission p : roleCreator.getPermissions()) {

                    RolePermissionCreator rolePermissionCreator = ROLE_PERMISSION_FACTORY.newCreator(roleCreator.getScopeId());

                    rolePermissionCreator.setRoleId(role.getId());
                    rolePermissionCreator.setPermission(p);

                    RolePermissionDAO.create(em, rolePermissionCreator);
                }
            }

            return role;
        });
    }

    @Override
    public Role update(Role role) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(role, "role");
        ArgumentValidator.notNull(role.getScopeId(), "role.scopeId");
        ArgumentValidator.notNull(role.getId(), "role.id");
        ArgumentValidator.notEmptyOrNull(role.getName(), "role.name");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.write, role.getScopeId()));

        //
        // Check existence
        if (find(role.getScopeId(), role.getId()) == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, role.getId());
        }

        //
        // Check duplicate name
        RoleQuery query = new RoleQueryImpl(role.getScopeId());
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(RoleAttributes.NAME, role.getName()),
                        new AttributePredicateImpl<>(RoleAttributes.ENTITY_ID, role.getId(), Operator.NOT_EQUAL)
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(role.getName());
        }

        //
        // Do update
        return entityManagerSession.onTransactedInsert(em -> RoleDAO.update(em, role));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, roleId) == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, roleId);
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> RoleDAO.delete(em, scopeId, roleId));
    }

    @Override
    public Role find(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> RoleDAO.find(em, scopeId, roleId));
    }

    @Override
    public RoleListResult query(KapuaQuery<Role> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> RoleDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Role> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> RoleDAO.count(em, query));
    }

    //@ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }

        LOG.info("RoleService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteRoleByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteRoleByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {

        RoleQuery query = new RoleQueryImpl(accountId);

        RoleListResult rolesToDelete = query(query);

        for (Role r : rolesToDelete.getItems()) {
            delete(r.getScopeId(), r.getId());
        }
    }
}
