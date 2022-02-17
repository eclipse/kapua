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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAdaptedTypeValueObject {

    @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    @XmlElement(name = "valueType")
    private Class<?> valueType;

    @XmlElement(name = "value")
    private String value;

    public XmlAdaptedTypeValueObject() {
        // Required by JAXB
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
        this.value = value;
    }
}
