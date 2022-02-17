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

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermissionAttributes;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

import javax.inject.Singleton;

/**
 * {@link AccessRoleService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class AccessRoleServiceImpl extends AbstractKapuaService implements AccessRoleService {

    public AccessRoleServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance(), AccessRoleCacheFactory.getInstance());
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
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.write, accessRoleCreator.getScopeId()));

        //
        // Check that AccessInfo exists
        AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
        AccessInfo accessInfo = accessInfoService.find(accessRoleCreator.getScopeId(), accessRoleCreator.getAccessInfoId());

        if (accessInfo == null) {
            throw new KapuaEntityNotFoundException(AccessInfo.TYPE, accessRoleCreator.getAccessInfoId());
        }

        //
        // Check that Role exists
        RoleService roleService = locator.getService(RoleService.class);
        Role role = roleService.find(accessRoleCreator.getScopeId(), accessRoleCreator.getRoleId());

        if (role == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, accessRoleCreator.getRoleId());
        }

        //
        // Check that Role is not already assigned
        AccessRoleQuery query = new AccessRoleQueryImpl(accessRoleCreator.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessRoleCreator.getAccessInfoId()),
                        query.attributePredicate(RolePermissionAttributes.ROLE_ID, accessRoleCreator.getRoleId())
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(role.getName());
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(
                EntityManagerContainer
                        .<AccessRole>create()
                        .onResultHandler(em -> AccessRoleDAO.create(em, accessRoleCreator))
                        .onAfterHandler((entity) -> entityCache.removeList(entity.getScopeId(), entity.getAccessInfoId())));
    }

    @Override
    public AccessRole find(KapuaId scopeId, KapuaId accessRoleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessRoleId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(
                EntityManagerContainer
                        .<AccessRole>create()
                        .onResultHandler(em -> AccessRoleDAO.find(em, scopeId, accessRoleId))
                        .onBeforeHandler(() -> (AccessRole) entityCache.get(scopeId, accessRoleId))
                        .onAfterHandler(entityCache::put));
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessInfoId, "accessInfoId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        //
        // Check cache
        AccessRoleListResult listResult = (AccessRoleListResult) entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {

            // Do find and populate cache
            AccessRoleQuery query = new AccessRoleQueryImpl(scopeId);
            query.setPredicate(query.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessInfoId));

            listResult = query(query);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }

    @Override
    public AccessRoleListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(
                EntityManagerContainer.
                        <AccessRoleListResult>create()
                        .onResultHandler(em -> AccessRoleDAO.query(em, query)));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(EntityManagerContainer.<Long>create().onResultHandler(em -> AccessRoleDAO.count(em, query)));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessRoleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessRoleId, KapuaEntityAttributes.ENTITY_ID);

        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.doTransactedAction(
                EntityManagerContainer
                        .<AccessRole>create()
                        .onResultHandler(em -> {
                            // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
                            //  without TYPE)
                            AccessRole accessRole = AccessRoleDAO.find(em, scopeId, accessRoleId);
                            if (accessRole == null) {
                                throw new KapuaEntityNotFoundException(AccessRole.TYPE, accessRoleId);
                            }

                            return AccessRoleDAO.delete(em, scopeId, accessRoleId);
                        })
                        .onAfterHandler((entity) -> {
                            entityCache.remove(scopeId, accessRoleId);
                            entityCache.removeList(scopeId, entity.getAccessInfoId());
                        }));
    }
}
