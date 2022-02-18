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
package org.eclipse.kapua.service.job.execution.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;

import java.util.Date;
import java.util.Set;

/**
 * {@link JobExecutionCreator} implementation
 *
 * @since 1.0.0
 */
public class JobExecutionCreatorImpl extends AbstractKapuaUpdatableEntityCreator<JobExecution> implements JobExecutionCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    private KapuaId jobId;
    private Date startedOn;
    private Set<KapuaId> targetIds;

    protected JobExecutionCreatorImpl(KapuaId scopeId) {
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
    public Date getStartedOn() {
        return startedOn;
    }

    @Override
    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    @Override
    public Set<KapuaId> getTargetIds() {
        return targetIds;
    }

    @Override
    public void setTargetIds(Set<KapuaId> targetIds) {
        this.targetIds = targetIds;
    }
}
