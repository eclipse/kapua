/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch.dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMetricDocumentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.KapuaInvalidTopicException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class EsMetricDAO
{

    private EsTypeDAO esTypeDAO;

    private EsMetricDAO()
    {
    }

    public EsMetricDAO setListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.setListener(daoListener);
        return this;
    }

    public EsMetricDAO unsetListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    public static EsMetricDAO connection(Client client)
        throws UnknownHostException
    {
        EsMetricDAO metricDAO = new EsMetricDAO();
        metricDAO.esTypeDAO = EsTypeDAO.connection(client);
        return metricDAO;
    }

    public EsMetricDAO instance(String indexName, String typeName)
    {
        this.esTypeDAO.instance(indexName, typeName);
        return this;
    }

    public UpdateRequest getUpsertRequest(EsMetricDocumentBuilder esTopicMetric)
    {
        return this.esTypeDAO.getUpsertRequest(esTopicMetric.getId(), esTopicMetric.getContent());
    }

    public UpdateResponse upsert(MetricInfo metricInfo) throws IOException, KapuaInvalidTopicException, ParseException
    {
        EsDocumentBuilder docBuilder = new EsDocumentBuilder().build(metricInfo.getScope(), metricInfo);
        List<EsMetricDocumentBuilder> metricInfos = docBuilder.getTopicMetrics();
        return esTypeDAO.upsert(metricInfos.get(0).getId(), metricInfos.get(0).getContent());
    }

    public UpdateResponse upsert(EsMetricDocumentBuilder esTopicMetric)
    {
        return esTypeDAO.upsert(esTopicMetric.getId(), esTopicMetric.getContent());
    }
//
//    public void deleteByQuery(BoolQueryBuilder boolQuery)
//    {
//        this.esTypeDAO.deleteByQuery(boolQuery);
//    }
//
//    public BoolQueryBuilder getQueryByTopic(String asset,
//                                            boolean isAnyAsset,
//                                            String semTopic,
//                                            boolean isAnySubtopic)
//    {
//
//        // Asset clauses
//        QueryBuilder assetQuery = null;
//        if (!isAnyAsset) {
//            assetQuery = QueryBuilders.termQuery(EsSchema.METRIC_ASSET, asset);
//        }
//
//        // Topic clauses
//        QueryBuilder topicQuery = null;
//        if (isAnySubtopic) {
//            topicQuery = QueryBuilders.prefixQuery(EsSchema.METRIC_SEM_NAME, semTopic);
//        }
//        else {
//            topicQuery = QueryBuilders.termQuery(EsSchema.METRIC_SEM_NAME, semTopic);
//        }
//
//        // Composite clause
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        if (assetQuery != null)
//            boolQuery.must(assetQuery);
//        boolQuery.must(topicQuery);
//        //
//
//        return boolQuery;
//    }
//
//    public BoolQueryBuilder getQueryByTopicAndDate(String asset,
//                                                   boolean isAnyAsset,
//                                                   String semTopic,
//                                                   boolean isAnySubtopic,
//                                                   long start,
//                                                   long end)
//    {
//
//        // Composite clause
//        BoolQueryBuilder boolQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
//        //
//        // Timestamp clauses
//        QueryBuilder dateQuery = QueryBuilders.rangeQuery(EsSchema.METRIC_MTR_TIMESTAMP).from(start).to(end);
//        boolQuery.must(dateQuery);
//
//        return boolQuery;
//    }

    public void deleteById(String id)
    {

        esTypeDAO.getClient().prepareDelete()
                 .setIndex(esTypeDAO.getIndexName())
                 .setType(esTypeDAO.getTypeName())
                 .setId(id)
                 .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    }

    public void deleteByQuery(MetricInfoQuery query) throws KapuaException
    {
        StorablePredicate predicate = query.getPredicate();
        PredicateConverter pc = new PredicateConverter();
        this.esTypeDAO.deleteByQuery(pc.toElasticsearchQuery(predicate));
    }
//
//    public void deleteByTopic(String asset,
//                              boolean isAnyAsset,
//                              String semTopic,
//                              boolean isAnySubtopic)
//    {
//
//        // Asset clauses
//        BoolQueryBuilder boolQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
//        this.esTypeDAO.deleteByQuery(boolQuery);
//    }
//
//    public void deleteByTopic(String asset,
//                              boolean isAnyAsset,
//                              String semTopic,
//                              boolean isAnySubtopic,
//                              long start,
//                              long end)
//    {
//
//        // Asset clauses
//        BoolQueryBuilder boolQuery = this.getQueryByTopicAndDate(asset, isAnyAsset, semTopic, isAnySubtopic, start, end);
//        this.esTypeDAO.deleteByQuery(boolQuery);
//    }
//
//    public void deleteByAccount(String accountName, long start, long end)
//        throws KapuaInvalidTopicException
//    {
//
//        KapuaTopic topic = new KapuaTopic(accountName, KapuaTopic.SINGLE_LEVEL_WCARD, KapuaTopic.MULTI_LEVEL_WCARD);
//        String asset = topic.getAsset();
//        boolean anyAsset = KapuaTopic.SINGLE_LEVEL_WCARD.equals(asset);
//        String semTopic = topic.getSemanticTopic();
//        boolean topicPrefix = KapuaTopic.MULTI_LEVEL_WCARD.equals(semTopic);
//        if (topicPrefix) {
//            semTopic = topic.getParentTopic();
//            semTopic = semTopic == null ? "" : semTopic;
//        }
//
//        this.deleteByTopic(asset, anyAsset, semTopic, topicPrefix, start, end);
//    }

    public BulkResponse bulk(BulkRequest aBulkRequest)
    {
        return this.esTypeDAO.bulk(aBulkRequest);
    }

    public MetricInfoListResult query(MetricInfoQuery query) throws Exception
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
        MetricInfoBuilder metricInfoBuilder = new MetricInfoBuilder();
        for(SearchHit searchHit:searchHits.getHits()) {
            if (i < query.getLimit()) {
                MetricInfo metricInfo = metricInfoBuilder.build(searchHit).getKapuaMetricInfo();
                metricInfos.add(metricInfo);
            }
            i++;
        }
        
        // TODO check equivalence with CX with Pierantonio
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHitsSize > query.getLimit()) {
            nextKey = query.getLimit();
        }
        
        MetricInfoListResult result = new MetricInfoListResultImpl(nextKey, metricInfos.size());
        result.addAll(metricInfos);
        
        return result;
    }

    public long count(MetricInfoQuery query)
        throws Exception
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
//    public SearchHits findByTopic(String asset,
//                                  boolean isAnyAsset,
//                                  String semTopic,
//                                  boolean isAnySubtopic,
//                                  int offset,
//                                  int limit)
//    {
//
//        long timeout = EsUtils.getQueryTimeout();
//
//        BoolQueryBuilder topicQuery = this.getQueryByTopic(asset, isAnyAsset, semTopic, isAnySubtopic);
//
//        SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
//                                           .setTypes(esTypeDAO.getTypeName())
//                                           .setFetchSource(false)
//                                           .addFields(EsSchema.METRIC_MTR_NAME_FULL,
//                                                      EsSchema.METRIC_MTR_TYPE_FULL,
//                                                      EsSchema.METRIC_MTR_VALUE_FULL,
//                                                      EsSchema.METRIC_MTR_TIMESTAMP_FULL,
//                                                      EsSchema.METRIC_MTR_MSG_ID_FULL)
//                                           .setQuery(topicQuery)
//                                           .setFrom(offset)
//                                           .setSize(limit)
//                                           .get(TimeValue.timeValueMillis(timeout));
//
//        SearchHits searchHits = response.getHits();
//        return searchHits;
//    }
}
