/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.generator.basic;

import static java.time.Duration.ofSeconds;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.eclipse.kapua.kura.simulator.generator.Generators.fromSingle;
import static org.eclipse.kapua.kura.simulator.generator.Generators.sine;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.GeneratorFactory;

public class SineGeneratorFactory implements GeneratorFactory {

    @Override
    public Optional<Generator> create(final Map<String, Object> configuration) {
        final String type = getNonEmptyString(configuration, "type").orElse(null);
        if (type == null || !type.equals("sine")) {
            return empty();
        }

        return ofNullable(createSine(configuration));
    }

    private Generator createSine(final Map<String, Object> configuration) {
        final Duration period = getLong(configuration, "period").map(Duration::ofMillis).orElse(ofSeconds(60));

        final Double amplitude = getDouble(configuration, "amplitude").orElse(100.0);
        final Double offset = getDouble(configuration, "offset").orElse(0.0);
        final Short shift = getInteger(configuration, "shift").map(i -> i.shortValue()).orElse(null);

        return Generator.onlyMetrics(fromSingle("value", sine(period, amplitude, offset, shift)));
    }

    private static Optional<Long> getLong(final Map<String, Object> configuration, final String key) {
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

    private static Optional<Integer> getInteger(final Map<String, Object> configuration, final String key) {
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

    private static Optional<Double> getDouble(final Map<String, Object> configuration, final String key) {
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

    private static Optional<String> getNonEmptyString(final Map<String, Object> configuration, final String key) {
        return getString(configuration, key).map(str -> !str.isEmpty() ? str : null);
    }

    private static Optional<String> getString(final Map<String, Object> configuration, final String key) {
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
