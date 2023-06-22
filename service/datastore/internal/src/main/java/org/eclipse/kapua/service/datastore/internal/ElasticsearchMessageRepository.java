/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ElasticsearchMessageRepository implements MessageRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ConfigurationProvider configProvider;
    private final ElasticsearchClientProvider elasticsearchClientProviderInstance;

    @Inject
    public ElasticsearchMessageRepository(ConfigurationProvider configProvider, ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        this.configProvider = configProvider;
        this.elasticsearchClientProviderInstance = elasticsearchClientProviderInstance;
    }

    public boolean isDatastoreServiceEnabled(KapuaId scopeId) throws ConfigurationException {
        MessageStoreConfiguration messageStoreConfiguration = configProvider.getConfiguration(scopeId);
        long ttl = messageStoreConfiguration.getDataTimeToLiveMilliseconds();

        return messageStoreConfiguration.getDataStorageEnabled() && ttl != MessageStoreConfiguration.DISABLED;
    }

    protected ElasticsearchClient<?> getElasticsearchClient() throws ClientUnavailableException {
        return elasticsearchClientProviderInstance.getElasticsearchClient();
    }

    @Override
    public MessageListResult query(MessageQuery query) {
        try {

            if (!this.isDatastoreServiceEnabled(query.getScopeId())) {
                logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
                return new MessageListResultImpl();
            }

            String dataIndexName = SchemaUtil.getDataIndexName(query.getScopeId());
            TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME);
            final ResultList<DatastoreMessage> queried = getElasticsearchClient().query(typeDescriptor, query, DatastoreMessage.class);
            MessageListResult result = new MessageListResultImpl(queried);
            AbstractRegistryFacade.setLimitExceed(query, queried.getTotalHitsExceedsCount(), result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
