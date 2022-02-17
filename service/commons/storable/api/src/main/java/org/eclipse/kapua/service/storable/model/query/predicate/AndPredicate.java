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

import java.util.List;

/**
 * {@link StorablePredicate} "AND" aggregation definition.
 *
 * @since 1.0.0
 */
public interface AndPredicate extends StorablePredicate {

    /**
     * Gets the {@link StorablePredicate} {@link List}
     *
     * @return The {@link StorablePredicate} {@link List}
     * @since 1.0.0
     */
    List<StorablePredicate> getPredicates();

    /**
     * Adds a {@link StorablePredicate} to the {@link List}.
     *
     * @param storablePredicate The {@link StorablePredicate} to add.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    AndPredicate addPredicate(StorablePredicate storablePredicate);

    /**
     * Sets the {@link StorablePredicate} {@link List}
     *
     * @param storablePredicates The {@link StorablePredicate} {@link List}
     * @since 1.3.0
     */
    void setPredicates(List<StorablePredicate> storablePredicates);
}
