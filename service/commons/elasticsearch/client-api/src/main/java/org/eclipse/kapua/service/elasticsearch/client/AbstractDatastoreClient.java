/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client;

import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUndefinedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * Datastore client definition. It defines the methods (crud and utilities) to be exposed to the caller.<br>
 * The datastore client implementation should provide a static init method and a static getInstance method that return the already initialized client instance.
 *
 * @since 1.0
 */
public abstract class AbstractDatastoreClient<C extends Closeable> implements DatastoreClient<C> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatastoreClient.class);

    private static final String CLIENT_CLEANUP_ERROR_MSG = "Cannot cleanup rest datastore driver. Cannot close Elasticsearch client instance";

    protected String clientType;
    protected ClientProvider<C> esClientProvider;
    protected ModelContext modelContext;
    protected QueryConverter queryConverter;

    protected AbstractDatastoreClient(String clientType) {
        this.clientType = clientType;
        init();
    }

    protected abstract ClientProvider<C> getNewInstance();

    @Override
    public void init() {
        synchronized (AbstractDatastoreClient.class) {
            logger.info("Starting Elasticsearch {} client...", clientType);
            esClientProvider = getNewInstance();
            logger.info("Starting Elasticsearch {} client... DONE", clientType);
        }
    }

    @Override
    public void close() throws ClientUnavailableException {
        synchronized (AbstractDatastoreClient.class) {
            if (esClientProvider != null) {
                logger.info("Stopping Elasticsearch {} client...", clientType);
                // all fine... try to cleanup the client
                try {
                    getClient().close();
                    esClientProvider = null;
                } catch (Throwable e) {
                    logger.error(CLIENT_CLEANUP_ERROR_MSG, e);
                }
                logger.info("Stopping Elasticsearch {} client... DONE", clientType);
            } else {
                logger.warn("Close method called for a not initialized client!");
            }
        }
    }

    protected C getClient() throws ClientUndefinedException {
        if (esClientProvider != null) {
            return esClientProvider.getClient();
        }

        throw new ClientUndefinedException();
    }

    /**
     * Set the model context
     *
     * @param modelContext
     */
    @Override
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    /**
     * Set the query converter
     *
     * @param queryConverter
     */
    @Override
    public void setQueryConverter(QueryConverter queryConverter) {
        this.queryConverter = queryConverter;
    }

}
