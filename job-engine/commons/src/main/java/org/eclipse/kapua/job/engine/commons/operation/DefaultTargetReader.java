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

import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.context.JobContextWrapper;
import org.eclipse.kapua.job.engine.commons.context.StepContextWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.job.operation.TargetReader;
import org.eclipse.kapua.service.job.step.JobStepIndex;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetPredicates;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.io.Serializable;

public class DefaultTargetReader extends AbstractItemReader implements TargetReader {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTargetReader.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

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
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);
        StepContextWrapper stepContextWrapper = new StepContextWrapper(stepContext);
        LOG.info("JOB {} - Opening cursor...", jobContextWrapper.getJobId());

        //
        // Job Id and JobTarget status filtering
        AndPredicateImpl andPredicate = new AndPredicateImpl(
                new AttributePredicateImpl<>(JobTargetPredicates.JOB_ID, jobContextWrapper.getJobId())
        );

        //
        // Step index filtering
        stepIndexFiltering(jobContextWrapper, stepContextWrapper, andPredicate);

        //
        // Filter selected target
        targetSublistFiltering(jobContextWrapper, andPredicate);

        //
        // Query the targets
        JobTargetQuery query = jobTargetFactory.newQuery(jobContextWrapper.getScopeId());
        query.setPredicate(andPredicate);

        jobTargets = KapuaSecurityUtils.doPrivileged(() -> jobTargetService.query(query));

        LOG.info("JOB {} - Opening cursor... Done!", jobContextWrapper.getJobId());
    }

    @Override
    public Object readItem() throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);
        LOG.info("JOB {} - Reading item...", jobContextWrapper.getJobId());

        JobTarget currentJobTarget = null;
        if (jobTargetIndex < jobTargets.getSize()) {
            currentJobTarget = jobTargets.getItem(jobTargetIndex++);
        }

        LOG.info("JOB {} - Reading item... Done!", jobContextWrapper.getJobId());
        return currentJobTarget;
    }

    /**
     * This method apply {@link AttributePredicate}s according to the parameters contained into the {@link JobContextWrapper} and {@link StepContextWrapper}.
     * <p>
     * It manages the options of resetting the status of the target to a certain {@link JobStepIndex}.
     * <p>
     * When no {@link JobStepIndex} is specified, the methods selects all targets that are set to the current {@link StepContextWrapper#getStepIndex()} and that don't have the
     * {@link JobTargetStatus} set to {@link JobTargetStatus#PROCESS_OK}.
     * <p>
     * When a {@link JobStepIndex} is specified, the methods ignores all targets until the {@link StepContextWrapper#getStepIndex()} doesn't match the {@link JobContextWrapper#getFromStepIndex()}.
     * When they match all the {@link JobTarget}s are seleted regardless of their {@link JobTargetStatus}. After passing the given {@link JobContextWrapper#getFromStepIndex()} {@link JobTarget}s
     * will be selected as regularly.
     * <p>
     * Regardless of the status of the {@link org.eclipse.kapua.service.job.Job} of the {@link StepContextWrapper#getStepIndex()} and the {@link JobContextWrapper#getFromStepIndex()} values,
     * {@link #targetSublistFiltering(JobContextWrapper, AndPredicateImpl)} can apply filter that will reduce the {@link JobTarget}s selected.
     *
     * @param jobContextWrapper  The {@link JobContextWrapper} from which extract data
     * @param stepContextWrapper The {@link StepContextWrapper} from which extract data
     * @param andPredicate       The {@link org.eclipse.kapua.model.query.predicate.AndPredicate} where to apply {@link org.eclipse.kapua.model.KapuaPredicates}
     * @since 1.0.0
     */
    protected void stepIndexFiltering(JobContextWrapper jobContextWrapper, StepContextWrapper stepContextWrapper, AndPredicateImpl andPredicate) {
        Integer fromStepIndex = jobContextWrapper.getFromStepIndex();
        if (fromStepIndex == null || fromStepIndex < stepContextWrapper.getStepIndex()) {
            andPredicate.and(new AttributePredicateImpl<>(JobTargetPredicates.STEP_INDEX, stepContextWrapper.getStepIndex()));
            andPredicate.and(new AttributePredicateImpl<>(JobTargetPredicates.STATUS, JobTargetStatus.PROCESS_OK, AttributePredicate.Operator.NOT_EQUAL));
        } else if (fromStepIndex > stepContextWrapper.getStepIndex()) {
            andPredicate.and(new AttributePredicateImpl<>(JobTargetPredicates.STEP_INDEX, JobStepIndex.NONE));
        }
    }

    /**
     * This method apply {@link AttributePredicate}s according to the parameters contained into the {@link JobContextWrapper#getTargetSublist()}.
     * <p>
     * If the {@link JobContextWrapper#getTargetSublist()} has one or more {@link org.eclipse.kapua.model.id.KapuaId}s they will be added to the
     * {@link org.eclipse.kapua.model.query.predicate.AndPredicate} to select only given {@link JobTarget}.
     *
     * @param jobContextWrapper The {@link JobContextWrapper} from which extract data
     * @param andPredicate      The {@link org.eclipse.kapua.model.query.predicate.AndPredicate} where to apply {@link org.eclipse.kapua.model.KapuaPredicates}
     * @since 1.0.0
     */
    protected void targetSublistFiltering(JobContextWrapper jobContextWrapper, AndPredicateImpl andPredicate) {
        if (!jobContextWrapper.getTargetSublist().isEmpty()) {
            andPredicate.and(new AttributePredicateImpl<>(JobTargetPredicates.ENTITY_ID, jobContextWrapper.getTargetSublist().toArray()));
        }
    }

}
