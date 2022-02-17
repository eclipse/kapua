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
package org.eclipse.kapua.service.storable.model.query.predicate;

import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * Match {@link StorablePredicate}.
 * <p>
 * It matches an expression against a {@link StorableField}.
 *
 * @since 1.3.0
 */
public interface MatchPredicate extends StorablePredicate {

    /**
     * Gets the {@link StorableField} to match against.
     *
     * @return The {@link StorableField} to match against
     * @since 1.3.0
     */
    StorableField getField();

    /**
     * Sets the {@link StorableField} to match against.
     *
     * @param storableField The {@link StorableField} to match against.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    MatchPredicate setField(StorableField storableField);

    /**
     * Gets the expression to apply to the {@link StorableField}
     *
     * @return The expression to apply to the {@link StorableField}
     * @since 1.3.0
     */
    String getExpression();

    /**
     * Sets the expression to apply to the {@link StorableField}
     *
     * @param expression The expression to apply to the {@link StorableField}.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    MatchPredicate setExpression(String expression);
}
