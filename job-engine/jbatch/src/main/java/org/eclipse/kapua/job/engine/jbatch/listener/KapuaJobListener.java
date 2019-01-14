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

import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.context.JobContextWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionAttributes;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KapuaJobListener extends AbstractJobListener implements JobListener {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaJobListener.class);

    private static final String JBATCH_EXECUTION_ID = "JBATCH_EXECUTION_ID";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobExecutionService JOB_EXECUTION_SERVICE = LOCATOR.getService(JobExecutionService.class);
    private static final JobExecutionFactory JOB_EXECUTION_FACTORY = LOCATOR.getFactory(JobExecutionFactory.class);

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);


    @Inject
    private JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);

        LOG.info("JOB {} - {} - Running before job...", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());

        JobExecutionCreator jobExecutionCreator = JOB_EXECUTION_FACTORY.newCreator(jobContextWrapper.getScopeId());

        jobExecutionCreator.setJobId(jobContextWrapper.getJobId());
        jobExecutionCreator.setStartedOn(new Date());
        jobExecutionCreator.getEntityAttributes().put(JBATCH_EXECUTION_ID, Long.toString(jobContextWrapper.getExecutionId()));

        if (jobContextWrapper.getTargetSublist().isEmpty()) {
            JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(jobContextWrapper.getScopeId());

            jobTargetQuery.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(JobTargetAttributes.JOB_ID, jobContextWrapper.getJobId())
                    )
            );

            JobTargetListResult jobTargets = KapuaSecurityUtils.doPrivileged(() -> JOB_TARGET_SERVICE.query(jobTargetQuery));

            Set<KapuaId> targetIds = new HashSet<>();
            jobTargets.getItems().forEach(jt -> targetIds.add(jt.getId()));

            jobExecutionCreator.setTargetIds(new HashSet<>(targetIds));
        } else {
            jobExecutionCreator.setTargetIds(jobContextWrapper.getTargetSublist().getTargetIds());
        }

        JobExecution jobExecution = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.create(jobExecutionCreator));

        jobContextWrapper.setKapuaExecutionId(jobExecution.getId());

        // prevent another instance running for the same job name (once a job is submitted its status is changed to STARTING by jbatch so,
        // at that point, if there are more than 1 job execution in running state (so STARTING, STARTED, STOPPING) it means that another instance is already running.
        List<Long> runningExecutionsIds = BatchRuntime.getJobOperator().getRunningExecutions(jobContextWrapper.getJobName());
        if (runningExecutionsIds.size() > 1) {

            JobExecutionQuery jobExecutionQuery = JOB_EXECUTION_FACTORY.newQuery(jobContextWrapper.getScopeId());

            jobExecutionQuery.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(JobExecutionAttributes.JOB_ID, jobContextWrapper.getJobId()),
                            new AttributePredicateImpl<>(JobExecutionAttributes.ENDED_ON, null),
                            new AttributePredicateImpl<>(JobExecutionAttributes.TARGET_IDS, jobExecutionCreator.getTargetIds().toArray())
                    )
            );

            long runningExecutionWithTargets = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.count(jobExecutionQuery));

            if (runningExecutionWithTargets > 1) {
                throw new KapuaIllegalStateException(String.format("Cannot start job [%s]. Another instance of this job is running.", jobContextWrapper.getJobName()));
            }
        }

        LOG.info("JOB {} - {} - Running before job... DONE!", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());
    }

    @Override
    public void afterJob() throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);

        LOG.info("JOB {} - {} - Running after job...", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());

        KapuaId kapuaExecutionId = jobContextWrapper.getKapuaExecutionId();
        if (kapuaExecutionId == null) {
            // don't send any exception to prevent the job engine to set the job exit status as failed
            String msg = String.format("Cannot update job execution (internal reference [%d]). Cannot find execution id", jobContextWrapper.getExecutionId());
            LOG.error(msg);
            // TODO will send service events when the feature will be implemented
        }
        JobExecution jobExecution = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.find(jobContextWrapper.getScopeId(), kapuaExecutionId));

        jobExecution.setEndedOn(new Date());

        KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.update(jobExecution));

        LOG.info("JOB {} - {} - Running after job... DONE!", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());
    }
}
