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
package org.eclipse.kapua.commons.storage.memory.adapters;

import org.eclipse.kapua.commons.storage.memory.KapuaQueryConverter;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class KapuaQueryConverterImpl implements KapuaQueryConverter {
    private final Set<QueryPredicateResolver> queryPredicateResolvers;

    @Inject
    public KapuaQueryConverterImpl(Set<QueryPredicateResolver> queryPredicateResolvers) {
        this.queryPredicateResolvers = queryPredicateResolvers;
    }

    @Override
    public <E> Predicate<E> convert(QueryPredicate queryPredicate, Map<String, Function<E, Object>> pluckers) {
        return queryPredicateResolvers
                .stream()
                .map(qpr -> qpr.mapToPredicate(queryPredicate, pluckers, this::convert))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot handle this"));
    }
}
