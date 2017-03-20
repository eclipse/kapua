/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Kapua identifier adapter. It marshal and unmarshal the Kapua identifier in a proper way.
 * 
 * @since 1.0.0
 */
public class MetricTypeAdapter extends XmlAdapter<String, Class<?>>{

    @Override
    public Class<?> unmarshal(String value) throws Exception {
        if ("string".equals(value))
            return String.class;

        if ("int".equals(value))
            return Integer.class;

        if ("long".equals(value))
            return Long.class;

        if ("float".equals(value))
            return Float.class;

        if ("double".equals(value)) 
            return Double.class;

        if ("date".equals(value))
            return Date.class;

        if ("binary".equals(value))
            return Byte[].class;

        if ("boolean".equals(value))
            return Boolean.class;

        return Class.forName(value);
    }

    @Override
    public String marshal(Class<?> value) throws Exception {
        if (value == String.class)
            return "string";

        if (value == Integer.class)
            return "int";

        if (value == Long.class)
            return "long";

        if (value == Float.class)
            return "float";

        if (value == Double.class)
            return "double";

        if (value == Date.class)
            return "date";

        if (value == Byte[].class)
            return "binary";

        if (value == Boolean.class)
            return "boolean";

        return value.getSimpleName().toLowerCase();
    }
}
