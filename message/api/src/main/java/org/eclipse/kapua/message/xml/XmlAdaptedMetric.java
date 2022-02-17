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
package org.eclipse.kapua.message.xml;

import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.model.xml.XmlAdaptedNameTypeValueObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedMetric extends XmlAdaptedNameTypeValueObject {

    public Object getCastedValue() {
        return ObjectValueConverter.fromString(getValue(), getValueType());
    }
}
