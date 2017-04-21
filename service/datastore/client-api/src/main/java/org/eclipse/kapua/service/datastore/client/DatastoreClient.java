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
package org.eclipse.kapua.service.datastore.client;

import org.eclipse.kapua.service.datastore.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsResponse;
import org.eclipse.kapua.service.datastore.client.model.InsertRequest;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Datastore client definition. It defines the methods (crud and utilities) to be exposed to the caller.
 * 
 * @since 1.0
 */
public interface DatastoreClient {

    /**
     * Insert
     * 
     * @param insertRequest
     * @return
     * @throws ClientException
     */
    public InsertResponse insert(InsertRequest insertRequest) throws ClientException;

    /**
     * Upsert
     * 
     * @param updateRequest
     * @return
     * @throws ClientException
     */
    public UpdateResponse upsert(UpdateRequest updateRequest) throws ClientException;

    /**
     * Bulk upsert
     * 
     * @param bulkUpdateRequest
     * @return
     * @throws ClientException
     */
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpdateRequest) throws ClientException;

    /**
     * Find by query (this method returns the first result found that matches the quesy)
     * 
     * @param typeDescriptor
     * @param query
     * @param clazz
     * @return
     * @throws ClientException
     */
    public <T> T find(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException;

    /**
     * Find by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @param clazz
     * @return
     * @throws ClientException
     */
    public <T> ResultList<T> query(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException;

    /**
     * Count by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @return
     * @throws ClientException
     */
    public long count(TypeDescriptor typeDescriptor, Object query) throws ClientException;

    /**
     * Delete by id
     * 
     * @param typeDescriptor
     * @param id
     * @throws ClientException
     */
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException;

    /**
     * Delete by query criteria
     * 
     * @param typeDescriptor
     * @param query
     * @throws ClientException
     */
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException;

    // Indexes / mappings section

    /**
     * Check if the index exists
     * 
     * @param indexExistsRequest
     * @return
     * @throws ClientException
     */
    public IndexExistsResponse isIndexExists(IndexExistsRequest indexExistsRequest) throws ClientException;

    /**
     * Create the index
     * 
     * @param indexName
     * @param indexSettings
     * @throws ClientException
     */
    public void createIndex(String indexName, ObjectNode indexSettings) throws ClientException;

    /**
     * Check if the mapping exists
     * 
     * @param typeDescriptor
     * @return
     * @throws ClientException
     */
    public boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException;

    /**
     * Put the mapping
     * 
     * @param typeDescriptor
     * @param mapping
     * @throws ClientException
     */
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException;

    // ModelContext

    /**
     * Set the model context
     * 
     * @param modelContext
     */
    public void setModelContext(ModelContext modelContext);

    /**
     * Set the query converter
     * 
     * @param queryConverter
     */
    public void setQueryConverter(QueryConverter queryConverter);

}
