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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * A topic
 * <p>
 * <b>Note:</b> This is not a technical MQTT topic, but an internal data topic.
 * For this reason special topics like wildcards are not supported and will cause
 * an {@link Exception}.
 * </p>
 */
public final class Topic {

    private final List<String> segments;

    private Topic(final List<String> segments) {
        this.segments = Collections.unmodifiableList(segments);
    }

    public List<String> getSegments() {
        return segments;
    }

    public Stream<String> stream() {
        return segments.stream();
    }

    @Override
    public String toString() {
        return String.join("/", segments);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (segments == null ? 0 : segments.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Topic other = (Topic) obj;
        if (segments == null) {
            if (other.segments != null) {
                return false;
            }
        } else if (!segments.equals(other.segments)) {
            return false;
        }
        return true;
    }

    public static Topic split(String path) {
        if (path == null) {
            return null;
        }

        path = path.replaceAll("(^/+|/$)+", "");

        if (path.isEmpty()) {
            return null;
        }

        return new Topic(Arrays.asList(path.split("\\/+")));
    }

    public static Topic of(final List<String> segments) {
        if (segments == null || segments.isEmpty()) {
            return null;
        }

        segments.forEach(Topic::ensureNotSpecial);

        return new Topic(new ArrayList<>(segments));
    }

    public static Topic of(final String first, final String... strings) {
        if (first == null) {
            return null;
        }

        if (strings == null || strings.length <= 0) {
            return new Topic(Collections.singletonList(ensureNotSpecial(first)));
        }

        final List<String> segments = new ArrayList<>(1 + strings.length);
        segments.add(ensureNotSpecial(first));
        for (final String segment : strings) {
            segments.add(ensureNotSpecial(segment));
        }
        return new Topic(segments);
    }

    public static String ensureNotSpecial(final String segment) {
        if (segment == null || segment.isEmpty()) {
            throw new IllegalArgumentException("Segment must not be null or empty");
        } else if ("#".equals(segment)) {
            throw new IllegalArgumentException("Wildcard topics are not allowed");
        } else if ("+".equals(segment)) {
            throw new IllegalArgumentException("Wildcard topics are not allowed");
        } else if (segment.contains("/")) {
            throw new IllegalArgumentException("Segments must not contain slashes. Use Topic.split to parse a multi-segment topic string.");
        }
        return segment;
    }

}
