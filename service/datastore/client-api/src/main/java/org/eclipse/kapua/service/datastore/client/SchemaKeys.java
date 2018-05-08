/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client;

/**
 * Schema keys and values type definition
 *
 * @since 1.0
 */
public class SchemaKeys {

    private SchemaKeys() {
    }

    /**
     * Query key
     */
    public static final String KEY_QUERY = "query";
    /**
     * Sort key
     */
    public static final String KEY_SORT = "sort";
    /**
     * Include fields key
     */
    public static final String KEY_INCLUDES = "includes";
    /**
     * Exclude fields key
     */
    public static final String KEY_EXCLUDES = "excludes";
    /**
     * From key (used by queries for paginating the result set)
     */
    public static final String KEY_FROM = "from";
    /**
     * Size key (used by queries to limit the result set size)
     */
    public static final String KEY_SIZE = "size";

    /**
     * Query ascending sort key
     */
    public static final String SORT_ASCENDING_VALUE = "asc";
    /**
     * Query descending sort key
     */
    public static final String SORT_DESCENDING_VALUE = "desc";
    /**
     * All key
     */
    public static final String KEY_ALL = "_all";
    /**
     * Format key
     */
    public static final String KEY_FORMAT = "format";
    /**
     * Index ky
     */
    public static final String KEY_INDEX = "index";
    /**
     * Source key
     */
    public static final String KEY_SOURCE = "_source";
    /**
     * Type key
     */
    public static final String KEY_TYPE = "type";
    /**
     * Enabled key
     */
    public static final String KEY_ENABLED = "enabled";
    /**
     * Dynamic key
     */
    public static final String KEY_DYNAMIC = "dynamic";
    /**
     * Include in all key
     */
    public static final String KEY_INCLUDE_IN_ALL = "include_in_all";
    /**
     * Object binary type
     */
    public static final String TYPE_BINARY = "binary";
    /**
     * Object date type
     */
    public static final String TYPE_DATE = "date";
    /**
     * Object double type
     */
    public static final String TYPE_DOUBLE = "double";
    /**
     * Object geo point type
     */
    public static final String TYPE_GEO_POINT = "geo_point";
    /**
     * Object integer type
     */
    public static final String TYPE_INTEGER = "integer";
    /**
     * Object ip address type
     */
    public static final String TYPE_IP = "ip";
    /**
     * Object object type
     */
    public static final String TYPE_OBJECT = "object";
    /**
     * Object string type
     */
    public static final String TYPE_STRING = "string";
    /**
     * Object keyword type (Structured string that can be indexed with new ES version)<br>
     * <b>Please leave the "index" property for the keyword fields to false (default value) otherwise the value will be analyzed and indexed for the search operation)<br>
     * (see https://www.elastic.co/guide/en/elasticsearch/reference/current/keyword.html)</b>
     */
    public static final String TYPE_KEYWORD = "keyword";

    /**
     * "false" field value
     */
    public static final String VALUE_FALSE = "false";
    /**
     * "true" field value
     */
    public static final String VALUE_TRUE = "true";

    /**
     * Refresh interval (for schema definition)
     */
    public static final String KEY_REFRESH_INTERVAL = "refresh_interval";
    /**
     * Shard number (for schema definition)
     */
    public static final String KEY_SHARD_NUMBER = "number_of_shards";
    /**
     * Replica number (for schema definition)
     */
    public static final String KEY_REPLICA_NUMBER = "number_of_replicas";

    /**
     * Message field
     */
    public static final String FIELD_NAME_MESSAGE = "message";
    /**
     * Metrics field
     */
    public static final String FIELD_NAME_METRICS = "metrics";
    /**
     * Position field
     */
    public static final String FIELD_NAME_POSITION = "position";
    /**
     * Properties field
     */
    public static final String FIELD_NAME_PROPERTIES = "properties";

}
