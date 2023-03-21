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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerRepository;
import org.eclipse.kapua.storage.TxContext;

public class FiredTriggerImplJpaRepository
        extends KapuaEntityJpaRepository<FiredTrigger, FiredTriggerImpl, FiredTriggerListResult>
        implements FiredTriggerRepository {
    public FiredTriggerImplJpaRepository(KapuaJpaRepositoryConfiguration configuration) {
        super(FiredTriggerImpl.class, () -> new FiredTriggerListResultImpl(), configuration);
    }

    @Override
    public FiredTrigger delete(TxContext tx, KapuaId scopeId, KapuaId firedTriggerId) throws KapuaException {
        final FiredTrigger toBeDeleted = this.find(tx, scopeId, firedTriggerId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(FiredTrigger.TYPE, firedTriggerId));
        return this.delete(tx, toBeDeleted);
    }
}
