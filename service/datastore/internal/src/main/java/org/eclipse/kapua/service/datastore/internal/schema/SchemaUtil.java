/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Schema utility class
 *
 * @since 1.0
 */
public class SchemaUtil {


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
     */
    public static String getDataIndexName(KapuaId scopeId) {
        return DatastoreUtils.getDataIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     *
     * @param scopeId
     * @return
     */
    public static String getKapuaIndexName(KapuaId scopeId) {
        return DatastoreUtils.getRegistryIndexName(scopeId);
    }

}
