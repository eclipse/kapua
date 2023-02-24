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
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceImpl;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
import org.eclipse.kapua.service.device.registry.connection.option.internal.DeviceConnectionOptionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.option.internal.DeviceConnectionOptionServiceImpl;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventFactoryImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceImplJpaRepository;
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
        bind(DeviceRegistryService.class).to(DeviceRegistryServiceImpl.class);
        bind(DeviceEntityManagerFactory.class).toInstance(new DeviceEntityManagerFactory());
        bind(DeviceFactory.class).to(DeviceFactoryImpl.class);

        bind(DeviceConnectionFactory.class).to(DeviceConnectionFactoryImpl.class);
        bind(DeviceConnectionService.class).to(DeviceConnectionServiceImpl.class);

        bind(DeviceConnectionOptionFactory.class).to(DeviceConnectionOptionFactoryImpl.class);
        bind(DeviceConnectionOptionService.class).to(DeviceConnectionOptionServiceImpl.class);

        bind(DeviceEventFactory.class).to(DeviceEventFactoryImpl.class);
        bind(DeviceEventService.class).to(DeviceEventServiceImpl.class);

        bind(DeviceLifeCycleService.class).to(DeviceLifeCycleServiceImpl.class);
    }

    @Provides
    @Singleton
    @Named("DeviceConnectionServiceConfigurationManager")
    ServiceConfigurationManager deviceConnectionServiceConfigurationManager(
            DeviceEntityManagerFactory deviceEntityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester) {
        return new ServiceConfigurationManagerCachingWrapper(new ServiceConfigurationManagerImpl(
                DeviceConnectionService.class.getName(),
                DeviceDomains.DEVICE_CONNECTION_DOMAIN,
                new ServiceConfigImplJpaRepository(new EntityManagerSession(deviceEntityManagerFactory)),
                permissionFactory,
                authorizationService,
                rootUserTester));
    }

    @Provides
    @Singleton
    @Named("DeviceRegistryServiceConfigurationManager")
    ServiceConfigurationManager deviceRegistryServiceConfigurationManager(
            DeviceEntityManagerFactory deviceEntityManagerFactory,
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
                        new ServiceConfigImplJpaRepository(new EntityManagerSession(deviceEntityManagerFactory)),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                DeviceDomains.DEVICE_DOMAIN,
                                deviceRepository,
                                authorizationService,
                                permissionFactory)
                ));
    }

    @Provides
    @Singleton
    DeviceRepository deviceRepository(DeviceFactory deviceFactory) {
        return new DeviceImplJpaRepository(
                () -> deviceFactory.newListResult(),
                new EntityManagerSession(new AbstractEntityManagerFactory("kapua-device") {
                }));
    }
}
