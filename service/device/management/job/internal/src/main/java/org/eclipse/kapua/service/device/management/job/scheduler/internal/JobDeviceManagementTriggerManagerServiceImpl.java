/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.job.scheduler.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.JobDeviceManagementTriggerManagerService;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.exception.ProcessOnConnectException;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

/**
 * {@link JobDeviceManagementTriggerManagerService} implementation.
 *
 * @since 1.1.0
 */
@Singleton
public class JobDeviceManagementTriggerManagerServiceImpl implements JobDeviceManagementTriggerManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDeviceManagementTriggerManagerServiceImpl.class);

    private final JobEngineService jobEngineService;
    private final JobEngineFactory jobEngineFactory;
    private final JobStepService jobStepService;
    private final JobStepFactory jobStepFactory;
    private final JobTargetService jobTargetService;
    private final JobTargetFactory jobTargetFactory;
    private final TriggerDefinitionService triggerDefinitionService;
    private final TriggerService triggerService;
    private final TriggerFactory triggerFactory;

    @Inject
    public JobDeviceManagementTriggerManagerServiceImpl(
            JobEngineService jobEngineService,
            JobEngineFactory jobEngineFactory,
            JobStepService jobStepService,
            JobStepFactory jobStepFactory,
            JobTargetService jobTargetService,
            JobTargetFactory jobTargetFactory,
            TriggerDefinitionService triggerDefinitionService,
            TriggerService triggerService,
            TriggerFactory triggerFactory) {
        this.jobEngineService = jobEngineService;
        this.jobEngineFactory = jobEngineFactory;
        this.jobStepService = jobStepService;
        this.jobStepFactory = jobStepFactory;
        this.jobTargetService = jobTargetService;
        this.jobTargetFactory = jobTargetFactory;
        this.triggerDefinitionService = triggerDefinitionService;
        this.triggerService = triggerService;
        this.triggerFactory = triggerFactory;
    }


    @Override
    public void processOnConnect(KapuaId scopeId, KapuaId deviceId) throws ProcessOnConnectException {

        Date now = new Date();

        try {
            JobTargetQuery jobTargetQuery = jobTargetFactory.newQuery(scopeId);

            jobTargetQuery.setPredicate(
                    jobTargetQuery.attributePredicate(JobTargetAttributes.JOB_TARGET_ID, deviceId)
            );

            JobTargetListResult jobTargetListResult = KapuaSecurityUtils.doPrivileged(() -> jobTargetService.query(jobTargetQuery));

            for (JobTarget jt : jobTargetListResult.getItems()) {
                JobStepQuery jobStepQuery = jobStepFactory.newQuery(jt.getScopeId());

                jobStepQuery.setPredicate(
                        jobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jt.getJobId())
                );

                long jobStepCount = jobStepService.count(jobStepQuery);

                if (JobTargetStatus.PROCESS_OK.equals(jt.getStatus()) && jobStepCount <= jt.getStepIndex() + 1) {
                    // The target is at the end of the job step processing
                    continue;
                }

                TriggerQuery triggerQuery = triggerFactory.newQuery(scopeId);

                triggerQuery.setPredicate(
                        triggerQuery.andPredicate(
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_DEFINITION_ID, getTriggerDefinition().getId()),
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName()),
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jt.getJobId().toCompactId()),
                                triggerQuery.attributePredicate(TriggerAttributes.STARTS_ON, now, AttributePredicate.Operator.LESS_THAN),
                                triggerQuery.orPredicate(
                                        triggerQuery.attributePredicate(TriggerAttributes.ENDS_ON, null),
                                        triggerQuery.attributePredicate(TriggerAttributes.ENDS_ON, now, AttributePredicate.Operator.GREATER_THAN)
                                )
                        )
                );

                TriggerListResult jobTriggers = KapuaSecurityUtils.doPrivileged(() -> triggerService.query(triggerQuery));

                for (Trigger t : jobTriggers.getItems()) {
                    JobStartOptions jobStartOptions = jobEngineFactory.newJobStartOptions();

                    jobStartOptions.addTargetIdToSublist(jt.getId());
                    jobStartOptions.setFromStepIndex(jt.getStepIndex());
                    jobStartOptions.setEnqueue(true);

                    KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(jt.getScopeId(), jt.getJobId(), jobStartOptions));
                }
            }

        } catch (Exception e) {
            throw new ProcessOnConnectException(e, scopeId, deviceId);
        }
    }

    private TriggerDefinition getTriggerDefinition() {
        /**
         * Looks fot the "Device Connect" {@link TriggerDefinition} to have access to its {@link TriggerDefinition#getId()}
         *
         * @since 1.1.0
         */
        TriggerDefinition deviceConnectTrigger;
        try {
            deviceConnectTrigger = KapuaSecurityUtils.doPrivileged(() -> triggerDefinitionService.findByName("Device Connect"));
            if (deviceConnectTrigger == null) {
                throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, "Device Connect");
            }
        } catch (Exception e) {
            LOG.error("Error while searching the Trigger Definition named 'Device Connect'", e);
            throw new ExceptionInInitializerError(e);
        }
        return deviceConnectTrigger;
    }
}
