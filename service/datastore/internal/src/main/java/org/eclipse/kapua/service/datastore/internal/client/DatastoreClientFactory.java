/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.client;

import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInitializationException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * Manages the {@link ElasticsearchClientProvider} as a singleton for the message store.
 *
 * @since 1.0.0
 */
public class DatastoreClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DatastoreClientFactory.class);

    private static ElasticsearchClientProvider<?> elasticsearchClientProviderInstance;

    private DatastoreClientFactory() {
    }

    /**
     * Gets the {@link ElasticsearchClientProvider} instance.
     * <p>
     * The implementation is specified by {@link DatastoreElasticsearchClientConfiguration#getProviderClassName()}.
     *
     * @return An Elasticsearch client.
     * @throws ClientInitializationException if error occurs if the {@link ElasticsearchClient} cannot be initialized
     */
    public static ElasticsearchClientProvider getInstance() throws ClientInitializationException {
        //lazy synchronization
        if (elasticsearchClientProviderInstance == null) {
            synchronized (DatastoreClientFactory.class) {
                if (elasticsearchClientProviderInstance == null) {

                    ElasticsearchClientConfiguration esClientConfiguration = DatastoreElasticsearchClientConfiguration.getInstance();

                    try {
                        Class<ElasticsearchClientProvider<?>> providerClass = (Class<ElasticsearchClientProvider<?>>) Class.forName(esClientConfiguration.getProviderClassName());
                        Constructor<?> constructor = providerClass.getConstructor();
                        elasticsearchClientProviderInstance = (ElasticsearchClientProvider<?>) constructor.newInstance();
                    } catch (Exception e) {
                        throw new ClientInitializationException(e, esClientConfiguration.getProviderClassName());
                    }

                    elasticsearchClientProviderInstance
                            .withClientConfiguration(esClientConfiguration)
                            .withModelContext(new ModelContextImpl())
                            .withModelConverter(new QueryConverterImpl())
                            .init();
                }
            }
        }

        return elasticsearchClientProviderInstance;
    }

    /**
     * Gets the {@link ElasticsearchClient} instance.
     *
     * @return The {@link ElasticsearchClient} instance.
     * @throws ClientInitializationException see {@link #getInstance()}
     * @since 1.3.0
     */
    public static ElasticsearchClient getElasticsearchClient() throws ClientInitializationException, ClientUnavailableException {
        return getInstance().getElasticsearchClient();
    }

    /**
     * Closes the {@link ElasticsearchClientProvider} instance.
     *
     * @since 1.0.0
     */
    public static void close() {
        if (elasticsearchClientProviderInstance != null) {
            synchronized (DatastoreClientFactory.class) {
                if (elasticsearchClientProviderInstance != null) {
                    try {
                        elasticsearchClientProviderInstance.close();
                    } catch (Exception e) {
                        LOG.error("Unable to close ElasticsearchClientProvider instance.", e);
                    } finally {
                        elasticsearchClientProviderInstance = null;
                    }
                }
            }
        }
    }

}
