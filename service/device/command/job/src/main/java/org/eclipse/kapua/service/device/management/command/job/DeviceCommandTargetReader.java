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

import java.io.Serializable;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.operation.TargetReader;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

import com.google.common.collect.Lists;

public class DeviceCommandTargetReader extends AbstractItemReader implements TargetReader {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);
    private final JobTargetService jobTargetService = locator.getService(JobTargetService.class);

    @Inject
    JobContext jobContext;

    protected KapuaJobContext kapuaJobContext;
    protected KapuaStepContext kapuaStepContext;

    protected JobTargetListResult jobTargets;
    protected int jobTargetIndex;

    @Override
    public void open(Serializable arg0) throws Exception {

        kapuaJobContext = (KapuaJobContext) jobContext.getTransientUserData();

        // JobTargetQuery query = jobTargetFactory.newQuery(kapuaJobContext.getScopeId());
        // query.setPredicate(new AttributePredicate<>(JobTargetPredicates.JOB_ID, kapuaJobContext.getJobId()));
        // query.setPredicate(new AttributePredicate<>(JobTargetPredicates.STEP_ID, kapuaStepContext.getStepId()));

        // jobTargets = jobTargetService.query(query);

        JobTarget jobTarget = jobTargetFactory.newEntity(kapuaJobContext.getJobId());
        jobTarget.setStepIndex(0);
        jobTarget.setStatus(JobTargetStatus.PROCESS_AWAITING);
        jobTarget.setJobTargetId(KapuaEid.ONE);

        jobTargets = jobTargetFactory.newListResult();

        jobTargets.addItems(Lists.asList(jobTarget, new JobTarget[] {}));
    }

    @Override
    public Object readItem() throws Exception {

        JobTarget currentJobTarget = null;
        if (jobTargetIndex < jobTargets.getSize()) {
            currentJobTarget = jobTargets.getItem(jobTargetIndex++);
        }

        return currentJobTarget;
    }
}
