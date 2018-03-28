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
package org.eclipse.kapua.job.engine.jbatch;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.driver.JbatchDriver;
import org.eclipse.kapua.job.engine.jbatch.exception.KapuaJobEngineErrorCodes;
import org.eclipse.kapua.job.engine.jbatch.exception.KapuaJobEngineException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepPredicates;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetPredicates;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

@KapuaProvider
public class JobEngineServiceJbatch implements JobEngineService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final JobService JOB_SERVICE = LOCATOR.getService(JobService.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Check Job Configuration
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);
        jobTargetQuery.setPredicate(new AttributePredicate<>(JobTargetPredicates.JOB_ID, jobId));
        if (JOB_TARGET_SERVICE.count(jobTargetQuery) <= 0) {
            throw new KapuaJobEngineException(KapuaJobEngineErrorCodes.JOB_TARGET_MISSING);
        }

        JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(scopeId);
        jobStepQuery.setPredicate(new AttributePredicate<>(JobStepPredicates.JOB_ID, jobId));
        if (JOB_STEP_SERVICE.count(jobStepQuery) <= 0) {
            throw new KapuaJobEngineException(KapuaJobEngineErrorCodes.JOB_STEP_MISSING);
        }

        //
        // Start the job
        try {
            if (JbatchDriver.isRunningJob(scopeId, jobId)) {
                throw new KapuaIllegalStateException(String.format("Job is running: [%s]", jobId));
            }

            JbatchDriver.startJob(scopeId, jobId);
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Cannot start job");
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Check existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Do check running
        try {
            return JbatchDriver.isRunningJob(scopeId, jobId);
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Cannot get job execution status");
        }
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Check existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Stop the job
        try {
            if (!JbatchDriver.isRunningJob(scopeId, jobId)) {
                throw new KapuaIllegalStateException(String.format("Job not running: [%s]", jobId));
            }

            JbatchDriver.stopJob(scopeId, jobId);
        } catch (Exception e) {
            throw new KapuaIllegalStateException(String.format("Cannot stop job '[%s]': [%s]", jobId, e.getMessage()), e);
        }
    }
}