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

import java.io.Serializable;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.operation.TargetReader;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetPredicates;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTargetReader extends AbstractItemReader implements TargetReader {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTargetReader.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobContextFactory JOB_CONTEXT_FACTORY = LOCATOR.getFactory(JobContextFactory.class);

    private final JobTargetFactory jobTargetFactory = LOCATOR.getFactory(JobTargetFactory.class);
    private final JobTargetService jobTargetService = LOCATOR.getService(JobTargetService.class);

    @Inject
    private JobContext jobContext;

    @Inject
    private StepContext stepContext;

    protected JobTargetListResult jobTargets;
    protected int jobTargetIndex;

    @Override
    public void open(Serializable arg0) throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);
        KapuaStepContext kapuaStepContext = JOB_CONTEXT_FACTORY.newStepContext(stepContext);
        LOG.info("JOB {} - Opening cursor...", kapuaJobContext.getJobId());

        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.JOB_ID, kapuaJobContext.getJobId()));
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.STEP_INDEX, kapuaStepContext.getStepIndex()));
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.STATUS, JobTargetStatus.PROCESS_OK, Operator.NOT_EQUAL));

        JobTargetQuery query = jobTargetFactory.newQuery(kapuaJobContext.getScopeId());
        query.setPredicate(andPredicate);

        jobTargets = KapuaSecurityUtils.doPrivileged(() -> jobTargetService.query(query));

        LOG.info("JOB {} - Opening cursor... Done!", kapuaJobContext.getJobId());
    }

    @Override
    public Object readItem() throws Exception {
        KapuaJobContext kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);
        LOG.info("JOB {} - Reading item...", kapuaJobContext.getJobId());

        JobTarget currentJobTarget = null;
        if (jobTargetIndex < jobTargets.getSize()) {
            currentJobTarget = jobTargets.getItem(jobTargetIndex++);
        }

        LOG.info("JOB {} - Reading item... Done!", kapuaJobContext.getJobId());
        return currentJobTarget;
    }
}
