/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.commons.operation;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.commons.logger.JobLogger;
import org.eclipse.kapua.job.engine.commons.wrappers.JobContextWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.StepContextWrapper;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

/**
 * {@link TargetOperation} {@code abstract} implementation.
 * <p>
 * All {@link org.eclipse.kapua.service.job.step.definition.JobStepDefinition} must provide their own implementation of the {@link TargetOperation}
 * containing the actual processing logic of the {@link JobTarget}
 *
 * @since 1.0.0
 */
public abstract class AbstractTargetProcessor implements TargetOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTargetProcessor.class);

    protected JobContextWrapper jobContextWrapper;
    protected StepContextWrapper stepContextWrapper;

    @Override
    public final Object processItem(Object item) throws Exception {
        JobTargetWrapper wrappedJobTarget = (JobTargetWrapper) item;

        initProcessing(wrappedJobTarget);

        JobLogger jobLogger = jobContextWrapper.getJobLogger();
        jobLogger.setClassLog(LOG);

        JobTarget jobTarget = wrappedJobTarget.getJobTarget();
        jobLogger.info("Processing item: {}", wrappedJobTarget.getJobTarget().getId());
        try {
            processTarget(jobTarget);

            jobTarget.setStatus(getCompletedStatus(jobTarget));

            jobLogger.info("Processing item: {} - DONE!", jobTarget.getId());
        } catch (Exception e) {
            jobLogger.error(e, "Processing item: {} - Error!", jobTarget.getId());

            jobTarget.setStatus(getFailedStatus(jobTarget));
            wrappedJobTarget.setProcessingException(e);
        }

        return wrappedJobTarget;
    }

    /**
     * Actions before {@link #processTarget(JobTarget)} invokation.
     *
     * @param wrappedJobTarget The current {@link JobTargetWrapper}
     * @since 1.1.0
     */
    protected abstract void initProcessing(JobTargetWrapper wrappedJobTarget);

    /**
     * Action of the actual processing of the {@link JobTarget}.
     *
     * @param jobTarget The current {@link JobTarget}
     * @throws KapuaException in case of exceptions during the processing.
     * @since 1.0.0
     */
    protected abstract void processTarget(JobTarget jobTarget) throws KapuaException;

    protected JobTargetStatus getCompletedStatus(JobTarget jobTarget) {
        return JobTargetStatus.PROCESS_OK;
    }

    protected JobTargetStatus getFailedStatus(JobTarget jobTarget) {
        return JobTargetStatus.PROCESS_FAILED;
    }

    /**
     * Sets {@link #jobContextWrapper} and {@link #stepContextWrapper} wrapping the given {@link JobContext} and the {@link StepContext}.
     *
     * @param jobContext  The {@code inject}ed {@link JobContext}.
     * @param stepContext The {@code inject}ed {@link StepContext}.
     * @since 1.0.0
     */
    protected void setContext(JobContext jobContext, StepContext stepContext) {
        jobContextWrapper = new JobContextWrapper(jobContext);
        stepContextWrapper = new StepContextWrapper(stepContext);
    }
}
