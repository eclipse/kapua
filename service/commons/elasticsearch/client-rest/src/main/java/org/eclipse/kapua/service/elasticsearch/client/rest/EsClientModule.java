/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.rest;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.datastore.exception.DatastoreInternalError;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreElasticsearchClientConfiguration;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;

import javax.inject.Singleton;

public class EsClientModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(MetricsEsClient.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    ElasticsearchClientProvider elasticsearchClientProvider(MetricsEsClient metricsEsClient, StorableIdFactory storableIdFactory, DatastoreUtils datastoreUtils) {
        try {
            ElasticsearchClientConfiguration esClientConfiguration = DatastoreElasticsearchClientConfiguration.getInstance();
            return new RestElasticsearchClientProvider(metricsEsClient)
                    .withClientConfiguration(esClientConfiguration)
                    .withModelContext(new ModelContextImpl(storableIdFactory, datastoreUtils))
                    .withModelConverter(new QueryConverterImpl());
        } catch (Exception e) {
            throw new DatastoreInternalError(e, "Cannot instantiate Elasticsearch Client");
        }
    }
}
