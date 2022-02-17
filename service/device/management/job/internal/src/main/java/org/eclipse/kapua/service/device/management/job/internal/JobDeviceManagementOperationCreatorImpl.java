/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationCreator;

/**
 * {@link JobDeviceManagementOperationCreator} implementation
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobDeviceManagementOperationCreatorImpl extends AbstractKapuaEntityCreator<JobDeviceManagementOperation> implements JobDeviceManagementOperationCreator {

    private KapuaId jobId;
    private KapuaId deviceManagementOperationId;

    public JobDeviceManagementOperationCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

    @Override
    public KapuaId getDeviceManagementOperationId() {
        return deviceManagementOperationId;
    }

    @Override
    public void setDeviceManagementOperationId(KapuaId deviceManagementOperationId) {
        this.deviceManagementOperationId = KapuaEid.parseKapuaId(deviceManagementOperationId);
    }
}
