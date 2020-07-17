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

import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUndefinedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * Elasticsearch client base implementation.
 *
 * @since 1.0.0
 */
public abstract class AbstractElasticsearchClient<C extends Closeable> implements ElasticsearchClient<C> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractElasticsearchClient.class);

    private static final String CLIENT_CLEANUP_ERROR_MSG = "Cannot cleanup REST Elasticsearch driver. Cannot close Elasticsearch client instance";

    protected String clientType;
    protected ElasticsearchClientProvider<C> elasticsearchClientProvider;
    protected ModelContext modelContext;
    protected QueryConverter queryConverter;

    protected AbstractElasticsearchClient(String clientType) {
        this.clientType = clientType;
        init();
    }

    @Override
    public void init() {
        synchronized (AbstractElasticsearchClient.class) {
            logger.info("Starting Elasticsearch {} client...", clientType);
            elasticsearchClientProvider = getNewInstance();
            logger.info("Starting Elasticsearch {} client... DONE", clientType);
        }
    }

    @Override
    public void close() {
        synchronized (AbstractElasticsearchClient.class) {
            if (elasticsearchClientProvider != null) {
                logger.info("Stopping Elasticsearch {} client...", clientType);
                // all fine... try to cleanup the client
                try {
                    getClient().close();
                    elasticsearchClientProvider = null;
                } catch (Exception e) {
                    logger.error(CLIENT_CLEANUP_ERROR_MSG, e);
                }
                logger.info("Stopping Elasticsearch {} client... DONE", clientType);
            } else {
                logger.warn("Close method called for a not initialized client!");
            }
        }
    }

    /**
     * Creates a new {@link ElasticsearchClientProvider}.
     * <p>
     * The instance returned must be used a singleton.
     *
     * @return A new {@link ElasticsearchClientProvider}.
     * @since 1.0.0
     */
    protected abstract ElasticsearchClientProvider<C> getNewInstance();

    /**
     * Gets the client from the {@link ElasticsearchClientProvider}.
     * <p>
     * It invokes {@link ElasticsearchClientProvider#getClient()} after {@code null} checking.
     *
     * @return The client from the {@link ElasticsearchClientProvider}.
     * @throws ClientUndefinedException if {@link ElasticsearchClientProvider} has not being initialized.
     */
    protected C getClient() throws ClientUndefinedException {
        if (elasticsearchClientProvider != null) {
            return elasticsearchClientProvider.getClient();
        }

        throw new ClientUndefinedException();
    }

    @Override
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    @Override
    public void setQueryConverter(QueryConverter queryConverter) {
        this.queryConverter = queryConverter;
    }

}
