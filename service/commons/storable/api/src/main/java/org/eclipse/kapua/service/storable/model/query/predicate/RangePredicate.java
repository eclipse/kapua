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

import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * Range {@link StorablePredicate}.
 * <p>
 * Matches a {@link StorableField} against a min a max value.
 *
 * @since 1.0.0
 */
public interface RangePredicate extends StorablePredicate {

    /**
     * Gets the {@link StorableField}.
     *
     * @return The {@link StorableField}.
     * @since 1.0.0
     */
    StorableField getField();

    /**
     * Sets the {@link StorableField}.
     *
     * @param field The {@link StorableField}.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    RangePredicate setField(StorableField field);

    /**
     * Gets the min value to match.
     *
     * @return The min value to match.
     * @since 1.0.0
     */
    Object getMinValue();

    /**
     * Gets the {@link #getMinValue()} as given {@link Class}.
     *
     * @param clazz The {@link Class} to cast the value to.
     * @return The {@link #getMinValue()} as given {@link Class}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> V getMinValue(Class<V> clazz);

    /**
     * Sets the min value to match.
     *
     * @param minValue The min value to match.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    <V extends Comparable<V>> RangePredicate setMinValue(V minValue);

    /**
     * Gets the max value to match.
     *
     * @return The max value to match.
     * @since 1.0.0
     */
    Object getMaxValue();

    /**
     * Gets the {@link #getMaxValue()} as given {@link Class}.
     *
     * @param clazz The {@link Class} to cast the value to.
     * @return The {@link #getMaxValue()} as given {@link Class}.
     * @since 1.0.0
     */
    <V extends Comparable<V>> V getMaxValue(Class<V> clazz);

    /**
     * Sets the max value to match.
     *
     * @param maxValue The max value to match.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    <V extends Comparable<V>> RangePredicate setMaxValue(V maxValue);
}
