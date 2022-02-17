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

import org.eclipse.kapua.model.type.ObjectTypeConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ObjectTypeXmlAdapter extends XmlAdapter<String, Class<?>> {

    @Override
    public String marshal(Class<?> clazz) {
        return ObjectTypeConverter.toString(clazz);
    }

    @Override
    public Class<?> unmarshal(String value) throws ClassNotFoundException {
        return ObjectTypeConverter.fromString(value);
    }
}
