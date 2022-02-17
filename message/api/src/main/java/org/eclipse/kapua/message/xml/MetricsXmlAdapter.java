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
package org.eclipse.kapua.message.xml;

import org.eclipse.kapua.model.type.ObjectValueConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MetricsXmlAdapter extends XmlAdapter<XmlAdaptedMetrics, Map<String, Object>> {

    @Override
    public XmlAdaptedMetrics marshal(Map<String, Object> sourceMap) throws Exception {

        List<XmlAdaptedMetric> adaptedMapItems = new ArrayList<>();

        for (Entry<String, Object> sourceEntry : sourceMap.entrySet()) {
            if (sourceEntry.getValue() != null) {
                XmlAdaptedMetric adaptedMapItem = new XmlAdaptedMetric();

                adaptedMapItem.setName(sourceEntry.getKey());
                adaptedMapItem.setValueType(sourceEntry.getValue().getClass());
                adaptedMapItem.setValue(ObjectValueConverter.toString(sourceEntry.getValue()));

                adaptedMapItems.add(adaptedMapItem);
            }
        }

        return new XmlAdaptedMetrics(adaptedMapItems);
    }

    //
    // Unmarshal
    //

    @Override
    public Map<String, Object> unmarshal(XmlAdaptedMetrics sourceAdapter) throws Exception {

        Map<String, Object> map = new HashMap<>();

        for (XmlAdaptedMetric sourceItem : sourceAdapter.getAdaptedMetrics()) {
            String name = sourceItem.getName();
            Object value = ObjectValueConverter.fromString(sourceItem.getValue(), sourceItem.getValueType());

            map.put(name, value);
        }

        return map;
    }
}
