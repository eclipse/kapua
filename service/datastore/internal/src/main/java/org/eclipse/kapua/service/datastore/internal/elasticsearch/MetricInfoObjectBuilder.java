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

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

/**
 * Metric information object builder.<br>
 * This object converts the schema coming from an Elasticsearch search hit to a Kapua metric information object (unmarshal).
 *
 * @since 1.0.0
 */
public class MetricInfoObjectBuilder {

    private MetricInfo metricInfo;

    /**
     * Build a {@link MetricInfoObjectBuilder} from the Elasticsearch search hit
     *
     * @param searchHit
     * @return
     * @throws EsObjectBuilderException
     * @since 1.0.0
     */
    public MetricInfoObjectBuilder build(SearchHit searchHit)
            throws EsObjectBuilderException {
        String id = searchHit.getId();

        Map<String, SearchHitField> fields = searchHit.fields();
        KapuaId scopeId = KapuaEid.parseCompactId(fields.get(MetricInfoField.SCOPE_ID.field()).getValue());
        String clientId = fields.get(MetricInfoField.CLIENT_ID.field()).getValue();
        String channel = fields.get(MetricInfoField.CHANNEL.field()).getValue();

        String name = (String) fields.get(MetricInfoField.NAME_FULL.field()).getValue();
        String typeString = (String) fields.get(MetricInfoField.TYPE_FULL.field()).getValue();

        String firstMsgTimestamp = (String) fields.get(MetricInfoField.TIMESTAMP_FULL.field()).getValue();
        String firstMsgId = (String) fields.get(MetricInfoField.MESSAGE_ID_FULL.field()).getValue();

        String metricName = EsUtils.restoreMetricName(name);
        Class<?> metricType = EsUtils.convertToKapuaType(typeString);
        Date timestamp = (Date) EsUtils.convertToKapuaObject("date", firstMsgTimestamp);

        MetricInfo finalMetricInfo = new MetricInfoImpl(scopeId, new StorableIdImpl(id));
        finalMetricInfo.setClientId(clientId);
        finalMetricInfo.setChannel(channel);
        finalMetricInfo.setName(metricName);
        finalMetricInfo.setMetricType(metricType);
        finalMetricInfo.setFirstMessageId(new StorableIdImpl(firstMsgId));
        finalMetricInfo.setFirstMessageOn(timestamp);

        this.metricInfo = finalMetricInfo;
        return this;
    }

    /**
     * Get the built Kapua metric information object
     *
     * @return
     * @since 1.0.0
     */
    public MetricInfo getKapuaMetricInfo() {
        return metricInfo;
    }
}
