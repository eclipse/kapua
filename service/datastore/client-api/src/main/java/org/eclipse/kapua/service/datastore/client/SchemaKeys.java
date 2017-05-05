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
package org.eclipse.kapua.service.datastore.client;

/**
 * Schema keys and values type definition
 * 
 * @since 1.0
 */
public class SchemaKeys {

    /**
     * Query key
     */
    public final static String KEY_QUERY = "query";
    /**
     * Sort key
     */
    public final static String KEY_SORT = "sort";
    /**
     * Include fields key
     */
    public final static String KEY_INCLUDES = "includes";
    /**
     * Exclude fields key
     */
    public final static String KEY_EXCLUDES = "excludes";
    /**
     * From key (used by queries for paginating the result set)
     */
    public final static String KEY_FROM = "from";
    /**
     * Size key (used by queries to limit the result set size)
     */
    public final static String KEY_SIZE = "size";

    /**
     * Query ascending sort key
     */
    public final static String SORT_ASCENDING_VALUE = "asc";
    /**
     * Query descending sort key
     */
    public final static String SORT_DESCENDING_VALUE = "desc";
    /**
     * All key
     */
    public final static String KEY_ALL = "_all";
    /**
     * Format key
     */
    public final static String KEY_FORMAT = "format";
    /**
     * Keyword key
     */
    public final static String KEY_KEYWORD = "keyword";
    /**
     * Index ky
     */
    public final static String KEY_INDEX = "index";
    /**
     * Source key
     */
    public final static String KEY_SOURCE = "_source";
    /**
     * Type key
     */
    public final static String KEY_TYPE = "type";
    /**
     * Enabled key
     */
    public final static String KEY_ENABLED = "enabled";
    /**
     * Dynamic key
     */
    public final static String KEY_DYNAMIC = "dynamic";
    /**
     * Include in all key
     */
    public final static String KEY_INCLUDE_IN_ALL = "include_in_all";
    /**
     * Object binary type
     */
    public final static String TYPE_BINARY = "binary";
    /**
     * Object date type
     */
    public final static String TYPE_DATE = "date";
    /**
     * Object double type
     */
    public final static String TYPE_DOUBLE = "double";
    /**
     * Object geo point type
     */
    public final static String TYPE_GEO_POINT = "geo_point";
    /**
     * Object integer type
     */
    public final static String TYPE_INTEGER = "integer";
    /**
     * Object ip address type
     */
    public final static String TYPE_IP = "ip";
    /**
     * Object object type
     */
    public final static String TYPE_OBJECT = "object";
    /**
     * Object string type
     */
    public final static String TYPE_STRING = "string";
    /**
     * Not analyzed field value
     */
    public final static String VALUE_FIELD_INDEXING_NOT_ANALYZED = "not_analyzed";
    /**
     * "No" field value
     */
    public final static String VALUE_NO = "no";

    /**
     * Refresh interval (for schema definition)
     */
    public final static String KEY_REFRESH_INTERVAL = "refresh_interval";
    /**
     * Shard number (for schema definition)
     */
    public final static String KEY_SHARD_NUMBER = "number_of_shards";
    /**
     * Replica number (for schema definition)
     */
    public final static String KEY_REPLICA_NUMBER = "number_of_replicas";

    /**
     * Position field
     */
    public final static String FIELD_NAME_POSITION = "position";
    /**
     * Properties field
     */
    public final static String FIELD_NAME_PROPERTIES = "properties";

}
