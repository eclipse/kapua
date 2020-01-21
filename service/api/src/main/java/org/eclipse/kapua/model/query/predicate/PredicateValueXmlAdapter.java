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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.Arrays;

import org.eclipse.kapua.model.type.ObjectValueConverter;

public class PredicateValueXmlAdapter extends XmlAdapter<XmlAdaptedPredicateValue, Object> {

    @Override
    public Object unmarshal(XmlAdaptedPredicateValue xmlAdaptedPredicateValue) throws Exception {
        if (xmlAdaptedPredicateValue.getArrayValue() != null) {
            return Arrays.stream(xmlAdaptedPredicateValue.getArrayValue()).map(value -> ObjectValueConverter.fromString(value, xmlAdaptedPredicateValue.getValueType())).toArray();
        } else {
            return ObjectValueConverter.fromString(xmlAdaptedPredicateValue.getValue(), xmlAdaptedPredicateValue.getValueType());
        }
    }

    @Override
    public XmlAdaptedPredicateValue marshal(Object value) throws Exception {
        XmlAdaptedPredicateValue xmlAdaptedPredicateValue = new XmlAdaptedPredicateValue();
        if (value.getClass().isArray()) {
            Object[] objects = (Object[]) value;
            xmlAdaptedPredicateValue.setArrayValue(Arrays.stream(objects).map(ObjectValueConverter::toString).toArray(String[]::new));
            if (objects.length > 0) {
                xmlAdaptedPredicateValue.setValueType(objects[0].getClass());
            }
        } else {
            xmlAdaptedPredicateValue.setValueType(value.getClass());
            xmlAdaptedPredicateValue.setValue(ObjectValueConverter.toString(value));
        }
        return xmlAdaptedPredicateValue;
    }

}
