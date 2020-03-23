/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Represents a typed property.
 * Various flags help in the interpretation and semantics of the property value.
 * For example, a property value might be an array or the property value might have been encrypted.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DeviceXmlConfigPropertyAdapted {

    @XmlEnum
    public enum ConfigPropertyType {
        @XmlEnumValue("String")stringType,
        @XmlEnumValue("Long")longType,
        @XmlEnumValue("Double")doubleType,
        @XmlEnumValue("Float")floatType,
        @XmlEnumValue("Integer")integerType,
        @XmlEnumValue("Byte")byteType,
        @XmlEnumValue("Char")charType,
        @XmlEnumValue("Boolean")booleanType,
        @XmlEnumValue("Short")shortType,
        @XmlEnumValue("Password")passwordType
    }

    /**
     * The name of the property.
     */
    private String name;

    /**
     * Whether the property value is an array.
     */
    private boolean array;

    /**
     * Whether the property value is encrypted.
     */
    private boolean encrypted;

    /**
     * The property type.
     */
    private ConfigPropertyType type;

    /**
     * The property value(s).
     */
    private String[] values;

    public DeviceXmlConfigPropertyAdapted() {
    }

    public DeviceXmlConfigPropertyAdapted(String name,
                                          ConfigPropertyType type,
                                          String[] values) {
        super();

        this.type = type;
        this.values = values;
        this.encrypted = false;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "array")
    public boolean getArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    @XmlAttribute(name = "type")
    public ConfigPropertyType getType() {
        return type;
    }

    public void setType(ConfigPropertyType type) {
        this.type = type;
    }

    @XmlAttribute(name = "encrypted")
    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    @XmlElement(name = "value")
    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
