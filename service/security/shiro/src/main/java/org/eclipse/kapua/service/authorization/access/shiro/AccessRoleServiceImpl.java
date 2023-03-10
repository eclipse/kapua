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
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermissionAttributes;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link AccessRoleService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class AccessRoleServiceImpl implements AccessRoleService {

    private final TxManager txManager;
    private final RoleRepository roleRepository;
    private final AccessInfoRepository accessInfoRepository;
    private final AccessRoleRepository accessRoleRepository;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;

    @Inject
    public AccessRoleServiceImpl(
            TxManager txManager,
            RoleRepository roleRepository,
            AccessInfoRepository accessInfoRepository,
            AccessRoleRepository accessRoleRepository,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory) {
        this.txManager = txManager;
        this.roleRepository = roleRepository;
        this.accessInfoRepository = accessInfoRepository;
        this.accessRoleRepository = accessRoleRepository;
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
    }

    @Override
    public AccessRole create(AccessRoleCreator accessRoleCreator)
            throws KapuaException {
        ArgumentValidator.notNull(accessRoleCreator, "accessRoleCreator");
        ArgumentValidator.notNull(accessRoleCreator.getAccessInfoId(), "accessRoleCreator.accessInfoId");
        ArgumentValidator.notNull(accessRoleCreator.getRoleId(), "accessRoleCreator.roleId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.write, accessRoleCreator.getScopeId()));

        return txManager.executeWithResult(tx -> {
            //
            // Check that AccessInfo exists
            final AccessInfo accessInfo = accessInfoRepository.find(tx, accessRoleCreator.getScopeId(), accessRoleCreator.getAccessInfoId());

            if (accessInfo == null) {
                throw new KapuaEntityNotFoundException(AccessInfo.TYPE, accessRoleCreator.getAccessInfoId());
            }

            //
            // Check that Role exists
            final Role role = roleRepository.find(tx, accessRoleCreator.getScopeId(), accessRoleCreator.getRoleId());

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

            if (accessRoleRepository.count(tx, query) > 0) {
                throw new KapuaDuplicateNameException(role.getName());
            }

            //
            // Do create
            AccessRole accessRole = new AccessRoleImpl(accessRoleCreator.getScopeId());

            accessRole.setAccessInfoId(accessRoleCreator.getAccessInfoId());
            accessRole.setRoleId(accessRoleCreator.getRoleId());
            return accessRoleRepository.create(tx, accessRole);
        });
    }

    @Override
    public AccessRole find(KapuaId scopeId, KapuaId accessRoleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessRoleId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return txManager.executeWithResult(tx -> accessRoleRepository.find(tx, scopeId, accessRoleId));
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessInfoId, "accessInfoId");

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, scopeId));

        // Check cache
        return txManager.executeWithResult(tx -> accessRoleRepository.findByAccessInfoId(tx, scopeId, accessInfoId));
    }

    @Override
    public AccessRoleListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return txManager.executeWithResult(tx -> accessRoleRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return txManager.executeWithResult(tx -> accessRoleRepository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessRoleId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accessRoleId, KapuaEntityAttributes.ENTITY_ID);

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.ACCESS_INFO_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        txManager.executeNoResult(tx -> accessRoleRepository.delete(tx, scopeId, accessRoleId));
    }
}
