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

import org.eclipse.kapua.model.xml.ObjectTypeXmlAdapter;

/**
 * XML Adapter from metric type. <br> 
 * This adapter extends {@link ObjectTypeXmlAdapter} and serialize/deserialize in a different way the 
 * {@link Byte}[] metric type.
 * 
 * This is because Kapua uses Base64 encoding of binary data. 
 * 
 * @since 1.0.0
 */
public class MetricTypeXmlAdapter extends ObjectTypeXmlAdapter {

    private static final String TYPE_BASE_64_BINARY = "base64binary";

    @Override
    public String marshal(Class<?> clazz) {

        String value;
        if (clazz == byte[].class) {
            value = TYPE_BASE_64_BINARY;
        } else {
            value = super.marshal(clazz);
        }

        return value;
    }

    @Override
    public Class<?> unmarshal(String value) throws ClassNotFoundException {
        Class<?> clazz;
        if (TYPE_BASE_64_BINARY.equals(value)) {
            clazz = byte[].class;
        } else {
            clazz = super.unmarshal(value);
        }
        return clazz;
    }
}
