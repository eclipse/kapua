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
import com.google.inject.multibindings.ProvidesIntoSet;
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
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.ServiceEventHouseKeeperFactoryImpl;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordRepository;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreServiceImpl;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.GroupQueryHelper;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
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
        bind(DeviceFactory.class).to(DeviceFactoryImpl.class);
        bind(DeviceConnectionFactory.class).to(DeviceConnectionFactoryImpl.class);
        bind(DeviceConnectionOptionFactory.class).to(DeviceConnectionOptionFactoryImpl.class);
        bind(DeviceEventFactory.class).to(DeviceEventFactoryImpl.class);
        bind(DeviceLifeCycleService.class).to(DeviceLifeCycleServiceImpl.class);
    }

    @ProvidesIntoSet
    ServiceModule deviceRegistryModule(DeviceConnectionService deviceConnectionService,
                                       DeviceRegistryService deviceRegistryService,
                                       AuthorizationService authorizationService,
                                       PermissionFactory permissionFactory,
                                       KapuaJpaTxManagerFactory txManagerFactory,
                                       EventStoreFactory eventStoreFactory,
                                       EventStoreRecordRepository eventStoreRecordRepository
    ) throws ServiceEventBusException {
        return new DeviceServiceModule(
                deviceConnectionService,
                deviceRegistryService,
                KapuaDeviceRegistrySettings.getInstance(),
                new ServiceEventHouseKeeperFactoryImpl(
                        new EventStoreServiceImpl(
                                authorizationService,
                                permissionFactory,
                                txManagerFactory.create("kapua-device"),
                                eventStoreFactory,
                                eventStoreRecordRepository
                        ),
                        txManagerFactory.create("kapua-device")
                ));
    }

    @Provides
    @Singleton
    DeviceRegistryService deviceRegistryService(
            @Named("DeviceRegistryServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceRepository deviceRepository,
            DeviceFactory deviceFactory,
            GroupQueryHelper groupQueryHelper,
            EventStorer eventStorer,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DeviceRegistryServiceImpl(
                serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-device"),
                deviceRepository,
                deviceFactory,
                groupQueryHelper,
                eventStorer);
    }

    @Provides
    @Singleton
    DeviceRepository deviceRepository(DeviceFactory deviceFactory,
                                      DeviceRegistryCache deviceRegistryCache,
                                      KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new CachingDeviceRepository(new DeviceImplJpaRepository(jpaRepoConfig),
                deviceRegistryCache
        );
    }

    @Provides
    @Singleton
    @Named("DeviceRegistryServiceConfigurationManager")
    ServiceConfigurationManager deviceRegistryServiceConfigurationManager(
            DeviceFactory factory,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            DeviceRepository deviceRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        DeviceRegistryService.class.getName(),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
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
            DeviceConnectionRepository repository,
            KapuaJpaTxManagerFactory jpaTxManagerFactory
    ) {
        return new DeviceConnectionServiceImpl(serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                entityFactory,
                jpaTxManagerFactory.create("kapua-device"),
                repository);
    }

    @Provides
    @Singleton
    @Named("DeviceConnectionServiceConfigurationManager")
    ServiceConfigurationManager deviceConnectionServiceConfigurationManager(
            RootUserTester rootUserTester,
            KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new ServiceConfigurationManagerCachingWrapper(new ServiceConfigurationManagerImpl(
                DeviceConnectionService.class.getName(),
                new CachingServiceConfigRepository(
                        new ServiceConfigImplJpaRepository(jpaRepoConfig),
                        new AbstractKapuaConfigurableServiceCache().createCache()
                ),
                rootUserTester));
    }

    @Provides
    @Singleton
    DeviceRegistryCache deviceRegistryCache(DeviceRegistryCacheFactory deviceRegistryCacheFactory) {
        return (DeviceRegistryCache) deviceRegistryCacheFactory.createCache();
    }

    @Provides
    @Singleton
    DeviceConnectionRepository deviceConnectionRepository(DeviceRegistryCache deviceRegistryCache,
                                                          KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new CachingDeviceConnectionRepository(
                new DeviceConnectionImplJpaRepository(jpaRepoConfig),
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
            DeviceConnectionOptionRepository repository,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DeviceConnectionOptionServiceImpl(
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-device"),
                deviceConnectionRepository,
                entityFactory,
                repository);
    }

    @Provides
    @Singleton
    DeviceConnectionOptionRepository deviceConnectionOptionRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new DeviceConnectionOptionImplJpaRepository(jpaRepoConfig);
    }

    @Provides
    @Singleton
    DeviceEventService deviceEventService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceRepository deviceRepository,
            DeviceEventFactory entityFactory,
            DeviceEventRepository deviceEventRepository,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DeviceEventServiceImpl(
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-device"),
                deviceRepository,
                entityFactory,
                deviceEventRepository);
    }

    @Provides
    @Singleton
    DeviceEventRepository deviceEventRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new DeviceEventImplJpaRepository(jpaRepoConfig);
    }
}
