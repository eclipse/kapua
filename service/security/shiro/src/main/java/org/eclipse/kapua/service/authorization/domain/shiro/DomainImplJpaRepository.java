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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaTxContext;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class DomainImplJpaRepository
        extends KapuaEntityJpaRepository<Domain, DomainImpl, DomainListResult>
        implements DomainRepository {
    public DomainImplJpaRepository() {
        super(DomainImpl.class, () -> new DomainListResultImpl());
    }

    //for the sole purpose of changing exception type
    @Override
    public Domain delete(TxContext tx, KapuaId scopeId, KapuaId domainId) throws KapuaException {

        final Domain toDelete = this.find(tx, scopeId, domainId);
        if (toDelete == null) {
            throw new KapuaEntityNotFoundException(Domain.TYPE, domainId);
        }

        return this.delete(tx, toDelete);
    }

    @Override
    public Domain findByName(TxContext txContext, KapuaId scopeId, String name) throws KapuaException {
        final EntityManager em = JpaTxContext.extractEntityManager(txContext);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<DomainImpl> criteriaSelectQuery = cb.createQuery(concreteClass);

        //
        // FROM
        final Root<DomainImpl> entityRoot = criteriaSelectQuery.from(concreteClass);

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

        TypedQuery<DomainImpl> query = em.createQuery(criteriaSelectQuery);
        query.setParameter(pName.getName(), name);

        if (pScopeId != null) {
            query.setParameter(pScopeId.getName(), scopeId);
        }

        //
        // QUERY!
        final List<DomainImpl> result = query.getResultList();
        switch (result.size()) {
            case 0:
                return null;
            case 1:
                return result.get(0);
            default:
                throw new NonUniqueResultException(String.format("Multiple %s results found for field %s with value %s", concreteClass.getName(), pName, name));
        }
    }

}
