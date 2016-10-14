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

import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.AssetInfoBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.AssetInfoQueryConverter;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.internal.model.AssetInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AssetInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.AssetInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class EsAssetDAO
{

    private EsTypeDAO esTypeDAO;

    private EsAssetDAO()
    {
    }

    public EsAssetDAO setListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.setListener(daoListener);
        return this;
    }

    public EsAssetDAO unsetListener(EsDaoListener daoListener)
        throws EsDatastoreException
    {
        this.esTypeDAO.unsetListener(daoListener);
        return this;
    }

    public static EsAssetDAO connection(Client client)
        throws UnknownHostException
    {
        EsAssetDAO esAssetDAO = new EsAssetDAO();
        esAssetDAO.esTypeDAO = EsTypeDAO.connection(client);
        return esAssetDAO;
    }

    public EsAssetDAO instance(String indexName, String typeName)
    {
        this.esTypeDAO.instance(indexName, typeName);
        return this;
    }
//
//    public BoolQueryBuilder getQueryByAsset(String asset,
//                                            boolean isAnyAsset)
//    {
//
//        // Asset clauses
//        QueryBuilder assetQuery = null;
//        if (!isAnyAsset) {
//            assetQuery = QueryBuilders.termQuery(EsSchema.ASSET_NAME, asset);
//        }
//
//        // Composite clause
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        if (assetQuery != null)
//            boolQuery.must(assetQuery);
//        //
//
//        return boolQuery;
//    }
//
//    public BoolQueryBuilder getQueryByAssetAndDate(String asset,
//                                                   boolean isAnyAsset,
//                                                   long start,
//                                                   long end)
//    {
//
//        // Composite clause
//        BoolQueryBuilder boolQuery = this.getQueryByAsset(asset, isAnyAsset);
//
//        // Timestamp clauses
//        QueryBuilder dateQuery = QueryBuilders.rangeQuery(EsSchema.ASSET_TIMESTAMP)
//                                              .from(start).to(end);
//        boolQuery.must(dateQuery);
//        //
//
//        return boolQuery;
//    }
//
//    public UpdateRequest getUpsertRequest(String id, XContentBuilder esAsset)
//    {
//        return this.esTypeDAO.getUpsertRequest(id, esAsset);
//    }

    public UpdateResponse upsert(AssetInfo assetInfo) throws IOException
    {
        EsDocumentBuilder assetInfoBuilder = new EsDocumentBuilder().build(assetInfo.getScope(), assetInfo);
        return this.esTypeDAO.upsert(assetInfoBuilder.getAssetId(), assetInfoBuilder.getAssetBuilder());
    }

    public UpdateResponse upsert(String id, XContentBuilder esAsset)
    {
        return this.esTypeDAO.upsert(id, esAsset);
    }

    public UpdateResponse update(AssetInfo assetInfo) throws IOException
    {
        EsDocumentBuilder assetInfoBuilder = new EsDocumentBuilder().build(assetInfo.getScope(), assetInfo);
        return this.esTypeDAO.upsert(assetInfoBuilder.getAssetId(), assetInfoBuilder.getAssetBuilder());
    }

    public UpdateResponse update(String id, XContentBuilder esAsset)
    {
        return this.esTypeDAO.update(id, esAsset);
    }

    public void deleteById(String id)
    {

        esTypeDAO.getClient().prepareDelete()
                 .setIndex(esTypeDAO.getIndexName())
                 .setType(esTypeDAO.getTypeName())
                 .setId(id)
                 .get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
    }

    public void deleteByQuery(AssetInfoQuery query) throws KapuaException
    {
        PredicateConverter pc = new PredicateConverter();
        this.esTypeDAO.deleteByQuery(pc.toElasticsearchQuery(query.getPredicate()));
    }
//
//    public void deleteByAccount(long start, long end)
//    {
//        this.deleteByQuery(this.getQueryByAssetAndDate(KapuaTopic.SINGLE_LEVEL_WCARD, true, start, end));
//    }
//
//    public void deleteByAsset(String asset, boolean isAnyAsset)
//    {
//        this.deleteByQuery(this.getQueryByAsset(asset, isAnyAsset));
//    }

    public AssetInfoListResult query(AssetInfoQuery query) throws UnknownHostException, KapuaException, EsDatastoreException, ParseException
    {
        AssetInfoQueryImpl localQuery = new AssetInfoQueryImpl();
        localQuery.copy(query);
        
        // get one plus (if there is one) to later get the next key value 
        localQuery.setLimit(query.getLimit()+1);
        
        AssetInfoQueryConverter aic = new AssetInfoQueryConverter();
        SearchRequestBuilder builder = aic.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();
        
        if (searchHits == null || searchHits.getTotalHits() == 0)
            return new AssetInfoListResultImpl();

        int i = 0;
        int searchHitsSize = searchHits.getHits().length;

        List<AssetInfo> assetInfos = new ArrayList<AssetInfo>();
        AssetInfoBuilder assetInfoBuilder = new AssetInfoBuilder();
        for(SearchHit searchHit:searchHits.getHits()) {
            if (i < query.getLimit()) {
                AssetInfo assetInfo = assetInfoBuilder.build(searchHit).getAssetInfo();
                assetInfos.add(assetInfo);
            }
            i++;
        }
        
        // TODO check equivalence with CX with Pierantonio
        // TODO what is this nextKey
        Object nextKey = null;
        if (searchHits.getTotalHits() > query.getLimit()) {
            nextKey = query.getLimit();
        }
        
        AssetInfoListResultImpl result = new AssetInfoListResultImpl(nextKey, searchHitsSize);
        result.addAll(assetInfos);
        
        return result;
    }

    public long count(AssetInfoQuery query)
        throws Exception
    {
        AssetInfoQueryConverter converter = new AssetInfoQueryConverter();
        SearchRequestBuilder builder = converter.toSearchRequestBuilder(esTypeDAO.getIndexName(), esTypeDAO.getTypeName(), query);
        SearchResponse response = builder.get(TimeValue.timeValueMillis(EsUtils.getQueryTimeout()));
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }
//    
//    public SearchHits findByAccount(String name,
//                                    int offset,
//                                    int size)
//    {
//
//        long timeout = EsUtils.getQueryTimeout();
//
//        SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
//                                           .setTypes(esTypeDAO.getTypeName())
//                                           .setFetchSource(false)
//                                           .addFields(EsSchema.ASSET_NAME,
//                                                      EsSchema.ASSET_TIMESTAMP,
//                                                      EsSchema.ASSET_ACCOUNT)
//                                           .setFrom(offset)
//                                           .setSize(size)
//                                           .get(TimeValue.timeValueMillis(timeout));
//
//        SearchHits searchHits = response.getHits();
//        return searchHits;
//    }
//    
//    public SearchHits findByAsset(String name,
//                                  boolean isAnyAccount,
//                                  String asset,
//                                  boolean isAnyAsset,
//                                  int offset,
//                                  int size)
//    {
//
//        long timeout = EsUtils.getQueryTimeout();
//
//        BoolQueryBuilder boolQuery = this.getQueryByAsset(asset, isAnyAsset);
//
//        SearchResponse response = esTypeDAO.getClient().prepareSearch(esTypeDAO.getIndexName())
//                                           .setTypes(esTypeDAO.getTypeName())
//                                           .setFetchSource(false)
//                                           .addFields(EsSchema.ASSET_NAME,
//                                                      EsSchema.ASSET_TIMESTAMP,
//                                                      EsSchema.ASSET_ACCOUNT)
//                                           .setQuery(boolQuery)
//                                           .setFrom(offset)
//                                           .setSize(size)
//                                           .get(TimeValue.timeValueMillis(timeout));
//
//        SearchHits searchHits = response.getHits();
//        return searchHits;
//    }
}
