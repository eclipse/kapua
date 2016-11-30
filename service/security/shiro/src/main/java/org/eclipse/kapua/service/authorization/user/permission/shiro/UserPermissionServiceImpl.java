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
package org.eclipse.kapua.service.authorization.user.permission.shiro;

import java.util.Set;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.user.permission.UserPermission;
import org.eclipse.kapua.service.authorization.user.permission.UserPermissionCreator;
import org.eclipse.kapua.service.authorization.user.permission.UserPermissionListResult;
import org.eclipse.kapua.service.authorization.user.permission.UserPermissionService;

/**
 * User permission service implementation.
 * 
 * @since 1.0
 *
 */
public class UserPermissionServiceImpl extends AbstractKapuaService implements UserPermissionService
{

    public UserPermissionServiceImpl()
    {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public UserPermission create(UserPermissionCreator userPermissionCreator)
        throws KapuaException
    {
        ArgumentValidator.notNull(userPermissionCreator, "userPermissionCreator");
        ArgumentValidator.notNull(userPermissionCreator.getPermission(), "userPermissionCreator.permission");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(UserPermissionDomain.USER_PERMISSION, Actions.write, userPermissionCreator.getScopeId()));

        return entityManagerSession.onTransactedInsert(em -> UserPermissionDAO.create(em, userPermissionCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId permissionId) throws KapuaException {
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(UserPermissionDomain.USER_PERMISSION, Actions.write, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (UserPermissionDAO.find(em, permissionId) == null) {
                throw new KapuaEntityNotFoundException(UserPermission.TYPE, permissionId);
            }
            UserPermissionDAO.delete(em, permissionId);
        });
    }

    @Override
    public UserPermission find(KapuaId scopeId, KapuaId permissionId)
        throws KapuaException
    {
        ArgumentValidator.notNull(scopeId, "accountId");
        ArgumentValidator.notNull(permissionId, "permissionId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(UserPermissionDomain.USER_PERMISSION, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> UserPermissionDAO.find(em, permissionId));
    }

    @Override
    public UserPermissionListResult query(KapuaQuery<UserPermission> query)
        throws KapuaException
    {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(UserPermissionDomain.USER_PERMISSION, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserPermissionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<UserPermission> query)
        throws KapuaException
    {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(UserPermissionDomain.USER_PERMISSION, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserPermissionDAO.count(em, query));
    }

    @Override
    public UserPermissionListResult merge(Set<UserPermissionCreator> newPermissions)
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
