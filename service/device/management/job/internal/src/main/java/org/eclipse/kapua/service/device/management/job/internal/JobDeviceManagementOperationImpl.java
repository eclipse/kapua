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

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public JobDeviceManagementOperationImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link JobDeviceManagementOperation}.
     * @since 1.1.0
     */
    public JobDeviceManagementOperationImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param jobDeviceManagementOperation The {@link JobDeviceManagementOperation} to clone.
     * @since 1.1.0
     */
    public JobDeviceManagementOperationImpl(JobDeviceManagementOperation jobDeviceManagementOperation) {
        super(jobDeviceManagementOperation);

        setJobId(jobDeviceManagementOperation.getJobId());
        setDeviceManagementOperationId(jobDeviceManagementOperation.getDeviceManagementOperationId());
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
