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
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.TagDomains;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagRepository;
import org.eclipse.kapua.service.tag.TagService;

import javax.inject.Named;
import javax.inject.Singleton;

public class TagModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(TagFactory.class).to(TagFactoryImpl.class);
    }

    @Provides
    @Singleton
    TagService tagService(
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            @Named("TagServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            TagRepository tagRepository,
            TagFactory tagFactory,
            @Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new TagServiceImpl(permissionFactory, authorizationService, serviceConfigurationManager,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-tag"), maxInsertAttempts),
                tagRepository,
                tagFactory);
    }

    @Provides
    @Singleton
    @Named("TagServiceConfigurationManager")
    ServiceConfigurationManager tagServiceConfigurationManager(
            TagFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            TagRepository tagRepository,
            @Named("maxInsertAttempts") Integer maxInsertAttempts
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        TagService.class.getName(),
                        TagDomains.TAG_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-tag"), maxInsertAttempts),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(new KapuaJpaRepositoryConfiguration()),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-tag"), maxInsertAttempts),
                                tagRepository
                        )));
    }

    @Provides
    @Singleton
    TagRepository tagRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new TagImplJpaRepository(jpaRepoConfig);
    }
}
