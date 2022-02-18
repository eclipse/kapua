/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utilities for KapuaPayload.
 *
 * @since 1.0.0
 */
public final class Payloads {

    private static final Comparator<Entry<String, Object>> ENTRY_COMPARATOR = Comparator.comparing(Map.Entry<String, Object>::getKey);

    private Payloads() {
    }

    public static String toDisplayString(Map<String, Object> properties) {
        if (properties == null) {
            return "";
        }

        List<Map.Entry<String, Object>> entries = new ArrayList<>(properties.entrySet());

        // Sort for a stable output
        entries.sort(ENTRY_COMPARATOR);

        // Assemble output
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : entries) {

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

    private static Object forDisplay(Object value) {
        if (value instanceof byte[]) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString((byte[]) value);
        } else if (value instanceof Float || value instanceof Double || value instanceof Integer || value instanceof Long || value instanceof Boolean || value instanceof String) {
            return value;
        } else {
            return "";
        }
    }
}
