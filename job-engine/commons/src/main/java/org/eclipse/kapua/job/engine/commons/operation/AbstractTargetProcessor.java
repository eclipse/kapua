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

            jobTarget.setStatus(JobTargetStatus.PROCESS_OK);

            jobLogger.info("Processing item: {} - Done!", jobTarget.getId());
        } catch (Exception e) {
            jobLogger.error(e, "Processing item: {} - Error!", jobTarget.getId());

            jobTarget.setStatus(JobTargetStatus.PROCESS_FAILED);
            wrappedJobTarget.setProcessingException(e);
        }

        return wrappedJobTarget;
    }

    protected abstract void initProcessing(JobTargetWrapper wrappedJobTarget);

    public abstract void processTarget(JobTarget jobTarget) throws KapuaException;

    public void setContext(JobContext jobContext, StepContext stepContext) {
        jobContextWrapper = new JobContextWrapper(jobContext);
        stepContextWrapper = new StepContextWrapper(stepContext);
    }
}
