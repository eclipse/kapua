/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerRepository;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerPropertyImpl;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerPropertyImpl_;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

public class TriggerImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Trigger, TriggerImpl, TriggerListResult>
        implements TriggerRepository {

    public TriggerImplJpaRepository() {
        super(TriggerImpl.class, () -> new TriggerListResultImpl());
    }

    @Override
    public void deleteAllByJobId(TxContext txContext, KapuaId scopeId, KapuaId jobId) {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaDelete<TriggerImpl> triggerQuery = cb.createCriteriaDelete(TriggerImpl.class);
        final Root<TriggerImpl> triggerRoot = triggerQuery.from(TriggerImpl.class);
        final ListJoin<TriggerImpl, TriggerPropertyImpl> join = triggerRoot.join(TriggerImpl_.triggerProperties, JoinType.LEFT);
        triggerQuery.where(
                // Find all the triggers that are associated with this job
                cb.and(
                        cb.equal(triggerRoot.get(KapuaEntityAttributes.SCOPE_ID), KapuaEid.parseKapuaId(scopeId)),
                        cb.and(
                                cb.equal(join.get(TriggerPropertyImpl_.name), "jobId"),
                                cb.equal(join.get(TriggerPropertyImpl_.propertyValue), jobId.toCompactId()),
                                cb.equal(join.get(TriggerPropertyImpl_.propertyType), KapuaId.class.getName())
                        )
                )
        );
        em.createQuery(triggerQuery).executeUpdate();
    }

    @Override
    public Trigger delete(TxContext tx, KapuaId scopeId, KapuaId triggerId) throws KapuaException {
        final Trigger toBeDeleted = this.find(tx, scopeId, triggerId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(Trigger.TYPE, triggerId));
        return this.delete(tx, toBeDeleted);
    }
}
