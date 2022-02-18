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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Represents a typed property.<br>
 * Various flags help in the interpretation and semantics of the property value.<br>
 * For example, a property value might be an array or the property value might have been
 * encrypted.
 *
 * @since 1.0
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlConfigPropertyAdapted {

    @XmlEnum
    public enum ConfigPropertyType {
        @XmlEnumValue("String")
        stringType, @XmlEnumValue("Long")
        longType, @XmlEnumValue("Double")
        doubleType, @XmlEnumValue("Float")
        floatType, @XmlEnumValue("Integer")
        integerType, @XmlEnumValue("Byte")
        byteType, @XmlEnumValue("Char")
        charType, @XmlEnumValue("Boolean")
        booleanType, @XmlEnumValue("Short")
        shortType, @XmlEnumValue("Password")
        passwordType
    }

    /**
     * The name of the property.
     */
    @XmlAttribute(name = "name")
    private String name;

    /**
     * Whether the property value is an array.
     */
    @XmlAttribute(name = "array")
    private boolean array;

    /**
     * Whether the property value is encrypted.
     */
    @XmlAttribute(name = "encrypted")
    private boolean encrypted;

    /**
     * The property type.
     */
    @XmlAttribute(name = "type")
    private ConfigPropertyType type;

    /**
     * The property value(s).
     */
    @XmlElement(name = "value")
    private String[] values;

    /**
     * Constructor
     */
    public XmlConfigPropertyAdapted() {
    }

    /**
     * Constructor
     *
     * @param name
     * @param type
     * @param values
     */
    public XmlConfigPropertyAdapted(String name,
            ConfigPropertyType type,
            String[] values) {
        super();

        this.type = type;
        this.values = values;
        this.encrypted = false;
    }

    /**
     * Get the property name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the property name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the is array flag property
     *
     * @return
     */
    public boolean getArray() {
        return array;
    }

    /**
     * Set the is array flag property
     *
     * @param array
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * Get the property type
     *
     * @return
     */
    public ConfigPropertyType getType() {
        return type;
    }

    /**
     * Set the property type
     *
     * @param type
     */
    public void setType(ConfigPropertyType type) {
        this.type = type;
    }

    /**
     * Get the is encrypted flag property
     *
     * @return
     */
    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * Set the is encrypted flag property
     *
     * @param encrypted
     */
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    /**
     * Get property values
     *
     * @return
     */
    public String[] getValues() {
        return values;
    }

    /**
     * Set property values
     *
     * @param values
     */
    public void setValues(String[] values) {
        this.values = values;
    }
}
