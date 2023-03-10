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
package org.eclipse.kapua.service.device.registry;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionRepository;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.internal.CachingDeviceConnectionRepository;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionImplJpaRepository;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceImpl;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionRepository;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
import org.eclipse.kapua.service.device.registry.connection.option.internal.DeviceConnectionOptionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.option.internal.DeviceConnectionOptionImplJpaRepository;
import org.eclipse.kapua.service.device.registry.connection.option.internal.DeviceConnectionOptionServiceImpl;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventFactoryImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventImplJpaRepository;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.CachingDeviceRepository;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceImplJpaRepository;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCache;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCacheFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.service.device.registry.lifecycle.internal.DeviceLifeCycleServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

public class DeviceRegistryModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceRegistryCacheFactory.class).toInstance(new DeviceRegistryCacheFactory());
        bind(DeviceEntityManagerFactory.class).toInstance(new DeviceEntityManagerFactory());
        bind(DeviceFactory.class).to(DeviceFactoryImpl.class);
        bind(DeviceConnectionFactory.class).to(DeviceConnectionFactoryImpl.class);
        bind(DeviceConnectionOptionFactory.class).to(DeviceConnectionOptionFactoryImpl.class);
        bind(DeviceEventFactory.class).to(DeviceEventFactoryImpl.class);
        bind(DeviceLifeCycleService.class).to(DeviceLifeCycleServiceImpl.class);
    }

    @Provides
    @Singleton
    DeviceRegistryService deviceRegistryService(
            @Named("DeviceRegistryServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            DeviceRepository deviceRepository,
            DeviceFactory deviceFactory,
            AccessInfoFactory accessInfoFactory,
            AccessInfoRepository accessInfoRepository,
            AccessPermissionRepository accessPermissionRepository,
            AccessRoleRepository accessRoleRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository) {
        return new DeviceRegistryServiceImpl(
                serviceConfigurationManager,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                deviceRepository,
                deviceFactory,
                accessInfoFactory,
                accessInfoRepository,
                accessPermissionRepository,
                accessRoleRepository,
                roleRepository,
                rolePermissionRepository);
    }

    @Provides
    @Singleton
    DeviceRepository deviceRepository(DeviceFactory deviceFactory,
                                      DeviceRegistryCache deviceRegistryCache) {
        return new CachingDeviceRepository(new DeviceImplJpaRepository(),
                deviceRegistryCache
        );
    }

    @Provides
    @Singleton
    @Named("DeviceRegistryServiceConfigurationManager")
    ServiceConfigurationManager deviceRegistryServiceConfigurationManager(
            DeviceFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            DeviceRepository deviceRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        DeviceRegistryService.class.getName(),
                        DeviceDomains.DEVICE_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
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
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                                deviceRepository)
                ));
    }

    @Provides
    @Singleton
    DeviceConnectionService deviceConnectionService(
            @Named("DeviceConnectionServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceConnectionFactory entityFactory,
            DeviceConnectionRepository repository
    ) {
        return new DeviceConnectionServiceImpl(serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                entityFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                repository);
    }

    @Provides
    @Singleton
    @Named("DeviceConnectionServiceConfigurationManager")
    ServiceConfigurationManager deviceConnectionServiceConfigurationManager(
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester) {
        return new ServiceConfigurationManagerCachingWrapper(new ServiceConfigurationManagerImpl(
                DeviceConnectionService.class.getName(),
                DeviceDomains.DEVICE_CONNECTION_DOMAIN,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                new CachingServiceConfigRepository(
                        new ServiceConfigImplJpaRepository(),
                        new AbstractKapuaConfigurableServiceCache().createCache()
                ),
                permissionFactory,
                authorizationService,
                rootUserTester));
    }

    @Provides
    @Singleton
    DeviceRegistryCache deviceRegistryCache(DeviceRegistryCacheFactory deviceRegistryCacheFactory) {
        return (DeviceRegistryCache) deviceRegistryCacheFactory.createCache();
    }

    @Provides
    @Singleton
    DeviceConnectionRepository deviceConnectionRepository(DeviceConnectionFactory entityFactory,
                                                          DeviceRegistryCache deviceRegistryCache) {
        return new CachingDeviceConnectionRepository(
                new DeviceConnectionImplJpaRepository(),
                deviceRegistryCache
        );
    }

    @Provides
    @Singleton
    DeviceConnectionOptionService deviceConnectionOptionService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceConnectionRepository deviceConnectionRepository,
            DeviceConnectionFactory entityFactory,
            DeviceConnectionOptionRepository repository) {
        return new DeviceConnectionOptionServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                deviceConnectionRepository,
                entityFactory,
                repository);
    }

    @Provides
    @Singleton
    DeviceConnectionOptionRepository deviceConnectionOptionRepository() {
        return new DeviceConnectionOptionImplJpaRepository();
    }

    @Provides
    @Singleton
    DeviceEventService deviceEventService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceRepository deviceRepository,
            DeviceEventFactory entityFactory,
            DeviceEventRepository deviceEventRepository) {
        return new DeviceEventServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device")),
                deviceRepository,
                entityFactory,
                deviceEventRepository);
    }

    @Provides
    @Singleton
    DeviceEventRepository deviceEventRepository(DeviceEventFactory entityFactory) {
        return new DeviceEventImplJpaRepository();
    }
}
