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
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
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

import java.util.Date;

/**
 * {@link JobDeviceManagementTriggerManagerService} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobDeviceManagementTriggerManagerServiceImpl implements JobDeviceManagementTriggerManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDeviceManagementTriggerManagerServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    private static final JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    private static final TriggerDefinitionService TRIGGER_DEFINITION_SERVICE = LOCATOR.getService(TriggerDefinitionService.class);

    private static final TriggerService TRIGGER_SERVICE = LOCATOR.getService(TriggerService.class);
    private static final TriggerFactory TRIGGER_FACTORY = LOCATOR.getFactory(TriggerFactory.class);

    private static final TriggerDefinition DEVICE_CONNECT_TRIGGER;

    /**
     * Looks fot the "Device Connect" {@link TriggerDefinition} to have access to its {@link TriggerDefinition#getId()}
     *
     * @since 1.1.0
     */
    static {
        TriggerDefinition deviceConnectTrigger;
        try {
            deviceConnectTrigger = KapuaSecurityUtils.doPrivileged(() -> TRIGGER_DEFINITION_SERVICE.findByName("Device Connect"));
            if (deviceConnectTrigger == null) {
                throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, "Device Connect");
            }
        } catch (Exception e) {
            LOG.error("Error while searching the Trigger Definition named 'Device Connect'", e);
            throw new ExceptionInInitializerError(e);
        }

        DEVICE_CONNECT_TRIGGER = deviceConnectTrigger;
    }

    @Override
    public void processOnConnect(KapuaId scopeId, KapuaId deviceId) throws ProcessOnConnectException {

        Date now = new Date();

        try {
            JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);

            jobTargetQuery.setPredicate(
                    jobTargetQuery.attributePredicate(JobTargetAttributes.JOB_TARGET_ID, deviceId)
            );

            JobTargetListResult jobTargetListResult = KapuaSecurityUtils.doPrivileged(() -> JOB_TARGET_SERVICE.query(jobTargetQuery));

            for (JobTarget jt : jobTargetListResult.getItems()) {
                JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(jt.getScopeId());

                jobStepQuery.setPredicate(
                        jobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jt.getJobId())
                );

                long jobStepCount = JOB_STEP_SERVICE.count(jobStepQuery);

                if (JobTargetStatus.PROCESS_OK.equals(jt.getStatus()) && jobStepCount <= jt.getStepIndex() + 1) {
                    // The target is at the end of the job step processing
                    continue;
                }

                TriggerQuery triggerQuery = TRIGGER_FACTORY.newQuery(scopeId);

                triggerQuery.setPredicate(
                        triggerQuery.andPredicate(
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_DEFINITION_ID, DEVICE_CONNECT_TRIGGER.getId()),
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName()),
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jt.getJobId().toCompactId()),
                                triggerQuery.attributePredicate(TriggerAttributes.STARTS_ON, now, AttributePredicate.Operator.LESS_THAN),
                                triggerQuery.orPredicate(
                                        triggerQuery.attributePredicate(TriggerAttributes.ENDS_ON, null),
                                        triggerQuery.attributePredicate(TriggerAttributes.ENDS_ON, now, AttributePredicate.Operator.GREATER_THAN)
                                )
                        )
                );

                TriggerListResult jobTriggers = KapuaSecurityUtils.doPrivileged(() -> TRIGGER_SERVICE.query(triggerQuery));

                for (Trigger t : jobTriggers.getItems()) {
                    JobStartOptions jobStartOptions = JOB_ENGINE_FACTORY.newJobStartOptions();

                    jobStartOptions.addTargetIdToSublist(jt.getId());
                    jobStartOptions.setFromStepIndex(jt.getStepIndex());
                    jobStartOptions.setEnqueue(true);

                    KapuaSecurityUtils.doPrivileged(() -> JOB_ENGINE_SERVICE.startJob(jt.getScopeId(), jt.getJobId(), jobStartOptions));
                }
            }

        } catch (Exception e) {
            throw new ProcessOnConnectException(e, scopeId, deviceId);
        }
    }
}
