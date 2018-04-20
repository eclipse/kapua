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
package org.eclipse.kapua.client.gateway;

import static java.time.Instant.now;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Payload data
 */
public class Payload {

    public static class Builder {

        private Instant timestamp;

        private final Map<String, Object> values = new HashMap<>();

        public Builder() {
            timestamp = Instant.now();
        }

        public Instant timestamp() {
            return timestamp;
        }

        public Builder timestamp(final Instant timestamp) {
            Objects.requireNonNull(timestamp);

            this.timestamp = timestamp;
            return this;
        }

        public Map<String, ?> values() {
            return values;
        }

        public Builder values(final Map<String, ?> values) {
            Objects.requireNonNull(values);

            this.values.clear();
            this.values.putAll(values);

            return this;
        }

        public Builder put(final String key, final Object value) {
            Objects.requireNonNull(key);

            values.put(key, value);
            return this;
        }

        public Payload build() {
            return new Payload(timestamp, values, true);
        }
    }

    private final Instant timestamp;
    private final Map<String, ?> values;

    private Payload(final Instant timestamp, final Map<String, ?> values, final boolean cloneValues) {
        this.timestamp = timestamp;
        this.values = unmodifiableMap(cloneValues ? new HashMap<>(values) : values);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, ?> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return String.format("[Payload - timestamp: %s, values: %s]", timestamp, values);
    }

    public static Payload of(final String key, final Object value) {
        Objects.requireNonNull(key);

        return new Payload(now(), singletonMap(key, value), false);
    }

    public static Payload of(final Map<String, ?> values) {
        Objects.requireNonNull(values);

        return new Payload(now(), values, true);
    }

    public static Payload of(final Instant timestamp, final String key, final Object value) {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(key);

        return new Payload(timestamp, singletonMap(key, value), false);
    }

    public static Payload of(final Instant timestamp, final Map<String, ?> values) {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(values);

        return new Payload(timestamp, values, true);
    }
}
