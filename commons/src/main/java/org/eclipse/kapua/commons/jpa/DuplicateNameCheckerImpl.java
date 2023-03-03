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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.DuplicateNameChecker;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.function.Function;

public class DuplicateNameCheckerImpl<E extends KapuaNamedEntity> implements DuplicateNameChecker<E> {
    private final KapuaNamedEntityRepository<E, ? extends KapuaListResult<E>> repository;
    private final Function<KapuaId, KapuaQuery> querySupplier;

    public DuplicateNameCheckerImpl(
            KapuaNamedEntityRepository<E, ? extends KapuaListResult<E>> repository,
            Function<KapuaId, KapuaQuery> querySupplier) {
        this.repository = repository;
        this.querySupplier = querySupplier;
    }

    @Override
    public long countOtherEntitiesWithName(TxContext tx, KapuaId scopeId, KapuaId excludedId, String name) throws KapuaException {
        KapuaQuery query = querySupplier.apply(scopeId);
        //TODO: is this necessary?
        query.setScopeId(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name));
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.ENTITY_ID, excludedId, AttributePredicate.Operator.NOT_EQUAL));

//            for (QueryPredicate additionalPredicate : additionalPredicates) {
//                andPredicate.and(additionalPredicate);
//            }

        query.setPredicate(andPredicate);

        return repository.count(tx, query);
    }

    @Override
    public long countOtherEntitiesWithName(TxContext tx, KapuaId scopeId, String name) throws KapuaException {
        KapuaQuery query = querySupplier.apply(scopeId);
        query.setScopeId(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name));
//
//        for (QueryPredicate additionalPredicate : additionalPredicates) {
//            andPredicate.and(additionalPredicate);
//        }

        query.setPredicate(andPredicate);

        return repository.count(tx, query);
//        if (repository.count(query) > 0) {
//            throw new KapuaDuplicateNameException(creator.getName());
//        }
    }
}
