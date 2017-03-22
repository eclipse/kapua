package org.eclipse.kapua.message.xml;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedMetric {

    private String name;
    private Class<?> type;
    private String value;

    public XmlAdaptedMetric() {
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(MetricTypeXmlAdapter.class)
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlTransient
    public Object getCastedValue() {
        Object convertedValue;

        Class<?> clazz = getType();
        String stringValue = getValue();

        if (clazz == String.class) {
            convertedValue = stringValue;
        } else if (clazz == Integer.class) {
            convertedValue = Integer.parseInt(stringValue);
        } else if (clazz == Long.class) {
            convertedValue = Long.parseLong(stringValue);
        } else if (clazz == Float.class) {
            convertedValue = Float.parseFloat(stringValue);
        } else if (clazz == Double.class) {
            convertedValue = Double.parseDouble(stringValue);
        } else if (clazz == Boolean.class) {
            convertedValue = Boolean.parseBoolean(stringValue);
        } else if (clazz == byte[].class) {
            convertedValue = DatatypeConverter.parseBase64Binary(stringValue);
        } else {
            convertedValue = stringValue;
        }

        return convertedValue;
    }
}
