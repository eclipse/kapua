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
 * KapuaMetricValue represents an instance of a metric stored in metricsByValue or metricsByTimestamp column families.
 * The metric is defined by a timestamp, a value and the UUID of the message on which the metric was published. The timestamp is a long type.
 * The value is the string representation of the corresponding value object.?????
 * For the primitive types, the conversion is straight forward.
 * Values of type base64Binary represent a metric of type byte array;
 * in this case, the metric value is serialized into a base64 encoded string.
 */

@XmlRootElement(name="metricValue")
@XmlType(propOrder= {"timestamp","value","uuid"})
public class KapuaMetricValue {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(KapuaMetricValue.class);

    /**
     * Timestamp of the Metric.
     */
    @XmlElement
    public Long timestamp;

    /**
     * String representation of the Metric value according to the Metric type.
     */
    @XmlElement
    public String value;

    /**
     * UUID of the messages on which the metric was published.
     */
    @XmlElement
    public String uuid;

    public KapuaMetricValue() {
    }

    public KapuaMetricValue(Long timestamp, String value, String uuid) {
        this.timestamp  = timestamp;
        this.value = value;
        this.uuid = uuid;
    }

    public KapuaMetricValue(Long timestamp, Object value, String uuid) {
        this.timestamp  = timestamp;
        this.value = getStringValue(value);
        this.uuid = uuid;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public String getValue() {
        return this.value;
    }

    public String getUUID() {
        return this.uuid;
    }

    public static String getStringValue(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof byte[]) {
            return DatatypeConverter.printBase64Binary((byte[]) value);
        } else {
            return String.valueOf(value);
        }
    }

}
