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
import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link KapuaAndPredicate} reference implementation.
 *
 * @since 0.1.0
 */
public class AndPredicate implements KapuaAndPredicate {

    private List<KapuaPredicate> predicates;

    /**
     * Constructor
     *
     * @since 0.1.0
     */
    public AndPredicate() {
        setPredicates(new ArrayList<>());
    }

    /**
     * Constructor which accepts a not null array of {@link KapuaPredicate}s.
     *
     * @param predicates the {@link KapuaPredicate}s to add.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    public AndPredicate(@NotNull KapuaPredicate... predicates) {
        Objects.requireNonNull(predicates);

        setPredicates(Lists.newArrayList(predicates));
    }

    /**
     * Adds a new {@link KapuaPredicate} to this {@link KapuaAndPredicate}.
     *
     * @param predicate The {@link KapuaPredicate} to add
     * @return {@code this} {@link AndPredicate}.
     * @throws NullPointerException if the given parameter is {@code null}.
     */
    @Override
    public AndPredicate and(@NotNull KapuaPredicate predicate) {
        Objects.requireNonNull(predicates);

        this.predicates.add(predicate);
        return this;
    }

    @Override
    public List<KapuaPredicate> getPredicates() {
        return this.predicates;
    }

    private void setPredicates(List<KapuaPredicate> predicates) {
        this.predicates = predicates;
    }
}
