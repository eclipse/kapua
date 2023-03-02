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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceBase;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionValidator;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link RoleService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class RoleServiceImpl extends KapuaConfigurableServiceBase implements RoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    private PermissionFactory permissionFactory;
    private AuthorizationService authorizationService;
    private RolePermissionFactory rolePermissionFactory;


    /**
     * Injectable constructor
     *
     * @param authorizationEntityManagerFactory The {@link AuthorizationEntityManagerFactory} instance.
     * @param roleCacheFactory                  The {@link RoleCacheFactory} instance.
     * @param permissionFactory                 The {@link PermissionFactory} instance.
     * @param authorizationService              The {@link AuthorizationService} instance.
     * @param rolePermissionFactory             The {@link RolePermissionFactory} instance.
     * @param serviceConfigurationManager       The {@link ServiceConfigurationManager} instance.
     */
    @Inject
    public RoleServiceImpl(AuthorizationEntityManagerFactory authorizationEntityManagerFactory,
                           RoleCacheFactory roleCacheFactory,
                           PermissionFactory permissionFactory,
                           AuthorizationService authorizationService,
                           RolePermissionFactory rolePermissionFactory,
                           ServiceConfigurationManager serviceConfigurationManager) {
        super(authorizationEntityManagerFactory, roleCacheFactory, serviceConfigurationManager);
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
        this.rolePermissionFactory = rolePermissionFactory;
    }

    @Override
    public Role create(RoleCreator roleCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(roleCreator, "roleCreator");
        ArgumentValidator.notNull(roleCreator.getScopeId(), "roleCreator.scopeId");
        ArgumentValidator.validateEntityName(roleCreator.getName(), "roleCreator.name");
        ArgumentValidator.notNull(roleCreator.getPermissions(), "roleCreator.permissions");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.write, roleCreator.getScopeId()));

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(roleCreator.getScopeId(), "Roles");

        //
        // Check duplicate name
        // TODO: INJECT
        new KapuaNamedEntityServiceUtils(new QueryFactoryImpl()).checkEntityNameUniqueness(this, roleCreator);

        //
        // If permission are created out of the role scope, check that the current user has the permission on the external scopeId.
        if (roleCreator.getPermissions() != null) {
            for (Permission p : roleCreator.getPermissions()) {
                if (p.getTargetScopeId() == null || !p.getTargetScopeId().equals(roleCreator.getScopeId())) {
                    getAuthorizationService().checkPermission(p);
                }
            }
        }

        //
        // Check that the given permission matches the definition of the Domains.
        PermissionValidator.validatePermissions(roleCreator.getPermissions());

        //
        // Do create
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<Role>create().onResultHandler(em -> {
            Role role = RoleDAO.create(em, roleCreator);

            if (!roleCreator.getPermissions().isEmpty()) {
                for (Permission p : roleCreator.getPermissions()) {

                    RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(roleCreator.getScopeId());

                    rolePermissionCreator.setRoleId(role.getId());
                    rolePermissionCreator.setPermission(p);

                    RolePermissionDAO.create(em, rolePermissionCreator);
                }
            }

            return role;
        }));
    }

    @Override
    public Role update(Role role) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(role, "role");
        ArgumentValidator.notNull(role.getId(), "role.id");
        ArgumentValidator.notNull(role.getScopeId(), "role.scopeId");
        ArgumentValidator.validateEntityName(role.getName(), "role.name");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.write, role.getScopeId()));

        //
        // Check existence
        if (find(role.getScopeId(), role.getId()) == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, role.getId());
        }

        //
        // Check duplicate name
        // TODO: INJECT
        new KapuaNamedEntityServiceUtils(new QueryFactoryImpl()).checkEntityNameUniqueness(this, role);

        //
        // Do update
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<Role>create().onResultHandler(em -> RoleDAO.update(em, role))
                .onBeforeHandler(() -> {
                    entityCache.remove(null, role);
                    return null;
                }));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, roleId) == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, roleId);
        }
        if (roleId.equals(KapuaId.ONE)) {
            throw new KapuaException(KapuaErrorCodes.ADMIN_ROLE_DELETED_ERROR);
        }

        //
        // Do delete
        entityManagerSession.doTransactedAction(EntityManagerContainer.<Role>create().onResultHandler(em -> RoleDAO.delete(em, scopeId, roleId))
                .onAfterHandler((emptyParam) -> entityCache.remove(scopeId, roleId)));
    }

    @Override
    public Role find(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(roleId, "roleId");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(EntityManagerContainer.<Role>create().onResultHandler(em -> RoleDAO.find(em, scopeId, roleId))
                .onBeforeHandler(() -> (Role) entityCache.get(scopeId, roleId))
                .onAfterHandler((entity) -> entityCache.put(entity)));
    }

    @Override
    public RoleListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(EntityManagerContainer.<RoleListResult>create().onResultHandler(em -> RoleDAO.query(em, query)));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(AuthorizationDomains.ROLE_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(EntityManagerContainer.<Long>create().onResultHandler(em -> RoleDAO.count(em, query)));
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

    /**
     * AuthorizationService should be provided by the Locator, but in most cases when this class is instantiated through the deprecated constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link AuthorizationService} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
        }
        return authorizationService;
    }

    /**
     * PermissionFactory should be provided by the Locator, but in most cases when this class is instantiated through this constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link PermissionFactory} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected PermissionFactory getPermissionFactory() {
        if (permissionFactory == null) {
            permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
        }
        return permissionFactory;
    }
}
