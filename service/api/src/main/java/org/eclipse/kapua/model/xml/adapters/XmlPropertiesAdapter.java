/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.xml.adapters;

import org.eclipse.kapua.model.xml.XmlPropertiesAdapted;
import org.eclipse.kapua.model.xml.XmlPropertyAdapted;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class XmlPropertiesAdapter<T extends Enum<T>, V extends XmlPropertyAdapted<T>> extends XmlAdapter<XmlPropertiesAdapted<T, V>, Map<String, Object>> {
    private final Supplier<XmlPropertiesAdapted<T, V>> adaptedPropertiesSupplier;
    private final Supplier<V> adaptedPropertySupplier;
    private final Map<T, XmlPropertyAdapter> xmlPropertyAdapters;

    public XmlPropertiesAdapter(Supplier<XmlPropertiesAdapted<T, V>> adaptedPropertiesSupplier, Supplier<V> adaptedPropertySupplier, Map<T, XmlPropertyAdapter> xmlPropertyAdapters) {
        this.adaptedPropertiesSupplier = adaptedPropertiesSupplier;
        this.adaptedPropertySupplier = adaptedPropertySupplier;
        this.xmlPropertyAdapters = xmlPropertyAdapters;
    }

    @Override
    public Map<String, Object> unmarshal(XmlPropertiesAdapted<T, V> adaptedPropsAdapted) {
        Collection<V> adaptedProps = adaptedPropsAdapted.getProperties();
        if (adaptedProps == null) {
            return new HashMap<>();
        }

        Map<String, Object> properties = new HashMap<>();
        for (V adaptedProp : adaptedProps) {
            String propName = adaptedProp.getName();
            T type = adaptedProp.getType();
            if (type != null) {
                final XmlPropertyAdapter xmlPropertyAdapter = xmlPropertyAdapters.get(adaptedProp.getType());
                final Object propValue = xmlPropertyAdapter.unmarshallValues(adaptedProp);
                properties.put(propName, propValue);
            }
        }
        return properties;
    }

    @Override
    public XmlPropertiesAdapted<T, V> marshal(Map<String, Object> props) {
        List<V> adaptedValues = new ArrayList<>();
        if (props != null) {
            props.forEach((name, value) -> {
                if (value == null) {
                    return;
                }
                V adaptedValue = adaptedPropertySupplier.get();
                adaptedValue.setName(name);
                final Optional<Map.Entry<T, XmlPropertyAdapter>> maybeAdapter = xmlPropertyAdapters
                        .entrySet()
                        .stream()
                        .filter(pa -> pa.getValue().canMarshall(value.getClass()))
                        .findFirst();
                if (maybeAdapter.isPresent() == false) {
                    //throw?
                    return;
                }
                final Map.Entry<T, XmlPropertyAdapter> typeToAdapter = maybeAdapter.get();
                adaptedValue.setType(typeToAdapter.getKey());
                typeToAdapter.getValue().marshallValues(adaptedValue, value);
                adaptedValues.add(adaptedValue);
            });
        }

        XmlPropertiesAdapted<T, V> result = adaptedPropertiesSupplier.get();
        result.setProperties(adaptedValues);
        return result;
    }
}
