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
package org.eclipse.kapua.kura.simulator.payload;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.eclipse.kapua.kura.simulator.generator.Payload;
import org.eclipse.kapua.kura.simulator.generator.Position;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.KuraMetric;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.KuraMetric.ValueType;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.KuraPosition;

import com.google.protobuf.ByteString;

public final class Metrics {

    private Metrics() {
    }

    public static final String KEY_REQUESTER_CLIENT_ID = "requester.client.id";
    public static final String KEY_REQUEST_ID = "request.id";
    public static final String KEY_RESPONSE_CODE = "response.code";
    public static final String KEY_RESPONSE_EXCEPTION_MESSAGE = "response.exception.message";
    public static final String KEY_RESPONSE_EXCEPTION_STACKTRACE = "response.exception.stack";

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

    public static KuraPayload toKuraPayload(final Payload payload) {
        if (payload == null) {
            return null;
        }

        final KuraPayload.Builder result = KuraPayload.newBuilder();
        buildPayload(result, payload);
        return result.build();
    }

    public static void buildPayload(final KuraPayload.Builder builder, final Payload payload) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(payload);

        buildBody(builder, payload.getBody());
        buildPosition(builder, payload.getPosition());
        buildMetrics(builder, payload.getMetrics());
    }

    public static void buildPosition(final KuraPayload.Builder builder, final Position position) {
        if (position == null) {
            return;
        }

        Objects.requireNonNull(builder);

        final KuraPosition.Builder result = KuraPosition.newBuilder();

        if (position.getAltitude() != null) {
            result.setAltitude(position.getAltitude());
        }

        if (position.getHeading() != null) {
            result.setHeading(position.getHeading());
        }

        if (position.getLatitude() != null) {
            result.setLatitude(position.getLatitude());
        }

        if (position.getLongitude() != null) {
            result.setLongitude(position.getLongitude());
        }

        if (position.getPrecision() != null) {
            result.setPrecision(position.getPrecision());
        }

        if (position.getSatellites() != null) {
            result.setSatellites(position.getSatellites());
        }

        if (position.getSpeed() != null) {
            result.setSpeed(position.getSpeed());
        }

        if (position.getTimestamp() != null) {
            result.setTimestamp(position.getTimestamp().toEpochMilli());
        }

        builder.setPosition(result);
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

    public static <T> T readFrom(final T object, final Map<String, Object> metrics) {
        Objects.requireNonNull(object);

        for (final Field field : FieldUtils.getFieldsListWithAnnotation(object.getClass(), Metric.class)) {
            final Metric m = field.getAnnotation(Metric.class);
            final boolean optional = field.isAnnotationPresent(Optional.class);

            final Object value = metrics.get(m.value());
            if (value == null && !optional) {
                throw new IllegalArgumentException(
                        String.format("Field '%s' is missing metric '%s'", field.getName(), m.value()));
            }

            if (value == null) {
                // not set but optional
                continue;
            }

            try {
                FieldUtils.writeField(field, object, value, true);
            } catch (final IllegalArgumentException e) {
                // provide a better message
                throw new IllegalArgumentException(String.format("Failed to assign '%s' (%s) to field '%s'", value,
                        value.getClass().getName(), field.getName()), e);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        for (final Method method : MethodUtils.getMethodsListWithAnnotation(object.getClass(), Metric.class)) {
            final Metric m = method.getAnnotation(Metric.class);
            final boolean optional = method.isAnnotationPresent(Optional.class);

            final Object value = metrics.get(m.value());
            if (value == null && !optional) {
                throw new IllegalArgumentException(
                        String.format("Method '%s' is missing metric '%s'", method.getName(), m.value()));
            }

            if (value == null) {
                // not set but optional
                continue;
            }

            try {
                method.invoke(object, value);
            } catch (final IllegalArgumentException e) {
                // provide a better message
                throw new IllegalArgumentException(String.format("Failed to call '%s' (%s) with method '%s'", value,
                        value.getClass().getName(), method.getName()), e);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return object;
    }
}
