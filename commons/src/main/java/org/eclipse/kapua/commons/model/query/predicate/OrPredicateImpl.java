/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.query.predicate;

import com.google.common.collect.Lists;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link OrPredicate} implementation.
 *
 * @since 1.0.0
 */
public class OrPredicateImpl implements OrPredicate {

    private List<QueryPredicate> predicates;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public OrPredicateImpl() {
        setPredicates(new ArrayList<>());
    }

    /**
     * Constructor which accepts a not {@code null} array of {@link QueryPredicate}s.
     *
     * @param predicates the {@link QueryPredicate}s to add.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    public OrPredicateImpl(@NotNull QueryPredicate... predicates) {
        Objects.requireNonNull(predicates);

        setPredicates(Lists.newArrayList(predicates));
    }

    @Override
    public OrPredicateImpl or(@NotNull QueryPredicate predicate) {
        Objects.requireNonNull(predicates);

        getPredicates().add(predicate);

        return this;
    }

    @Override
    public List<QueryPredicate> getPredicates() {
        return this.predicates;
    }

    @Override
    public void setPredicates(List<QueryPredicate> predicates) {
        this.predicates = predicates;
    }
}
