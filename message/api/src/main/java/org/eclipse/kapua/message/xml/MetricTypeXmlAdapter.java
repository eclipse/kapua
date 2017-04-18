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

    @Override
    public String marshal(Class<?> clazz) {
        return MetricTypeConverter.toString(clazz);
    }

    @Override
    public Class<?> unmarshal(String value) throws ClassNotFoundException {
        return MetricTypeConverter.fromString(value);
    }
}
