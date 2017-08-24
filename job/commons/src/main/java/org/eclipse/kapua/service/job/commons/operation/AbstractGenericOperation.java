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

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.job.context.JobContextFactory;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.operation.GenericOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetPredicates;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenericOperation extends AbstractBatchlet implements GenericOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericOperation.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobContextFactory JOB_CONTEXT_FACTORY = LOCATOR.getFactory(JobContextFactory.class);

    private final JobTargetFactory jobTargetFactory = LOCATOR.getFactory(JobTargetFactory.class);
    private final JobTargetService jobTargetService = LOCATOR.getService(JobTargetService.class);

    private KapuaJobContext kapuaJobContext;
    private KapuaStepContext kapuaStepContext;

    @Override
    public final String process() throws Exception {
        retrieveContext();

        Exception exception = null;
        JobTargetListResult jobTargets;
        try {
            jobTargets = retrieveJobTargets();
        } catch (Exception e) {
            exception = e;
            jobTargets = null;
        }

        boolean success = false;
        try {
            processInternal();

            success = true;
        } catch (Exception e) {
            exception = e;
        }

        moveJobTargets(jobTargets, success, exception);

        return null;
    }

    protected JobTargetListResult retrieveJobTargets() throws KapuaException {
        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.JOB_ID, getJobContext().getJobId()));
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.STEP_INDEX, getStepContext().getStepIndex()));
        andPredicate.and(new AttributePredicate<>(JobTargetPredicates.STATUS, JobTargetStatus.PROCESS_OK, Operator.NOT_EQUAL));

        JobTargetQuery query = jobTargetFactory.newQuery(getJobContext().getScopeId());
        query.setPredicate(andPredicate);

        return KapuaSecurityUtils.doPrivileged(() -> jobTargetService.query(query));
    }

    protected void moveJobTargets(JobTargetListResult jobTargets, boolean success, Exception exception) throws KapuaException {
        for (JobTarget jobTarget : jobTargets.getItems()) {

            if (success) {
                if (getStepContext().getNextStepIndex() != null) {
                    jobTarget.setStepIndex(getStepContext().getNextStepIndex());
                    jobTarget.setStatus(JobTargetStatus.PROCESS_AWAITING);
                } else {
                    jobTarget.setStatus(JobTargetStatus.PROCESS_OK);
                }
            } else {
                jobTarget.setStatus(JobTargetStatus.PROCESS_FAILED);
            }
            KapuaSecurityUtils.doPrivileged(() -> jobTargetService.update(jobTarget));
        }
    }

    protected abstract void processInternal() throws Exception;

    protected abstract void retrieveContext();

    protected void setContext(JobContext jobContext, StepContext stepContext) {
        kapuaJobContext = JOB_CONTEXT_FACTORY.newJobContext(jobContext);
        kapuaStepContext = JOB_CONTEXT_FACTORY.newStepContext(stepContext);
    }

    protected KapuaJobContext getJobContext() {
        if (kapuaJobContext == null) {
            retrieveContext();
        }

        return kapuaJobContext;
    }

    protected KapuaStepContext getStepContext() {
        if (kapuaStepContext == null) {
            retrieveContext();
        }

        return kapuaStepContext;
    }
}
