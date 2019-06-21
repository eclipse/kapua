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
package org.eclipse.kapua.job.engine.remote;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.scheduler.quartz.utils.QuartzTriggerUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a partial implementation of the {@link JobEngineService}.
 * <p>
 * It leverages the Quartz Framework to remotely trigger a {@link org.eclipse.kapua.service.job.execution.JobExecution} that is run from the Job Engine (currently) running in the Console container.
 * <p>
 * This is a workaround to let the Broker start a {@link org.eclipse.kapua.service.job.execution.JobExecution} when a COMPLETE device management notification is received by the broker.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobEngineServiceRemote implements JobEngineService {

    private static final Logger LOG = LoggerFactory.getLogger(JobEngineServiceRemote.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final KapuaIdFactory KAPUA_ID_FACTORY = LOCATOR.getFactory(KapuaIdFactory.class);

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        startJob(scopeId, jobId, new JobStartOptionsRemote());
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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.execute, scopeId));

        //
        // Seed the trigger
        try {
            LOG.info("Scheduling remote job start! ScopeId: {} - JobId: {} - JobOptions: {}", scopeId, jobId, jobStartOptions.getTargetIdSublist().size());

            // Build job data map
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("scopeId", scopeId);
            jobDataMap.put("jobId", jobId);
            jobDataMap.put("jobStartOptions", jobStartOptions);

            // Create the trigger
            QuartzTriggerUtils.createQuartzTrigger(scopeId, jobId, KAPUA_ID_FACTORY.newKapuaId(IdGenerator.generate()), jobDataMap);
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Error!");
        }
    }

    @Override
    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resumeJobExecution(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
