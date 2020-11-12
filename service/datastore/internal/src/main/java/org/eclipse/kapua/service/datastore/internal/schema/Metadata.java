/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
    private String dataIndexName;
    private String registryIndexName;
    //

    // Custom mappings can only increase within the same account
    // No removal of existing cached mappings or changes in the
    // existing mappings.
    private Map<String, Metric> messageMappingsCache;
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
     * Contructor.
     *
     * @since 1.0.0
     */
    public Metadata(String dataIndexName, String registryIndexName) {
        messageMappingsCache = new HashMap<>(100);
        this.dataIndexName = dataIndexName;
        this.registryIndexName = registryIndexName;
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
     * Get the Kapua data index name
     *
     * @return
     * @since 1.0.0
     */
    public String getRegistryIndexName() {
        return registryIndexName;
    }
}
