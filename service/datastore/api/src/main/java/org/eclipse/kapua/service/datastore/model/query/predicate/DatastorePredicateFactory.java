/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query.predicate;

import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;

/**
 * @since 1.3.0
 */
public interface DatastorePredicateFactory extends StorablePredicateFactory {

    /**
     * Instantiates a {@link ChannelMatchPredicate}.
     *
     * @param expression The expression.
     * @return The newly instantiated {@link ChannelMatchPredicate}.
     * @since 1.0.0
     */
    ChannelMatchPredicate newChannelMatchPredicate(String expression);

    /**
     * Instantiates a {@link MetricPredicate}
     *
     * @param metricName The metric name to filter.
     * @param type       The type of the metric to filter
     * @param minValue   The lower limit. Can be {@code null}.
     * @param maxValue   The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link MetricPredicate}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> MetricPredicate newMetricPredicate(String metricName, Class<V> type, V minValue, V maxValue);

    /**
     * Instantiates a {@link MetricExistsPredicate}.
     *
     * @param metricName The metric name to look for.
     * @param type       The {@link Class} type of the Metric.
     * @param <V>        The {@link Comparable} type.
     * @return The newly instantiated {@link MetricExistsPredicate}
     * @since 1.0.0
     */
    <V extends Comparable<V>> MetricExistsPredicate newMetricExistsPredicate(String metricName, Class<V> type);
}
