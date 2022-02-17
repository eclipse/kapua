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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoCreator;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link MetricInfo} client schema
 *
 * @since 1.0.0
 */
public enum MetricInfoField implements StorableField {
    /**
     * Account name
     *
     * @since 1.0.0
     */
    SCOPE_ID(MetricInfoSchema.METRIC_SCOPE_ID),

    /**
     * Client identifier
     *
     * @since 1.0.0
     */
    CLIENT_ID(MetricInfoSchema.METRIC_CLIENT_ID),

    /**
     * Channel
     *
     * @since 1.0.0
     */
    CHANNEL(MetricInfoSchema.METRIC_CHANNEL),

    /**
     * Full metric name (so with the metric type suffix)
     *
     * @since 1.0.0
     */
    NAME_FULL(MetricInfoSchema.METRIC_MTR_NAME_FULL),

    /**
     * Metric type full name (not the acronym)
     *
     * @since 1.0.0
     */
    TYPE_FULL(MetricInfoSchema.METRIC_MTR_TYPE_FULL),

    /**
     * Metric timestamp (derived from the message that published the metric)
     *
     * @since 1.3.0
     */
    TIMESTAMP(MetricInfoSchema.METRIC_MTR_TIMESTAMP),

    /**
     * Metric timestamp (derived from the message that published the metric)
     *
     * @since 1.0.0
     */
    TIMESTAMP_FULL(MetricInfoSchema.METRIC_MTR_TIMESTAMP_FULL),


    /**
     * Message identifier
     *
     * @since 1.0.0
     */
    MESSAGE_ID_FULL(MetricInfoSchema.METRIC_MTR_MSG_ID_FULL);

    private String field;

    private MetricInfoField(String name) {
        this.field = name;
    }

    @Override
    public String field() {
        return field;
    }

    /**
     * Get the metric identifier (return the hash code of the string obtained by combining accountName, clientId, channel and the converted metricName and metricType).<br>
     * <b>If the id is null then it is generated.</b>
     *
     * @param id
     * @param scopeId
     * @param clientId
     * @param channel
     * @param metricName
     * @param metricType
     * @return
     */
    private static String getOrDeriveId(StorableId id, KapuaId scopeId, String clientId, String channel, String metricName, Class<?> metricType) {
        if (id == null) {
            String metricMappedName = DatastoreUtils.getMetricValueQualifier(metricName, DatastoreUtils.convertToClientMetricType(metricType));

            return DatastoreUtils.getHashCode(scopeId.toCompactId(), clientId, channel, metricMappedName);
        } else {
            return id.toString();
        }
    }

    /**
     * Get the metric identifier getting parameters from the metricInfoCreator. Then it calls {@link MetricInfoField#getOrDeriveId(StorableId id, KapuaId scopeId, String clientId, String channel, String
     * metricName, Class metricType)}
     *
     * @param id
     * @param metricInfoCreator
     * @return
     */
    public static String getOrDeriveId(StorableId id, MetricInfoCreator metricInfoCreator) {
        return getOrDeriveId(id,
                metricInfoCreator.getScopeId(),
                metricInfoCreator.getClientId(),
                metricInfoCreator.getChannel(),
                metricInfoCreator.getName(),
                metricInfoCreator.getMetricType());// use short me
    }

    /**
     * Get the metric identifier getting parameters from the metricInfo. Then it calls {@link MetricInfoField#getOrDeriveId(StorableId id, KapuaId scopeId, String clientId, String channel, String
     * metricName, Class metricType)}
     *
     * @param id
     * @param metricInfo
     * @return
     */
    public static String getOrDeriveId(StorableId id, MetricInfo metricInfo) {
        return getOrDeriveId(id,
                metricInfo.getScopeId(),
                metricInfo.getClientId(),
                metricInfo.getChannel(),
                metricInfo.getName(),
                metricInfo.getMetricType());
    }

}
