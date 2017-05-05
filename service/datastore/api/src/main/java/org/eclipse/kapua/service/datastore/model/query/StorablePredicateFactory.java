/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaObjectFactory;

public interface StorablePredicateFactory extends KapuaObjectFactory {

    /**
     * Returns a new instance of {@link AndPredicate}.
     * 
     * @return The newly instantiated {@link AndPredicate}.
     * @since 1.0.0
     */
    public AndPredicate newAndPredicate();

    /**
     * Returns a new instance of {@link ChannelMatchPredicate}.
     * 
     * @param expression
     *            The expression to match with the channel name.
     * 
     * @return The newly instantiated {@link ChannelMatchPredicate}.
     * @since 1.0.0
     */
    public ChannelMatchPredicate newChannelMatchPredicate(String expression);

    /**
     * Return a new instance of {@link RangePredicate}
     * 
     * @param fieldName
     *            The field name to filter.
     * @param minValue
     *            The lower limit. Can be {@code null}.
     * @param maxValue
     *            The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link RangePredicate}.
     * 
     * @since 1.0.0
     */
    public <V extends Comparable<V>> RangePredicate newRangePredicate(String fieldName, V minValue, V maxValue);

    /**
     * Return a new instance of {@link MetricPredicate}
     * 
     * @param fieldName
     *            The metric name to filter.
     * @param minValue
     *            The lower limit. Can be {@code null}.
     * @param maxValue
     *            The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link MetricPredicate}.
     * 
     * @since 1.0.0
     */
    public <V extends Comparable<V>> MetricPredicate newMetricPredicate(String fieldName, Class<V> type, V minValue, V maxValue);

    /**
     * Return a new instance of {@link TermPredicate}
     * 
     * @param field
     *            The field name to filter
     * @param value
     *            The value to filter
     * @return The newly instantiated {@link TermPredicate}.
     * 
     * @since 1.0.0
     */
    public <V> TermPredicate newTermPredicate(StorableField field, V value);

    /**
     * Returns a new instance of {@link ExistsPredicate}.
     * 
     * @param fieldName
     *            The field name to search.
     * @return The newly instantiated {@link ExistsPredicate}.
     */
    public ExistsPredicate newExistsPredicate(String fieldName);
}
