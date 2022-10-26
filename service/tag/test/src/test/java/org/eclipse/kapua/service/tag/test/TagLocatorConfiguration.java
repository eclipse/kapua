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
package org.eclipse.kapua.service.tag.test;

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
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.internal.KapuaMessageFactoryImpl;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountFactoryImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceImpl;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventFactoryImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.tag.internal.TagEntityManagerFactory;
import org.eclipse.kapua.service.tag.internal.TagFactoryImpl;
import org.eclipse.kapua.service.tag.internal.TagServiceImpl;
import org.mockito.Matchers;
import org.mockito.Mockito;

@Singleton
public class TagLocatorConfiguration {

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for service unit tests.
     */
    @Before(value = "@setup", order = 1)
    public void setupDI() {
        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                bind(PermissionFactory.class).toInstance(Mockito.mock(PermissionFactory.class));
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(QueryFactory.class).toInstance(new QueryFactoryImpl());

                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // binding Account related services
                bind(AccountChildrenFinder.class).toInstance(Mockito.mock(AccountChildrenFinder.class));
                bind(AccountService.class).toInstance(Mockito.mock(AccountService.class));
                bind(AccountFactory.class).toInstance(Mockito.spy(new AccountFactoryImpl()));
                bind(RootUserTester.class).toInstance(Mockito.mock(RootUserTester.class));

                // Inject actual Tag service related services
                bind(TagEntityManagerFactory.class).toInstance(new TagEntityManagerFactory());

                // Inject actual Device service related services
                DeviceEntityManagerFactory deviceEntityManagerFactory = DeviceEntityManagerFactory.getInstance();
                bind(DeviceEntityManagerFactory.class).toInstance(deviceEntityManagerFactory);

                bind(ServiceConfigurationManager.class)
                        .annotatedWith(Names.named("DeviceRegistryServiceConfigurationManager"))
                        .toInstance(Mockito.mock(ServiceConfigurationManager.class));
                bind(DeviceRegistryService.class).to(DeviceRegistryServiceImpl.class);
                bind(DeviceFactory.class).toInstance(new DeviceFactoryImpl());

                bind(ServiceConfigurationManager.class)
                        .annotatedWith(Names.named("DeviceConnectionServiceConfigurationManager"))
                        .toInstance(Mockito.mock(ServiceConfigurationManager.class));
                bind(DeviceConnectionService.class).to(DeviceConnectionServiceImpl.class);
                bind(DeviceConnectionFactory.class).to(DeviceConnectionFactoryImpl.class);

                bind(DeviceEventService.class).toInstance(new DeviceEventServiceImpl());
                bind(DeviceEventFactory.class).toInstance(new DeviceEventFactoryImpl());
                bind(KapuaMessageFactory.class).toInstance(new KapuaMessageFactoryImpl());
                bind(ServiceConfigurationManager.class)
                        .annotatedWith(Names.named("TagServiceConfigurationManager"))
                        .toInstance(Mockito.mock(ServiceConfigurationManager.class));

                bind(TagFactory.class).to(TagFactoryImpl.class);
                bind(TagService.class).to(TagServiceImpl.class);
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}