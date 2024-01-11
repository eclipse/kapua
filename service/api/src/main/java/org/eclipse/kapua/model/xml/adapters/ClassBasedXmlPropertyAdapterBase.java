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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ClassBasedXmlPropertyAdapterBase<T> implements XmlPropertyAdapter {

    private final Class<T> clazz;

    protected ClassBasedXmlPropertyAdapterBase(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean canMarshall(Class<?> objectClass) {
        return objectClass.isArray() ? clazz.equals(objectClass.getComponentType()) : clazz.equals(objectClass);
    }

    @Override
    public void marshallValues(XmlPropertyAdapted<?> property, Object value) {
        if (!value.getClass().isArray()) {
            property.setArray(false);
            property.setEncrypted(false);
            property.setValues(new String[]{marshallValue(value)});
        } else {
            property.setArray(true);
            Object[] nativeValues = (Object[]) value;
            String[] stringValues = new String[nativeValues.length];
            for (int i = 0; i < nativeValues.length; i++) {
                if (nativeValues[i] != null) {
                    stringValues[i] = this.marshallValue(nativeValues[i]);
                }
            }
            property.setValues(stringValues);
        }
    }

    public String marshallValue(Object object) {
        return object.toString();
    }

    public abstract T unmarshallValue(String value);

    @Override
    public Object unmarshallValues(XmlPropertyAdapted<?> property) {
        if (!property.getArray()) {
            return unmarshallValue(property.getValues()[0]);
        } else {
            final List<T> items = Arrays.stream(property.getValues()).map(this::unmarshallValue).collect(Collectors.toList());
            return items.toArray((T[]) Array.newInstance(clazz, items.size()));
        }
    }

}
