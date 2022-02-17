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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.schema;

import org.eclipse.kapua.service.datastore.internal.mediator.Metric;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata object
 *
 * @since 1.0.0
 */
public class Metadata {

    // Info fields does not change within the same account name
    private final String dataIndexName;
    private final String channelRegistryIndexName;
    private final String clientRegistryIndexName;
    private final String metricRegistryIndexName;
    //

    // Custom mappings can only increase within the same account
    // No removal of existing cached mappings or changes in the
    // existing mappings.
    private final Map<String, Metric> messageMappingsCache;
    //

    /**
     * Get the mappings cache
     *
     * @return
     * @since 1.0.0
     */
    public Map<String, Metric> getMessageMappingsCache() {
        return messageMappingsCache;
    }

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public Metadata(String dataIndexName, String channelRegistryIndexName, String clientRegistryIndexName, String metricRegistryIndexName) {
        this.messageMappingsCache = new HashMap<>(100);
        this.dataIndexName = dataIndexName;
        this.channelRegistryIndexName = channelRegistryIndexName;
        this.clientRegistryIndexName = clientRegistryIndexName;
        this.metricRegistryIndexName = metricRegistryIndexName;
    }

    /**
     * Get the Elasticsearch data index name
     *
     * @return
     * @since 1.0.0
     */
    public String getDataIndexName() {
        return dataIndexName;
    }

    /**
     * Get the Kapua channel index name
     *
     * @return
     * @since 1.4.0
     */
    public String getChannelRegistryIndexName() {
        return channelRegistryIndexName;
    }

    /**
     * Get the Kapua client index name
     *
     * @return
     * @since 1.4.0
     */
    public String getClientRegistryIndexName() {
        return clientRegistryIndexName;
    }

    /**
     * Get the Kapua metric index name
     *
     * @return
     * @since 1.4.0
     */
    public String getMetricRegistryIndexName() {
        return metricRegistryIndexName;
    }
}
