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
 * @since 1.0
 *
 */
public class EsMessageDAO
{

    private EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     * 
     * @throws EsClientUnavailableException
     */
    public EsMessageDAO() throws EsClientUnavailableException
    {
        esTypeDAO = new EsTypeDAO(ElasticsearchClient.getInstance());
    }

    /**
     * Set the dao listener
     * 
     * @param daoListener
     * @return
     * @throws EsDatastoreException
     */
    public EsMessageDAO setListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.setListener(daoListener);
        return this;
    }

    /**
     * Unset the dao listener
     * 
     * @param daoListener
     * @return
     * @throws EsDatastoreException
     */
    public EsMessageDAO unsetListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Message DAO instance factory
     * 
     * @return
     * @throws EsClientUnavailableException
     */
    public static EsMessageDAO getInstance() throws EsClientUnavailableException
    {
        return new EsMessageDAO();
    }

    /**
     * Set the index name
     * 
     * @param indexName
     * @return
     */
    public EsMessageDAO index(String indexName)
    {
        this.esTypeDAO.type(indexName, EsSchema.MESSAGE_TYPE_NAME);
        return this;
    }

    /**
     * Build the upsert request
     * 
     * @param id
     * @param esClient
     * @return
     */
    public UpdateRequest getUpsertRequest(String id, XContentBuilder esClient)
    {
        return this.esTypeDAO.getUpsertRequest(id, esClient);
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param id
     * @param esClient
     * @return
     */
    public UpdateResponse upsert(String id, XContentBuilder esClient)
    {
        return this.esTypeDAO.upsert(id, esClient);
    }

    /**
     * Delete query action (delete documents from the database)
     * 
     * @param query
     * @throws EsQueryConversionException
     */
    public void deleteByQuery(MessageQuery query)
        throws EsQueryConversionException
    {
        PredicateConverter pc = new PredicateConverter();
        this.esTypeDAO.deleteByQuery(pc.toElasticsearchQuery(query.getPredicate()));
    }

    /**
     * Query action (return objects matching the given query)
     * 
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     */
    public MessageListResult query(MessageQuery query)
        throws EsQueryConversionException,
        EsClientUnavailableException,
        EsObjectBuilderException
    {
        MessageQueryImpl localQuery = new MessageQueryImpl();
        localQuery.copy(query);
        localQuery.setLimit(query.getLimit() + 1);

        MessageQueryConverter converter = new MessageQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null || searchHits.getTotalHits() == 0)
            return new MessageListResultImpl();

        int i = 0;
        int searchHitsSize = searchHits.getHits().length;
        List<DatastoreMessage> messages = new ArrayList<DatastoreMessage>();
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

        if (totalCount != null && totalCount > Integer.MAX_VALUE)
            throw new RuntimeException("Total hits exceeds integer max value");

        MessageListResultImpl result = new MessageListResultImpl(nextKey, totalCount);
        result.addAll(messages);
        return result;
    }

    /**
     * Query count action (return the count of the objects matching the given query)
     * 
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public long count(MessageQuery query)
        throws EsQueryConversionException,
        EsClientUnavailableException
    {
        MessageQueryConverter converter = new MessageQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }

    //
    // public SearchHits findByTopic(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // long start,
    // long end,
    // int indexOffset,
    // int limit,
    // Boolean isSortAscending,
    // MessageFetchStyle fetchStyle)
    // {
    //
    // // Asset clauses
    // QueryBuilder boolQuery = this.getQueryByTopicAndDate(asset, isAnyAsset, semTopic, isAnySubtopic, start, end);
    // //
    //
    // String[] includeFetch = this.getFetchIncluded(fetchStyle);
    // String[] excludeFetch = this.getFetchExcluded(fetchStyle);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(includeFetch, excludeFetch)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // isSortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(indexOffset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // public SearchHits findByAsset(String asset,
    // boolean isAnyAsset,
    // long start,
    // long end,
    // int indexOffset,
    // int limit,
    // Boolean sortAscending,
    // MessageFetchStyle fetchStyle)
    // {
    //
    // BoolQueryBuilder boolQuery = this.getQueryByAssetAndDate(asset, isAnyAsset, start, end);
    //
    // String[] includeFetch = this.getFetchIncluded(fetchStyle);
    // String[] excludeFetch = this.getFetchExcluded(fetchStyle);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(includeFetch, excludeFetch)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // sortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(indexOffset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // public SearchHits findByAccount(String account,
    // long start,
    // long end,
    // int indexOffset,
    // int limit,
    // Boolean sortAscending,
    // MessageFetchStyle fetchStyle)
    // {
    //
    // boolean isAnyAccount = true;
    // QueryBuilder boolQuery = this.getQueryByAccountAndDate(null, isAnyAccount, start, end);
    //
    // String[] includeFetch = this.getFetchIncluded(fetchStyle);
    // String[] excludeFetch = this.getFetchExcluded(fetchStyle);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(includeFetch, excludeFetch)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // sortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(indexOffset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // public SearchHits findByAccountCount(String account)
    // {
    //
    // QueryBuilder accountQuery = QueryBuilders.termQuery(EsSchema.MESSAGE_ACCOUNT, account);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(false)
    // .setQuery(QueryBuilders.boolQuery().filter(accountQuery))
    // .setSize(0)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // public SearchHits findById(String account,
    // List<String> ids,
    // MessageFetchStyle fetchStyle)
    // {
    //
    // QueryBuilder idsQuery = QueryBuilders.idsQuery(ids.toArray(new String[] {}));
    //
    // String[] includeFetch = this.getFetchIncluded(fetchStyle);
    // String[] excludeFetch = this.getFetchExcluded(fetchStyle);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(includeFetch, excludeFetch)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP)
    // .setQuery(QueryBuilders.boolQuery().filter(idsQuery))
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // /**
    // * @param topic exepected semantic topic
    // * @param metric exepected full qualified metric name (example: "PROBE_TEMP.dbl")
    // * @param start
    // * @param end
    // * @param min
    // * @param max
    // * @param indexOffset
    // * @param limit
    // * @param sortAscending
    // * @param fetchStyle
    // * @return
    // */
    // public SearchHits findByMetricValue(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // String metric,
    // long start,
    // long end,
    // Object min,
    // Object max,
    // int offset,
    // int limit,
    // Boolean sortAscending,
    // MessageFetchStyle fetchStyle)
    // {
    //
    // // Query clause
    // String fullMetricName = String.format("%s.%s", EsSchema.MESSAGE_MTR, metric);
    // BoolQueryBuilder boolQuery = this.getQueryByMetricValueAndDate(asset,
    // isAnyAsset,
    // semTopic,
    // isAnySubtopic,
    // fullMetricName,
    // start,
    // end,
    // min,
    // max);
    //
    // String[] includeFetch = this.getFetchIncluded(fetchStyle);
    // String[] excludeFetch = this.getFetchExcluded(fetchStyle);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(includeFetch, excludeFetch)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // sortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(offset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }

    public void deleteById(String id)
    {

        esTypeDAO.getClient().prepareDelete()
                 .setIndex(esTypeDAO.getIndexName())
                 .setType(esTypeDAO.getTypeName())
                 .setId(id)
                 .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    }
    //
    // /**
    // * @param topic exepected semantic topic
    // * @param metric exepected full qualified metric name (example: "PROBE_TEMP.dbl")
    // * @param start
    // * @param end
    // * @param min
    // * @param max
    // * @param indexOffset
    // * @param limit
    // * @param sortAscending
    // * @param fetchStyle
    // * @return
    // */
    // public SearchHits findMetricsByValue(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // String metric,
    // long start,
    // long end,
    // Object min,
    // Object max,
    // int offset,
    // int limit,
    // Boolean sortAscending)
    // {
    //
    // String fullMetricName = String.format("%s.%s", EsSchema.MESSAGE_MTR, metric);
    // BoolQueryBuilder boolQuery = this.getQueryByMetricValueAndDate(asset, isAnyAsset, semTopic,
    // isAnySubtopic, fullMetricName,
    // start, end, min, max);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(false)
    // .addFields(EsSchema.ASSET_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP,
    // EsSchema.MESSAGE_MTR + ".*", fullMetricName)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // sortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(offset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
    //
    // public SearchHits findMetricsByTimestamp(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // String metric,
    // long start,
    // long end,
    // int offset,
    // int limit,
    // Boolean sortAscending)
    // {
    //
    // // TODO Check if adding the field in the field list (below) is enough ... what id the metric does not exist ??
    // String fullMetricName = String.format("%s.%s", EsSchema.MESSAGE_MTR, metric);
    //
    // BoolQueryBuilder boolQuery = this.getQueryByTopicAndDate(asset, isAnyAsset, semTopic, isAnySubtopic, start, end);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(false)
    // .addFields(EsSchema.MESSAGE_ACCOUNT,
    // EsSchema.MESSAGE_AS_NAME,
    // EsSchema.MESSAGE_SEM_TOPIC,
    // EsSchema.MESSAGE_TIMESTAMP,
    // fullMetricName)
    // .setQuery(boolQuery)
    // .addSort(EsSchema.MESSAGE_TIMESTAMP,
    // sortAscending ? SortOrder.ASC : SortOrder.DESC)
    // .setFrom(offset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
}
