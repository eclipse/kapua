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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link MetricInfo} Elasticsearch schema
 * 
 * @since 1.0
 *
 */
public enum MetricInfoField implements StorableField
{
    /**
     * Account name
     */
    ACCOUNT(EsSchema.METRIC_ACCOUNT),
    /**
     * Client identifier
     */
    CLIENT_ID(EsSchema.METRIC_CLIENT_ID),
    /**
     * Channel
     */
    CHANNEL(EsSchema.METRIC_CHANNEL),
    /**
     * Full metric name (so with the metric type suffix)
     */
    NAME_FULL(EsSchema.METRIC_MTR_NAME_FULL),
    /**
     * Metric type full name (not the acronym)
     */
    TYPE_FULL(EsSchema.METRIC_MTR_TYPE_FULL),
    /**
     * Metric value
     */
    VALUE_FULL(EsSchema.METRIC_MTR_VALUE_FULL),
    /**
     * Metric timestamp (derived from the message that published the metric)
     */
    TIMESTAMP_FULL(EsSchema.METRIC_MTR_TIMESTAMP_FULL),
    /**
     * Message identifier
     */
    MESSAGE_ID_FULL(EsSchema.METRIC_MTR_MSG_ID_FULL);

    private String field;

    private MetricInfoField(String name)
    {
        this.field = name;
    }

    @Override
    public String field()
    {
        return field;
    }
}
