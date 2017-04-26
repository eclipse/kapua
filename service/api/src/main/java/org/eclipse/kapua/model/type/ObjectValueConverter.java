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
package org.eclipse.kapua.model.type;

import javax.xml.bind.DatatypeConverter;

public class ObjectValueConverter {

    public static String toString(Object value) {

        String stringValue = null;
        if (value != null) {
            Class<?> clazz = value.getClass();
            if (clazz == Byte[].class) {
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

    public static Object fromString(String stringValue, Class<?> type) {

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
            } else if (type == byte[].class) {
                value = DatatypeConverter.parseBase64Binary(stringValue);
            } else {
                value = stringValue;
            }
        }

        return value;
    }

}
