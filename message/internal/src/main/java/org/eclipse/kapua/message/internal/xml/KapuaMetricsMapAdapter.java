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
package org.eclipse.kapua.message.internal.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Kapua metrics map adapter. It marshal and unmarshal the Kapua metrics map in a proper way.
 * 
 * @since 1.0
 *
 */
public class KapuaMetricsMapAdapter extends XmlAdapter<KapuaMetricsMapType,Map<String,Object>> {

    @Override
    public KapuaMetricsMapType marshal(Map<String,Object> metrics) throws Exception {

        KapuaMetricsMapType metricsMap = new KapuaMetricsMapType();
        for(Entry<String,Object> entry : metrics.entrySet()) {
            if (entry != null) {
                if (entry.getValue() != null) {
                    KapuaMetric metricType = new KapuaMetric(entry.getKey(), entry.getValue().getClass(), entry.getValue());
                    metricsMap.metrics.add(metricType);
                }
            }
        }
        return metricsMap;
    }


    @Override
    public Map<String,Object> unmarshal(KapuaMetricsMapType metrics) throws Exception {

        Map<String,Object> metricsMap = new HashMap<String,Object>();
        for(KapuaMetric metric : metrics.metrics) {

            String name  = metric.name;
            Object value = metric.getValue();
            metricsMap.put(name, value);
        }
        return metricsMap;
    }
}
