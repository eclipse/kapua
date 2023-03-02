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

import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoImplJpaRepository;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionCacheFactory;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionImplJpaRepository;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleCacheFactory;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleImplJpaRepository;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.CachingAccessPermissionRepository;
import org.eclipse.kapua.service.authorization.access.shiro.CachingAccessRoleRepository;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainRegistryServiceImpl;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupRepository;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupImplJpaRepository;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImplJpaRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

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

    @Provides
    @Singleton
    @Named("RoleServiceConfigurationManager")
    public ServiceConfigurationManager roleServiceConfigurationManager(
            RoleFactory roleFactory,
            AuthorizationEntityManagerFactory authorizationEntityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            RoleRepository roleRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        RoleService.class.getName(),
                        AuthorizationDomains.ROLE_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                roleFactory,
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                                roleRepository
                        )));
    }

    @Provides
    @Singleton
    RoleRepository roleRepository() {
        return new RoleImplJpaRepository();
    }

    @Provides
    @Singleton
    GroupRepository groupRepository() {
        return new GroupImplJpaRepository();
    }

    @Provides
    @Singleton
    AccessInfoRepository accessInfoRepository() {
        return new AccessInfoImplJpaRepository();
    }

    @Provides
    @Singleton
    AccessPermissionRepository accessPermissionRepository() {
        return new CachingAccessPermissionRepository(
                new AccessPermissionImplJpaRepository()
                , new AccessPermissionCacheFactory().createCache()
        );
    }

    @Provides
    @Singleton
    AccessRoleRepository accessRoleRepository() {
        return new CachingAccessRoleRepository(
                new AccessRoleImplJpaRepository()
                , new AccessRoleCacheFactory().createCache()
        );
    }

    @Provides
    @Singleton
    @Named("GroupServiceConfigurationManager")
    public ServiceConfigurationManager groupServiceConfigurationManager(
            AuthorizationEntityManagerFactory authorizationEntityManagerFactory,
            GroupFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            GroupRepository groupRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        GroupService.class.getName(),
                        AuthorizationDomains.GROUP_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                                groupRepository
                        )));
    }
}
