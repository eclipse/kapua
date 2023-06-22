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
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.eclipse.kapua.storage.TxContext;

import javax.inject.Named;
import javax.inject.Singleton;

public class DatastoreModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(MessageRepository.class).to(ElasticsearchMessageRepository.class).in(Singleton.class);
        bind(DatastoreMediator.class).in(Singleton.class);
        bind(MessageStoreMediator.class).to(DatastoreMediator.class);
        bind(ClientInfoRegistryMediator.class).to(DatastoreMediator.class);
        bind(ChannelInfoRegistryMediator.class).to(DatastoreMediator.class);
        bind(MetricInfoRegistryMediator.class).to(DatastoreMediator.class);

        bind(ChannelInfoFactory.class).to(ChannelInfoFactoryImpl.class);
        bind(ChannelInfoRegistryService.class).to(ChannelInfoRegistryServiceImpl.class);
        bind(ClientInfoFactory.class).to(ClientInfoFactoryImpl.class);
        bind(ClientInfoRegistryService.class).to(ClientInfoRegistryServiceImpl.class);
        bind(MessageStoreFactory.class).to(MessageStoreFactoryImpl.class);
        bind(MetricInfoFactory.class).to(MetricInfoFactoryImpl.class);
        bind(MetricInfoRegistryService.class).to(MetricInfoRegistryServiceImpl.class);
    }

    @ProvidesIntoSet
    public Domain dataStoreDomain() {
        return new DomainEntry(Domains.DATASTORE, "org.eclipse.kapua.service.datastore.DatastoreService", false, Actions.read, Actions.delete, Actions.write);
    }

    @Provides
    @Singleton
    ClientInfoRegistryFacade clientInfoRegistryFacade(
            ConfigurationProvider configProvider,
            StorableIdFactory storableIdFactory,
            StorablePredicateFactory storablePredicateFactory,
            ClientInfoRegistryMediator mediator) {
        return new ClientInfoRegistryFacadeImpl(configProvider, storableIdFactory, storablePredicateFactory, mediator);
    }

    @Provides
    @Singleton
    MetricInfoRegistryFacade metricInfoRegistryFacade(
            ConfigurationProvider configProvider,
            StorableIdFactory storableIdFactory,
            StorablePredicateFactory storablePredicateFactory,
            MetricInfoRegistryMediator mediator) {
        return new MetricInfoRegistryFacadeImpl(configProvider, storableIdFactory, storablePredicateFactory, mediator);
    }

    @Provides
    @Singleton
    ChannelInfoRegistryFacade channelInfoRegistryFacade(
            ConfigurationProvider configProvider,
            StorableIdFactory storableIdFactory,
            StorablePredicateFactory storablePredicateFactory,
            ChannelInfoRegistryMediator mediator) {
        return new ChannelInfoRegistryFacadeImpl(configProvider, storableIdFactory, storablePredicateFactory, mediator);
    }

    @Provides
    @Singleton
    MessageStoreFacade messageStoreFacade(
            ConfigurationProvider configurationProvider,
            StorableIdFactory storableIdFactory,
            StorablePredicateFactory storablePredicateFactory,
            ClientInfoRegistryFacade clientInfoRegistryFacade,
            ChannelInfoRegistryFacade channelInfoStoreFacade,
            MetricInfoRegistryFacade metricInfoStoreFacade,
            MessageStoreMediator mediator,
            MessageRepository messageRepository
    ) {

        return new MessageStoreFacadeImpl(
                configurationProvider,
                storableIdFactory,
                storablePredicateFactory,
                clientInfoRegistryFacade,
                channelInfoStoreFacade,
                metricInfoStoreFacade,
                mediator, messageRepository);
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
    MessageStoreService messageStoreService(
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            @Named("MessageStoreServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            KapuaJpaTxManagerFactory jpaTxManagerFactory,
            MessageStoreFacade messageStoreFacade) {
        return new MessageStoreServiceImpl(
                jpaTxManagerFactory.create("kapua-datastore"),
                permissionFactory,
                authorizationService,
                serviceConfigurationManager,
                messageStoreFacade);
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
