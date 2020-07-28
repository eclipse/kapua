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
package org.eclipse.kapua.service.elasticsearch.client;

import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInitializationException;

/**
 * {@link ElasticsearchClient} wrapper definition.
 *
 * @param <C> {@link ElasticsearchClient} type.
 * @since 1.0.0
 */
public interface ElasticsearchClientProvider<C extends ElasticsearchClient> extends AutoCloseable {

    /**
     * Initializes the {@link ElasticsearchClientProvider}.
     *
     * @return Itself, to chain invocations.
     * @throws ClientInitializationException in case of error while initializing {@link ElasticsearchClientProvider}
     * @since 1.3.0
     */
    ElasticsearchClientProvider<C> init() throws ClientInitializationException;

    /**
     * Closes the {@link ElasticsearchClientProvider} and all {@link ElasticsearchClient}s
     *
     * @throws Exception in case of errors.
     * @since 1.0.0
     */
    @Override
    void close() throws Exception;

    /**
     * Sets the {@link ElasticsearchClientConfiguration} to use to instantiate and manage the {@link ElasticsearchClient}.
     *
     * @param elasticsearchClientConfiguration The {@link ElasticsearchClientConfiguration}.
     * @return Itself, to chain invocations.
     * @since 1.3.0
     */
    ElasticsearchClientProvider<C> withClientConfiguration(ElasticsearchClientConfiguration elasticsearchClientConfiguration);

    /**
     * Sets the {@link ModelContext} to use in the {@link ElasticsearchClient}.
     *
     * @param modelContext The {@link ElasticsearchClientConfiguration}.
     * @return Itself, to chain invocations.
     * @since 1.3.0
     */
    ElasticsearchClientProvider<C> withModelContext(ModelContext modelContext);

    /**
     * Sets the {@link QueryConverter} to use in the {@link ElasticsearchClient}/
     *
     * @param queryConverter The {@link QueryConverter}.
     * @return Itself, to chain invocations.
     * @since 1.3.0
     */
    ElasticsearchClientProvider<C> withModelConverter(QueryConverter queryConverter);


    /**
     * Gets an initialized {@link ElasticsearchClient} instance.
     *
     * @return An initialized {@link ElasticsearchClient} instance.
     * @since 1.0.0
     */
    C getElasticsearchClient();
}
