/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.commons.operation;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.logger.JobLogger;
import org.eclipse.kapua.job.engine.commons.wrappers.JobContextWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.job.engine.commons.wrappers.StepContextWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.operation.TargetReader;
import org.eclipse.kapua.service.job.step.JobStepIndex;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TargetReader} implementation.
 * <p>
 * All {@link org.eclipse.kapua.service.job.step.definition.JobStepDefinition} can use this {@link TargetReader} implementation or extend or provide one on their own.
 *
 * @since 1.0.0
 */
public class DefaultTargetReader extends AbstractItemReader implements TargetReader {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTargetReader.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private final JobTargetFactory jobTargetFactory = LOCATOR.getFactory(JobTargetFactory.class);
    private final JobTargetService jobTargetService = LOCATOR.getService(JobTargetService.class);

    @Inject
    private JobContext jobContext;

    @Inject
    private StepContext stepContext;

    protected List<JobTargetWrapper> wrappedJobTargets = new ArrayList<>();
    protected int jobTargetIndex;

    @Override
    public void open(Serializable arg0) throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);
        StepContextWrapper stepContextWrapper = new StepContextWrapper(stepContext);

        JobLogger jobLogger = jobContextWrapper.getJobLogger();
        jobLogger.setClassLog(LOG);

        int stepIndex = stepContextWrapper.getStepIndex();
        String stepName = stepContextWrapper.getKapuaStepName();

        jobLogger.info("Reading target chunk. Step:{} (index:{})...", stepName, stepIndex);

        //
        // Job Id and JobTarget status filtering
        JobTargetQuery query = jobTargetFactory.newQuery(jobContextWrapper.getScopeId());

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(JobTargetAttributes.JOB_ID, jobContextWrapper.getJobId())
        );

        //
        // Step index filtering
        stepIndexFiltering(jobContextWrapper, stepContextWrapper, query, andPredicate);

        //
        // Filter selected target
        targetSublistFiltering(jobContextWrapper, query, andPredicate);

        //
        // Query the targets
        query.setPredicate(andPredicate);

        JobTargetListResult jobTargets = KapuaSecurityUtils.doPrivileged(() -> jobTargetService.query(query));

        //
        // Wrap the JobTargets in a wrapper object to store additional informations
        jobTargets.getItems().forEach(jt -> wrappedJobTargets.add(new JobTargetWrapper(jt)));

        jobLogger.info("Reading target chunk. Step:{} (index:{})...DONE", stepName, stepIndex);
    }

    @Override
    public Object readItem() throws Exception {
        JobContextWrapper jobContextWrapper = new JobContextWrapper(jobContext);

        JobLogger jobLogger = jobContextWrapper.getJobLogger();
        jobLogger.setClassLog(LOG);

        return KapuaSecurityUtils.doPrivileged(() -> {
            JobTargetWrapper currentWrappedJobTarget = null;
            if (jobTargetIndex < wrappedJobTargets.size()) {
                currentWrappedJobTarget = wrappedJobTargets.get(jobTargetIndex++);
                JobTarget jobTarget = jobTargetService.find(jobContextWrapper.getScopeId(), currentWrappedJobTarget.getJobTarget().getId());
                jobLogger.info("Read target: {} (id: {})", getTargetDisplayName(jobTarget), jobTarget.getId().toCompactId());
            }
            return currentWrappedJobTarget;
        });
    }

    /**
     * This method apply {@link AttributePredicate}s according to the parameters contained into the {@link JobContextWrapper} and {@link StepContextWrapper}.
     * <p>
     * When no {@link JobStepIndex} is specified, the methods selects all targets that are set to the current {@link StepContextWrapper#getStepIndex()} and that don't have the
     * {@link JobTargetStatus} set to {@link JobTargetStatus#PROCESS_OK}.
     * <p>
     * When a {@link JobStepIndex} is specified, the methods ignores all targets until the {@link StepContextWrapper#getStepIndex()} doesn't match the {@link JobContextWrapper#getFromStepIndex()}.
     * Then the {@link JobTarget}s will be selected as regularly.
     *
     * @param jobContextWrapper  The {@link JobContextWrapper} from which extract data
     * @param stepContextWrapper The {@link StepContextWrapper} from which extract data
     * @param query              The {@link KapuaQuery} to perform
     * @param andPredicate       The {@link org.eclipse.kapua.model.query.predicate.AndPredicate} where to apply {@link org.eclipse.kapua.model.query.predicate.QueryPredicate}
     * @since 1.0.0
     */
    protected void stepIndexFiltering(JobContextWrapper jobContextWrapper, StepContextWrapper stepContextWrapper, KapuaQuery query, AndPredicate andPredicate) {

        // Select all targets that aren't in PROCESS_OK status
        andPredicate.and(query.attributePredicate(JobTargetAttributes.STATUS, JobTargetStatus.PROCESS_OK, AttributePredicate.Operator.NOT_EQUAL));

        // Select all target that are at the current step
        andPredicate.and(query.attributePredicate(JobTargetAttributes.STEP_INDEX, stepContextWrapper.getStepIndex()));

        // Select all targets at or after the given fromStepIndex (if specified)
        if (jobContextWrapper.getFromStepIndex() != null) {
            andPredicate.and(query.attributePredicate(JobTargetAttributes.STEP_INDEX, jobContextWrapper.getFromStepIndex(), AttributePredicate.Operator.GREATER_THAN_OR_EQUAL));
        }
    }

    /**
     * This method apply {@link AttributePredicate}s according to the parameters contained into the {@link JobContextWrapper#getTargetSublist()}.
     * <p>
     * If the {@link JobContextWrapper#getTargetSublist()} has one or more {@link org.eclipse.kapua.model.id.KapuaId}s they will be added to the
     * {@link org.eclipse.kapua.model.query.predicate.AndPredicate} to select only given {@link JobTarget}.
     *
     * @param jobContextWrapper The {@link JobContextWrapper} from which extract data
     * @param query             The {@link KapuaQuery} to perform
     * @param andPredicate      The {@link org.eclipse.kapua.model.query.predicate.AndPredicate} where to apply {@link org.eclipse.kapua.model.query.predicate.QueryPredicate}
     * @since 1.0.0
     */
    protected void targetSublistFiltering(JobContextWrapper jobContextWrapper, KapuaQuery query, AndPredicate andPredicate) {
        if (!jobContextWrapper.getTargetSublist().isEmpty()) {
            andPredicate.and(query.attributePredicate(JobTargetAttributes.ENTITY_ID, jobContextWrapper.getTargetSublist().toArray()));
        }
    }

    protected String getTargetDisplayName(JobTarget jobTarget) throws KapuaException {
        Device device = KapuaSecurityUtils.doPrivileged(() -> DEVICE_REGISTRY_SERVICE.find(jobTarget.getScopeId(), jobTarget.getJobTargetId()));
        if (device == null) {
            return "N/A";
        }
        return device.getClientId();
    }

}
