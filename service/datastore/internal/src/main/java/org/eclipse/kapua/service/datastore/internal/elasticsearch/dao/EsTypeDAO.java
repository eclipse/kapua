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

import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

public class EsTypeDAO
{

    private Client        client;

    private String        indexName;
    private String        typeName;
    private EsDaoListener eventListener;

    protected EsTypeDAO()
    {
    }

    protected Client getClient()
    {
        return client;
    }

    protected String getIndexName()
    {
        return indexName;
    }

    protected String getTypeName()
    {
        return typeName;
    }

    public EsTypeDAO setListener(EsDaoListener eventListener)
        throws EsDatastoreException
    {

        if (this.eventListener != null && this.eventListener != eventListener)
            throw new EsDatastoreException("Listener already set. Use unset to uregister the previuos listener.");

        this.eventListener = eventListener;
        return this;
    }

    public EsTypeDAO unsetListener(EsDaoListener eventListener)
        throws EsDatastoreException
    {

        if (this.eventListener != null && this.eventListener != eventListener)
            throw new EsDatastoreException("Listener cannot be unset. The provided listener does not match.");

        this.eventListener = null;
        return this;
    }

    public static EsTypeDAO connection(Client client)
    {
        EsTypeDAO esTypeDAO = new EsTypeDAO();
        esTypeDAO.client = client;
        return esTypeDAO;
    }

    public EsTypeDAO instance(String indexName, String typeName)
    {
        this.indexName = indexName;
        this.typeName = typeName;
        return this;
    }

    public String insert(XContentBuilder esAsset)
    {

        assert this.client != null : "ES client must be not null";
        long timeout = EsUtils.getQueryTimeout();

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName).source(esAsset);
        IndexResponse response = this.client.index(idxRequest).actionGet(TimeValue.timeValueMillis(timeout));
        return response.getId();
    }

    public UpdateResponse update(String id, XContentBuilder esAsset)
    {

        assert this.client != null : "ES client must be not null";
        assert id != null : "ES client must be not null";
        assert !id.isEmpty() : "Document id must be not empty";

        long timeout = EsUtils.getQueryTimeout();

        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esAsset);
        UpdateResponse response = this.client.update(updRequest)
                                             .actionGet(TimeValue.timeValueMillis(timeout));
        return response;
    }

    public UpdateRequest getUpsertRequest(String id, Map<String, Object> esAsset)
    {

        assert this.client != null : "ES client must be not null";

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esAsset);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esAsset);
        updRequest.upsert(idxRequest);
        return updRequest;
    }

    public UpdateRequest getUpsertRequest(String id, XContentBuilder esAsset)
    {

        assert this.client != null : "ES client must be not null";

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esAsset);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esAsset);
        updRequest.upsert(idxRequest);
        return updRequest;
    }

    public UpdateResponse upsert(String id, XContentBuilder esAsset)
    {

        assert this.client != null : "ES client must be not null";

        long timeout = EsUtils.getQueryTimeout();

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esAsset);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esAsset);
        UpdateResponse response = this.client.update(updRequest.upsert(idxRequest)).actionGet(TimeValue.timeValueMillis(timeout));
        return response;
    }

    public UpdateResponse upsert(String id, Map<String, Object> esMessage)
    {

        assert this.client != null : "ES client must be not null";

        long timeout = EsUtils.getQueryTimeout();

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esMessage);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esMessage);
        UpdateResponse response = this.client.update(updRequest.upsert(idxRequest)).actionGet(TimeValue.timeValueMillis(timeout));
        return response;
    }

    public void deleteByQuery(QueryBuilder query)
    {

        TimeValue queryTimeout = TimeValue.timeValueMillis(EsUtils.getQueryTimeout());
        TimeValue scrollTimeout = TimeValue.timeValueMillis(EsUtils.getScrollTimeout());

        // delete by query API is deprecated, scroll with bulk delete must be used
        SearchResponse scrollResponse = this.client.prepareSearch(this.getIndexName())
                                                   .setTypes(this.getTypeName())
                                                   .setFetchSource(false)
                                                   .addSort("_doc", SortOrder.ASC)
                                                   .setVersion(true)
                                                   .setScroll(scrollTimeout)
                                                   .setQuery(query)
                                                   .setSize(100)
                                                   .get(queryTimeout);

        // Scroll until no hits are returned
        while (true) {

            // Break condition: No hits are returned
            if (scrollResponse.getHits().getHits().length == 0)
                break;

            BulkRequest bulkRequest = new BulkRequest();
            for (SearchHit hit : scrollResponse.getHits().hits()) {
                DeleteRequest delete = new DeleteRequest().index(hit.index())
                                                          .type(hit.type())
                                                          .id(hit.id())
                                                          .version(hit.version());
                bulkRequest.add(delete);
            }

            this.getClient().bulk(bulkRequest).actionGet(queryTimeout);

            // TODO manage events
            if (eventListener != null)
                eventListener.notify(new EsTypeCrudEvent());

            scrollResponse = this.client.prepareSearchScroll(scrollResponse.getScrollId())
                                        .setScroll(scrollTimeout)
                                        .execute()
                                        .actionGet(queryTimeout);
        }
    }

    public BulkResponse bulk(BulkRequest bulkRequest)
    {

        assert this.client != null : "ES client must be not null";
        assert bulkRequest != null : "Bulk request list must not be null";

        long timeout = EsUtils.getQueryTimeout();

        BulkResponse bulkResponse = this.getClient().bulk(bulkRequest).actionGet(timeout);
        return bulkResponse;
    }
}
