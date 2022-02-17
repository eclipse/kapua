/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleServiceImpl;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainRegistryServiceImpl;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;

public class AuthorizationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(AuthorizationService.class).to(AuthorizationServiceImpl.class);

        bind(RoleService.class).to(RoleServiceImpl.class);
        bind(RoleFactory.class).to(RoleFactoryImpl.class);

        bind(DomainRegistryService.class).to(DomainRegistryServiceImpl.class);
        bind(DomainFactory.class).to(DomainFactoryImpl.class);

        bind(PermissionFactory.class).to(PermissionFactoryImpl.class);

        bind(AccessInfoService.class).to(AccessInfoServiceImpl.class);
        bind(AccessInfoFactory.class).to(AccessInfoFactoryImpl.class);
        bind(AccessPermissionService.class).to(AccessPermissionServiceImpl.class);
        bind(AccessPermissionFactory.class).to(AccessPermissionFactoryImpl.class);
        bind(AccessRoleService.class).to(AccessRoleServiceImpl.class);
        bind(AccessRoleFactory.class).to(AccessRoleFactoryImpl.class);

        bind(RolePermissionService.class).to(RolePermissionServiceImpl.class);
        bind(RolePermissionFactory.class).to(RolePermissionFactoryImpl.class);

        bind(GroupService.class).to(GroupServiceImpl.class);
        bind(GroupFactory.class).to(GroupFactoryImpl.class);
    }
}
