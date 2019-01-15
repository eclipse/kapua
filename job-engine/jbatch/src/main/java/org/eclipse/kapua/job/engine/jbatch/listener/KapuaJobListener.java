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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.context.JobContextWrapper;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.job.engine.jbatch.exception.JobAlreadyRunningException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
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
    /**
     * Before starting the actual {@link org.eclipse.kapua.service.job.Job} processing, create the {@link JobExecution} to track progress and
     * check if there are other {@link JobExecution} running with the same {@link JobExecution#getTargetIds()}.
     */
    public void beforeJob() throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);

        LOG.info("JOB {} - {} - Running before job...", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());

        JobExecution jobExecution = createJobExecution(
                jobContextWrapper.getScopeId(),
                jobContextWrapper.getJobId(),
                jobContextWrapper.getTargetSublist(),
                jobContextWrapper.getExecutionId());

        jobContextWrapper.setKapuaExecutionId(jobExecution.getId());

        // prevent another instance running for the same job name (once a job is submitted its status is changed to STARTING by jbatch so,
        // at that point, if there are more than 1 job execution in running state (so STARTING, STARTED, STOPPING) it means that another instance is already running.
        checkJobRunning(
                jobExecution.getScopeId(),
                jobExecution.getJobId(),
                jobContextWrapper.getJobName(),
                jobExecution.getTargetIds());

        LOG.info("JOB {} - {} - Running before job... DONE!", jobContextWrapper.getJobId(), jobContextWrapper.getJobName());
    }

    @Override
    /**
     * Close the {@link JobExecution} setting the {@link JobExecution#getEndedOn()}.
     */
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

    /**
     * Creates the {@link JobExecution} from the data in the {@link JobContextWrapper}.
     * <p>
     * If the {@link org.eclipse.kapua.service.job.Job} has started without a defined set of {@link JobStartOptions#getTargetIdSublist()}
     * all {@link org.eclipse.kapua.service.job.targets.JobTarget}s will be added to the {@link JobExecution#getTargetIds()}.
     *
     * @param scopeId           The {@link Job#getScopeId()}
     * @param jobId             The {@link Job#getId()}
     * @param jobTargetSublist  The {@link JobStartOptions#getTargetIdSublist()} of the targeted items
     * @param jBatchExecutionId The {@link JobContext#getExecutionId()}
     * @return The newly created {@link JobExecution}
     * @throws KapuaException If any error happens during the processing
     */
    private JobExecution createJobExecution(KapuaId scopeId, KapuaId jobId, JobTargetSublist jobTargetSublist, Long jBatchExecutionId) throws KapuaException {
        JobExecutionCreator jobExecutionCreator = JOB_EXECUTION_FACTORY.newCreator(scopeId);

        jobExecutionCreator.setJobId(jobId);
        jobExecutionCreator.setStartedOn(new Date());
        jobExecutionCreator.getEntityAttributes().put(JBATCH_EXECUTION_ID, Long.toString(jBatchExecutionId));

        if (jobTargetSublist.isEmpty()) {
            JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);

            jobTargetQuery.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(JobTargetAttributes.JOB_ID, jobId)
                    )
            );

            JobTargetListResult jobTargets = KapuaSecurityUtils.doPrivileged(() -> JOB_TARGET_SERVICE.query(jobTargetQuery));

            Set<KapuaId> targetIds = new HashSet<>();
            jobTargets.getItems().forEach(jt -> targetIds.add(jt.getId()));

            jobExecutionCreator.setTargetIds(new HashSet<>(targetIds));
        } else {
            jobExecutionCreator.setTargetIds(jobTargetSublist.getTargetIds());
        }

        return KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.create(jobExecutionCreator));
    }

    /**
     * Checks if there are other {@link JobExecution}s running in this moment.
     * <p>
     * First it checks for an execution running from the {@link BatchRuntime}.
     * This will return the jBatch execution ids that are currently active.
     * <p>
     * If 0, no execution is currently running so the new execution can continue.
     * <p>
     * If greater than 0, it checks if the running {@link JobExecution} has a subset of {@link org.eclipse.kapua.service.job.targets.JobTarget}s compatible with the current {@link JobTargetSublist}.
     * If the current {@link JobTargetSublist} doesn't match {@link org.eclipse.kapua.service.job.targets.JobTarget}s of any other running JobExecution, this execution can continue.
     * <p>
     * In any other case, {@link JobAlreadyRunningException} is thrown.
     *
     * @param scopeId           The current {@link JobExecution#getScopeId()}
     * @param jobId             The current {@link JobExecution#getJobId()}
     * @param jobName           The current {@link JobContextWrapper#getJobName()}
     * @param jobTargetIdSubset The current {@link JobExecution#getTargetIds()} }
     * @throws KapuaException If any error happens during the processing.
     */
    private void checkJobRunning(KapuaId scopeId, KapuaId jobId, String jobName, Set<KapuaId> jobTargetIdSubset) throws KapuaException {
        List<Long> runningExecutionsIds = BatchRuntime.getJobOperator().getRunningExecutions(jobName);
        if (runningExecutionsIds.size() > 1) {

            JobExecutionQuery jobExecutionQuery = JOB_EXECUTION_FACTORY.newQuery(scopeId);

            jobExecutionQuery.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(JobExecutionAttributes.JOB_ID, jobId),
                            new AttributePredicateImpl<>(JobExecutionAttributes.ENDED_ON, null),
                            new AttributePredicateImpl<>(JobExecutionAttributes.TARGET_IDS, jobTargetIdSubset.toArray())
                    )
            );

            long runningExecutionWithTargets = KapuaSecurityUtils.doPrivileged(() -> JOB_EXECUTION_SERVICE.count(jobExecutionQuery));

            if (runningExecutionWithTargets > 1) {
                throw new JobAlreadyRunningException(scopeId, jobId, jobTargetIdSubset);
            }
        }
    }
}
