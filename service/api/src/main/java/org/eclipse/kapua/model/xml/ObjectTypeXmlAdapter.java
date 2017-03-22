package org.eclipse.kapua.model.xml;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ObjectTypeXmlAdapter extends XmlAdapter<String, Class<?>> {

    private static final String TYPE_STRING = "string";
    private static final String TYPE_INTEGER = "int";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_BOOLEAN = "boolean";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_BINARY = "binary";

    @Override
    public String marshal(Class<?> clazz) {
        String value;
        if (clazz == String.class) {
            value = TYPE_STRING;
        } else if (clazz == Integer.class) {
            value = TYPE_INTEGER;
        } else if (clazz == Long.class) {
            value = TYPE_LONG;
        } else if (clazz == Float.class) {
            value = TYPE_FLOAT;
        } else if (clazz == Double.class) {
            value = TYPE_DOUBLE;
        } else if (clazz == Boolean.class) {
            value = TYPE_BOOLEAN;
        } else if (clazz == Date.class) {
            value = TYPE_DATE;
        } else if (clazz == byte[].class) {
            value = TYPE_BINARY;
        } else {
            value = clazz.getName();
        }
        return value;
    }

    @Override
    public Class<?> unmarshal(String value) throws Exception {
        Class<?> clazz;
        if (TYPE_STRING.equals(value)) {
            clazz = String.class;
        } else if (TYPE_INTEGER.equals(value)) {
            clazz = Integer.class;
        } else if (TYPE_LONG.equals(value)) {
            clazz = Long.class;
        } else if (TYPE_FLOAT.equals(value)) {
            clazz = Float.class;
        } else if (TYPE_DOUBLE.equals(value)) {
            clazz = Double.class;
        } else if (TYPE_BOOLEAN.equals(value)) {
            clazz = Boolean.class;
        } else if (TYPE_DATE.equals(value)) {
            clazz = Date.class;
        } else if (TYPE_BINARY.equals(value)) {
            clazz = byte[].class;
        } else {
            clazz = Class.forName(value);
        }
        return clazz;
    }

}
