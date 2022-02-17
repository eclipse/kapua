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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Schema utility class
 *
 * @since 1.0.0
 */
public class SchemaUtil {


    /**
     * @since 1.0.0
     */
    private SchemaUtil() {
    }

    /**
     * Return a map of map. The contained map has, as entries, the couples subKeys-values.<br>
     * <b>NOTE! No arrays subKeys-values coherence will be done (length or null check)!</b>
     *
     * @param key
     * @param subKeys
     * @param values
     * @return
     * @since 1.0.0
     */
    public static Map<String, Object> getMapOfMap(String key, String[] subKeys, String[] values) {
        Map<String, String> mapChildren = new HashMap<>();
        for (int i = 0; i < subKeys.length; i++) {
            mapChildren.put(subKeys[i], values[i]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(key, mapChildren);
        return map;
    }

    /**
     * Get the Elasticsearch data index name
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    public static String getDataIndexName(KapuaId scopeId) {
        return DatastoreUtils.getDataIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     *
     * @param scopeId
     * @return
     * @since 1.0.0
     */
    public static String getChannelIndexName(KapuaId scopeId) {
        return DatastoreUtils.getChannelIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     *
     * @param scopeId
     * @return
     */
    public static String getClientIndexName(KapuaId scopeId) {
        return DatastoreUtils.getClientIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     *
     * @param scopeId
     * @return
     */
    public static String getMetricIndexName(KapuaId scopeId) {
        return DatastoreUtils.getMetricIndexName(scopeId);
    }

}
