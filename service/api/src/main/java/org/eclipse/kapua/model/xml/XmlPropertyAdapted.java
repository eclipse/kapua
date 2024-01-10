/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public abstract class XmlPropertyAdapted<T extends Enum<T>> {
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
    private T type;

    /**
     * The property value(s).
     */
    @XmlElement(name = "value")
    private String[] values;

    public XmlPropertyAdapted() {
    }

    public XmlPropertyAdapted(String name,
                              T type,
                              String[] values) {
        super();

        this.type = type;
        this.values = values;
        this.encrypted = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
