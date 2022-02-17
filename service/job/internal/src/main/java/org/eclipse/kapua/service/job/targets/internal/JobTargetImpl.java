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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * {@link JobTarget} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "JobTarget")
@Table(name = "job_job_target")
public class JobTargetImpl extends AbstractKapuaUpdatableEntity implements JobTarget {

    private static final long serialVersionUID = -5686107367635300337L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    private KapuaEid jobId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_target_id", nullable = false, updatable = false))
    })
    private KapuaEid jobTargetId;

    @Basic
    @Column(name = "step_index", nullable = false, updatable = true)
    private Integer stepIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    private JobTargetStatus status;

    @Basic
    @Column(name = "status_message", nullable = true, updatable = true)
    private String statusMessage;

    @Transient
    private Exception e;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JobTargetImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link JobTarget}
     * @since 1.0.0
     */
    public JobTargetImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param jobTarget The {@link JobTarget} to clone.
     * @since 1.1.0
     */
    public JobTargetImpl(JobTarget jobTarget) {
        super(jobTarget);

        setJobId(jobTarget.getJobId());
        setJobTargetId(jobTarget.getJobTargetId());
        setStepIndex(jobTarget.getStepIndex());
        setStatus(jobTarget.getStatus());
        setException(jobTarget.getException());
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
    public KapuaId getJobTargetId() {
        return jobTargetId;
    }

    @Override
    public void setJobTargetId(KapuaId jobTargetId) {
        this.jobTargetId = KapuaEid.parseKapuaId(jobTargetId);
    }

    @Override
    public int getStepIndex() {
        return stepIndex;
    }

    @Override
    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    @Override
    public JobTargetStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(JobTargetStatus status) {
        this.status = status;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public Exception getException() {
        return e;
    }

    @Override
    public void setException(Exception e) {
        this.e = e;
    }

}
