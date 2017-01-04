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
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoObjectBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoCreator;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Metric information DAO
 *
 * @since 1.0
 * 
 */
public class EsMetricInfoDAO
{

    private EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     * 
     * @throws EsClientUnavailableException
     */
    public EsMetricInfoDAO() throws EsClientUnavailableException
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
    public EsMetricInfoDAO setListener(EsDaoListener daoListener)
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
    public EsMetricInfoDAO unsetListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Metric information DAO instance factory
     * 
     * @return
     * @throws EsClientUnavailableException
     */
    public static EsMetricInfoDAO getInstance() throws EsClientUnavailableException
    {
        return new EsMetricInfoDAO();
    }

    /**
     * Set the index name
     * 
     * @param indexName
     * @return
     */
    public EsMetricInfoDAO index(String indexName)
    {
        this.esTypeDAO.type(indexName, EsSchema.METRIC_TYPE_NAME);
        return this;
    }

    /**
     * Build the upsert request
     * 
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     */
    public UpdateRequest getUpsertRequest(MetricInfoCreator metricInfoCreator)
        throws EsDocumentBuilderException
    {
        String id = MetricInfoXContentBuilder.getOrDeriveId(null, metricInfoCreator);

        MetricInfoImpl metricInfo = new MetricInfoImpl(metricInfoCreator.getAccount(), new StorableIdImpl(id));
        metricInfo.setClientId(metricInfoCreator.getClientId());
        metricInfo.setChannel(metricInfoCreator.getChannel());
        metricInfo.setFirstPublishedMessageId(metricInfoCreator.getMessageId());
        metricInfo.setFirstPublishedMessageTimestamp(metricInfoCreator.getMessageTimestamp());
        metricInfo.setName(metricInfoCreator.getName());
        metricInfo.setType(metricInfoCreator.getType());
        metricInfo.setValue(metricInfoCreator.getValue(Object.class));
        return this.getUpsertRequest(metricInfo);
    }

    /**
     * Build the upsert request
     * 
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     */
    public UpdateRequest getUpsertRequest(MetricInfo metricInfo)
        throws EsDocumentBuilderException
    {
        MetricInfoXContentBuilder builder = new MetricInfoXContentBuilder();
        builder.build(metricInfo);
        List<MetricXContentBuilder> metricBuilders = builder.getBuilders();
        return this.esTypeDAO.getUpsertRequest(metricBuilders.get(0).getId(), metricBuilders.get(0).getContent());
    }

    /**
     * Build the upsert request
     * 
     * @param esChannelMetric
     * @return
     */
    public UpdateRequest getUpsertRequest(MetricXContentBuilder esChannelMetric)
    {
        return this.esTypeDAO.getUpsertRequest(esChannelMetric.getId(), esChannelMetric.getContent());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     */
    public UpdateResponse upsert(MetricInfoCreator metricInfoCreator)
        throws EsDocumentBuilderException
    {
        String id = MetricInfoXContentBuilder.getOrDeriveId(null, metricInfoCreator);

        MetricInfoImpl metricInfo = new MetricInfoImpl(metricInfoCreator.getAccount(), new StorableIdImpl(id));
        metricInfo.setChannel(metricInfoCreator.getChannel());
        metricInfo.setFirstPublishedMessageId(metricInfoCreator.getMessageId());
        metricInfo.setFirstPublishedMessageTimestamp(metricInfoCreator.getMessageTimestamp());
        metricInfo.setName(metricInfoCreator.getName());
        metricInfo.setType(metricInfoCreator.getType());
        metricInfo.setValue(metricInfoCreator.getValue(Object.class));
        return this.upsert(metricInfo);
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     */
    public UpdateResponse upsert(MetricInfo metricInfo)
        throws EsDocumentBuilderException
    {
        MetricInfoXContentBuilder docBuilder = new MetricInfoXContentBuilder().build(metricInfo);
        List<MetricXContentBuilder> metricInfos = docBuilder.getBuilders();
        return esTypeDAO.upsert(metricInfos.get(0).getId(), metricInfos.get(0).getContent());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param esChannelMetric
     * @return
     */
    public UpdateResponse upsert(MetricXContentBuilder esChannelMetric)
    {
        return esTypeDAO.upsert(esChannelMetric.getId(), esChannelMetric.getContent());
    }
    //
    // public void deleteByQuery(BoolQueryBuilder boolQuery)
    // {
    // this.esTypeDAO.deleteByQuery(boolQuery);
    // }
    //
    // public BoolQueryBuilder getQueryByTopic(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic)
    // {
    //
    // // Asset clauses
    // QueryBuilder assetQuery = null;
    // if (!isAnyAsset) {
    // assetQuery = QueryBuilders.termQuery(EsSchema.METRIC_ASSET, asset);
    // }
    //
    // // Topic clauses
    // QueryBuilder topicQuery = null;
    // if (isAnySubtopic) {
    // topicQuery = QueryBuilders.prefixQuery(EsSchema.METRIC_SEM_NAME, semTopic);
    // }
    // else {
    // topicQuery = QueryBuilders.termQuery(EsSchema.METRIC_SEM_NAME, semTopic);
    // }
    //
    // // Composite clause
    // BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    // if (assetQuery != null)
    // boolQuery.must(assetQuery);
    // boolQuery.must(topicQuery);
    // //
    //
    // return boolQuery;
    // }
    //
    // public BoolQueryBuilder getQueryByTopicAndDate(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // long start,
    // long end)
    // {
    //
    // // Composite clause
    // BoolQueryBuilder boolQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
    // //
    // // Timestamp clauses
    // QueryBuilder dateQuery = QueryBuilders.rangeQuery(EsSchema.METRIC_MTR_TIMESTAMP).from(start).to(end);
    // boolQuery.must(dateQuery);
    //
    // return boolQuery;
    // }

    /**
     * Delete query action (delete document from the database by id)
     * 
     * @param id
     */
    public void deleteById(String id)
    {

        esTypeDAO.getClient().prepareDelete()
                 .setIndex(esTypeDAO.getIndexName())
                 .setType(esTypeDAO.getTypeName())
                 .setId(id)
                 .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    }

    /**
     * Delete query action (delete documents from the database)
     * 
     * @param query
     * @throws EsQueryConversionException
     */
    public void deleteByQuery(MetricInfoQuery query)
        throws EsQueryConversionException
    {
        StorablePredicate predicate = query.getPredicate();
        PredicateConverter pc = new PredicateConverter();
        this.esTypeDAO.deleteByQuery(pc.toElasticsearchQuery(predicate));
    }
    //
    // public void deleteByTopic(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic)
    // {
    //
    // // Asset clauses
    // BoolQueryBuilder boolQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
    // this.esTypeDAO.deleteByQuery(boolQuery);
    // }
    //
    // public void deleteByTopic(String asset,
    // boolean isAnyAsset,
    // String semTopic,
    // boolean isAnySubtopic,
    // long start,
    // long end)
    // {
    //
    // // Asset clauses
    // BoolQueryBuilder boolQuery = this.getQueryByTopicAndDate(asset, isAnyAsset, semTopic, isAnySubtopic, start, end);
    // this.esTypeDAO.deleteByQuery(boolQuery);
    // }
    //
    // public void deleteByAccount(String accountName, long start, long end)
    // throws KapuaInvalidTopicException
    // {
    //
    // KapuaTopic topic = new KapuaTopic(accountName, KapuaTopic.SINGLE_LEVEL_WCARD, KapuaTopic.MULTI_LEVEL_WCARD);
    // String asset = topic.getAsset();
    // boolean anyAsset = KapuaTopic.SINGLE_LEVEL_WCARD.equals(asset);
    // String semTopic = topic.getSemanticTopic();
    // boolean topicPrefix = KapuaTopic.MULTI_LEVEL_WCARD.equals(semTopic);
    // if (topicPrefix) {
    // semTopic = topic.getParentTopic();
    // semTopic = semTopic == null ? "" : semTopic;
    // }
    //
    // this.deleteByTopic(asset, anyAsset, semTopic, topicPrefix, start, end);
    // }

    /**
     * Execute bulk request
     * 
     * @param aBulkRequest
     * @return
     */
    public BulkResponse bulk(BulkRequest aBulkRequest)
    {
        return this.esTypeDAO.bulk(aBulkRequest);
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
    public MetricInfoListResult query(MetricInfoQuery query)
        throws EsQueryConversionException,
        EsClientUnavailableException,
        EsObjectBuilderException
    {
        // get one plus (if there is one) to later get the next key value
        MetricInfoQueryImpl localQuery = new MetricInfoQueryImpl();
        localQuery.copy(query);
        localQuery.setLimit(query.getLimit() + 1);

        MetricInfoQueryConverter mic = new MetricInfoQueryConverter();
        SearchRequestBuilder builder = mic.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), localQuery);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null || searchHits.getTotalHits() == 0)
            return new MetricInfoListResultImpl();

        int i = 0;
        int searchHitsSize = searchHits.getHits().length;

        List<MetricInfo> metricInfos = new ArrayList<MetricInfo>();
        MetricInfoObjectBuilder metricInfoBuilder = new MetricInfoObjectBuilder();
        for (SearchHit searchHit : searchHits.getHits()) {
            if (i < query.getLimit()) {
                MetricInfo metricInfo = metricInfoBuilder.build(searchHit).getKapuaMetricInfo();
                metricInfos.add(metricInfo);
            }
            i++;
        }

        // TODO check equivalence with CX
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHitsSize > query.getLimit()) {
            nextKey = query.getLimit();
        }

        MetricInfoListResult result = new MetricInfoListResultImpl(nextKey, (long) metricInfos.size());
        result.addAll(metricInfos);

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
    public long count(MetricInfoQuery query)
        throws EsQueryConversionException,
        EsClientUnavailableException
    {
        MetricInfoQueryConverter converter = new MetricInfoQueryConverter();
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
    // int offset,
    // int limit)
    // {
    //
    // long timeout = EsUtils.getQueryTimeout();
    //
    // BoolQueryBuilder topicQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
    //
    // SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
    // .setTypes(esTypeDAO.getTypeName())
    // .setFetchSource(false)
    // .addFields(EsSchema.METRIC_MTR_NAME_FULL,
    // EsSchema.METRIC_MTR_TYPE_FULL,
    // EsSchema.METRIC_MTR_VALUE_FULL,
    // EsSchema.METRIC_MTR_TIMESTAMP_FULL,
    // EsSchema.METRIC_MTR_MSG_ID_FULL)
    // .setQuery(topicQuery)
    // .setFrom(offset)
    // .setSize(limit)
    // .get(TimeValue.timeValueMillis(timeout));
    //
    // SearchHits searchHits = response.getHits();
    // return searchHits;
    // }
}
