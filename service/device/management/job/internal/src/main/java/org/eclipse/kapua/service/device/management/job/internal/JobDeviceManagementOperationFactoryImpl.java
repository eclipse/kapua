/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationQuery;

/**
 * {@link JobDeviceManagementOperationFactory} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobDeviceManagementOperationFactoryImpl implements JobDeviceManagementOperationFactory {

    @Override
    public JobDeviceManagementOperation newEntity(KapuaId scopeId) {
        return new JobDeviceManagementOperationImpl(scopeId);
    }

    @Override
    public JobDeviceManagementOperationCreator newCreator(KapuaId scopeId) {
        return new JobDeviceManagementOperationCreatorImpl(scopeId);
    }

    @Override
    public JobDeviceManagementOperationQuery newQuery(KapuaId scopeId) {
        return new JobDeviceManagementOperationQueryImpl(scopeId);
    }

    @Override
    public JobDeviceManagementOperationListResult newListResult() {
        return new JobDeviceManagementOperationListResultImpl();
    }

    @Override
    public JobDeviceManagementOperation clone(JobDeviceManagementOperation jobDeviceManagementOperation) {
        try {
            return new JobDeviceManagementOperationImpl(jobDeviceManagementOperation);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, JobDeviceManagementOperation.TYPE, jobDeviceManagementOperation);
        }
    }
}
