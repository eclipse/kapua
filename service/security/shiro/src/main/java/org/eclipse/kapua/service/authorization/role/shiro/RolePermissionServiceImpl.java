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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

/**
 * {@link RolePermission} service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class RolePermissionServiceImpl extends AbstractKapuaService implements RolePermissionService {

    private static final Domain roleDomain = new RoleDomain();

    public RolePermissionServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public RolePermission create(RolePermissionCreator rolePermissionCreator)
            throws KapuaException {
        ArgumentValidator.notNull(rolePermissionCreator, "rolePermissionCreator");
        ArgumentValidator.notNull(rolePermissionCreator.getRoleId(), "rolePermissionCreator.roleId");
        ArgumentValidator.notNull(rolePermissionCreator.getPermission(), "rolePermissionCreator.permission");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(roleDomain, Actions.write, rolePermissionCreator.getScopeId()));
        return entityManagerSession.onTransactedInsert(em -> RolePermissionDAO.create(em, rolePermissionCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId rolePermissionId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(rolePermissionId, "rolePermissionId");

        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(roleDomain, Actions.delete, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (RolePermissionDAO.find(em, rolePermissionId) == null) {
                throw new KapuaEntityNotFoundException(RolePermission.TYPE, rolePermissionId);
            }

            RolePermissionDAO.delete(em, rolePermissionId);
        });
    }

    @Override
    public RolePermission find(KapuaId scopeId, KapuaId rolePermissionId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(rolePermissionId, "rolePermissionId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(roleDomain, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> RolePermissionDAO.find(em, rolePermissionId));
    }

    @Override
    public RolePermissionListResult findByRoleId(KapuaId scopeId, KapuaId roleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Build query
        RolePermissionQuery query = new RolePermissionQueryImpl(scopeId);
        query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));

        return query(query);
    }

    @Override
    public RolePermissionListResult query(KapuaQuery<RolePermission> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(roleDomain, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> RolePermissionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<RolePermission> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(roleDomain, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> RolePermissionDAO.count(em, query));
    }
}
