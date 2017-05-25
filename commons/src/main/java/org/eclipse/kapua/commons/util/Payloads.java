/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.io.BaseEncoding;

public final class Payloads {

    private static final BaseEncoding HEX_ENCODER = BaseEncoding.base16().upperCase();
    private static final Comparator<Entry<String, ?>> ENTRY_COMPARATOR = Comparator.comparing(Map.Entry<String, ?>::getKey);

    private Payloads() {
    }

    private static Object forDisplay(Object value) {
        if (value instanceof byte[]) {
            return HEX_ENCODER.encode((byte[]) value);
        } else if (value instanceof Float || value instanceof Double || value instanceof Integer || value instanceof Long || value instanceof Boolean || value instanceof String) {
            return value;
        } else {
            return "";
        }
    }

    public static String toDisplayString(final Map<String, ?> properties) {
        if (properties == null) {
            // we have nothing
            return "";
        }

        final List<Map.Entry<String, ?>> entries = new ArrayList<>(properties.entrySet());

        // sort for a stable output
        Collections.sort(entries, ENTRY_COMPARATOR);

        // assemble output

        boolean first = true;
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, ?> entry : entries) {

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
