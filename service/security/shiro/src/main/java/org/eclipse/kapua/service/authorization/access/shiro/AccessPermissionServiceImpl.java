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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionValidator;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link AccessPermission} service implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AccessPermissionServiceImpl extends AbstractKapuaService implements AccessPermissionService {

    public AccessPermissionServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance(), AccessPermissionCacheFactory.getInstance());
    }

    @Override
    public AccessPermission create(AccessPermissionCreator accessPermissionCreator)
            throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accessPermissionCreator, "accessPermissionCreator");
        ArgumentValidator.notNull(accessPermissionCreator.getAccessInfoId(), "accessPermissionCreator.accessInfoId");
        ArgumentValidator.notNull(accessPermissionCreator.getPermission(), "accessPermissionCreator.permission");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.write, accessPermissionCreator.getScopeId()));

        //
        // If permission are created out of the access permission scope, check that the current user has the permission on the external scopeId.
        Permission permission = accessPermissionCreator.getPermission();
        if (permission.getTargetScopeId() == null || !permission.getTargetScopeId().equals(accessPermissionCreator.getScopeId())) {
            authorizationService.checkPermission(permission);
        }

        PermissionValidator.validatePermission(permission);

        //
        // Check duplicates
        AccessPermissionQuery query = new AccessPermissionQueryImpl(accessPermissionCreator.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, accessPermissionCreator.getScopeId()),
                        query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessPermissionCreator.getAccessInfoId()),
                        query.attributePredicate(AccessPermissionAttributes.PERMISSION_DOMAIN, accessPermissionCreator.getPermission().getDomain()),
                        query.attributePredicate(AccessPermissionAttributes.PERMISSION_ACTION, accessPermissionCreator.getPermission().getAction()),
                        query.attributePredicate(AccessPermissionAttributes.PERMISSION_TARGET_SCOPE_ID, accessPermissionCreator.getPermission().getTargetScopeId()),
                        query.attributePredicate(AccessPermissionAttributes.PERMISSION_GROUP_ID, accessPermissionCreator.getPermission().getGroupId()),
                        query.attributePredicate(AccessPermissionAttributes.PERMISSION_FORWARDABLE, accessPermissionCreator.getPermission().getForwardable())
                )
        );
        if (count(query) > 0) {
            List<Map.Entry<String, Object>> uniquesFieldValues = new ArrayList<>();

            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(KapuaEntityAttributes.SCOPE_ID, accessPermissionCreator.getScopeId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.ACCESS_INFO_ID, accessPermissionCreator.getAccessInfoId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.PERMISSION_DOMAIN, accessPermissionCreator.getPermission().getDomain()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.PERMISSION_ACTION, accessPermissionCreator.getPermission().getAction()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.PERMISSION_TARGET_SCOPE_ID, accessPermissionCreator.getPermission().getTargetScopeId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.PERMISSION_GROUP_ID, accessPermissionCreator.getPermission().getGroupId()));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(AccessPermissionAttributes.PERMISSION_FORWARDABLE, accessPermissionCreator.getPermission().getForwardable()));

            throw new KapuaEntityUniquenessException(AccessPermission.TYPE, uniquesFieldValues);
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<AccessPermission>create().onResultHandler(em -> {
            //
            // Check that accessInfo exists
            AccessInfo accessInfo = AccessInfoDAO.find(em, accessPermissionCreator.getScopeId(), accessPermissionCreator.getAccessInfoId());

            if (accessInfo == null) {
                throw new KapuaEntityNotFoundException(AccessInfo.TYPE, accessPermissionCreator.getAccessInfoId());
            }

            return AccessPermissionDAO.create(em, accessPermissionCreator);
        }).onAfterHandler((entity) -> entityCache.removeList(entity.getScopeId(), entity.getAccessInfoId())));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessPermissionId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessPermissionId, KapuaEntityAttributes.ENTITY_ID);

        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(EntityManagerContainer.<AccessPermission>create().onResultHandler(em -> {
            // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
            //  without TYPE)
            AccessPermission accessPermission = AccessPermissionDAO.find(em, scopeId, accessPermissionId);
            if (accessPermission == null) {
                throw new KapuaEntityNotFoundException(AccessPermission.TYPE, accessPermissionId);
            }

            return AccessPermissionDAO.delete(em, scopeId, accessPermissionId);
        }).onAfterHandler((entity) -> {
            entityCache.remove(scopeId, accessPermissionId);
            entityCache.removeList(scopeId, entity.getAccessInfoId());
        }));
    }

    @Override
    public AccessPermission find(KapuaId scopeId, KapuaId accessPermissionId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessPermissionId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(EntityManagerContainer.<AccessPermission>create().onResultHandler(em -> AccessPermissionDAO.find(em, scopeId, accessPermissionId))
                .onBeforeHandler(() -> (AccessPermission) entityCache.get(scopeId, accessPermissionId))
                .onAfterHandler((entity) -> entityCache.put(entity)));
    }

    @Override
    public AccessPermissionListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessInfoId, "accessInfoId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN,
                Actions.read, scopeId));

        AccessPermissionListResult listResult = (AccessPermissionListResult) entityCache.getList(scopeId,
                accessInfoId);
        if (listResult == null) {

            //
            // Build query
            AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
            query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));

            listResult = query(query);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }

    @Override
    public AccessPermissionListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(EntityManagerContainer.<AccessPermissionListResult>create().onResultHandler(em -> AccessPermissionDAO.query(em, query)));
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

        return entityManagerSession.doAction(EntityManagerContainer.<Long>create().onResultHandler(em -> AccessPermissionDAO.count(em, query)));
    }
}
