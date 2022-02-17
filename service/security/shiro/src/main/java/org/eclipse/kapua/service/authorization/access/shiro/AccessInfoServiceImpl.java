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
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionValidator;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.exception.KapuaAuthorizationErrorCodes;
import org.eclipse.kapua.service.authorization.shiro.exception.KapuaAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AccessInfoService} implementation based on JPA.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AccessInfoServiceImpl extends AbstractKapuaService implements AccessInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessInfoServiceImpl.class);

    /**
     * Constructor.<br>
     * It initialize the {@link AbstractEntityManagerFactory} with the specific {@link AuthorizationEntityManagerFactory#getInstance()}.
     *
     * @since 1.0.0
     */
    public AccessInfoServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance(), AccessInfoCacheFactory.getInstance());
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
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.write, accessInfoCreator.getScopeId()));

        //
        // If permission are created out of the access info scope, check that the current user has the permission on the external scopeId.
        if (accessInfoCreator.getPermissions() != null) {
            for (Permission p : accessInfoCreator.getPermissions()) {
                if (p.getTargetScopeId() == null || !p.getTargetScopeId().equals(accessInfoCreator.getScopeId())) {
                    authorizationService.checkPermission(p);
                }
            }
        }

        PermissionValidator.validatePermissions(accessInfoCreator.getPermissions());

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

        return entityManagerSession.doTransactedAction(EntityManagerContainer.<AccessInfo>create().onResultHandler(em -> {
            AccessInfo accessInfo = AccessInfoDAO.create(em, accessInfoCreator);

            if (!accessInfoCreator.getPermissions().isEmpty()) {
                AccessPermissionFactory accessInfoFactory = locator.getFactory(AccessPermissionFactory.class);
                for (Permission p : accessInfoCreator.getPermissions()) {
                    AccessPermissionCreator accessPermissionCreator = accessInfoFactory.newCreator(accessInfoCreator.getScopeId());

                    accessPermissionCreator.setAccessInfoId(accessInfo.getId());
                    accessPermissionCreator.setPermission(p);

                    AccessPermissionDAO.create(em, accessPermissionCreator);
                }
            }

            if (!accessInfoCreator.getRoleIds().isEmpty()) {
                AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
                for (KapuaId roleId : accessInfoCreator.getRoleIds()) {
                    AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(accessInfoCreator.getScopeId());

                    accessRoleCreator.setAccessInfoId(accessInfo.getId());
                    accessRoleCreator.setRoleId(roleId);

                    AccessRoleDAO.create(em, accessRoleCreator);
                }
            }

            return accessInfo;
        }));
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
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(EntityManagerContainer.<AccessInfo>create().onResultHandler(em -> AccessInfoDAO.find(em, scopeId, accessInfoId))
                .onBeforeHandler(() -> (AccessInfo) entityCache.get(scopeId, accessInfoId))
                .onAfterHandler((entity) -> entityCache.put(entity))
        );
    }

    @Override
    public AccessInfo findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "accountId");
        ArgumentValidator.notNull(userId, "userId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));
        AccessInfoQuery query = accessInfoFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(AccessInfoAttributes.USER_ID, userId));
        return entityManagerSession.doAction(EntityManagerContainer.<AccessInfo>create().onResultHandler(em -> {
            AccessInfoListResult result = AccessInfoDAO.query(em, query);
            if (!result.isEmpty()) {
                return result.getFirstItem();
            }
            return null;
        }).onBeforeHandler(() -> (AccessInfo) ((AccessInfoCache) entityCache).getByUserId(scopeId, userId))
                .onAfterHandler((entity) -> entityCache.put(entity)));
    }

    @Override
    public AccessInfoListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(EntityManagerContainer.<AccessInfoListResult>create().onResultHandler(em -> AccessInfoDAO.query(em, query)));
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

        return entityManagerSession.doAction(EntityManagerContainer.<Long>create().onResultHandler(em -> AccessInfoDAO.count(em, query)));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(EntityManagerContainer.<AccessInfo>create().onResultHandler(em -> {
                    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
                    //  without TYPE)
                    if (AccessInfoDAO.find(em, scopeId, accessInfoId) == null) {
                        throw new KapuaEntityNotFoundException(AccessInfo.TYPE, accessInfoId);
                    }
                    return AccessInfoDAO.delete(em, scopeId, accessInfoId);
                }
        ).onAfterHandler((emptyParam) -> entityCache.remove(scopeId, accessInfoId)));
    }

    //@ListenServiceEvent(fromAddress="account")
    //@ListenServiceEvent(fromAddress="user")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("AccessInfoService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("user".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteAccessInfoByUserId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        } else if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteAccessInfoByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteAccessInfoByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);

        AccessInfoQuery query = accessInfoFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(AccessInfoAttributes.USER_ID, userId));

        AccessInfoListResult accessInfosToDelete = query(query);

        for (AccessInfo at : accessInfosToDelete.getItems()) {
            delete(at.getScopeId(), at.getId());
        }
    }

    private void deleteAccessInfoByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);

        AccessInfoQuery query = accessInfoFactory.newQuery(accountId);

        AccessInfoListResult accessInfosToDelete = query(query);

        for (AccessInfo at : accessInfosToDelete.getItems()) {
            delete(at.getScopeId(), at.getId());
        }
    }
}
