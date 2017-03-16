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

import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoObjectBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ElasticsearchClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Client information DAO
 *
 * @since 1.0.0
 */
public class EsClientInfoDAO {

    private EsTypeDAO esTypeDAO;

    /**
     * Default constructor
     * 
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public EsClientInfoDAO() throws EsClientUnavailableException {
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
    public EsClientInfoDAO setListener(EsDaoListener daoListener)
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
    public EsClientInfoDAO unsetListener(EsDaoListener daoListener)
            throws EsDatastoreException {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    /**
     * Client information DAO instance factory
     * 
     * @return
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public static EsClientInfoDAO getInstance() throws EsClientUnavailableException {
        return new EsClientInfoDAO();
    }

    /**
     * Set the index name
     * 
     * @param indexName
     * @return
     * 
     * @since 1.0.0
     */
    public EsClientInfoDAO index(String indexName) {
        this.esTypeDAO.type(indexName, EsSchema.CLIENT_TYPE_NAME);
        return this;
    }

    /**
     * Upsert action (insert the document (if not present) or update the document (if present) into the database)
     * 
     * @param clientInfo
     * @return
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public UpdateResponse upsert(ClientInfo clientInfo)
            throws EsDocumentBuilderException {
        ClientInfoXContentBuilder clientInfoBuilder = new ClientInfoXContentBuilder().build(clientInfo);
        return this.esTypeDAO.upsert(clientInfoBuilder.getClientId(), clientInfoBuilder.getClientBuilder());
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
     * @param clientInfo
     * @return
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public UpdateResponse update(ClientInfo clientInfo)
            throws EsDocumentBuilderException {
        ClientInfoXContentBuilder clientInfoBuilder = new ClientInfoXContentBuilder().build(clientInfo);
        return this.esTypeDAO.upsert(clientInfoBuilder.getClientId(), clientInfoBuilder.getClientBuilder());
    }

    /**
     * Update action (update the document into the database)
     * 
     * @param id
     * @param esClient
     * @return
     * 
     * @since 1.0.0
     */
    public UpdateResponse update(String id, XContentBuilder esClient) {
        return this.esTypeDAO.update(id, esClient);
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
    public void deleteByQuery(ClientInfoQuery query)
            throws EsQueryConversionException {
        this.esTypeDAO.deleteByQuery(PredicateConverter.convertQueryPredicates(query));
    }

    /**
     * Query action (return objects matching the given query)
     * 
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public ClientInfoListResult query(ClientInfoQuery query)
            throws EsQueryConversionException,
            EsClientUnavailableException,
            EsObjectBuilderException {
        ClientInfoQueryImpl localQuery = new ClientInfoQueryImpl(query.getScopeId());
        localQuery.copy(query);

        // get one plus (if there is one) to later get the next key value
        localQuery.setLimit(query.getLimit() + 1);

        ClientInfoQueryConverter aic = new ClientInfoQueryConverter();
        SearchRequestBuilder builder = aic.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), localQuery);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null || searchHits.getTotalHits() == 0)
            return new ClientInfoListResultImpl();

        int i = 0;
        long searchHitsSize = searchHits.getHits().length;

        List<ClientInfo> clientInfos = new ArrayList<>();
        ClientInfoObjectBuilder clientInfoBuilder = new ClientInfoObjectBuilder();
        for (SearchHit searchHit : searchHits.getHits()) {
            if (i < query.getLimit()) {
                ClientInfo clientInfo = clientInfoBuilder.build(searchHit).getClientInfo();
                clientInfos.add(clientInfo);
            }
            i++;
        }

        // TODO check equivalence with CX
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHits.getTotalHits() > query.getLimit()) {
            nextKey = query.getLimit();
        }

        ClientInfoListResult result = new ClientInfoListResultImpl(nextKey, searchHitsSize);
        result.addItems(clientInfos);

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
    public long count(ClientInfoQuery query)
            throws EsQueryConversionException, EsClientUnavailableException {
        ClientInfoQueryConverter converter = new ClientInfoQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }
}
