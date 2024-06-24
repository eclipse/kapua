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

import org.eclipse.kapua.model.xml.XmlPropertyAdapted;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class XmlPropertiesAdapter<T extends Enum<T>, V extends XmlPropertyAdapted<T>> extends XmlAdapter<V[], Map<String, Object>> {
    private final Class<V> propertyClass;
    private final Supplier<V> adaptedPropertyFactory;
    private final Map<T, XmlPropertyAdapter> xmlPropertyAdapters;

    public XmlPropertiesAdapter(Class<V> propertyClass, Supplier<V> adaptedPropertyFactory, Map<T, XmlPropertyAdapter> xmlPropertyAdapters) {
        this.propertyClass = propertyClass;
        this.adaptedPropertyFactory = adaptedPropertyFactory;
        this.xmlPropertyAdapters = xmlPropertyAdapters;
    }

    @Override
    public Map<String, Object> unmarshal(V[] properties) {
        Map<String, Object> unmarshalledProperties = new HashMap<>();

         Optional.ofNullable(properties)
                .map(Arrays::asList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(adaptedProp -> adaptedProp.getType() == null || xmlPropertyAdapters.containsKey((adaptedProp.getType())))
                .forEach(adaptedProp -> unmarshalledProperties.put(adaptedProp.getName(), unmarshalProperty(adaptedProp)));

        // This was a better way to do it, but Collectors.toMap does not accept `null` values.
        // See Collectors#153 for reference.
        //                .collect(Collectors.toMap(
        //                        adaptedProp -> adaptedProp.getName(),
        //                        adaptedProp -> {
        //                            final XmlPropertyAdapter xmlPropertyAdapter = xmlPropertyAdapters.get(adaptedProp.getType());
        //                            return xmlPropertyAdapter.unmarshallValues(adaptedProp);
        //                        }));

        return unmarshalledProperties;
    }

    /**
     * It unmarshal the given {@link XmlPropertyAdapted}.
     * <p>
     * If {@link XmlPropertyAdapted#getType()} is {@code null} it means that {@link XmlPropertyAdapted#getValues()} was {@code null}, so we can't determine its {@link XmlPropertyAdapted#getType()}.
     * By the way, if original value was {@code null} we can safely return {@code null}.
     *
     * @param adaptedProp The {@link XmlPropertyAdapted} to unmarshal
     * @return The unmarshalled {@link XmlPropertyAdapted}
     * @since 2.1.0
     */
    public Object unmarshalProperty(V adaptedProp) {
        if (adaptedProp.getType() != null) {
            XmlPropertyAdapter xmlPropertyAdapter = xmlPropertyAdapters.get(adaptedProp.getType());
            return xmlPropertyAdapter.unmarshallValues(adaptedProp);
        }
        else {
            return null;
        }
    }

    @Override
    public V[] marshal(Map<String, Object> props) {
        final List<V> adaptedProperties = Optional.ofNullable(props)
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .map(nameAndValue -> {
                    final V resEntry = adaptedPropertyFactory.get();
                    resEntry.setName(nameAndValue.getKey());

                    // Some properties can be sent as `null` so we cannot determine the type by the value.
                    if (nameAndValue.getValue() != null) {
                        final Object value = nameAndValue.getValue();
                        xmlPropertyAdapters
                                .entrySet()
                                .stream()
                                .filter(pa -> pa.getValue().canMarshall(value.getClass()))
                                .findFirst()
                                .ifPresent(typeToAdapter -> {
                                    resEntry.setType(typeToAdapter.getKey());
                                    typeToAdapter.getValue().marshallValues(resEntry, value);
                                });
                    }

                    return resEntry;
                })
                .collect(Collectors.toList());

        return adaptedProperties.toArray((V[]) Array.newInstance(propertyClass, adaptedProperties.size()));
    }
}
