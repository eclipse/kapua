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

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.wrappers.JobContextWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.StepContextWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.operation.TargetWriter;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.util.List;

public class DefaultTargetWriter extends AbstractItemWriter implements TargetWriter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTargetWriter.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public void writeItems(List<Object> items) throws Exception {

        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);
        StepContextWrapper stepContextWrapper = new StepContextWrapper(stepContext);
        LOG.info("JOB {} - Writing items...", jobContextWrapper.getJobId());

        for (Object item : items) {
            JobTargetWrapper processedWrappedJobTarget = (JobTargetWrapper) item;
            JobTarget processedJobTarget = processedWrappedJobTarget.getJobTarget();

            JobTarget jobTarget = KapuaSecurityUtils.doPrivileged(() -> JOB_TARGET_SERVICE.find(processedJobTarget.getScopeId(), processedJobTarget.getId()));

            jobTarget.setStepIndex(stepContextWrapper.getStepIndex());
            jobTarget.setStatus(processedJobTarget.getStatus());
            jobTarget.setStatusMessage(processedWrappedJobTarget.getProcessingException() != null ? processedWrappedJobTarget.getProcessingException().getMessage() : null);

            if (JobTargetStatus.PROCESS_OK.equals(jobTarget.getStatus())) {

                if (stepContextWrapper.getNextStepIndex() != null) {
                    jobTarget.setStepIndex(stepContextWrapper.getNextStepIndex());
                    jobTarget.setStatus(JobTargetStatus.PROCESS_AWAITING);
                } else {
                    jobTarget.setStatus(JobTargetStatus.PROCESS_OK);
                }
            }

            KapuaSecurityUtils.doPrivileged(() -> JOB_TARGET_SERVICE.update(jobTarget));
        }

        LOG.info("JOB {} - Writing items... Done!", jobContextWrapper.getJobId());
    }

}
