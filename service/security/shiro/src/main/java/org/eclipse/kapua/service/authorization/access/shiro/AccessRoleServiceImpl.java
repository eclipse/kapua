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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

/**
 * {@link AccessRole} service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class AccessRoleServiceImpl extends AbstractKapuaService implements AccessRoleService {

    public AccessRoleServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public AccessRole create(AccessRoleCreator accessRoleCreator)
            throws KapuaException {
        ArgumentValidator.notNull(accessRoleCreator, "accessRoleCreator");
        ArgumentValidator.notNull(accessRoleCreator.getAccessInfoId(), "accessRoleCreator.accessInfoId");
        ArgumentValidator.notNull(accessRoleCreator.getRoleId(), "accessRoleCreator.roleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.write, accessRoleCreator.getScopeId()));
        return entityManagerSession.onTransactedInsert(em -> AccessRoleDAO.create(em, accessRoleCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessRoleId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessRoleId, "accessRoleId");

        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.delete, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (AccessRoleDAO.find(em, accessRoleId) == null) {
                throw new KapuaEntityNotFoundException(AccessRole.TYPE, accessRoleId);
            }

            AccessRoleDAO.delete(em, accessRoleId);
        });
    }

    @Override
    public AccessRole find(KapuaId scopeId, KapuaId accessRoleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessRoleId, "accessRoleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> AccessRoleDAO.find(em, accessRoleId));
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accessInfoId, "accessInfoId");

        //
        // Build query
        AccessRoleQuery query = new AccessRoleQueryImpl(scopeId);
        query.setPredicate(new AttributePredicate<KapuaId>(AccessRolePredicates.ACCESS_INFO_ID, accessInfoId));

        return query(query);
    }

    @Override
    public AccessRoleListResult query(KapuaQuery<AccessRole> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> AccessRoleDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<AccessRole> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> AccessRoleDAO.count(em, query));
    }
}
