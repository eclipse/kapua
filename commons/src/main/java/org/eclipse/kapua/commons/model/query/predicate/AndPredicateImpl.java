/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query.predicate;

import com.google.common.collect.Lists;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link AndPredicate} reference implementation.
 *
 * @since 0.1.0
 */
public class AndPredicateImpl implements AndPredicate {

    private List<QueryPredicate> predicates;

    /**
     * Constructor
     *
     * @since 0.1.0
     */
    public AndPredicateImpl() {
        setPredicates(new ArrayList<>());
    }

    /**
     * Constructor which accepts a not null array of {@link QueryPredicate}s.
     *
     * @param predicates the {@link QueryPredicate}s to add.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    public AndPredicateImpl(@NotNull QueryPredicate... predicates) {
        Objects.requireNonNull(predicates);

        setPredicates(Lists.newArrayList(predicates));
    }

    /**
     * Adds a new {@link QueryPredicate} to this {@link AndPredicate}.
     *
     * @param predicate The {@link QueryPredicate} to add
     * @return {@code this} {@link AndPredicateImpl}.
     * @throws NullPointerException if the given parameter is {@code null}.
     */
    @Override
    public AndPredicateImpl and(@NotNull QueryPredicate predicate) {
        Objects.requireNonNull(predicates);

        this.predicates.add(predicate);
        return this;
    }

    @Override
    public List<QueryPredicate> getPredicates() {
        return this.predicates;
    }

    private void setPredicates(List<QueryPredicate> predicates) {
        this.predicates = predicates;
    }
}
