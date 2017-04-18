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
package org.eclipse.kapua.message.xml;

import org.eclipse.kapua.model.type.ObjectTypeConverter;

public class MetricTypeConverter extends ObjectTypeConverter {

    private static final String TYPE_BASE_64_BINARY = "base64binary";

    public static String toString(Class<?> clazz) {

        String value;
        if (clazz == byte[].class) {
            value = TYPE_BASE_64_BINARY;
        } else {
            value = ObjectTypeConverter.toString(clazz);
        }

        return value;
    }

    public static Class<?> fromString(String value) throws ClassNotFoundException {
        String lowerCaseValue = value.toLowerCase();
        
        Class<?> clazz;
        if (TYPE_BASE_64_BINARY.equals(lowerCaseValue)) {
            clazz = byte[].class;
        } else {
            clazz = ObjectTypeConverter.fromString(lowerCaseValue);
        }
        return clazz;
    }
}
