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

import java.util.Set;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizationErrorCodes;
import org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizationException;

/**
 * User permission service implementation.
 * 
 * @since 1.0
 *
 */
public class AccessInfoServiceImpl extends AbstractKapuaService implements AccessInfoService {

    public AccessInfoServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public AccessInfo create(AccessInfoCreator accessInfoCreator)
            throws KapuaException {
        ArgumentValidator.notNull(accessInfoCreator, "accessInfoCreator");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.write, accessInfoCreator.getScopeId()));

        RoleService roleService = locator.getService(RoleService.class);
        if (accessInfoCreator.getRoleIds() != null) {
            for (KapuaId roleId : accessInfoCreator.getRoleIds()) {
                // This checks also that the role belong to the same scopeId in which the access info is created
                Role role = roleService.find(accessInfoCreator.getScopeId(), roleId);

                // If (role == null) then roleId does not exists or it isn't in the same scope.
                if (role == null) {
                    throw new KapuaAuthorizationException(KapuaAuthorizationErrorCodes.ENTITY_SCOPE_MISSMATCH, null, "Role not found in the scope: " + accessInfoCreator.getScopeId());
            }
            }
        }

        return entityManagerSession.onEntityManagerInsert(em -> {
            em.beginTransaction();
            AccessInfo accessInfo = AccessInfoDAO.create(em, accessInfoCreator);
            em.commit();
            return accessInfo;
        });
    }

    @Override
    public AccessInfo find(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "accountId");
        ArgumentValidator.notNull(accessInfoId, "accessInfoId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, scopeId));

        return entityManagerSession.onEntityManagerResult(em -> AccessInfoDAO.find(em, accessInfoId));
    }

    @Override
    public AccessInfoListResult query(KapuaQuery<AccessInfo> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, query.getScopeId()));

        return entityManagerSession.onEntityManagerResult(em -> AccessInfoDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<AccessInfo> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.read, query.getScopeId()));

        return entityManagerSession.onEntityManagerResult(em -> AccessInfoDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AccessInfoDomain.ACCESS_INFO, Actions.write, scopeId));

        entityManagerSession.onEntityManagerAction(em -> {
            if (AccessInfoDAO.find(em, accessInfoId) == null) {
                throw new KapuaEntityNotFoundException(AccessPermission.TYPE, accessInfoId);
        }

            em.beginTransaction();
            AccessInfoDAO.delete(em, accessInfoId);
            em.commit();
        });
    }

    @Override
    public AccessInfoListResult merge(Set<AccessInfoCreator> newPermissions)
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }
}
