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
package org.eclipse.kapua.service.tag.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerBase;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.TagDomains;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagService;

import javax.inject.Named;

public class TagModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(TagService.class).to(TagServiceImpl.class);
        bind(TagFactory.class).to(TagFactoryImpl.class);
    }

    @Provides
    @Named("TagServiceConfigurationManager")
    ServiceConfigurationManager userServiceConfigurationManager(
            TagEntityManagerFactory entityManagerFactory,
            TagFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerBase(
                        TagService.class.getName(),
                        TagDomains.TAG_DOMAIN,
                        new EntityManagerSession(entityManagerFactory),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                TagDomains.TAG_DOMAIN,
                                TagDAO::count,
                                authorizationService,
                                permissionFactory,
                                new EntityManagerSession(entityManagerFactory)
                        )) {
                });
    }
}
