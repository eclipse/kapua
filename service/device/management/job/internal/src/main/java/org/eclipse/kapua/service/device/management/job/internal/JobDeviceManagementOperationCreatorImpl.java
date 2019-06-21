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
