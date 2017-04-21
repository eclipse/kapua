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
package org.eclipse.kapua.service.datastore.internal.schema;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.mediator.Metric;

/**
 * Metadata object
 * 
 * @since 1.0
 *
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
     */
    public Map<String, Metric> getMessageMappingsCache() {
        return messageMappingsCache;
    }

    /**
     * Contruct metadata
     */
    public Metadata(String dataIndexName, String registryIndexName) {
        messageMappingsCache = new HashMap<String, Metric>(100);
        this.dataIndexName = dataIndexName;
        this.registryIndexName = registryIndexName;
    }

    /**
     * Get the Elasticsearch data index name
     * 
     * @return
     */
    public String getDataIndexName() {
        return dataIndexName;
    }

    /**
     * Get the Kapua data index name
     * 
     * @return
     */
    public String getRegistryIndexName() {
        return registryIndexName;
    }
}