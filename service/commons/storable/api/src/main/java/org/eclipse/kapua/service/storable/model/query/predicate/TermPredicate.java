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
 * Term {@link StorablePredicate} definition.
 * <p>
 * It matches a value against a {@link StorableField}.
 *
 * @since 1.0.0
 */
public interface TermPredicate extends StorablePredicate {

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
     * @param storableField The {@link StorableField}.
     * @return Itself, to chain method invocation.
     * @since 1.0.0
     */
    TermPredicate setField(StorableField storableField);

    /**
     * Gets the value to match against.
     *
     * @return The value to match against.
     * @since 1.0.0
     */
    Object getValue();

    /**
     * Gets the {@link #getValue()} as given {@link Class}.
     *
     * @param clazz The {@link Class} to cast the value to.
     * @return The {@link #getValue()} as given {@link Class}.
     * @since 1.0.0
     */
    <V> V getValue(Class<V> clazz);

    /**
     * Sets the value to match against.
     *
     * @param value The value to match against.
     * @return Itself, to chain method invocation.
     * @since 1.0.0
     */
    TermPredicate setValue(Object value);
}
