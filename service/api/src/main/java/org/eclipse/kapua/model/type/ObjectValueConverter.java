/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

/**
 * Utilities to convert the value of objects to serialize them.
 *
 * @since 1.0.0
 */
public class ObjectValueConverter {

    private ObjectValueConverter() {
    }

    /**
     * Converts the given value to its {@link String} representation.
     * <p>
     * For {@link Byte}[] and {@code byte}[] {@link ByteArrayConverter#toString(Byte[])} is used.
     * Other types are converted using {@link Object#toString()}
     *
     * @param value The value to convert.
     * @return The {@link String} representation.
     * @since 1.0.0
     */
    public static String toString(Object value) {

        String stringValue = null;
        if (value != null) {
            Class<?> clazz = value.getClass();
            if (clazz == byte[].class) {
                stringValue = ByteArrayConverter.toString((byte[]) value);
            } else if (clazz == Byte[].class) {
                stringValue = ByteArrayConverter.toString((Byte[]) value);
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

    /**
     * Converts the given {@link String} into the given {@link Class} type.
     *
     * @param stringValue The {@link String} to convert.
     * @param type        The {@link Class} to which to convert to.
     * @return The converted value.
     * @since 1.0.0
     */
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
            } else if (type == byte[].class || type == Byte[].class) {
                value = ByteArrayConverter.fromString(stringValue);
            } else {
                value = stringValue;
            }
        }

        return value;
    }
}
