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
import com.google.inject.name.Names;
import io.cucumber.java.Before;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.jpa.EventStorerImpl;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.metric.MetricsServiceImpl;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreRecordImplJpaRepository;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.internal.KapuaMessageFactoryImpl;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.GroupQueryHelper;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.authentication.UserPassDeviceConnectionCredentialAdapter;
import org.eclipse.kapua.service.device.authentication.api.DeviceConnectionCredentialAdapter;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.KapuaDeviceRegistrySettingKeys;
import org.eclipse.kapua.service.device.registry.KapuaDeviceRegistrySettings;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.service.device.registry.common.DeviceValidationImpl;
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
import org.eclipse.kapua.service.tag.internal.TagFactoryImpl;
import org.eclipse.kapua.service.tag.internal.TagImplJpaRepository;
import org.eclipse.kapua.service.tag.internal.TagServiceImpl;
import org.eclipse.kapua.storage.TxManager;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

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
                bind(String.class).annotatedWith(Names.named("metricModuleName")).toInstance("tests");
                bind(MetricsService.class).to(MetricsServiceImpl.class).in(Singleton.class);

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
                final CredentialsFactory credentialsFactory = Mockito.mock(CredentialsFactory.class);
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                bind(CredentialsFactory.class).toInstance(credentialsFactory);
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

                final Map<String, DeviceConnectionCredentialAdapter> availableDeviceConnectionAdapters = new HashMap<>();
                availableDeviceConnectionAdapters.put("USER_PASS", new UserPassDeviceConnectionCredentialAdapter(credentialsFactory));
                bind(ServiceConfigurationManager.class)
                        .annotatedWith(Names.named("DeviceConnectionServiceConfigurationManager"))
                        .toInstance(Mockito.mock(ServiceConfigurationManager.class));
                bind(DeviceFactory.class).toInstance(new DeviceFactoryImpl());
                final KapuaJpaRepositoryConfiguration jpaRepoConfig = new KapuaJpaRepositoryConfiguration();
                final TxManager txManager = new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-device");
                final EventStorer eventStorer = new EventStorerImpl(new EventStoreRecordImplJpaRepository(jpaRepoConfig));
                final DeviceConnectionService deviceConnectionService = new DeviceConnectionServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        mockedAuthorization,
                        permissionFactory,
                        new DeviceConnectionFactoryImpl(),
                        txManager,
                        new DeviceConnectionImplJpaRepository(jpaRepoConfig),
                        availableDeviceConnectionAdapters,
                        eventStorer);
                bind(DeviceConnectionService.class).toInstance(deviceConnectionService);
                bind(DeviceConnectionFactory.class).toInstance(new DeviceConnectionFactoryImpl());

                bind(DeviceRepository.class).toInstance(new DeviceImplJpaRepository(jpaRepoConfig));
                bind(DeviceConnectionRepository.class).toInstance(new DeviceConnectionImplJpaRepository(jpaRepoConfig));
                bind(DeviceEventRepository.class).toInstance(new DeviceEventImplJpaRepository(jpaRepoConfig));
                final DeviceEventService deviceEventService = new DeviceEventServiceImpl(
                        mockedAuthorization,
                        permissionFactory,
                        txManager,
                        new DeviceImplJpaRepository(jpaRepoConfig),
                        new DeviceEventFactoryImpl(),
                        new DeviceEventImplJpaRepository(jpaRepoConfig)
                );
                bind(DeviceEventService.class).toInstance(deviceEventService);
                bind(DeviceEventFactory.class).toInstance(new DeviceEventFactoryImpl());
                bind(KapuaMessageFactory.class).toInstance(new KapuaMessageFactoryImpl());

                final DeviceValidation deviceValidation = new DeviceValidationImpl(KapuaDeviceRegistrySettings.getInstance().getInt(KapuaDeviceRegistrySettingKeys.DEVICE_LIFECYCLE_BIRTH_VAR_FIELDS_LENGTH_MAX),
                        KapuaDeviceRegistrySettings.getInstance().getInt(KapuaDeviceRegistrySettingKeys.DEVICE_LIFECYCLE_BIRTH_EXTENDED_PROPERTIES_LENGTH_MAX),
                        mockedAuthorization,
                        permissionFactory,
                        Mockito.mock(GroupService.class),
                        deviceConnectionService,
                        deviceEventService,
                        new DeviceImplJpaRepository(jpaRepoConfig),
                        new DeviceFactoryImpl(),
                        new TagServiceImpl(
                                permissionFactory,
                                mockedAuthorization,
                                Mockito.mock(ServiceConfigurationManager.class),
                                new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-tag"),
                                new TagImplJpaRepository(jpaRepoConfig),
                                new TagFactoryImpl())
                );

                bind(DeviceValidation.class).toInstance(deviceValidation);
                bind(DeviceRegistryService.class).toInstance(new DeviceRegistryServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        mockedAuthorization,
                        permissionFactory,
                        txManager,
                        new DeviceImplJpaRepository(jpaRepoConfig),
                        new DeviceFactoryImpl(),
                        Mockito.mock(GroupQueryHelper.class),
                        eventStorer,
                        deviceValidation)
                );
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}
