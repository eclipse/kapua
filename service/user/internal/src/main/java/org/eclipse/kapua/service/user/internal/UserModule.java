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
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.UserDomains;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserRepository;
import org.eclipse.kapua.service.user.UserService;

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
    RootUserTester rootUserTester(UserRepository userRepository) {
        return new RootUserTesterImpl(
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-user")),
                userRepository
        );
    }

    @Provides
    @Singleton
    public UserService userService(
            @Named("UserServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            UserRepository userRepository,
            UserFactory userFactory) {
        return new UserServiceImpl(
                serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-user")),
                userRepository,
                userFactory,
                new DuplicateNameCheckerImpl<>(userRepository, userFactory::newQuery), eventStorer);
    }

    @Provides
    @Singleton
    @Named("UserServiceConfigurationManager")
    ServiceConfigurationManager userServiceConfigurationManager(
            UserFactory userFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            UserRepository userRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(UserService.class.getName(),
                        UserDomains.USER_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-user")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                userFactory,
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-user")),
                                userRepository
                        )));
    }

    @Provides
    @Singleton
    UserRepository userRepository(UserCacheFactory userCacheFactory) {
        return new UserCachedRepository(
                new UserImplJpaRepository(),
                (NamedEntityCache) userCacheFactory.createCache()
        );
    }
}
