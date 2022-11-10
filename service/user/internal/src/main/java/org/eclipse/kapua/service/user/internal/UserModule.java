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
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerBase;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.RootUserTesterImpl;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.UserDomains;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserNamedEntityService;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Named;

public class UserModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(RootUserTester.class).to(RootUserTesterImpl.class);
        bind(UserNamedEntityService.class).to(UserNamedEntityServiceImpl.class);
        bind(UserFactory.class).to(UserFactoryImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserEntityManagerFactory.class).toInstance(new UserEntityManagerFactory());
        bind(UserCacheFactory.class).toInstance(new UserCacheFactory());
    }


    @Provides
    @Named("UserServiceConfigurationManager")
    ServiceConfigurationManager userServiceConfigurationManager(
            UserEntityManagerFactory userEntityManagerFactory,
            UserFactory userFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerBase(UserService.class.getName(),
                        UserDomains.USER_DOMAIN,
                        new EntityManagerSession(userEntityManagerFactory),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                userFactory,
                                UserDomains.USER_DOMAIN,
                                UserDAO::count,
                                authorizationService,
                                permissionFactory,
                                new EntityManagerSession(userEntityManagerFactory))
                ) {
                });
    }
}
