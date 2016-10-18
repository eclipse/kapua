/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal.xml;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KapuaMetric represents an instance of a metric published as part of the message.<br>
 * The metric is defined by a name, a type and a value. The name is an alphanumeric string.<br>
 * The type is expressed as a string and can be one of the following values: string, double, int, float, long boolean, base64Binary.
 * The value is the string representation of the corresponding value object.<br>
 * For the primitive types, the conversion is straight forward.<br>
 * Values of type base64Binary represent a metric of type byte array;
 * in this case, the metric value is serialized into a base64 encoded string.
 * 
 * @since 1.0
 * 
 */
@XmlRootElement(name = "metric")
@XmlType(propOrder = { "name", "type", "value" })
public class KapuaMetric
{
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(KapuaMetric.class);

    /**
     * Name of the Metric.
     */
    @XmlElement
    public String               name;

    /**
     * Type of the Metric value. Allowed types are: string, double, int, float, long, boolean, base64Binary.
     */
    @XmlElement
    public String               type;

    /**
     * String representation of the Metric value according to the Metric type.
     */
    @XmlElement
    public String               value;

    /**
     * Constructor
     */
    public KapuaMetric()
    {
    }

    /**
     * Constructor
     * 
     * @param name
     * @param type
     * @param value
     */
    public KapuaMetric(String name, String type, String value)
    {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Constructor
     * 
     * @param name
     * @param type
     * @param value
     */
    public KapuaMetric(String name, Class<?> type, Object value)
    {
        this.name = name;
        this.type = getMetricType(type);
        this.value = getStringValue(value);
    }

    /**
     * Get metric name
     * 
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get metric type.<br>
     * It returns a String representation of the metric type (e.g. string, double, int, ...).
     * 
     * @param type
     * @return
     */
    public static String getMetricType(Class<?> type)
    {

        if (type == null) {
            return null;
        }

        if (String.class.equals(type)) {
            return "string";
        }
        else if (Double.class.equals(type)) {
            return "double";
        }
        else if (Integer.class.equals(type)) {
            return "int";
        }
        else if (Float.class.equals(type)) {
            return "float";
        }
        else if (Long.class.equals(type)) {
            return "long";
        }
        else if (Boolean.class.equals(type)) {
            return "boolean";
        }
        else if (byte[].class.equals(type)) {
            return "base64Binary";
        }
        else {
            // FIXME: What do we do in this case?
            // It should never happen but...
            return type.toString();
        }
    }

    /**
     * Converts the value to a string representation.<br>
     * <br>
     * If the value is a byte[] it returns a base 64 string conversion:<br>
     * <code>
     *    DatatypeConverter.printBase64Binary((byte[]) value);
     * </code>
     * <br>
     * <br>
     * otherwise it invokes the<br>
     * <code>
     *    Sting.valueOf(value);
     * </code>
     * 
     * @param value
     * @return
     */
    public static String getStringValue(Object value)
    {

        if (value == null) {
            return null;
        }

        if (value instanceof byte[]) {
            return DatatypeConverter.printBase64Binary((byte[]) value);
        }
        else {
            return String.valueOf(value);
        }
    }

    /**
     * Get the metric value.<br>
     * It returns the metric value as instance of the metric type (e.g Double for double, Integer for int, ...).
     * 
     * @return
     */
    public Object getValue()
    {

        if (type == null) {
            return null;
        }

        if ("string".equals(type)) {
            return value;
        }
        else if ("double".equals(type)) {
            return Double.valueOf(value);
        }
        else if ("int".equals(type)) {
            return Integer.valueOf(value);
        }
        else if ("float".equals(type)) {
            return Float.valueOf(value);
        }
        else if ("long".equals(type)) {
            return Long.valueOf(value);
        }
        else if ("boolean".equals(type)) {
            return Boolean.valueOf(value);
        }
        else if ("base64Binary".equals(type)) {
            return DatatypeConverter.parseBase64Binary(value);
        }
        else {
            // FIXME: What do we do in this case?
            // It should never happen but...
            return String.valueOf(value);
        }
    }
}
