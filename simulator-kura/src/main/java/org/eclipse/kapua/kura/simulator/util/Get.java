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
package org.eclipse.kapua.kura.simulator.util;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Map;
import java.util.Optional;

public final class Get {

    private Get() {
    }

    public static Optional<Long> getLong(final Map<String, Object> configuration, final String key) {
        final Object value = configuration.get(key);
        if (value == null) {
            return empty();
        }
        if (value instanceof Long) {
            return of((Long) value);
        }
        if (value instanceof Number) {
            return of(((Number) value).longValue());
        }
        return of(Long.parseLong(value.toString()));
    }

    public static Optional<Integer> getInteger(final Map<String, Object> configuration, final String key) {
        final Object value = configuration.get(key);
        if (value == null) {
            return empty();
        }
        if (value instanceof Integer) {
            return of((Integer) value);
        }
        if (value instanceof Number) {
            return of(((Number) value).intValue());
        }
        return of(Integer.parseInt(value.toString()));
    }

    public static Optional<Double> getDouble(final Map<String, Object> configuration, final String key) {
        final Object value = configuration.get(key);
        if (value == null) {
            return empty();
        }
        if (value instanceof Double) {
            return of((Double) value);
        }
        if (value instanceof Number) {
            return of(((Number) value).doubleValue());
        }
        return of(Double.parseDouble(value.toString()));
    }

    public static Optional<String> getNonEmptyString(final Map<String, Object> configuration, final String key) {
        return getString(configuration, key).map(str -> !str.isEmpty() ? str : null);
    }

    public static Optional<String> getString(final Map<String, Object> configuration, final String key) {
        final Object value = configuration.get(key);
        if (value == null) {
            return empty();
        }
        if (value instanceof String) {
            return of((String) value);
        }
        return of(value.toString());
    }

}
