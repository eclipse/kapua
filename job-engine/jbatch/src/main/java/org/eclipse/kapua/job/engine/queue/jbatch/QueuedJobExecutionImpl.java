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
package org.eclipse.kapua.job.engine.queue.jbatch;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionStatus;
import org.eclipse.kapua.model.id.KapuaId;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity(name = "QueuedJobExecution")
@Table(name = "job_queued_job_execution")
public class QueuedJobExecutionImpl extends AbstractKapuaUpdatableEntity implements QueuedJobExecution {

    private static final long serialVersionUID = -5686107367635300337L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    private KapuaEid jobId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_execution_id", nullable = false, updatable = false))
    })
    private KapuaEid jobExecutionId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "wait_for_job_execution_id", nullable = false, updatable = false))
    })
    private KapuaEid waitForJobExecutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true)
    private QueuedJobExecutionStatus status;

    public QueuedJobExecutionImpl() {
    }

    public QueuedJobExecutionImpl(KapuaId scopeId) {
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
    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }

    @Override
    public void setJobExecutionId(KapuaId jobExecutionId) {
        this.jobExecutionId = KapuaEid.parseKapuaId(jobExecutionId);
    }

    @Override
    public KapuaId getWaitForJobExecutionId() {
        return waitForJobExecutionId;
    }

    @Override
    public void setWaitForJobExecutionId(KapuaId waitForJobExecutionId) {
        this.waitForJobExecutionId = KapuaEid.parseKapuaId(waitForJobExecutionId);
    }

    @Override
    public QueuedJobExecutionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(QueuedJobExecutionStatus status) {
        this.status = status;
    }
}
