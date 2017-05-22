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

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_ALL;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_DYNAMIC;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_ENABLED;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_FORMAT;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INCLUDE_IN_ALL;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INDEX;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_TYPE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SOURCE;

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_DATE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_OBJECT;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_STRING;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.VALUE_FIELD_INDEXING_NOT_ANALYZED;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.FIELD_NAME_PROPERTIES;

import org.eclipse.kapua.commons.util.KapuaDateUtils;

/**
 * Metric info schema definition
 * 
 * @since 1.0
 */
public class MetricInfoSchema {

    private MetricInfoSchema() {

    }

    /**
     * Metric information schema name
     */
    public final static String METRIC_TYPE_NAME = "metric";
    /**
     * Metric information - channel
     */
    public final static String METRIC_CHANNEL = "channel";
    /**
     * Metric information - client identifier
     */
    public final static String METRIC_CLIENT_ID = "client_id";
    /**
     * Metric information - scope id
     */
    public static final String METRIC_SCOPE_ID = "scope_id";
    /**
     * Metric information - metric map prefix
     */
    public final static String METRIC_MTR = "metric";
    /**
     * Metric information - name
     */
    public final static String METRIC_MTR_NAME = "name";
    /**
     * Metric information - full name (so with the metric type suffix)
     */
    public final static String METRIC_MTR_NAME_FULL = "metric.name";
    /**
     * Metric information - type
     */
    public final static String METRIC_MTR_TYPE = "type";
    /**
     * Metric information - full type (so with the metric type suffix)
     */
    public final static String METRIC_MTR_TYPE_FULL = "metric.type";
    /**
     * Metric information - value
     */
    public final static String METRIC_MTR_VALUE = "value";
    /**
     * Metric information - full value (so with the metric type suffix)
     */
    public final static String METRIC_MTR_VALUE_FULL = "metric.value";
    /**
     * Metric information - message timestamp (of the first message published in this channel)
     */
    public final static String METRIC_MTR_TIMESTAMP = "timestamp";
    /**
     * Metric information - message timestamp (of the first message published in this channel, with the metric type suffix)
     */
    public final static String METRIC_MTR_TIMESTAMP_FULL = "metric.timestamp";
    /**
     * Metric information - message identifier (of the first message published in this channel)
     */
    public final static String METRIC_MTR_MSG_ID = "message_id";
    /**
     * Metric information - full message identifier (of the first message published in this channel, with the metric type suffix)
     */
    public final static String METRIC_MTR_MSG_ID_FULL = "metric.message_id";

    /**
     * Create and return the Json representation of the metric info schema
     * 
     * @param allEnable
     * @param sourceEnable
     * @return
     * @throws DatamodelMappingException
     */
    public static JsonNode getMetricTypeSchema(boolean allEnable, boolean sourceEnable) throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();

        ObjectNode metricName = SchemaUtil.getObjectNode();
        ObjectNode sourceMetric = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_ENABLED, sourceEnable) });
        metricName.set(KEY_SOURCE, sourceMetric);

        ObjectNode allMetric = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_ENABLED, allEnable) });
        metricName.set(KEY_ALL, allMetric);

        ObjectNode propertiesNode = SchemaUtil.getObjectNode();
        ObjectNode metricAccount = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(METRIC_SCOPE_ID, metricAccount);
        ObjectNode metricClientId = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(METRIC_CLIENT_ID, metricClientId);
        ObjectNode metricChannel = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(METRIC_CHANNEL, metricChannel);

        ObjectNode metricMtrNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_OBJECT), new KeyValueEntry(KEY_ENABLED, true),
                        new KeyValueEntry(KEY_DYNAMIC, false), new KeyValueEntry(KEY_INCLUDE_IN_ALL, false) });
        ObjectNode metricMtrPropertiesNode = SchemaUtil.getObjectNode();
        ObjectNode metricMtrNameNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        metricMtrPropertiesNode.set(METRIC_MTR_NAME, metricMtrNameNode);
        ObjectNode metricMtrTypeNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        metricMtrPropertiesNode.set(METRIC_MTR_TYPE, metricMtrTypeNode);
        ObjectNode metricMtrValueNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        metricMtrPropertiesNode.set(METRIC_MTR_VALUE, metricMtrValueNode);
        ObjectNode metricMtrTimestampNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_DATE), new KeyValueEntry(KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
        metricMtrPropertiesNode.set(METRIC_MTR_TIMESTAMP, metricMtrTimestampNode);
        ObjectNode metricMtrMsgIdNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        metricMtrPropertiesNode.set(METRIC_MTR_MSG_ID, metricMtrMsgIdNode);
        metricMtrNode.set(FIELD_NAME_PROPERTIES, metricMtrPropertiesNode);
        propertiesNode.set(METRIC_MTR, metricMtrNode);

        metricName.set(FIELD_NAME_PROPERTIES, propertiesNode);

        rootNode.set(METRIC_TYPE_NAME, metricName);
        return rootNode;
    }

}
