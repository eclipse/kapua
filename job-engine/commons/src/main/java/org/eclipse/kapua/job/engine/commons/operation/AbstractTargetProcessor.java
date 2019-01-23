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

public abstract class AbstractTargetProcessor implements TargetOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTargetProcessor.class);

    protected JobContextWrapper jobContextWrapper;
    protected StepContextWrapper stepContextWrapper;

    @Override
    public final Object processItem(Object item) throws Exception {
        JobTargetWrapper wrappedJobTarget = (JobTargetWrapper) item;

        JobTarget jobTarget = wrappedJobTarget.getJobTarget();
        LOG.info("Processing item: {}", wrappedJobTarget.getJobTarget().getId());
        try {
            processTarget(jobTarget);

            jobTarget.setStatus(JobTargetStatus.PROCESS_OK);

            LOG.info("Processing item: {} - Done!", jobTarget.getId());
        } catch (Exception e) {
            LOG.info("Processing item: {} - Error!", jobTarget.getId(), e);
            jobTarget.setStatus(JobTargetStatus.PROCESS_FAILED);
            wrappedJobTarget.setProcessingException(e);
        }

        return wrappedJobTarget;
    }

    public abstract void processTarget(JobTarget jobTarget) throws KapuaException;

    public void setContext(JobContext jobContext, StepContext stepContext) {
        jobContextWrapper = new JobContextWrapper(jobContext);
        stepContextWrapper = new StepContextWrapper(stepContext);
    }
}
