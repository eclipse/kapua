/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * {@link KapuaObjectFactory} for {@link StorablePredicate}s
 *
 * @since 1.0.0
 */
public interface StorablePredicateFactory extends KapuaObjectFactory {

    /**
     * Returns a new instance of {@link AndPredicate}.
     *
     * @return The newly instantiated {@link AndPredicate}.
     * @since 1.0.0
     */
    AndPredicate newAndPredicate();

    /**
     * Returns a new instance of {@link MatchPredicate}.
     *
     * @param expression The expression to match with the channel name.
     * @return The newly instantiated {@link MatchPredicate}.
     * @since 1.0.0
     */
    MatchPredicate newMatchPredicate(String expression);

    /**
     * Return a new instance of {@link RangePredicate}
     *
     * @param fieldName The field name to filter.
     * @param minValue  The lower limit. Can be {@code null}.
     * @param maxValue  The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link RangePredicate}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> RangePredicate newRangePredicate(String fieldName, V minValue, V maxValue);

    /**
     * Return a new instance of {@link TermPredicate}
     *
     * @param field The field name to filter
     * @param value The value to filter
     * @return The newly instantiated {@link TermPredicate}.
     * @since 1.0.0
     */
    <V> TermPredicate newTermPredicate(StorableField field, V value);

    OrPredicate newOrPredicate();

}
