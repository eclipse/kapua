/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaNamedEntityRepository;

import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Supplier;

public class KapuaNamedEntityRepositoryJpaImpl<E extends KapuaNamedEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryJpaImpl<E, C, L>
        implements KapuaNamedEntityRepository<E, L> {
    public KapuaNamedEntityRepositoryJpaImpl(Class<C> concreteClass,
                                             Supplier<L> listSupplier,
                                             EntityManagerSession entityManagerSession) {
        super(concreteClass, listSupplier, entityManagerSession);
    }

    @Override
    public E findByName(String name) throws KapuaException {
        return doFindByName(KapuaId.ANY, name);
    }

    @Override
    public E findByName(KapuaId scopeId, String name) throws KapuaException {
        return doFindByName(scopeId, name);
    }

    private C doFindByName(KapuaId scopeId, String name) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> {
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<C> criteriaSelectQuery = cb.createQuery(concreteClass);

            //
            // FROM
            final Root<C> entityRoot = criteriaSelectQuery.from(concreteClass);

            //
            // SELECT
            criteriaSelectQuery.select(entityRoot);

            // name
            final ParameterExpression<String> pName = cb.parameter(String.class, KapuaNamedEntityAttributes.NAME);
            final Predicate namePredicate = cb.equal(entityRoot.get(KapuaNamedEntityAttributes.NAME), pName);

            ParameterExpression<KapuaId> pScopeId = null;

            if (!KapuaId.ANY.equals(scopeId)) {
                pScopeId = cb.parameter(KapuaId.class, KapuaEntityAttributes.SCOPE_ID);
                Predicate scopeIdPredicate = cb.equal(entityRoot.get(KapuaEntityAttributes.SCOPE_ID), pScopeId);

                Predicate andPredicate = cb.and(namePredicate, scopeIdPredicate);
                criteriaSelectQuery.where(andPredicate);
            } else {
                criteriaSelectQuery.where(namePredicate);
            }

            TypedQuery<C> query = em.createQuery(criteriaSelectQuery);
            query.setParameter(pName.getName(), name);

            if (pScopeId != null) {
                query.setParameter(pScopeId.getName(), scopeId);
            }

            //
            // QUERY!
            final List<C> result = query.getResultList();
            switch (result.size()) {
                case 0:
                    return null;
                case 1:
                    return result.get(0);
                default:
                    throw new NonUniqueResultException(String.format("Multiple %s results found for field %s with value %s", concreteClass.getName(), pName, name));
            }
        });
    }
}
