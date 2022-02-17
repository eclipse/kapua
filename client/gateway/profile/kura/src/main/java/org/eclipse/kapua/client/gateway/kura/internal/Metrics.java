/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
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
package org.eclipse.kapua.client.gateway.kura.internal;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.eclipse.kapua.gateway.client.kura.payload.KuraPayloadProto.KuraPayload;
import org.eclipse.kapua.gateway.client.kura.payload.KuraPayloadProto.KuraPayload.KuraMetric;
import org.eclipse.kapua.gateway.client.kura.payload.KuraPayloadProto.KuraPayload.KuraMetric.ValueType;

import com.google.protobuf.ByteString;

public final class Metrics {

    private Metrics() {
    }

    /**
     * Convert plain key value map into a Kura metric structure <br>
     * Only the supported Kura values types must be used (String, boolean, int,
     * long, float, double, byte[])
     *
     * @param builder
     *            the builder to append the metrics to
     * @param metrics
     *            the metrics map
     * @throws IllegalArgumentException
     *             in case of an unsupported value type
     */
    public static void buildMetrics(final KuraPayload.Builder builder, final Map<String, ?> metrics) {
        Objects.requireNonNull(metrics);

        for (final Map.Entry<String, ?> metric : metrics.entrySet()) {
            addMetric(builder, metric.getKey(), metric.getValue());
        }
    }

    public static void buildBody(final KuraPayload.Builder builder, final ByteBuffer body) {
        if (body == null) {
            return;
        }

        Objects.requireNonNull(builder);

        builder.setBody(ByteString.copyFrom(body));
    }

    public static void addMetric(final KuraPayload.Builder builder, final String key, final Object value) {
        final KuraMetric.Builder b = KuraMetric.newBuilder();
        b.setName(key);

        if (value == null) {
            return;
        } else if (value instanceof Boolean) {
            b.setType(ValueType.BOOL);
            b.setBoolValue((boolean) value);
        } else if (value instanceof Integer) {
            b.setType(ValueType.INT32);
            b.setIntValue((int) value);
        } else if (value instanceof String) {
            b.setType(ValueType.STRING);
            b.setStringValue((String) value);
        } else if (value instanceof Long) {
            b.setType(ValueType.INT64);
            b.setLongValue((Long) value);
        } else if (value instanceof Double) {
            b.setType(ValueType.DOUBLE);
            b.setDoubleValue((Double) value);
        } else if (value instanceof Float) {
            b.setType(ValueType.FLOAT);
            b.setFloatValue((Float) value);
        } else if (value instanceof byte[]) {
            b.setType(ValueType.BYTES);
            b.setBytesValue(ByteString.copyFrom((byte[]) value));
        } else {
            throw new IllegalArgumentException(String.format("Illegal metric data type: %s", value.getClass()));
        }

        builder.addMetric(b);
    }

    public static Map<String, Object> extractMetrics(final KuraPayload payload) {
        if (payload == null) {
            return null;
        }
        return extractMetrics(payload.getMetricList());
    }

    public static Map<String, Object> extractMetrics(final List<KuraMetric> metricList) {
        if (metricList == null) {
            return null;
        }

        /*
         * We are using a TreeMap in order to have a stable order of properties
         */
        final Map<String, Object> result = new TreeMap<>();

        for (final KuraMetric metric : metricList) {
            final String name = metric.getName();
            switch (metric.getType()) {
            case BOOL:
                result.put(name, metric.getBoolValue());
                break;
            case BYTES:
                result.put(name, metric.getBytesValue().toByteArray());
                break;
            case DOUBLE:
                result.put(name, metric.getDoubleValue());
                break;
            case FLOAT:
                result.put(name, metric.getFloatValue());
                break;
            case INT32:
                result.put(name, metric.getIntValue());
                break;
            case INT64:
                result.put(name, metric.getLongValue());
                break;
            case STRING:
                result.put(name, metric.getStringValue());
                break;
            }
        }

        return result;
    }

    public static String getAsString(final Map<String, Object> metrics, final String key) {
        return getAsString(metrics, key, null);
    }

    public static String getAsString(final Map<String, Object> metrics, final String key, final String defaultValue) {
        final Object value = metrics.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return defaultValue;
    }
}
