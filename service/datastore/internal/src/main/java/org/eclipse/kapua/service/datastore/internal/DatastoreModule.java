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
package org.eclipse.kapua.service.datastore.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigTransactedRepository;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaTransactedRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.ChannelInfoFactory;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreDomains;
import org.eclipse.kapua.service.datastore.MessageStoreFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;

import javax.inject.Named;
import javax.inject.Singleton;

public class DatastoreModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(ChannelInfoFactory.class).to(ChannelInfoFactoryImpl.class);
        bind(ChannelInfoRegistryService.class).to(ChannelInfoRegistryServiceImpl.class);
        bind(ClientInfoFactory.class).to(ClientInfoFactoryImpl.class);
        bind(ClientInfoRegistryService.class).to(ClientInfoRegistryServiceImpl.class);
        bind(MessageStoreFactory.class).to(MessageStoreFactoryImpl.class);
        bind(MessageStoreService.class).to(MessageStoreServiceImpl.class);
        bind(MetricInfoFactory.class).to(MetricInfoFactoryImpl.class);
        bind(MetricInfoRegistryService.class).to(MetricInfoRegistryServiceImpl.class);
    }

    @Provides
    @Singleton
    @Named("MessageStoreServiceConfigurationManager")
    ServiceConfigurationManager messageStoreServiceConfigurationManager(
            DatastoreEntityManagerFactory entityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester
    ) {
        return new ServiceConfigurationManagerCachingWrapper(new ServiceConfigurationManagerImpl(
                MessageStoreService.class.getName(),
                DatastoreDomains.DATASTORE_DOMAIN,
                new CachingServiceConfigTransactedRepository(
                        new ServiceConfigImplJpaTransactedRepository(
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-datastore"))),
                        new AbstractKapuaConfigurableServiceCache().createCache()
                ),
                permissionFactory,
                authorizationService,
                rootUserTester
        ) {
            @Override
            public boolean isServiceEnabled(KapuaId scopeId) {
                return !DatastoreSettings.getInstance().getBoolean(DatastoreSettingsKey.DISABLE_DATASTORE, false);
            }
        });
    }
}
