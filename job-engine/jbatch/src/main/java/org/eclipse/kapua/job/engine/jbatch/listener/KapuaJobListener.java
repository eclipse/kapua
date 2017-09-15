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
package org.eclipse.kapua.job.engine.jbatch.listener;

import java.util.Date;
import java.util.List;

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaJobListener extends AbstractJobListener implements JobListener {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaJobListener.class);

    private static final String JBATCH_EXECUTION_ID = "JBATCH_EXECUTION_ID";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobContextFactory JOB_CONTEXT_FACTORY = LOCATOR.getFactory(JobContextFactory.class);

    private static final JobExecutionService JOB_EXECUTION_SERVICE = LOCATOR.getService(JobExecutionService.class);
    private static final JobExecutionFactory JOB_EXECUTION_FACTORY = LOCATOR.getFactory(JobExecutionFactory.class);

    @Inject
    private JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);

        LOG.info("JOB {} - Running before job...", kapuaJobContext.getJobId());

        // prevent another instance running for the same job name (once a job is submitted its status is changed to STARTING by jbatch so,
        // at that point, if there are more than 1 job execution in running state (so STARTING, STARTED, STOPPING) it means that another instance is already running.
        List<Long> runningExecutionsIds = BatchRuntime.getJobOperator().getRunningExecutions(kapuaJobContext.getJobName());
        if (runningExecutionsIds != null && runningExecutionsIds.size() > 1) {
            throw new KapuaIllegalStateException(String.format("Cannot start job [%s]. Another instance of this job is running.", kapuaJobContext.getJobName()));
        }

        JobExecutionCreator jobExecutionCreator = JOB_EXECUTION_FACTORY.newCreator(kapuaJobContext.getScopeId());

        jobExecutionCreator.setJobId(kapuaJobContext.getJobId());
        jobExecutionCreator.setStartedOn(new Date());
        jobExecutionCreator.getEntityAttributes().put(JBATCH_EXECUTION_ID, Long.toString(kapuaJobContext.getExecutionId()));

        JobExecution jobExecution = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.create(jobExecutionCreator));

        kapuaJobContext.setKapuaExecutionId(jobExecution.getId());

        LOG.info("JOB {} - Running before job... DONE!", kapuaJobContext.getJobId());
    }

    @Override
    public void afterJob() throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);

        LOG.info("JOB {} - Running after job...", kapuaJobContext.getJobId());

        KapuaId kapuaExecutionId = kapuaJobContext.getKapuaExecutionId();
        if (kapuaExecutionId == null) {
            throw new KapuaIllegalStateException(String.format("Cannot update job execution (internal reference [%d]). Cannot find execution id", kapuaJobContext.getExecutionId()));
        }
        JobExecution jobExecution = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.find(kapuaJobContext.getScopeId(), kapuaExecutionId));

        jobExecution.setEndedOn(new Date());

        KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.update(jobExecution));

        LOG.info("JOB {} - Running after job... DONE!", kapuaJobContext.getJobId());
    }
}
