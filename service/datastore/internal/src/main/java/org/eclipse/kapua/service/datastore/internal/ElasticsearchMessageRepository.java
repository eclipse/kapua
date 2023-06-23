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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;

import javax.inject.Inject;

public class ElasticsearchMessageRepository extends ElasticsearchRepository<DatastoreMessage, MessageQuery> implements MessageRepository {
    private final StorablePredicateFactory storablePredicateFactory;

    @Inject
    public ElasticsearchMessageRepository(
            ElasticsearchClientProvider elasticsearchClientProviderInstance,
            StorablePredicateFactory storablePredicateFactory) {
        super(elasticsearchClientProviderInstance, MessageSchema.MESSAGE_TYPE_NAME, DatastoreMessage.class);
        this.storablePredicateFactory = storablePredicateFactory;
    }

    /**
     * Store a message
     *
     * @throws ClientException
     */
    @Override
    public String store(String indexName, DatastoreMessage messageToStore) throws ClientException {
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        InsertRequest insertRequest = new InsertRequest(messageToStore.getDatastoreId().toString(), typeDescriptor, messageToStore);
        return elasticsearchClientProviderInstance.getElasticsearchClient().insert(insertRequest).getId();
    }

    /**
     * Find message by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws QueryMappingException
     * @throws ClientException
     */
    @Override
    public DatastoreMessage find(KapuaId scopeId, String indexName, StorableId id)
            throws KapuaIllegalArgumentException, ClientException {
        return doFind(scopeId, indexName, id);
    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().refreshAllIndexes();
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteAllIndexes();
    }
    
    @Override
    public void deleteIndexes(String indexExp) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteIndexes(indexExp);
    }

    public DatastoreMessage doFind(KapuaId scopeId, String indexName, StorableId id) throws ClientException {
        MessageQueryImpl idsQuery = new MessageQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicate idsPredicate = storablePredicateFactory.newIdsPredicate(MessageSchema.MESSAGE_TYPE_NAME);
        idsPredicate.addId(id);
        idsQuery.setPredicate(idsPredicate);

        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        final DatastoreMessage res = (DatastoreMessage) elasticsearchClientProviderInstance.getElasticsearchClient().<DatastoreMessage>find(typeDescriptor, idsQuery, DatastoreMessage.class);
        return res;
    }
}
