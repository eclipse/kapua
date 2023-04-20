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
import com.google.inject.multibindings.ProvidesIntoSet;
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
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.ServiceEventHouseKeeperFactoryImpl;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordRepository;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreServiceImpl;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.event.ServiceEventBusException;
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
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;

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

    @ProvidesIntoSet
    ServiceModule authorizationServiceModule(AccessInfoService accessInfoService,
                                             RoleService roleService,
                                             DomainRegistryService domainRegistryService,
                                             GroupService groupService,
                                             AuthorizationService authorizationService,
                                             PermissionFactory permissionFactory,
                                             KapuaJpaTxManagerFactory txManagerFactory,
                                             EventStoreFactory eventStoreFactory,
                                             EventStoreRecordRepository eventStoreRecordRepository
    ) throws ServiceEventBusException {
        return new AuthorizationServiceModule(
                accessInfoService,
                roleService,
                domainRegistryService,
                groupService,
                KapuaAuthorizationSetting.getInstance(),
                new ServiceEventHouseKeeperFactoryImpl(
                        new EventStoreServiceImpl(
                                authorizationService,
                                permissionFactory,
                                txManagerFactory.create("kapua-authorization"),
                                eventStoreFactory,
                                eventStoreRecordRepository
                        ),
                        txManagerFactory.create("kapua-authorization")
                ));
    }

    @Provides
    @Singleton
    DomainRegistryService domainRegistryService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DomainRepository domainRepository,
            DomainFactory domainFactory,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DomainRegistryServiceImpl(
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-authorization"),
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
                                                RolePermissionRepository rolePermissionRepository,
                                                KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new RolePermissionServiceImpl(
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-authorization"),
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
                            RolePermissionRepository rolePermissionRepository,
                            KapuaJpaTxManagerFactory jpaTxManagerFactory
    ) {
        return new RoleServiceImpl(
                permissionFactory,
                authorizationService,
                rolePermissionFactory,
                serviceConfigurationManager,
                jpaTxManagerFactory.create("kapua-authorization"),
                roleRepository,
                rolePermissionRepository
        );
    }

    @Provides
    @Singleton
    @Named("RoleServiceConfigurationManager")
    public ServiceConfigurationManager roleServiceConfigurationManager(
            RoleFactory roleFactory,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            RoleRepository roleRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        RoleService.class.getName(),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                roleFactory,
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
                              GroupRepository groupRepository,
                              KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new GroupServiceImpl(permissionFactory, authorizationService, serviceConfigurationManager,
                jpaTxManagerFactory.create("kapua-authorization"),
                groupRepository);
    }

    @Provides
    @Singleton
    @Named("GroupServiceConfigurationManager")
    public ServiceConfigurationManager groupServiceConfigurationManager(
            GroupFactory factory,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            GroupRepository groupRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        GroupService.class.getName(),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
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
            AccessPermissionFactory accessPermissionFactory,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new AccessInfoServiceImpl(authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-authorization"),
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
            AccessInfoRepository accessInfoRepository,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new AccessPermissionServiceImpl(authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-authorization"),
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
                                        PermissionFactory permissionFactory,
                                        KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new AccessRoleServiceImpl(
                jpaTxManagerFactory.create("kapua-authorization"),
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
