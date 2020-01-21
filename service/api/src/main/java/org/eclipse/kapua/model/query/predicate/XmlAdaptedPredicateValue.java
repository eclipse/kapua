/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.query.predicate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.xml.ObjectTypeXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAdaptedPredicateValue {

    @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    @XmlElement(name = "valueType")
    private Class<?> valueType;

    @XmlElement(name = "value")
    private String value;

    @XmlElement(name = "arrayValue")
    private String[] arrayValue;

    public XmlAdaptedPredicateValue() {
    }

    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> type) {
        this.valueType = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (getArrayValue() != null) {
            throw new IllegalStateException("\"value\" and \"arrayValue\" properties are mutually exclusive");
        }
        this.value = value;
    }

    public String[] getArrayValue() {
        return arrayValue;
    }

    public void setArrayValue(String[] arrayValue) {
        if (getValue() != null) {
            throw new IllegalStateException("\"value\" and \"arrayValue\" properties are mutually exclusive");
        }
        this.arrayValue = arrayValue;
    }

}
