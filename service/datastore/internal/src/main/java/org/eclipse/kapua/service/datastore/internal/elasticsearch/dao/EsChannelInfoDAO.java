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

import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoObjectBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ElasticsearchClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoCreator;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Channel information DAO
 *
 * @since 1.0.0
 */
public class EsChannelInfoDAO {

    private EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     * 
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public EsChannelInfoDAO() throws EsClientUnavailableException {
        esTypeDAO = new EsTypeDAO(ElasticsearchClient.getInstance());
    }

    /**
     * Set the dao listener
     * 
     * @param daoListener
     * @return
     * @throws EsDatastoreException
     * 
     * @since 1.0.0
     */
    public EsChannelInfoDAO setListener(EsDaoListener daoListener)
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
     * 
     * @since 1.0.0
     */
    public EsChannelInfoDAO unsetListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Channel information DAO instance factory
     * 
     * @return
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public static EsChannelInfoDAO getInstance() throws EsClientUnavailableException {
        return new EsChannelInfoDAO();
    }

    /**
     * Set the index name
     * 
     * @param indexName
     * @return
     * 
     * @since 1.0.0
     */
    public EsChannelInfoDAO index(String indexName) {
        this.esTypeDAO.type(indexName, EsSchema.CHANNEL_TYPE_NAME);
        return this;
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param channelInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public UpdateResponse upsert(ChannelInfoCreator channelInfoCreator)
            throws EsDocumentBuilderException {
        ChannelInfoXContentBuilder documentBuilder = new ChannelInfoXContentBuilder().build(channelInfoCreator);
        return this.esTypeDAO.upsert(documentBuilder.getChannelId(), documentBuilder.getBuilder());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param channelInfo
     * @return
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public UpdateResponse upsert(ChannelInfo channelInfo)
            throws EsDocumentBuilderException {
        ChannelInfoXContentBuilder documentBuilder = new ChannelInfoXContentBuilder().build(channelInfo);
        return this.esTypeDAO.upsert(documentBuilder.getChannelId(), documentBuilder.getBuilder());
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param id
     * @param esClient
     * @return
     * 
     * @since 1.0.0
     */
    public UpdateResponse upsert(String id, XContentBuilder esClient) {
        return this.esTypeDAO.upsert(id, esClient);
    }

    /**
     * Update action (update the document into the database)
     * 
     * @param channelInfo
     * @return
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public UpdateResponse update(ChannelInfo channelInfo)
            throws EsDocumentBuilderException {
        ChannelInfoXContentBuilder documentBuilder = new ChannelInfoXContentBuilder().build(channelInfo);
        return this.esTypeDAO.update(documentBuilder.getChannelId(), documentBuilder.getBuilder());
    }

    /**
     * Update action (update the document into the database)
     * 
     * @param id
     * @param esAsset
     * @return
     * 
     * @since 1.0.0
     */
    public UpdateResponse update(String id, XContentBuilder esAsset) {
        return this.esTypeDAO.update(id, esAsset);
    }

    /**
     * Delete query action (delete document from the database by id)
     * 
     * @param id
     * 
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
     * 
     * @since 1.0.0
     */
    public void deleteByQuery(ChannelInfoQuery query)
            throws EsQueryConversionException {
        this.esTypeDAO.deleteByQuery(PredicateConverter.convertQueryPredicates(query));
    }

    /**
     * Query action (return objects matching the given query)
     * 
     * @param query
     * @return
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public ChannelInfoListResult query(ChannelInfoQuery query)
            throws EsClientUnavailableException,
            EsQueryConversionException,
            EsObjectBuilderException {
        // get one plus (if there is one) to later get the next key value
        ChannelInfoQueryImpl localQuery = new ChannelInfoQueryImpl(query.getScopeId());
        localQuery.copy(query);
        localQuery.setLimit(query.getLimit() + 1);

        ChannelInfoQueryConverter queryConverter = new ChannelInfoQueryConverter();
        SearchRequestBuilder builder = queryConverter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), localQuery);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null || searchHits.getTotalHits() == 0)
            return new ChannelInfoListResultImpl();

        int i = 0;
        long searchHitsSize = searchHits.getHits().length;

        List<ChannelInfo> channelInfos = new ArrayList<ChannelInfo>();
        ChannelInfoObjectBuilder channelInfoBuilder = new ChannelInfoObjectBuilder();
        for (SearchHit searchHit : searchHits.getHits()) {
            if (i < query.getLimit()) {
                ChannelInfo channelInfo;
                channelInfo = channelInfoBuilder.build(searchHit).getChannelInfo();
                channelInfos.add(channelInfo);
            }
            i++;
        }

        // TODO check equivalence with CX with Pierantonio
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHits.getTotalHits() > query.getLimit()) {
            nextKey = query.getLimit();
        }

        ChannelInfoListResult result = new ChannelInfoListResultImpl(nextKey, searchHitsSize);
        result.addItems(channelInfos);

        return result;
    }

    /**
     * Query count action (return the count of the objects matching the given query)
     * 
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public long count(ChannelInfoQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException {
        ChannelInfoQueryConverter converter = new ChannelInfoQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }
}
