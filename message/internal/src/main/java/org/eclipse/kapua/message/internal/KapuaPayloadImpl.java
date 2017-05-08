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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kapua.message.KapuaPayload;

import com.google.common.io.BaseEncoding;

/**
 * Kapua message payload object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaPayloadImpl implements KapuaPayload {

    private static final BaseEncoding HEX_ENCODER = BaseEncoding.base16().upperCase();
    private static final Comparator<Entry<String, Object>> ENTRY_COMPARATOR = Comparator.comparing(Map.Entry<String, Object>::getKey);
    private Map<String, Object> properties;
    private byte[] body;

    /**
     * Constructor
     */
    public KapuaPayloadImpl() {
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }

        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    public static Object forDisplay(Object value) {
        if (value instanceof byte[]) {
            return HEX_ENCODER.encode((byte[]) value);
        } else if (value instanceof Float || value instanceof Double || value instanceof Integer || value instanceof Long || value instanceof Boolean || value instanceof String) {
            return value;
        } else {
            return "";
        }
    }

    @Override
    public String toDisplayString() {
        if (properties == null) {
            // we have nothing
            return "";
        }

        final List<Map.Entry<String, Object>> entries = new ArrayList<>(this.properties.entrySet());

        // sort for a stable output
        Collections.sort(entries, ENTRY_COMPARATOR);

        // assemble output

        boolean first = true;
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, Object> entry : entries) {

            if (entry.getValue() == null) {
                continue;
            }

            if (!first) {
                sb.append("~~");
            } else {
                first = false;
            }

            sb.append(entry.getKey()).append('=').append(forDisplay(entry.getValue()));
        }

        return sb.toString();
    }
}
