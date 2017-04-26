/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch.dao;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.datastore.internal.elasticsearch.ElasticsearchClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageObjectBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Message DAO
 *
 * @since 1.0.0
 */
public class EsMessageDAO {

    private final EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     *
     * @throws EsClientUnavailableException
     * @since 1.0.0
     */
    public EsMessageDAO() throws EsClientUnavailableException {
        esTypeDAO = new EsTypeDAO(ElasticsearchClient.getInstance());
    }

    /**
     * Set the dao listener
     *
     * @param daoListener
     * @return
     * @throws EsDatastoreException
     * @since 1.0.0
     */
    public EsMessageDAO setListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        esTypeDAO.setListener(daoListener);
        return this;
    }

    /**
     * Unset the dao listener
     *
     * @param daoListener
     * @return
     * @throws EsDatastoreException
     * @since 1.0.0
     */
    public EsMessageDAO unsetListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Message DAO instance factory
     *
     * @return
     * @throws EsClientUnavailableException
     * @since 1.0.0
     */
    public static EsMessageDAO getInstance() throws EsClientUnavailableException {
        return new EsMessageDAO();
    }

    /**
     * Set the index name
     *
     * @param indexName
     * @return
     * @since 1.0.0
     */
    public EsMessageDAO index(String indexName) {
        esTypeDAO.type(indexName, EsSchema.MESSAGE_TYPE_NAME);
        return this;
    }

    /**
     * Build the upsert request
     *
     * @param id
     * @param esClient
     * @return
     * @since 1.0.0
     */
    public UpdateRequest getUpsertRequest(String id, XContentBuilder esClient) {
        return esTypeDAO.getUpsertRequest(id, esClient);
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     *
     * @param id
     * @param esClient
     * @return
     * @since 1.0.0
     */
    public UpdateResponse upsert(String id, XContentBuilder esClient) {
        return esTypeDAO.upsert(id, esClient);
    }

    /**
     * Delete query action (delete documents from the database)
     *
     * @param query
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    public void deleteByQuery(MessageQuery query)
            throws EsQueryConversionException {
        esTypeDAO.deleteByQuery(PredicateConverter.convertQueryPredicates(query));
    }

    /**
     * Query action (return objects matching the given query)
     *
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     * @since 1.0.0
     */
    public MessageListResult query(MessageQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException,
            EsObjectBuilderException {
        MessageQueryImpl localQuery = new MessageQueryImpl(query.getScopeId());
        localQuery.copy(query);
        localQuery.setLimit(query.getLimit() + 1);

        MessageQueryConverter converter = new MessageQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), localQuery);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null || searchHits.getTotalHits() == 0) {
            return new MessageListResultImpl();
        }

        int i = 0;
        int searchHitsSize = searchHits.getHits().length;
        List<DatastoreMessage> messages = new ArrayList<>();
        MessageObjectBuilder msgBuilder = new MessageObjectBuilder();
        for (SearchHit searchHit : searchHits.getHits()) {
            if (i < query.getLimit()) {
                DatastoreMessage message = msgBuilder.build(searchHit, query.getFetchStyle()).getMessage();
                messages.add(message);
            }
            i++;
        }

        // TODO check equivalence with CX
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHitsSize > query.getLimit()) {
            DatastoreMessage lastMessage = msgBuilder.build(searchHits.getHits()[searchHitsSize - 1], query.getFetchStyle()).getMessage();
            nextKey = lastMessage.getTimestamp().getTime();
        }

        // TODO verifiy total count
        Long totalCount = null;
        if (query.isAskTotalCount()) {
            // totalCount = MessageDAO.findByTopicCount(keyspace, topic, start, end);
            totalCount = searchHits.getTotalHits();
        }

        if (totalCount != null && totalCount > Integer.MAX_VALUE) {
            throw new RuntimeException("Total hits exceeds integer max value");
        }

        MessageListResult result = new MessageListResultImpl(nextKey, totalCount);
        result.addItems(messages);
        return result;
    }

    /**
     * Query count action (return the count of the objects matching the given query)
     *
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @since 1.0.0
     */
    public long count(MessageQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException {
        MessageQueryConverter converter = new MessageQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null) {
            return 0;
        }

        return searchHits.getTotalHits();
    }

    /**
     * Deletes by message id
     *
     * @param id
     * @since 1.0.0
     */
    public void deleteById(String id) {

        esTypeDAO.getClient().prepareDelete()
                .setIndex(esTypeDAO.getIndexName())
                .setType(esTypeDAO.getTypeName())
                .setId(id)
                .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    }
}
