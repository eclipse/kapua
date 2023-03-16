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
package org.eclipse.kapua.service.device.management.job.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationRepository;
import org.eclipse.kapua.storage.TxContext;

public class JobDeviceManagementOperationImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<JobDeviceManagementOperation, JobDeviceManagementOperationImpl, JobDeviceManagementOperationListResult>
        implements JobDeviceManagementOperationRepository {
    public JobDeviceManagementOperationImplJpaRepository() {
        super(JobDeviceManagementOperationImpl.class, () -> new JobDeviceManagementOperationListResultImpl());
    }


    @Override
    public JobDeviceManagementOperation delete(TxContext tx, KapuaId scopeId, KapuaId jobDeviceManagementOperationId) throws KapuaException {
        // Check existence
        final JobDeviceManagementOperation toBeDeleted = this.find(tx, scopeId, jobDeviceManagementOperationId);
        if (toBeDeleted == null) {
            throw new KapuaEntityNotFoundException(JobDeviceManagementOperation.TYPE, jobDeviceManagementOperationId);
        }
        return this.delete(tx, toBeDeleted);
    }
}
