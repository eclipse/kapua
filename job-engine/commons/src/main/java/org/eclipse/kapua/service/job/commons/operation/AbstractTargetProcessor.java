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
package org.eclipse.kapua.service.job.commons.operation;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTargetProcessor implements TargetOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTargetProcessor.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobContextFactory JOB_CONTEXT_FACTORY = LOCATOR.getFactory(JobContextFactory.class);

    protected KapuaJobContext kapuaJobContext;
    protected KapuaStepContext kapuaStepContext;

    @Override
    public final Object processItem(Object item) throws Exception {
        JobTarget jobTarget = (JobTarget) item;
        LOG.info("Processing item: {}", jobTarget.getId());

        try {
            processTarget(jobTarget);

            jobTarget.setStatus(JobTargetStatus.PROCESS_OK);
        } catch (Exception e) {
            jobTarget.setStatus(JobTargetStatus.PROCESS_FAILED);
            jobTarget.setException(e);
        }

        LOG.info("Processing item:{} - DONE!", jobTarget.getId());
        return jobTarget;
    }

    public abstract void processTarget(JobTarget jobTarget) throws KapuaException;

    public void setContext(JobContext jobContext, StepContext stepContext) {
        kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);
        kapuaStepContext = JOB_CONTEXT_FACTORY.newStepContext(stepContext);
    }
}
