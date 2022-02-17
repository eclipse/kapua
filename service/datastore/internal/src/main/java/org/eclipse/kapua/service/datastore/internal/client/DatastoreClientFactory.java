/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.client;

import org.eclipse.kapua.service.datastore.exception.DatastoreInternalError;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
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
     */
    public static ElasticsearchClientProvider<?> getInstance() {
        if (elasticsearchClientProviderInstance == null) {
            synchronized (DatastoreClientFactory.class) {
                if (elasticsearchClientProviderInstance == null) {

                    ElasticsearchClientProvider<?> elasticsearchClientProvider;
                    try {
                        ElasticsearchClientConfiguration esClientConfiguration = DatastoreElasticsearchClientConfiguration.getInstance();

                        Class<ElasticsearchClientProvider<?>> providerClass = (Class<ElasticsearchClientProvider<?>>) Class.forName(esClientConfiguration.getProviderClassName());
                        Constructor<?> constructor = providerClass.getConstructor();
                        elasticsearchClientProvider = (ElasticsearchClientProvider<?>) constructor.newInstance();

                        elasticsearchClientProvider
                                .withClientConfiguration(esClientConfiguration)
                                .withModelContext(new ModelContextImpl())
                                .withModelConverter(new QueryConverterImpl())
                                .init();
                    } catch (Exception e) {
                        throw new DatastoreInternalError(e, "Cannot instantiate Elasticsearch Client");
                    }

                    elasticsearchClientProviderInstance = elasticsearchClientProvider;
                }
            }
        }

        return elasticsearchClientProviderInstance;
    }

    /**
     * Gets the {@link ElasticsearchClient} instance.
     *
     * @return The {@link ElasticsearchClient} instance.
     * @throws ClientUnavailableException see {@link ElasticsearchClientProvider#getElasticsearchClient()}
     * @since 1.3.0
     */
    public static ElasticsearchClient<?> getElasticsearchClient() throws ClientUnavailableException {
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
