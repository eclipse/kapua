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

/**
 * {@link QueuedJobExecution} implementation.
 *
 * @since 1.1.0
 */
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

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public QueuedJobExecutionImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link QueuedJobExecution}.
     * @since 1.1.0
     */
    public QueuedJobExecutionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param queuedJobExecution The {@link QueuedJobExecution} to clone.
     * @since 1.1.0
     */
    public QueuedJobExecutionImpl(QueuedJobExecution queuedJobExecution) {
        super(queuedJobExecution);

        setJobId(queuedJobExecution.getJobId());
        setJobExecutionId(queuedJobExecution.getJobExecutionId());
        setWaitForJobExecutionId(queuedJobExecution.getWaitForJobExecutionId());
        setStatus(queuedJobExecution.getStatus());
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
