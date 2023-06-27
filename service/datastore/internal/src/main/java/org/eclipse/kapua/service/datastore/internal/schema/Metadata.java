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

    // Custom mappings can only increase within the same account
    // No removal of existing cached mappings or changes in the
    // existing mappings.
    private final Map<String, Metric> messageMappingsCache;

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
    public Metadata() {
        this.messageMappingsCache = new HashMap<>(100);
    }


}
