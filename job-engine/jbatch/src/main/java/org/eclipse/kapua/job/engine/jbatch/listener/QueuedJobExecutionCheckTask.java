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
package org.eclipse.kapua.job.engine.jbatch.listener;

import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.setting.JobEngineSetting;
import org.eclipse.kapua.job.engine.jbatch.setting.JobEngineSettingKeys;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionAttributes;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionQuery;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionStatus;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class QueuedJobExecutionCheckTask extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(QueuedJobExecutionCheckTask.class);

    private static final JobEngineSetting JOB_ENGINE_SETTING = JobEngineSetting.getInstance();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);

    private static final QueuedJobExecutionService QUEUED_JOB_EXECUTION_SERVICE = LOCATOR.getService(QueuedJobExecutionService.class);
    private static final QueuedJobExecutionFactory QUEUED_JOB_EXECUTION_FACTORY = LOCATOR.getFactory(QueuedJobExecutionFactory.class);


    private KapuaId scopeId;
    private KapuaId jobId;
    private KapuaId jobExecutionId;

    public QueuedJobExecutionCheckTask(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId) {
        this.scopeId = scopeId;
        this.jobId = jobId;
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public void run() {
        LOG.info("Checking Job Execution queue for: {}...", jobExecutionId);

        try {
            QueuedJobExecutionQuery query = QUEUED_JOB_EXECUTION_FACTORY.newQuery(scopeId);

            query.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(QueuedJobExecutionAttributes.JOB_ID, jobId),
                            new AttributePredicateImpl<>(QueuedJobExecutionAttributes.WAIT_FOR_JOB_EXECUTION_ID, jobExecutionId)
                    )
            );

            QueuedJobExecutionListResult queuedJobExecutions = KapuaSecurityUtils.doPrivileged(() -> QUEUED_JOB_EXECUTION_SERVICE.query(query));

            int i = 0;
            int failedToResumeExecution = 0;
            for (QueuedJobExecution qje : queuedJobExecutions.getItems()) {
                Thread.sleep(JOB_ENGINE_SETTING.getInt(JobEngineSettingKeys.JOB_ENGINE_QUEUE_PROCESSING_RUN_DELAY));

                LOG.info("Resuming Job Execution ({}/{}): {}...", ++i, queuedJobExecutions.getSize(), qje.getJobExecutionId());

                try {
                    KapuaSecurityUtils.doPrivileged(() -> JOB_ENGINE_SERVICE.resumeJobExecution(qje.getScopeId(), qje.getJobId(), qje.getJobExecutionId()));

                    qje.setStatus(QueuedJobExecutionStatus.PROCESSED);
                    KapuaSecurityUtils.doPrivileged(() -> QUEUED_JOB_EXECUTION_SERVICE.update(qje));
                } catch (Exception e) {
                    LOG.error("Resuming Job Execution ({}/{}): {}... ERROR!", i, queuedJobExecutions.getSize(), qje.getJobExecutionId(), e);
                    failedToResumeExecution++;

                    qje.setStatus(QueuedJobExecutionStatus.FAILED_TO_RESUME);
                    KapuaSecurityUtils.doPrivileged(() -> QUEUED_JOB_EXECUTION_SERVICE.update(qje));
                    continue;
                }

                LOG.info("Resuming Job Execution ({}/{}): {}... DONE!", i, queuedJobExecutions.getSize(), qje.getJobExecutionId());
            }

            LOG.info("Checking Job Execution queue for: {}... DONE! Queued job failed to resume: {}.", jobExecutionId, failedToResumeExecution);
        } catch (Exception e) {
            LOG.error("Checking Job Execution queue for: {}... ERROR!", jobExecutionId, e);
        }
    }
}
