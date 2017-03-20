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
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.Metric;
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

    private static final DatastoreObjectFactory datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);
    
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
        String name = (String) fields.get(MetricInfoField.NAME_FULL.field()).getValue();
        String type = (String) fields.get(MetricInfoField.TYPE_FULL.field()).getValue();
        String firstMsgTimestamp = (String) fields.get(MetricInfoField.TIMESTAMP_FULL.field()).getValue();
        String firstMsgId = (String) fields.get(MetricInfoField.MESSAGE_ID_FULL.field()).getValue();
        Object value = fields.get(MetricInfoField.VALUE_FULL.field()).getValue();
        String clientId = fields.get(MetricInfoField.CLIENT_ID.field()).getValue();
        String channel = fields.get(MetricInfoField.CHANNEL.field()).getValue();

        value = fixEsTypeOptimization(type, value);
        
        String metricName = EsUtils.restoreMetricName(name);
        Metric<?> metric = datastoreObjectFactory.newMetric(metricName, value);

        Date timestamp = (Date) EsUtils.convertToKapuaObject("date", firstMsgTimestamp);
        
        MetricInfoImpl finalMetricInfo = new MetricInfoImpl(scopeId, new StorableIdImpl(id));
        finalMetricInfo.setClientId(clientId);
        finalMetricInfo.setChannel(channel);
        finalMetricInfo.setFirstMessageId(new StorableIdImpl(firstMsgId));
        finalMetricInfo.setFirstMessageOn(timestamp);
        finalMetricInfo.setMetric(metric);

        this.metricInfo = finalMetricInfo;
        return this;
    }

    private Object fixEsTypeOptimization(String type, Object value) {
        if (EsUtils.ES_TYPE_STRING.equals(type)) {
            // Do nothing
        }
        else if (EsUtils.ES_TYPE_INTEGER.equals(type)) {
            if (value != null && value instanceof Number)
                return ((Number) value).intValue();
        }
        else if (EsUtils.ES_TYPE_LONG.equals(type)) {
            if (value != null && value instanceof Number)
                return ((Number) value).longValue();
        }
        else if (EsUtils.ES_TYPE_FLOAT.equals(type)) {
            if (value != null && value instanceof Number)
                return ((Number) value).floatValue();
        }
        else if (EsUtils.ES_TYPE_DOUBLE.equals(type)) {
            if (value != null && value instanceof Number)
                return ((Number) value).doubleValue();
        }
        else if (EsUtils.ES_TYPE_BOOL.equals(type)) {
            // Do nothing 
        }
        else if (EsUtils.ES_TYPE_BINARY.equals(type)) {
            // Do nothing
        }
        if (EsUtils.ES_TYPE_DATE.equals(type)) {
            // Do nothing
        }
        
        return value;
    }

    /**
     * Get the built Kapua metric information object
     *
     * @return
     * @since 1.0.0
     */
    public MetricInfo getKapuaMetricInfo() {
        return this.metricInfo;
    }
}
