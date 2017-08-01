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
package org.eclipse.kapua.service.device.management.command.job;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.operation.TargetWriter;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

public class DeviceCommandTargetWriter extends AbstractItemWriter implements TargetWriter {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    // private final JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);
    private final JobTargetService jobTargetService = locator.getService(JobTargetService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    protected KapuaJobContext kapuaJobContext;
    protected KapuaStepContext kapuaStepContext;

    @Override
    public void writeItems(List<Object> items) throws Exception {

        kapuaJobContext = (KapuaJobContext) jobContext.getTransientUserData();
        kapuaStepContext = (KapuaStepContext) stepContext.getTransientUserData();

        for (Object item : items) {
            JobTarget processedJobTarget = (JobTarget) item;

            JobTarget jobTarget = jobTargetService.find(processedJobTarget.getScopeId(), processedJobTarget.getId());

            jobTarget.setStatus(processedJobTarget.getStatus());
            jobTarget.setException(processedJobTarget.getException());

            if (JobTargetStatus.PROCESS_OK.equals(jobTarget.getStatus())) {
                jobTarget.setStepIndex(kapuaStepContext.getNextStepId());
                jobTarget.setStatus(JobTargetStatus.PROCESS_AWAITING);
            }

            jobTargetService.update(jobTarget);
        }

    }

}
