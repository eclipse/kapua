/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.query.predicate.KapuaOrPredicate;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link KapuaOrPredicate} reference implementation.
 *
 * @since 0.1.0
 */
public class OrPredicate implements KapuaOrPredicate {

    private List<KapuaPredicate> predicates;

    /**
     * Constructor
     *
     * @since 0.1.0
     */
    public OrPredicate() {
        setPredicates(new ArrayList<>());
    }

    /**
     * Constructor which accepts a not null array of {@link KapuaPredicate}s.
     *
     * @param predicates the {@link KapuaPredicate}s to add.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    public OrPredicate(@NotNull KapuaPredicate... predicates) {
        Objects.requireNonNull(predicates);

        setPredicates(Lists.newArrayList(predicates));
    }

    /**
     * Adds a new {@link KapuaPredicate} to this {@link KapuaOrPredicate}.
     *
     * @param predicate The {@link KapuaPredicate} to add
     * @return {@code this} {@link OrPredicate}.
     * @throws NullPointerException if the given parameter is {@code null}.
     */
    @Override
    public OrPredicate or(@NotNull KapuaPredicate predicate) {
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
