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

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class XmlPropertyAdapter<T> {

    private final Class<T> clazz;

    protected XmlPropertyAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public boolean canMarshall(Class<?> objectClass) {
        return objectClass.isArray() ? clazz.equals(objectClass.getComponentType()) : clazz.equals(objectClass);
    }

    public void marshallValues(XmlPropertyAdapted<?> property, Object value) {
        if (!value.getClass().getClass().isArray()) {
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

    public Object unmarshallValues(XmlPropertyAdapted<?> property) {
        Object propValue = null;
        if (!property.getArray()) {
            return unmarshallValue(property.getValues()[0]);
        } else {
            return Arrays.stream(property.getValues()).map(this::unmarshallValue).collect(Collectors.toList()).toArray();
        }
    }

}
