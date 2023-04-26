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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity_;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class KapuaNamedEntityJpaRepository<E extends KapuaNamedEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityJpaRepository<E, C, L>
        implements KapuaNamedEntityRepository<E, L> {
    public KapuaNamedEntityJpaRepository(
            Class<C> concreteClass,
            String entityName,
            Supplier<L> listSupplier,
            KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(concreteClass, entityName, listSupplier, jpaRepoConfig);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, String name) {
        return doFindByField(txContext, KapuaId.ANY, KapuaNamedEntityAttributes.NAME, name);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, KapuaId scopeId, String name) {
        return doFindByField(txContext, scopeId, KapuaNamedEntityAttributes.NAME, name);
    }

    @Override
    public long countOtherEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, KapuaId excludedId, String name) throws KapuaException {
        return doCount(tx, name,
                (cb, r) -> mapScopeIdToCriteria(scopeId, cb, r),
                (cb, r) -> cb.notEqual(r.get(AbstractKapuaNamedEntity_.ID), KapuaEid.parseKapuaId(excludedId))
        );
    }

    @Override
    public long countEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, String name) throws KapuaException {
        return doCount(tx, name,
                (cb, r) -> mapScopeIdToCriteria(scopeId, cb, r)
        );
    }

    @Override
    public long countEntitiesWithName(TxContext tx, String name) throws KapuaException {
        return doCount(tx, name);
    }

    protected Long doCount(TxContext tx, String name, BiFunction<CriteriaBuilder, Root<C>, Predicate>... additionalPredicates) {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaSelectQuery = cb.createQuery(Long.class);
        // FROM
        final Root<C> entityRoot = criteriaSelectQuery.from(concreteClass);
        // SELECT
        criteriaSelectQuery.select(cb.countDistinct(entityRoot));
        //WHERE
        final Predicate resolvedAdditionalPredicates = Arrays.stream(additionalPredicates)
                .map(f -> f.apply(cb, entityRoot))
                .reduce(cb.conjunction(), cb::and);
        criteriaSelectQuery.where(
                cb.and(
                        cb.equal(entityRoot.get(AbstractKapuaNamedEntity_.NAME), name),
                        resolvedAdditionalPredicates
                )
        );
        return em.createQuery(criteriaSelectQuery).getSingleResult();
    }
}
