/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

public class MetricInfoBuilder {

	private MetricInfo metricInfo;
	
	public MetricInfoBuilder build(SearchHit searchHit) throws Exception {
		
		Map<String, SearchHitField> fields = searchHit.fields();
		String id = searchHit.getId();
		String scope = fields.get(EsSchema.METRIC_ACCOUNT).getValue();
		String name = (String) fields.get(EsSchema.METRIC_MTR_NAME_FULL).getValue();
		String type = (String) fields.get(EsSchema.METRIC_MTR_TYPE_FULL).getValue();
		String lastMsgTimestamp = (String) fields.get(EsSchema.METRIC_MTR_TYPE_FULL).getValue();
		String lastMsgId = (String) fields.get(EsSchema.METRIC_MTR_MSG_ID_FULL).getValue();
		Object value = fields.get(EsSchema.METRIC_MTR_VALUE_FULL).getValue();
        String asset = fields.get(EsSchema.METRIC_ASSET).getValue();
        String semTopic = fields.get(EsSchema.METRIC_SEM_NAME).getValue();
		
        Date timestamp = (Date)EsUtils.convertToKapuaObject("date", lastMsgTimestamp);
        KapuaTopic topic = new KapuaTopic(scope, asset, semTopic);
        
		String metricName = EsUtils.restoreMetricName(name);
		MetricInfo finalMetricInfo = new MetricInfoImpl(scope, new StorableIdImpl(id));
		finalMetricInfo.setFullTopicName(topic.getFullTopic());
		finalMetricInfo.setLastMessageId(new StorableIdImpl(lastMsgId));
		finalMetricInfo.setLastMessageTimestamp(timestamp);
		finalMetricInfo.setName(metricName);

        if (EsUtils.ES_TYPE_STRING.equals(type)) {
            finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
			finalMetricInfo.setValue((String)value);
		}
		
		if (EsUtils.ES_TYPE_INTEGER.equals(type)) {
		    finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
		    finalMetricInfo.setValue((Integer)value);
		}
		
		if (EsUtils.ES_TYPE_LONG.equals(type)) {
			Object obj = value;
			if (value != null && value instanceof Integer) 
				obj = ((Integer)value).longValue();
			
			finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
			finalMetricInfo.setValue((Long)obj);
		}
		
		if (EsUtils.ES_TYPE_FLOAT.equals(type)) {
		    finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
		    finalMetricInfo.setValue((Float)value);
		}
		
		if (EsUtils.ES_TYPE_DOUBLE.equals(type)) {
		    finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
		    finalMetricInfo.setValue((Double)value);
		}
		
		if (EsUtils.ES_TYPE_BOOL.equals(type)) {
		    finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
		    finalMetricInfo.setValue((Boolean)value);
		}
		
		if (EsUtils.ES_TYPE_BINARY.equals(type)) {
		    finalMetricInfo.setType(EsUtils.convertToKapuaType(type));
		    finalMetricInfo.setValue((byte[])value);
		}
		
		if (finalMetricInfo.getType() == null)
			throw new Exception(String.format("Unknown metric type [%s]", type));

		// FIXME - review all this generic
		this.metricInfo = finalMetricInfo;
		return this;
	}
	
	public MetricInfo getKapuaMetricInfo() {
		return this.metricInfo;
	}
}
