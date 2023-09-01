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
package org.eclipse.kapua.service.user.internal;

import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.RootUserTesterImpl;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.ServiceEventHouseKeeperFactoryImpl;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordRepository;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreServiceImpl;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.domain.DomainEntry;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserRepository;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;

import javax.inject.Named;
import javax.inject.Singleton;

public class UserModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(UserFactory.class).to(UserFactoryImpl.class);
        bind(UserCacheFactory.class).toInstance(new UserCacheFactory());
    }

    @Provides
    @Singleton
    RootUserTester rootUserTester(UserService userService) {
        return new RootUserTesterImpl(
                userService
        );
    }

    @ProvidesIntoSet
    public Domain userDomain() {
        return new DomainEntry(Domains.USER, UserService.class.getName(), false, Actions.read, Actions.delete, Actions.write);
    }

    @Provides
    @Singleton
    public UserService userService(
            @Named("UserServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            UserRepository userRepository,
            UserFactory userFactory,
            EventStorer eventStorer,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new UserServiceImpl(
                serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-user"),
                userRepository,
                userFactory,
                eventStorer);
    }

    @ProvidesIntoSet
    public ServiceModule userServiceModule(UserService userService,
                                           AuthorizationService authorizationService,
                                           PermissionFactory permissionFactory,
                                           KapuaJpaTxManagerFactory txManagerFactory,
                                           EventStoreFactory eventStoreFactory,
                                           EventStoreRecordRepository eventStoreRecordRepository,
                                           ServiceEventBus serviceEventBus
    ) throws ServiceEventBusException {
        return new UserServiceModule(
                userService,
                KapuaUserSetting.getInstance(),
                new ServiceEventHouseKeeperFactoryImpl(
                        new EventStoreServiceImpl(
                                authorizationService,
                                permissionFactory,
                                txManagerFactory.create("kapua-user"),
                                eventStoreFactory,
                                eventStoreRecordRepository
                        ),
                        txManagerFactory.create("kapua-user"),
                        serviceEventBus
                ), serviceEventBus);
    }

    @Provides
    @Singleton
    @Named("UserServiceConfigurationManager")
    ServiceConfigurationManager userServiceConfigurationManager(
            UserFactory userFactory,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            UserRepository userRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(UserService.class.getName(),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                userFactory,
                                userRepository
                        )));
    }

    @Provides
    @Singleton
    UserRepository userRepository(UserCacheFactory userCacheFactory, KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new UserCachedRepository(
                new UserImplJpaRepository(jpaRepoConfig),
                (NamedEntityCache) userCacheFactory.createCache()
        );
    }
}
