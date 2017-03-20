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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ElasticsearchClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
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
import org.eclipse.kapua.service.datastore.model.Metric;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoCreator;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
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
 * @since 1.0.0
 */
public class EsMetricInfoDAO {

    private static final DatastoreObjectFactory datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);
    
    private EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     *
     * @throws EsClientUnavailableException
     * @since 1.0.0
     */
    public EsMetricInfoDAO() throws EsClientUnavailableException {
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
    public EsMetricInfoDAO setListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        this.esTypeDAO.setListener(daoListener);
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
    public EsMetricInfoDAO unsetListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Metric information DAO instance factory
     *
     * @return
     * @throws EsClientUnavailableException
     * @since 1.0.0
     */
    public static EsMetricInfoDAO getInstance() throws EsClientUnavailableException {
        return new EsMetricInfoDAO();
    }

    /**
     * Set the index name
     *
     * @param indexName
     * @return
     * @since 1.0.0
     */
    public EsMetricInfoDAO index(String indexName) {
        this.esTypeDAO.type(indexName, EsSchema.METRIC_TYPE_NAME);
        return this;
    }

    /**
     * Build the upsert request
     *
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public UpdateRequest getUpsertRequest(MetricInfoCreator<?> metricInfoCreator)
            throws EsDocumentBuilderException {
        String id = MetricInfoXContentBuilder.getOrDeriveId(null, metricInfoCreator);

        Metric<?> metric = datastoreObjectFactory.newMetric(metricInfoCreator.getName(), metricInfoCreator.getValue());
        
        MetricInfoImpl metricInfo = new MetricInfoImpl(metricInfoCreator.getScopeId(), new StorableIdImpl(id));
        metricInfo.setClientId(metricInfoCreator.getClientId());
        metricInfo.setChannel(metricInfoCreator.getChannel());
        metricInfo.setFirstMessageId(metricInfoCreator.getMessageId());
        metricInfo.setFirstMessageOn(metricInfoCreator.getMessageTimestamp());
        metricInfo.setMetric(metric);

        return getUpsertRequest(metricInfo);
    }

    /**
     * Build the upsert request
     *
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public UpdateRequest getUpsertRequest(MetricInfo metricInfo)
            throws EsDocumentBuilderException {
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
     * @since 1.0.0
     */
    public UpdateRequest getUpsertRequest(MetricXContentBuilder esChannelMetric) {
        return this.esTypeDAO.getUpsertRequest(esChannelMetric.getId(), esChannelMetric.getContent());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     *
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public UpdateResponse upsert(MetricInfoCreator<?> metricInfoCreator)
            throws EsDocumentBuilderException {
        String id = MetricInfoXContentBuilder.getOrDeriveId(null, metricInfoCreator);

        Metric<?> metric = datastoreObjectFactory.newMetric(metricInfoCreator.getName(), metricInfoCreator.getValue());;
        
        MetricInfoImpl metricInfo = new MetricInfoImpl(metricInfoCreator.getScopeId(), new StorableIdImpl(id));
        metricInfo.setChannel(metricInfoCreator.getChannel());
        metricInfo.setFirstMessageId(metricInfoCreator.getMessageId());
        metricInfo.setFirstMessageOn(metricInfoCreator.getMessageTimestamp());
        metricInfo.setMetric(metric);
        
        return upsert(metricInfo);
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     *
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public UpdateResponse upsert(MetricInfo metricInfo)
            throws EsDocumentBuilderException {
        MetricInfoXContentBuilder docBuilder = new MetricInfoXContentBuilder().build(metricInfo);
        List<MetricXContentBuilder> metricInfos = docBuilder.getBuilders();
        return esTypeDAO.upsert(metricInfos.get(0).getId(), metricInfos.get(0).getContent());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     *
     * @param esChannelMetric
     * @return
     * @since 1.0.0
     */
    public UpdateResponse upsert(MetricXContentBuilder esChannelMetric) {
        return esTypeDAO.upsert(esChannelMetric.getId(), esChannelMetric.getContent());
    }

    /**
     * Delete query action (delete document from the database by id)
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

    /**
     * Delete query action (delete documents from the database)
     *
     * @param query
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    public void deleteByQuery(MetricInfoQuery query)
            throws EsQueryConversionException {
        this.esTypeDAO.deleteByQuery(PredicateConverter.convertQueryPredicates(query));
    }

    /**
     * Execute bulk request
     *
     * @param aBulkRequest
     * @return
     */
    public BulkResponse bulk(BulkRequest aBulkRequest) {
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
     * @since 1.0.0
     */
    public MetricInfoListResult query(MetricInfoQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException,
            EsObjectBuilderException {
        // get one plus (if there is one) to later get the next key value
        MetricInfoQueryImpl localQuery = new MetricInfoQueryImpl(query.getScopeId());
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

        List<MetricInfo> metricInfos = new ArrayList<>();
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
        result.addItems(metricInfos);

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
    public long count(MetricInfoQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException {
        MetricInfoQueryConverter converter = new MetricInfoQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }
}
