/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.quartz.job;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.JobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class KapuaJobLauncer implements Job {

    KapuaLocator locator = KapuaLocator.getInstance();
    JobService jobService = locator.getService(JobService.class);
    JobEngineService jobEngineService = locator.getService(JobEngineService.class);

    private KapuaId scopeId;
    private KapuaId jobId;

    public KapuaJobLauncer() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            org.eclipse.kapua.service.job.Job job = KapuaSecurityUtils.doPrivileged(() -> jobService.find(getScopeId(), getJobId()));
            if (job == null) {
                throw new KapuaEntityNotFoundException(org.eclipse.kapua.service.job.Job.class.getName(), jobId);
            }

            KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(scopeId, jobId));

        } catch (Exception e) {
            throw new JobExecutionException("Cannot start job!", e);
        }
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }

    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

}
