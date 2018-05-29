/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.jbatch.driver.JbatchDriver;
import org.eclipse.kapua.job.engine.jbatch.exception.CleanJobDataException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobAlreadyRunningException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobCheckRunningException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobInvalidTargetException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobNotRunningException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobRunningException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobStartingException;
import org.eclipse.kapua.job.engine.jbatch.exception.JobStopppingException;
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
        startJob(scopeId, jobId, new JobStartOptionsImpl());
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");
        ArgumentValidator.notNull(jobStartOptions, "jobStartOptions");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Check Job Existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Check job targets
        JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);
        jobTargetQuery.setPredicate(new AttributePredicateImpl<>(JobTargetPredicates.JOB_ID, jobId));
        if (JOB_TARGET_SERVICE.count(jobTargetQuery) <= 0) {
            throw new KapuaJobEngineException(KapuaJobEngineErrorCodes.JOB_TARGET_MISSING);
        }

        //
        // Check job target sublist
        if (!jobStartOptions.getTargetIdSublist().isEmpty()) {
            jobTargetQuery.setPredicate(
                    new AndPredicateImpl(
                            jobTargetQuery.getPredicate(),
                            new AttributePredicateImpl<>(JobTargetPredicates.ENTITY_ID, jobStartOptions.getTargetIdSublist().toArray())
                    )
            );

            if (jobStartOptions.getTargetIdSublist().size() != JOB_TARGET_SERVICE.count(jobTargetQuery)) {
                throw new JobInvalidTargetException(scopeId, jobId, jobStartOptions.getTargetIdSublist());
            }
        }

        //
        // Check job steps
        JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(scopeId);
        jobStepQuery.setPredicate(new AttributePredicateImpl<>(JobStepPredicates.JOB_ID, jobId));
        if (JOB_STEP_SERVICE.count(jobStepQuery) <= 0) {
            throw new KapuaJobEngineException(KapuaJobEngineErrorCodes.JOB_STEP_MISSING);
        }

        //
        // Check job running
        if (JbatchDriver.isRunningJob(scopeId, jobId)) {
            throw new JobAlreadyRunningException(scopeId, jobId);
        }

        //
        // Start the job
        try {
            JbatchDriver.startJob(scopeId, jobId, jobStartOptions);
        } catch (Exception e) {
            throw new JobStartingException(e, scopeId, jobId);
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
            throw new JobCheckRunningException(e, scopeId, jobId);
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
        // Check job running
        if (!JbatchDriver.isRunningJob(scopeId, jobId)) {
            throw new JobNotRunningException(scopeId, jobId);
        }

        //
        // Stop the job
        try {
            JbatchDriver.stopJob(scopeId, jobId);
        } catch (Exception e) {
            throw new JobStopppingException(e, scopeId, jobId);
        }
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.delete, null));

        //
        // Check existence
        Job job = JOB_SERVICE.find(scopeId, jobId);
        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Check job not running
        if (JbatchDriver.isRunningJob(scopeId, jobId)) {
            throw new JobRunningException(scopeId, jobId);
        }
        try {
            JbatchDriver.cleanJobData(scopeId, jobId);
        } catch (Exception ex) {
            throw new CleanJobDataException(ex, scopeId, jobId);
        }
    }
}
