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

public class KuraObjectTypeConverter {

    private static final String TYPE_STRING = "STRING";
    private static final String TYPE_INT = "INTEGER";
    private static final String TYPE_LONG = "LONG";
    private static final String TYPE_FLOAT = "FLOAT";
    private static final String TYPE_DOUBLE = "DOUBLE";
    private static final String TYPE_BOOLEAN = "BOOLEAN";
    private static final String TYPE_BINARY = "BYTE_ARRAY";

    private KuraObjectTypeConverter() {
    }

    public static String toString(Class<?> clazz) {
        String value;
        if (clazz == String.class) {
            value = TYPE_STRING;
        } else if (clazz == Integer.class) {
            value = TYPE_INT;
        } else if (clazz == Long.class) {
            value = TYPE_LONG;
        } else if (clazz == Float.class) {
            value = TYPE_FLOAT;
        } else if (clazz == Double.class) {
            value = TYPE_DOUBLE;
        } else if (clazz == Boolean.class) {
            value = TYPE_BOOLEAN;
        } else if (clazz == byte[].class || clazz == Byte[].class) {
            value = TYPE_BINARY;
        } else {
            value = clazz.getName();
        }
        return value;
    }

    public static Class<?> fromString(String value) throws ClassNotFoundException {
        Class<?> clazz;
        if (TYPE_STRING.equals(value)) {
            clazz = String.class;
        } else if (TYPE_INT.equals(value)) {
            clazz = Integer.class;
        } else if (TYPE_LONG.equals(value)) {
            clazz = Long.class;
        } else if (TYPE_FLOAT.equals(value)) {
            clazz = Float.class;
        } else if (TYPE_DOUBLE.equals(value)) {
            clazz = Double.class;
        } else if (TYPE_BOOLEAN.equals(value)) {
            clazz = Boolean.class;
        } else if (TYPE_BINARY.equals(value)) {
            clazz = byte[].class;
        } else {
            clazz = Class.forName(value);
        }
        return clazz;
    }

}
