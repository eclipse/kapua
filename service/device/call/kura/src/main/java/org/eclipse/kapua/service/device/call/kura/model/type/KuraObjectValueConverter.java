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
package org.eclipse.kapua.service.device.call.kura.model.type;

import javax.xml.bind.DatatypeConverter;

public class KuraObjectValueConverter {

    private KuraObjectValueConverter() {
    }

    public static String toString(Object value) {

        String stringValue = null;
        if (value != null) {
            Class<?> clazz = value.getClass();
            if (clazz == byte[].class || clazz == Byte[].class) {
                stringValue = DatatypeConverter.printBase64Binary((byte[]) value);
            } else {
                // String
                // Integer
                // Long
                // Float
                // Double
                // Boolean
                stringValue = value.toString();
            }
        }

        return stringValue;
    }

    public static Object fromString(String stringValue, Class<?> type) throws ClassNotFoundException {

        Object value = null;
        if (stringValue != null) {
            if (type == String.class) {
                value = stringValue;
            } else if (type == Integer.class) {
                value = Integer.parseInt(stringValue);
            } else if (type == Long.class) {
                value = Long.parseLong(stringValue);
            } else if (type == Float.class) {
                value = Float.parseFloat(stringValue);
            } else if (type == Double.class) {
                value = Double.parseDouble(stringValue);
            } else if (type == Boolean.class) {
                value = Boolean.parseBoolean(stringValue);
            } else if (type == byte[].class || type == Byte[].class) {
                value = DatatypeConverter.parseBase64Binary(stringValue);
            } else {
                value = stringValue;
            }
        }

        return value;
    }

}
