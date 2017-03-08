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

import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

/**
 * Metric information object builder.<br>
 * This object converts the schema coming from an Elasticsearch search hit to a Kapua metric information object (unmarshal).
 * 
 * @since 1.0
 *
 */
public class MetricInfoObjectBuilder
{

    private MetricInfo metricInfo;

    /**
     * Build a {@link MetricInfoObjectBuilder} from the Elasticsearch search hit
     * 
     * @param searchHit
     * @return
     * @throws EsObjectBuilderException
     */
    public MetricInfoObjectBuilder build(SearchHit searchHit)
        throws EsObjectBuilderException
    {
        String id = searchHit.getId();

        Map<String, SearchHitField> fields = searchHit.fields();
        String account = fields.get(MetricInfoField.ACCOUNT.field()).getValue();
        String name = (String) fields.get(MetricInfoField.NAME_FULL.field()).getValue();
        String type = (String) fields.get(MetricInfoField.TYPE_FULL.field()).getValue();
        String lastMsgTimestamp = (String) fields.get(MetricInfoField.TIMESTAMP_FULL.field()).getValue();
        String lastMsgId = (String) fields.get(MetricInfoField.MESSAGE_ID_FULL.field()).getValue();
        Object value = fields.get(MetricInfoField.VALUE_FULL.field()).getValue();
        String clientId = fields.get(MetricInfoField.CLIENT_ID.field()).getValue();
        String channel = fields.get(MetricInfoField.CHANNEL.field()).getValue();

        MetricInfoImpl finalMetricInfo = new MetricInfoImpl(account, new StorableIdImpl(id));
        finalMetricInfo.setClientId(clientId);
        finalMetricInfo.setChannel(channel);
        finalMetricInfo.setFirstPublishedMessageId(new StorableIdImpl(lastMsgId));

        String metricName = EsUtils.restoreMetricName(name);
        finalMetricInfo.setName(metricName);

        Date timestamp = (Date) EsUtils.convertToKapuaObject("date", lastMsgTimestamp);
        finalMetricInfo.setFirstPublishedMessageTimestamp(timestamp);

        if (EsUtils.ES_TYPE_STRING.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((String) value);
        }

        if (EsUtils.ES_TYPE_INTEGER.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Integer) value);
        }

        if (EsUtils.ES_TYPE_LONG.equals(type)) {
            Object obj = value;
            if (value != null && value instanceof Integer)
                obj = ((Integer) value).longValue();

            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Long) obj);
        }

        if (EsUtils.ES_TYPE_FLOAT.equals(type)) {
            Object obj = value;
            if (value != null && value instanceof Double)
                obj = ((Double) value).floatValue();

            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Float) obj);
        }

        if (EsUtils.ES_TYPE_DOUBLE.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Double) value);
        }

        if (EsUtils.ES_TYPE_BOOL.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Boolean) value);
        }

        if (EsUtils.ES_TYPE_BINARY.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((byte[]) value);
        }

        if (EsUtils.ES_TYPE_DATE.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
            finalMetricInfo.setValue((Date) EsUtils.convertToKapuaObject(type, (String) value));
        }

        if (finalMetricInfo.getType() == null)
            throw new EsObjectBuilderException(String.format("Unknown metric type [%s]", type));

        // FIXME - review all this generic
        this.metricInfo = finalMetricInfo;
        return this;
    }

    /**
     * Get the built Kapua metric information object
     * 
     * @return
     */
    public MetricInfo getKapuaMetricInfo()
    {
        return this.metricInfo;
    }
}
