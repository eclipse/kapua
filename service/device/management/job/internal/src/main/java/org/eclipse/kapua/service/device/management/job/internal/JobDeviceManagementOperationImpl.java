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

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * {@link JobDeviceManagementOperation} implementation.
 *
 * @since 1.1.0
 */
@Entity(name = "JobDeviceManagementOperation")
@Table(name = "jbm_job_device_management_operation")
public class JobDeviceManagementOperationImpl extends AbstractKapuaUpdatableEntity implements JobDeviceManagementOperation {

    private static final long serialVersionUID = -5686107367635300337L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    private KapuaEid jobId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "device_management_operation_id", nullable = false, updatable = false))
    })
    private KapuaEid deviceManagementOperationId;

    public JobDeviceManagementOperationImpl() {
    }

    public JobDeviceManagementOperationImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = KapuaEid.parseKapuaId(jobId);
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
