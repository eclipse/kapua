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
package org.eclipse.kapua.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedTypeValueObject {

    private Class<?> valueType;
    private String value;

    public XmlAdaptedTypeValueObject() {
        // Required by JAXB
    }

    @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    @XmlElement(name = "valueType")
    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> type) {
        this.valueType = type;
    }

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
