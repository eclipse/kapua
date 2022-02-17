/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.storable.model.query.predicate;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * {@link StorablePredicate}'s {@link KapuaObjectFactory}.
 *
 * @since 1.0.0
 */
public interface StorablePredicateFactory extends KapuaObjectFactory {

    /**
     * Instantiates a {@link AndPredicate}.
     *
     * @return The newly instantiated {@link AndPredicate}.
     * @since 1.0.0
     */
    AndPredicate newAndPredicate();

    /**
     * Instantiates a {@link OrPredicate}.
     *
     * @return The newly instantiated {@link OrPredicate}.
     * @since 1.0.0
     */
    OrPredicate newOrPredicate();

    /**
     * Instantiates an {@link ExistsPredicate}.
     *
     * @param fields The {@link java.util.List} of fields to match existence.
     * @return The newly instantiated {@link ExistsPredicate}
     * @since 1.3.0
     */
    ExistsPredicate newExistsPredicate(String... fields);

    /**
     * Instantiates an {@link IdsPredicate}.
     *
     * @param typeDescriptor The type descriptor
     * @return The newly instantiated {@link IdsPredicate}
     * @since 1.3.0
     */
    IdsPredicate newIdsPredicate(String typeDescriptor);

    /**
     * Instantiates a {@link MatchPredicate}.
     *
     * @param field      The {@link StorableField}.
     * @param expression The expression to match against the {@link StorableField}
     * @return The newly instantiated {@link MatchPredicate}.
     * @since 1.3.0
     */
    MatchPredicate newMatchPredicate(StorableField field, String expression);

    /**
     * Instantiates a {@link RangePredicate}
     *
     * @param field    The {@link StorableField}.
     * @param minValue The lower limit. Can be {@code null}.
     * @param maxValue The upper limit. Can be {@code null}.
     * @return The newly instantiated {@link RangePredicate}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> RangePredicate newRangePredicate(StorableField field, V minValue, V maxValue);

    /**
     * Instantiates a {@link TermPredicate}
     *
     * @param field The {@link StorableField}.
     * @param value The value match.
     * @return The newly instantiated {@link TermPredicate}.
     * @since 1.0.0
     */
    <V> TermPredicate newTermPredicate(StorableField field, V value);

}
