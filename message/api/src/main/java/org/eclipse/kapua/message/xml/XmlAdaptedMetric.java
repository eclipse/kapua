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

    public Object getCastedValue() {
        return ObjectValueConverter.fromString(getValue(), getValueType());
    }
}
