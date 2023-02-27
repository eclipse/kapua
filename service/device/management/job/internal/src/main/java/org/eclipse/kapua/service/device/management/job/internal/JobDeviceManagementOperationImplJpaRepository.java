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

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;

import java.util.function.Supplier;

public class JobDeviceManagementOperationImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<JobDeviceManagementOperation, JobDeviceManagementOperationImpl, JobDeviceManagementOperationListResult>
        implements JobDeviceManagementOperationRepository {
    public JobDeviceManagementOperationImplJpaRepository(Supplier<JobDeviceManagementOperationListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(JobDeviceManagementOperationImpl.class, listSupplier, entityManagerSession);
    }
}
