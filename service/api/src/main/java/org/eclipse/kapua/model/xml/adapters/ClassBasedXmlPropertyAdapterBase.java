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

import com.google.common.base.Strings;
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
    public boolean doesEncrypt() {
        return false;
    }

    @Override
    public void marshallValues(XmlPropertyAdapted<?> property, Object value) {
        if (!value.getClass().isArray()) {
            property.setArray(false);
            property.setEncrypted(doesEncrypt());
            property.setValues(new String[]{marshallValue(value)});
        } else {
            property.setArray(true);
            property.setEncrypted(doesEncrypt());
            Object[] nativeValues = (Object[]) value;
            String[] stringValues = new String[nativeValues.length];
            for (int i = 0; i < nativeValues.length; i++) {
                if (nativeValues[i] != null) {
                    stringValues[i] = this.marshallValue(nativeValues[i]);
                }
                else {
                    stringValues[i] = null;
                }
            }
            property.setValues(stringValues);
        }
    }

    public String marshallValue(Object object) {
        return object.toString();
    }

    /**
     * Whether the {@link ClassBasedXmlPropertyAdapterBase} implementation can unmarshall a empty {@link String}.

     * @return {@code true} if it can, {@code false} otherwise.
     * @since 2.1.0
     */
    public abstract boolean canUnmarshallEmptyString();

    public abstract T unmarshallValue(String value);

    @Override
    public Object unmarshallValues(XmlPropertyAdapted<?> property) {
        if (!property.getArray()) {
            String[] values = property.getValues();

            // Values might not have been defined
            // ie:
            //
            // <property name="propertyName" array="false" encrypted="false" type="Integer">
            // </property>
            if (values == null || values.length == 0) {
                return null;
            }

            // Value might be empty and some XmlPropertyAdapter can't handle empty values (ie: DoublePropertyAdapter).
            //
            // ie:
            //
            // <property name="propertyName" array="false" encrypted="false" type="Integer">
            //     <value/>
            // </property>
            //
            // <property name="propertyName" array="false" encrypted="false" type="Integer">
            //     <value><value/>
            // </property>
            String value = values[0];
            if (Strings.isNullOrEmpty(value) && !canUnmarshallEmptyString()) {
                // FIXME: we should return an empty String, but ESF is not currently handling the DeviceConfiguration recevied.
                // This might be the default behaviour to treat single values and arrays values the same...
                return null;
            }

            return unmarshallValue(property.getValues()[0]);
        } else {
            String[] values = property.getValues();

            // Values might not have been defined
            // ie:
            //
            // <property name="propertyName" array="true" encrypted="false" type="Integer">
            // </property>
            if (values == null) {
                return null;
            }

            final List<T> items = Arrays
                    .stream(property.getValues())
                    .map(value ->
                            !Strings.isNullOrEmpty(value) || canUnmarshallEmptyString() ?
                                unmarshallValue(value) :
                                null
                    )
                    .collect(Collectors.toList());
            return items.toArray((T[]) Array.newInstance(clazz, items.size()));
        }
    }

}
