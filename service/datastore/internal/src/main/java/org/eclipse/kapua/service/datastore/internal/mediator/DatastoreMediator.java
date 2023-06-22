/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.Schema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.storable.exception.MappingException;

import javax.inject.Inject;
import java.util.Map;

/**
 * Datastore mediator definition
 *
 * @since 1.0.0
 */
public class DatastoreMediator implements MessageStoreMediator,
        ClientInfoRegistryMediator,
        ChannelInfoRegistryMediator,
        MetricInfoRegistryMediator {

    private final Schema esSchema;

    @Inject
    public DatastoreMediator(ElasticsearchClientProvider elasticsearchClientProvider) {
        this.esSchema = new Schema(elasticsearchClientProvider);
    }

    // Message Store Mediator methods

    @Override
    public Metadata getMetadata(KapuaId scopeId, long indexedOn) throws ClientException, MappingException {
        return esSchema.synch(scopeId, indexedOn);
    }

    @Override
    public void onUpdatedMappings(KapuaId scopeId, long indexedOn, Map<String, Metric> metrics) throws ClientException, MappingException {
        esSchema.updateMessageMappings(scopeId, indexedOn, metrics);
    }

    /*
     * ClientInfo Store Mediator methods
     */
    @Override
    public void onAfterClientInfoDelete(KapuaId scopeId, ClientInfo clientInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    /*
     * ChannelInfo Store Mediator methods
     */
    @Override
    public void onBeforeChannelInfoDelete(ChannelInfo channelInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    @Override
    public void onAfterChannelInfoDelete(ChannelInfo channelInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    /*
     *
     * MetricInfo Store Mediator methods
     */
    @Override
    public void onAfterMetricInfoDelete(KapuaId scopeId, MetricInfo metricInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }
}
