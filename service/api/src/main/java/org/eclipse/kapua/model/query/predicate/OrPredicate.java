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
package org.eclipse.kapua.model.query.predicate;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@link OrPredicate} definition.
 * <p>
 * Used to link multiple {@link QueryPredicate}s in OR clause.
 *
 * @since 1.0.0
 */
public interface OrPredicate extends QueryPredicate {

    /**
     * Adds the given {@link QueryPredicate} to the {@link OrPredicate}.
     *
     * @param predicate The {@link OrPredicate} to concatenate
     * @return {@code this} {@link OrPredicate}.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    OrPredicate or(@NotNull QueryPredicate predicate);

    /**
     * Gets all {@link QueryPredicate} set for this {@link OrPredicate}
     *
     * @return The {@link List} of {@link QueryPredicate}s
     * @since 1.0.0
     */
    List<QueryPredicate> getPredicates();

    /**
     * Sets a {@link List} of {@link QueryPredicate}s in OR clause
     *
     * @param predicates The {@link List} of {@link QueryPredicate}s
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.1.0
     */
    void setPredicates(@NotNull List<QueryPredicate> predicates);

}
