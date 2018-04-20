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