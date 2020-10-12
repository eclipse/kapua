/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRegistryFacade {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegistryFacade.class);

    private final ConfigurationProvider configProvider;

    public AbstractRegistryFacade(ConfigurationProvider configProvider) {
        this.configProvider = configProvider;
    }

    public ConfigurationProvider getConfigProvider() {
        return configProvider;
    }

    protected boolean isDatastoreServiceEnabled(KapuaId scopeId) throws ConfigurationException {
        MessageStoreConfiguration messageStoreConfiguration = configProvider.getConfiguration(scopeId);
        long ttl = messageStoreConfiguration.getDataTimeToLiveMilliseconds();

        return messageStoreConfiguration.getDataStorageEnabled() && ttl != MessageStoreConfiguration.DISABLED;
    }

    protected ElasticsearchClient<?> getElasticsearchClient() throws ClientUnavailableException {
        return DatastoreClientFactory.getElasticsearchClient();
    }
}
