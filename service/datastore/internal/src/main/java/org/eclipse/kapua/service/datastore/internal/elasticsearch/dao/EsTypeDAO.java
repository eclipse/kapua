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

/**
 * Type dao
 * 
 * @since 1.0
 *
 */
public class EsTypeDAO
{
    private static final String CLIENT_UNDEFINED_MSG    = "ES client must be not null";
    private static final String INVALID_DOCUMENT_ID_MSG = "Document id must be not empty %s";

    private Client client;

    private String        indexName;
    private String        typeName;
    private EsDaoListener eventListener;

    public EsTypeDAO(Client client)
    {
        this.client = client;
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

    /**
     * Set the dao listener
     * 
     * @param eventListener
     * @return
     * @throws EsDatastoreException
     */
    public EsTypeDAO setListener(EsDaoListener eventListener)
        throws EsDatastoreException
    {
        if (this.eventListener != null && this.eventListener != eventListener)
            throw new EsDatastoreException("Listener already set. Use unset to uregister the previuos listener.");

        this.eventListener = eventListener;
        return this;
    }

    /**
     * Unset the dao listener
     * 
     * @param eventListener
     * @return
     * @throws EsDatastoreException
     */
    public EsTypeDAO unsetListener(EsDaoListener eventListener)
        throws EsDatastoreException
    {
        if (this.eventListener != null && this.eventListener != eventListener)
            throw new EsDatastoreException("Listener cannot be unset. The provided listener does not match.");

        this.eventListener = null;
        return this;
    }

    /**
     * Set the DAO type by index and type name (schema)
     * 
     * @param indexName
     * @param typeName
     * @return
     */
    public EsTypeDAO type(String indexName, String typeName)
    {
        this.indexName = indexName;
        this.typeName = typeName;
        return this;
    }

    /**
     * Insert action (insert the document into the database)
     * 
     * @param esClient
     * @return
     */
    public String insert(XContentBuilder esClient)
    {
        if (this.client == null)
            throw new IllegalStateException(CLIENT_UNDEFINED_MSG);

        long timeout = EsUtils.getQueryTimeout();

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName).source(esClient);
        IndexResponse response = this.client.index(idxRequest).actionGet(TimeValue.timeValueMillis(timeout));
        return response.getId();
    }

    /**
     * Update action (update the document into the database)
     * 
     * @param id
     * @param esClient
     * @return
     */
    public UpdateResponse update(String id, XContentBuilder esClient)
    {
        if (this.client == null)
            throw new IllegalStateException(CLIENT_UNDEFINED_MSG);

        long timeout = EsUtils.getQueryTimeout();

        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esClient);
        UpdateResponse response = this.client.update(updRequest)
                                             .actionGet(TimeValue.timeValueMillis(timeout));
        return response;
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
        if (this.client == null)
            throw new IllegalStateException(CLIENT_UNDEFINED_MSG);

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esClient);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esClient);
        updRequest.upsert(idxRequest);
        return updRequest;
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
        if (this.client == null)
            throw new IllegalStateException(CLIENT_UNDEFINED_MSG);

        long timeout = EsUtils.getQueryTimeout();

        IndexRequest idxRequest = new IndexRequest(this.indexName, this.typeName, id).source(esClient);
        UpdateRequest updRequest = new UpdateRequest(this.indexName, this.typeName, id).doc(esClient);
        UpdateResponse response = this.client.update(updRequest.upsert(idxRequest)).actionGet(TimeValue.timeValueMillis(timeout));
        return response;
    }

    /**
     * Delete query action (delete documents from the database)
     * 
     * @param query
     */
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

    /**
     * Execute bulk request
     * 
     * @param bulkRequest
     * @return
     */
    public BulkResponse bulk(BulkRequest bulkRequest)
    {
        if (this.client == null)
            throw new IllegalStateException(CLIENT_UNDEFINED_MSG);

        long timeout = EsUtils.getQueryTimeout();

        BulkResponse bulkResponse = this.getClient().bulk(bulkRequest).actionGet(timeout);
        return bulkResponse;
    }
}
