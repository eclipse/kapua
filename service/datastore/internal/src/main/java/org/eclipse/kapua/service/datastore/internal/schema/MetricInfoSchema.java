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

import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;

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
    public static final String METRIC_TYPE_NAME = "metric";
    /**
     * Metric information - channel
     */
    public static final String METRIC_CHANNEL = "channel";
    /**
     * Metric information - client identifier
     */
    public static final String METRIC_CLIENT_ID = "client_id";
    /**
     * Metric information - scope id
     */
    public static final String METRIC_SCOPE_ID = "scope_id";
    /**
     * Metric information - metric map prefix
     */
    public static final String METRIC_MTR = "metric";
    /**
     * Metric information - name
     */
    public static final String METRIC_MTR_NAME = "name";
    /**
     * Metric information - full name (so with the metric type suffix)
     */
    public static final String METRIC_MTR_NAME_FULL = "metric.name";
    /**
     * Metric information - type
     */
    public static final String METRIC_MTR_TYPE = "type";
    /**
     * Metric information - full type (so with the metric type suffix)
     */
    public static final String METRIC_MTR_TYPE_FULL = "metric.type";
    /**
     * Metric information - value
     */
    public static final String METRIC_MTR_VALUE = "value";
    /**
     * Metric information - full value (so with the metric type suffix)
     */
    public static final String METRIC_MTR_VALUE_FULL = "metric.value";
    /**
     * Metric information - message timestamp (of the first message published in this channel)
     */
    public static final String METRIC_MTR_TIMESTAMP = "timestamp";
    /**
     * Metric information - message timestamp (of the first message published in this channel, with the metric type suffix)
     */
    public static final String METRIC_MTR_TIMESTAMP_FULL = "metric.timestamp";
    /**
     * Metric information - message identifier (of the first message published in this channel)
     */
    public static final String METRIC_MTR_MSG_ID = "message_id";
    /**
     * Metric information - full message identifier (of the first message published in this channel, with the metric type suffix)
     */
    public static final String METRIC_MTR_MSG_ID_FULL = "metric.message_id";

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
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, sourceEnable) });
        metricName.set(SchemaKeys.KEY_SOURCE, sourceMetric);

        ObjectNode allMetric = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, allEnable) });
        metricName.set(SchemaKeys.KEY_ALL, allMetric);

        ObjectNode propertiesNode = SchemaUtil.getObjectNode();
        ObjectNode metricAccount = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(METRIC_SCOPE_ID, metricAccount);
        ObjectNode metricClientId = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(METRIC_CLIENT_ID, metricClientId);
        ObjectNode metricChannel = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(METRIC_CHANNEL, metricChannel);

        ObjectNode metricMtrNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_OBJECT), new KeyValueEntry(SchemaKeys.KEY_ENABLED, true),
                        new KeyValueEntry(SchemaKeys.KEY_DYNAMIC, false), new KeyValueEntry(SchemaKeys.KEY_INCLUDE_IN_ALL, false) });
        ObjectNode metricMtrPropertiesNode = SchemaUtil.getObjectNode();
        ObjectNode metricMtrNameNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        metricMtrPropertiesNode.set(METRIC_MTR_NAME, metricMtrNameNode);
        ObjectNode metricMtrTypeNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        metricMtrPropertiesNode.set(METRIC_MTR_TYPE, metricMtrTypeNode);
        ObjectNode metricMtrValueNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        metricMtrPropertiesNode.set(METRIC_MTR_VALUE, metricMtrValueNode);
        ObjectNode metricMtrTimestampNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
        metricMtrPropertiesNode.set(METRIC_MTR_TIMESTAMP, metricMtrTimestampNode);
        ObjectNode metricMtrMsgIdNode = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        metricMtrPropertiesNode.set(METRIC_MTR_MSG_ID, metricMtrMsgIdNode);
        metricMtrNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, metricMtrPropertiesNode);
        propertiesNode.set(METRIC_MTR, metricMtrNode);

        metricName.set(SchemaKeys.FIELD_NAME_PROPERTIES, propertiesNode);

        rootNode.set(METRIC_TYPE_NAME, metricName);
        return rootNode;
    }

}
