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

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionStatus;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link QueuedJobExecutionCreator} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class QueuedJobExecutionCreatorImpl extends AbstractKapuaUpdatableEntityCreator<QueuedJobExecution> implements QueuedJobExecutionCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    private KapuaId jobId;
    private KapuaId jobExecutionId;
    private KapuaId waitForJobExecutionId;
    private QueuedJobExecutionStatus status;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link QueuedJobExecutionCreator}.
     * @since 1.1.0
     */
    protected QueuedJobExecutionCreatorImpl(KapuaId scopeId) {
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
    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }

    @Override
    public void setJobExecutionId(KapuaId jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public KapuaId getWaitForJobExecutionId() {
        return waitForJobExecutionId;
    }

    @Override
    public void setWaitForJobExecutionId(KapuaId waitForJobExecutionId) {
        this.waitForJobExecutionId = waitForJobExecutionId;
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
