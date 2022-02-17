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
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import java.time.Instant;
import java.util.Collections;
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
        this.values = Collections.unmodifiableMap(cloneValues ? new HashMap<>(values) : values);
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

        return new Payload(Instant.now(), Collections.singletonMap(key, value), false);
    }

    public static Payload of(final Map<String, ?> values) {
        Objects.requireNonNull(values);

        return new Payload(Instant.now(), values, true);
    }

    public static Payload of(final Instant timestamp, final String key, final Object value) {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(key);

        return new Payload(timestamp, Collections.singletonMap(key, value), false);
    }

    public static Payload of(final Instant timestamp, final Map<String, ?> values) {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(values);

        return new Payload(timestamp, values, true);
    }
}
