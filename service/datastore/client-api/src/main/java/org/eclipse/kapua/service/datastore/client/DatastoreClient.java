/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client;

import org.eclipse.kapua.service.datastore.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.datastore.client.model.IndexRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexResponse;
import org.eclipse.kapua.service.datastore.client.model.InsertRequest;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Datastore client definition. It defines the methods (crud and utilities) to be exposed to the caller.<br>
 * The datastore client implementation should provide a static init method and a static getInstance method that return the already initialized client instance.
 * 
 * @since 1.0
 */
public interface DatastoreClient {

    /**
     * Close the underlying client resources (such as datastore client connection)
     * 
     * @throws ClientException
     */
    void close() throws ClientException;

    /**
     * Insert
     * 
     * @param insertRequest
     * @return
     * @throws ClientException
     */
    InsertResponse insert(InsertRequest insertRequest) throws ClientException;

    /**
     * Upsert
     * 
     * @param updateRequest
     * @return
     * @throws ClientException
     */
    UpdateResponse upsert(UpdateRequest updateRequest) throws ClientException;

    /**
     * Bulk upsert
     * 
     * @param bulkUpdateRequest
     * @return
     * @throws ClientException
     */
    BulkUpdateResponse upsert(BulkUpdateRequest bulkUpdateRequest) throws ClientException;

    /**
     * Find by query (this method returns the first result found that matches the quesy)
     * 
     * @param typeDescriptor
     * @param query
     * @param clazz
     * @return
     * @throws ClientException
     */
    <T> T find(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException;

    /**
     * Find by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @param clazz
     * @return
     * @throws ClientException
     */
    <T> ResultList<T> query(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException;

    /**
     * Count by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @return
     * @throws ClientException
     */
    long count(TypeDescriptor typeDescriptor, Object query) throws ClientException;

    /**
     * Delete by id
     * 
     * @param typeDescriptor
     * @param id
     * @throws ClientException
     */
    void delete(TypeDescriptor typeDescriptor, String id) throws ClientException;

    /**
     * Delete by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @throws ClientException
     */
    void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException;

    // Indexes / mappings section

    /**
     * Check if the index exists
     * 
     * @param indexRequest
     * @return
     * @throws ClientException
     */
    IndexResponse isIndexExists(IndexRequest indexRequest) throws ClientException;

    /**
     * Create the index
     * 
     * @param indexName
     * @param indexSettings
     * @throws ClientException
     */
    void createIndex(String indexName, ObjectNode indexSettings) throws ClientException;

    /**
     * Check if the mapping exists
     * 
     * @param typeDescriptor
     * @return
     * @throws ClientException
     */
    boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException;

    /**
     * Put the mapping
     * 
     * @param typeDescriptor
     * @param mapping
     * @throws ClientException
     */
    void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException;

    /**
     * Force the datastore to refresh the indexes.
     * 
     * @throws ClientException
     */
    void refreshAllIndexes() throws ClientException;

    /**
     * Delete all indexes.<br>
     * <b>WARNING!<br>
     * This call will erase the whole database!<br>
     * Be careful using it! :)
     * </b>
     */
    void deleteAllIndexes() throws ClientException;

    /**
     * Delete the specified indexes.<br>
     * <b>WARNING!<br>
     * Once dropped the indexes cannot be restored!<br>
     * Be careful using it! :)
     * </b>
     * 
     * @param indexes
     * @throws ClientException
     */
    public void deleteIndexes(String... indexes) throws ClientException;

    /**
     * Find indexes by prefix.
     * 
     * @param indexRequest
     * @return
     * @throws ClientException
     */
    public IndexResponse findIndexes(IndexRequest indexRequest) throws ClientException;

    // ModelContext

    /**
     * Set the model context
     * 
     * @param modelContext
     */
    void setModelContext(ModelContext modelContext);

    /**
     * Set the query converter
     * 
     * @param queryConverter
     */
    void setQueryConverter(QueryConverter queryConverter);

}
