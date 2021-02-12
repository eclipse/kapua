/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.quartz.job;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerService;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @since 1.0.0
 */
public class KapuaJobLauncher implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaJobLauncher.class);

    KapuaLocator locator = KapuaLocator.getInstance();

    KapuaIdFactory kapuaIdFactory = locator.getFactory(KapuaIdFactory.class);

    JobService jobService = locator.getService(JobService.class);
    JobEngineService jobEngineService = locator.getService(JobEngineService.class);

    FiredTriggerService firedTriggerService = locator.getService(FiredTriggerService.class);
    FiredTriggerFactory firedTriggerFactory = locator.getFactory(FiredTriggerFactory.class);

    private KapuaId scopeId;
    private KapuaId jobId;
    private JobStartOptions jobStartOptions;

    /**
     * @since 1.0.0
     */
    public KapuaJobLauncher() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String triggerIdString = context.getTrigger().getKey().getName();
        KapuaId triggerId = kapuaIdFactory.newKapuaId(triggerIdString);
        try {
            org.eclipse.kapua.service.job.Job job = KapuaSecurityUtils.doPrivileged(() -> jobService.find(getScopeId(), getJobId()));

            if (job == null) {
                throw new KapuaEntityNotFoundException(org.eclipse.kapua.service.job.Job.class.getName(), jobId);
            }

            if (jobStartOptions == null) {
                KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(scopeId, jobId));
            } else {
                KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(scopeId, jobId, jobStartOptions));
            }

        } catch (Exception e) {
            createdUnsuccessfulFiredTrigger(scopeId, triggerId, context.getFireTime(), e.getMessage());
            throw new JobExecutionException("Cannot start job!", e);
        }

        createSuccessfulFiredTrigger(scopeId, triggerId, context.getFireTime());
    }

    /**
     * @since 1.0.0
     */
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * @since 1.0.0
     */
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    /**
     * @since 1.0.0
     */
    public KapuaId getJobId() {
        return jobId;
    }

    /**
     * @since 1.0.0
     */
    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

    /**
     * @since 1.0.0
     */
    public JobStartOptions getJobStartOptions() {
        return jobStartOptions;
    }

    /**
     * @since 1.0.0
     */
    public void setJobStartOptions(JobStartOptions jobStartOptions) {
        this.jobStartOptions = jobStartOptions;
    }

    /**
     * @since 1.5.0
     */
    public void createSuccessfulFiredTrigger(KapuaId scopeId, KapuaId triggerId, Date fireTime) {
        createFiredTrigger(scopeId, triggerId, fireTime, FiredTriggerStatus.FIRED, null);
    }

    /**
     * @since 1.5.0
     */
    public void createdUnsuccessfulFiredTrigger(KapuaId scopeId, KapuaId triggerId, Date fireTime, String errorMessage) {
        createFiredTrigger(scopeId, triggerId, fireTime, FiredTriggerStatus.FAILED, errorMessage);
    }

    /**
     * @since 1.5.0
     */
    public void createFiredTrigger(KapuaId scopeId, KapuaId triggerId, Date fireTime, FiredTriggerStatus status, String errorMessage) {
        try {
            FiredTriggerCreator firedTriggerCreator = firedTriggerFactory.newCreator(scopeId);
            firedTriggerCreator.setTriggerId(triggerId);
            firedTriggerCreator.setFiredOn(fireTime);
            firedTriggerCreator.setStatus(status);
            firedTriggerCreator.setMessage(errorMessage);

            firedTriggerService.create(firedTriggerCreator);
        } catch (Exception e) {
            LOG.error("Failed create {} FiredTrigger for Trigger: {}", status, triggerId);
        }
    }
}
