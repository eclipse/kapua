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
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.domain.DomainEntry;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.ChannelInfoFactory;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.MessageStoreFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.exception.DatastoreInternalError;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreElasticsearchClientConfiguration;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;
import org.eclipse.kapua.storage.TxContext;

import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;

public class DatastoreModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(ClientInfoFactory.class).to(ClientInfoFactoryImpl.class);
        bind(ClientInfoRepository.class).to(ClientInfoElasticsearchRepository.class).in(Singleton.class);
        bind(ClientInfoRegistryFacade.class).to(ClientInfoRegistryFacadeImpl.class).in(Singleton.class);
        bind(ClientInfoRegistryService.class).to(ClientInfoRegistryServiceImpl.class);

        bind(MetricInfoFactory.class).to(MetricInfoFactoryImpl.class);
        bind(MetricInfoRepository.class).to(MetricInfoRepositoryImpl.class).in(Singleton.class);
        bind(MetricInfoRegistryFacade.class).to(MetricInfoRegistryFacadeImpl.class).in(Singleton.class);
        bind(MetricInfoRegistryService.class).to(MetricInfoRegistryServiceImpl.class);

        bind(ChannelInfoFactory.class).to(ChannelInfoFactoryImpl.class);
        bind(ChannelInfoRepository.class).to(ChannelInfoElasticsearchRepository.class).in(Singleton.class);
        bind(ChannelInfoRegistryFacade.class).to(ChannelInfoRegistryFacadeImpl.class).in(Singleton.class);
        bind(ChannelInfoRegistryService.class).to(ChannelInfoRegistryServiceImpl.class);

        bind(MessageStoreFactory.class).to(MessageStoreFactoryImpl.class);
        bind(MessageRepository.class).to(MessageElasticsearchRepository.class).in(Singleton.class);
        bind(MessageStoreFacade.class).to(MessageStoreFacadeImpl.class).in(Singleton.class);
        bind(MetricsDatastore.class).in(Singleton.class);
    }

    @ProvidesIntoSet
    public Domain dataStoreDomain() {
        return new DomainEntry(Domains.DATASTORE, "org.eclipse.kapua.service.datastore.DatastoreService", false, Actions.read, Actions.delete, Actions.write);
    }

    @Provides
    @Singleton
    ConfigurationProvider configurationProvider(
            @Named("MessageStoreServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            KapuaJpaTxManagerFactory jpaTxManagerFactory,
            AccountService accountService
    ) {
        final ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(jpaTxManagerFactory.create("kapua-datastore"), serviceConfigurationManager, accountService);
        return configurationProvider;
    }


    @Provides
    @Singleton
    ElasticsearchClientProvider elasticsearchClientProvider(StorableIdFactory storableIdFactory) {
        ElasticsearchClientProvider<?> elasticsearchClientProvider;
        try {
            ElasticsearchClientConfiguration esClientConfiguration = DatastoreElasticsearchClientConfiguration.getInstance();

            Class<ElasticsearchClientProvider<?>> providerClass = (Class<ElasticsearchClientProvider<?>>) Class.forName(esClientConfiguration.getProviderClassName());
            Constructor<?> constructor = providerClass.getConstructor();
            elasticsearchClientProvider = (ElasticsearchClientProvider<?>) constructor.newInstance();

            elasticsearchClientProvider
                    .withClientConfiguration(esClientConfiguration)
                    .withModelContext(new ModelContextImpl(storableIdFactory))
                    .withModelConverter(new QueryConverterImpl())
                    .init();
        } catch (Exception e) {
            throw new DatastoreInternalError(e, "Cannot instantiate Elasticsearch Client");
        }

        return elasticsearchClientProvider;
    }

    @Provides
    @Singleton
    MessageStoreService messageStoreService(
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            @Named("MessageStoreServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            KapuaJpaTxManagerFactory jpaTxManagerFactory,
            MessageStoreFacade messageStoreFacade,
            MetricsDatastore metricsDatastore) {
        return new MessageStoreServiceImpl(
                jpaTxManagerFactory.create("kapua-datastore"),
                permissionFactory,
                authorizationService,
                serviceConfigurationManager,
                messageStoreFacade,
                metricsDatastore);
    }

    @Provides
    @Singleton
    @Named("MessageStoreServiceConfigurationManager")
    ServiceConfigurationManager messageStoreServiceConfigurationManager(
            RootUserTester rootUserTester,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(new ServiceConfigurationManagerImpl(
                MessageStoreService.class.getName(),
                new CachingServiceConfigRepository(
                        new ServiceConfigImplJpaRepository(jpaRepoConfig),
                        new AbstractKapuaConfigurableServiceCache().createCache()
                ),
                rootUserTester
        ) {
            @Override
            public boolean isServiceEnabled(TxContext txContext, KapuaId scopeId) {
                return !DatastoreSettings.getInstance().getBoolean(DatastoreSettingsKey.DISABLE_DATASTORE, false);
            }
        });
    }
}
