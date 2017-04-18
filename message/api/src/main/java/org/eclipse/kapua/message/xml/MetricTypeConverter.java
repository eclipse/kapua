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
        Class<?> clazz;
        if (TYPE_BASE_64_BINARY.equals(value)) {
            clazz = byte[].class;
        } else {
            clazz = ObjectTypeConverter.fromString(value);
        }
        return clazz;
    }
}
