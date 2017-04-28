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
package org.eclipse.kapua.message.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.model.xml.XmlAdaptedNameTypeValueObject;

@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedMetric extends XmlAdaptedNameTypeValueObject {
    //
    // private String name;
    // private Class<?> type;
    // private String value;
    //
    // public XmlAdaptedMetric() {
    // }
    //
    // @XmlElement(name = "name")
    // public String getName() {
    // return name;
    // }
    //
    // public void setName(String name) {
    // this.name = name;
    // }
    //
    // @XmlElement(name = "type")
    // @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    // public Class<?> getType() {
    // return type;
    // }
    //
    // public void setType(Class<?> type) {
    // this.type = type;
    // }
    //
    // @XmlElement(name = "value")
    // public String getValue() {
    // return value;
    // }
    //
    // public void setValue(String value) {
    // this.value = value;
    // }

    public Object getCastedValue() {
        return ObjectValueConverter.fromString(getValue(), getType());
    }
}
