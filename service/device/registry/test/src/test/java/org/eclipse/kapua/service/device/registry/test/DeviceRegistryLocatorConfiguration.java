/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.cucumber.java.Before;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.EventStorerImpl;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreRecordImplJpaRepository;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.internal.KapuaMessageFactoryImpl;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoImplJpaRepository;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionImplJpaRepository;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleImplJpaRepository;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImplJpaRepository;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImplJpaRepository;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionImplJpaRepository;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceImpl;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventFactoryImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventImplJpaRepository;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceImplJpaRepository;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCacheFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.storage.TxManager;
import org.mockito.Matchers;
import org.mockito.Mockito;

@Singleton
public class DeviceRegistryLocatorConfiguration {

    @Before(value = "@setup", order = 1)
    public void setupDI() {
        System.setProperty("locator.class.impl", "org.eclipse.kapua.qa.common.MockedLocator");
        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();
        final int maxInsertAttempts = 3;
        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                // Inject mocked Permission Factory
                final PermissionFactory permissionFactory = Mockito.mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(permissionFactory);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                bind(KapuaJpaRepositoryConfiguration.class).toInstance(new KapuaJpaRepositoryConfiguration());
                bind(AccountChildrenFinder.class).toInstance(Mockito.mock(AccountChildrenFinder.class));
                bind(AccountFactory.class).toInstance(Mockito.mock(AccountFactory.class));
                bind(AccountService.class).toInstance(Mockito.mock(AccountService.class));

                bind(RootUserTester.class).toInstance(Mockito.mock(RootUserTester.class));
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual Device registry service related services
                final DeviceRegistryCacheFactory deviceRegistryCacheFactory = new DeviceRegistryCacheFactory();
                bind(DeviceRegistryCacheFactory.class).toInstance(deviceRegistryCacheFactory);

                bind(DeviceFactory.class).toInstance(new DeviceFactoryImpl());
                final KapuaJpaRepositoryConfiguration jpaRepoConfig = new KapuaJpaRepositoryConfiguration();
                final TxManager txManager = new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-device");
                bind(DeviceConnectionService.class).toInstance(new DeviceConnectionServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        mockedAuthorization,
                        permissionFactory,
                        new DeviceConnectionFactoryImpl(),
                        txManager,
                        new DeviceConnectionImplJpaRepository(jpaRepoConfig)));
                bind(DeviceConnectionFactory.class).toInstance(new DeviceConnectionFactoryImpl());

                bind(DeviceRepository.class).toInstance(new DeviceImplJpaRepository(jpaRepoConfig));
                bind(DeviceConnectionRepository.class).toInstance(new DeviceConnectionImplJpaRepository(jpaRepoConfig));
                bind(DeviceEventRepository.class).toInstance(new DeviceEventImplJpaRepository(jpaRepoConfig));
                bind(DeviceEventService.class).toInstance(new DeviceEventServiceImpl(
                        mockedAuthorization,
                        permissionFactory,
                        txManager,
                        new DeviceImplJpaRepository(jpaRepoConfig),
                        new DeviceEventFactoryImpl(),
                        new DeviceEventImplJpaRepository(jpaRepoConfig)
                ));
                bind(DeviceEventFactory.class).toInstance(new DeviceEventFactoryImpl());
                bind(KapuaMessageFactory.class).toInstance(new KapuaMessageFactoryImpl());
                bind(DeviceRegistryService.class).toInstance(new DeviceRegistryServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        mockedAuthorization,
                        permissionFactory,
                        txManager,
                        new DeviceImplJpaRepository(jpaRepoConfig),
                        new DeviceFactoryImpl(),
                        new AccessInfoFactoryImpl(),
                        new AccessInfoImplJpaRepository(jpaRepoConfig),
                        new AccessPermissionImplJpaRepository(jpaRepoConfig),
                        new AccessRoleImplJpaRepository(jpaRepoConfig),
                        new RoleImplJpaRepository(jpaRepoConfig),
                        new RolePermissionImplJpaRepository(jpaRepoConfig),
                        new EventStorerImpl(new EventStoreRecordImplJpaRepository(jpaRepoConfig)))
                );
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}
