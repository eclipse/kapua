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
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerRepository;
import org.eclipse.kapua.storage.TxContext;

public class TriggerImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Trigger, TriggerImpl, TriggerListResult>
        implements TriggerRepository {

    public TriggerImplJpaRepository() {
        super(TriggerImpl.class, () -> new TriggerListResultImpl());
    }

    @Override
    public void deleteAllByJobId(TxContext tx, KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Find all the triggers that are associated with this job
        final TriggerQuery query = new TriggerQueryImpl(scopeId);
        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_NAME, "jobId"),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jobId.toCompactId()),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName())
        );

        query.setPredicate(andPredicate);

        //
        // Query for and delete all the triggers that are associated with this job
        TriggerListResult triggers = this.query(tx, query);
        for (Trigger trig : triggers.getItems()) {
            this.delete(tx, trig);
        }
    }

    @Override
    public Trigger delete(TxContext tx, KapuaId scopeId, KapuaId triggerId) throws KapuaException {
        final Trigger toBeDeleted = this.find(tx, scopeId, triggerId);
        if (toBeDeleted == null) {
            throw new KapuaEntityNotFoundException(Trigger.TYPE, triggerId);
        }
        return this.delete(tx, toBeDeleted);
    }
}
