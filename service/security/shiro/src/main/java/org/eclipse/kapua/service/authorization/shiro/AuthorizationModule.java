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
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
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
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoCache;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoCacheFactory;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoCachingRepository;
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
import org.eclipse.kapua.service.authorization.domain.DomainRepository;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainImplJpaRepository;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainRegistryServiceImpl;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupRepository;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupImplJpaRepository;
import org.eclipse.kapua.service.authorization.group.shiro.GroupQueryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionRepository;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleCacheFactory;
import org.eclipse.kapua.service.authorization.role.shiro.RoleCachingRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RoleFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImplJpaRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionCacheFactory;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionCachingRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImplJpaRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleQueryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

public class AuthorizationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(AuthorizationService.class).to(AuthorizationServiceImpl.class);
        bind(RoleFactory.class).to(RoleFactoryImpl.class);

        bind(DomainFactory.class).to(DomainFactoryImpl.class);

        bind(PermissionFactory.class).to(PermissionFactoryImpl.class);

        bind(AccessInfoFactory.class).to(AccessInfoFactoryImpl.class);
        bind(AccessPermissionFactory.class).to(AccessPermissionFactoryImpl.class);
        bind(AccessRoleFactory.class).to(AccessRoleFactoryImpl.class);

        bind(RolePermissionFactory.class).to(RolePermissionFactoryImpl.class);

        bind(GroupFactory.class).to(GroupFactoryImpl.class);
    }

    @Provides
    @Singleton
    DomainRegistryService domainRegistryService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DomainRepository domainRepository,
            DomainFactory domainFactory) {
        return new DomainRegistryServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                domainRepository,
                domainFactory);
    }

    @Provides
    @Singleton
    DomainRepository domainRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new DomainImplJpaRepository(jpaRepoConfig);
    }

    @Provides
    @Singleton
    RolePermissionService rolePermissionService(PermissionFactory permissionFactory,
                                                AuthorizationService authorizationService,
                                                RoleRepository roleRepository,
                                                RolePermissionRepository rolePermissionRepository) {
        return new RolePermissionServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                roleRepository,
                rolePermissionRepository
        );
    }

    @Provides
    @Singleton
    RoleService roleService(PermissionFactory permissionFactory,
                            AuthorizationService authorizationService,
                            RolePermissionFactory rolePermissionFactory,
                            @Named("RoleServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
                            RoleRepository roleRepository,
                            RolePermissionRepository rolePermissionRepository
    ) {
        return new RoleServiceImpl(
                permissionFactory,
                authorizationService,
                rolePermissionFactory,
                serviceConfigurationManager,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                roleRepository,
                rolePermissionRepository,
                new DuplicateNameCheckerImpl<>(roleRepository, (scopeId) -> new RoleQueryImpl(scopeId))
        );
    }

    @Provides
    @Singleton
    @Named("RoleServiceConfigurationManager")
    public ServiceConfigurationManager roleServiceConfigurationManager(
            RoleFactory roleFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            RoleRepository roleRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        RoleService.class.getName(),
                        AuthorizationDomains.ROLE_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
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
    RoleRepository roleRepository(RoleCacheFactory roleCacheFactory, KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new RoleCachingRepository(new RoleImplJpaRepository(jpaRepoConfig),
                (NamedEntityCache) roleCacheFactory.createCache());
    }

    @Provides
    @Singleton
    RolePermissionRepository rolePermissionRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new RolePermissionCachingRepository(new RolePermissionImplJpaRepository(jpaRepoConfig),
                new RolePermissionCacheFactory().createCache());
    }

    @Provides
    @Singleton
    GroupService groupService(PermissionFactory permissionFactory,
                              AuthorizationService authorizationService,
                              @Named("GroupServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
                              GroupRepository groupRepository) {
        return new GroupServiceImpl(permissionFactory, authorizationService, serviceConfigurationManager,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                groupRepository,
                new DuplicateNameCheckerImpl<>(groupRepository, scopeId -> new GroupQueryImpl(scopeId)));
    }

    @Provides
    @Singleton
    @Named("GroupServiceConfigurationManager")
    public ServiceConfigurationManager groupServiceConfigurationManager(
            GroupFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            GroupRepository groupRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        GroupService.class.getName(),
                        AuthorizationDomains.GROUP_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
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

    @Provides
    @Singleton
    GroupRepository groupRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new GroupImplJpaRepository(jpaRepoConfig);
    }

    @Provides
    @Singleton
    AccessInfoService accessInfoService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            RoleRepository roleRepository,
            AccessRoleFactory accessRoleFactory,
            AccessRoleRepository accessRoleRepository,
            AccessInfoRepository accessInfoRepository,
            AccessInfoFactory accessInfoFactory,
            AccessPermissionRepository accessPermissionRepository,
            AccessPermissionFactory accessPermissionFactory) {
        return new AccessInfoServiceImpl(authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                roleRepository,
                accessRoleFactory,
                accessRoleRepository,
                accessInfoRepository,
                accessInfoFactory,
                accessPermissionRepository,
                accessPermissionFactory);
    }

    @Provides
    @Singleton
    AccessInfoRepository accessInfoRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new AccessInfoCachingRepository(
                new AccessInfoImplJpaRepository(jpaRepoConfig),
                (AccessInfoCache) new AccessInfoCacheFactory().createCache()
        );
    }

    @Provides
    @Singleton
    AccessPermissionService accessPermissionService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            AccessPermissionRepository accessPermissionRepository,
            AccessInfoRepository accessInfoRepository) {
        return new AccessPermissionServiceImpl(authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                accessPermissionRepository,
                accessInfoRepository);
    }


    @Provides
    @Singleton
    AccessPermissionRepository accessPermissionRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new CachingAccessPermissionRepository(
                new AccessPermissionImplJpaRepository(jpaRepoConfig),
                new AccessPermissionCacheFactory().createCache()
        );
    }

    @Provides
    @Singleton
    AccessRoleService accessRoleService(RoleRepository roleRepository,
                                        AccessInfoRepository accessInfoRepository,
                                        AccessRoleRepository accessRoleRepository,
                                        AuthorizationService authorizationService,
                                        PermissionFactory permissionFactory) {
        return new AccessRoleServiceImpl(
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authorization")),
                roleRepository,
                accessInfoRepository,
                accessRoleRepository,
                authorizationService,
                permissionFactory
        );
    }

    @Provides
    @Singleton
    AccessRoleRepository accessRoleRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new CachingAccessRoleRepository(
                new AccessRoleImplJpaRepository(jpaRepoConfig)
                , new AccessRoleCacheFactory().createCache()
        );
    }

}
