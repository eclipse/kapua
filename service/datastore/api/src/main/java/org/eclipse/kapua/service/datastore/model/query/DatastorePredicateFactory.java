/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query;

import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;

/**
 * @since 1.3.0
 */
public interface DatastorePredicateFactory extends StorablePredicateFactory {

    /**
     * Returns a new instance of {@link ChannelMatchPredicate}.
     *
     * @param expression The expression to match with the channel name.
     * @return The newly instantiated {@link ChannelMatchPredicate}.
     * @since 1.0.0
     */
    ChannelMatchPredicate newChannelMatchPredicate(String expression);

    /**
     * Return a new instance of {@link MetricPredicate}
     *
     * @param fieldName The metric name to filter.
     * @param type      The type of the metric to filter
     * @param minValue  The lower limit. Can be {@code null}.
     * @param maxValue  The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link MetricPredicate}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> MetricPredicate newMetricPredicate(String fieldName, Class<V> type, V minValue, V maxValue);


    <V extends Comparable<V>> MetricExistsPredicate newMetricExistsPredicate(String fieldName, Class<V> type);
}
