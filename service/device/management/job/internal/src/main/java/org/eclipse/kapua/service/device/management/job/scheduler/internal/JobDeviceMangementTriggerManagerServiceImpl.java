/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job.scheduler.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.JobDeviceMangementTriggerManagerService;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.targets.JobTarget;
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
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionAttributes;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class JobDeviceMangementTriggerManagerServiceImpl implements JobDeviceMangementTriggerManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDeviceMangementTriggerManagerServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    private static final JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    private static final TriggerDefinitionService TRIGGER_DEFINITION_SERVICE = LOCATOR.getService(TriggerDefinitionService.class);
    private static final TriggerDefinitionFactory TRIGGER_DEFINITION_FACTORY = LOCATOR.getFactory(TriggerDefinitionFactory.class);

    private static final TriggerService TRIGGER_SERVICE = LOCATOR.getService(TriggerService.class);
    private static final TriggerFactory TRIGGER_FACTORY = LOCATOR.getFactory(TriggerFactory.class);

    private static final TriggerDefinition DEVICE_CONNECT_TRIGGER;

    static {
        TriggerDefinition deviceConnectTrigger = null;
        try {
            TriggerDefinitionQuery query = TRIGGER_DEFINITION_FACTORY.newQuery(null);

            query.setPredicate(query.attributePredicate(TriggerDefinitionAttributes.NAME, "Device Connect"));

            TriggerDefinitionListResult triggerDefinitions = KapuaSecurityUtils.doPrivileged(() -> TRIGGER_DEFINITION_SERVICE.query(query));

            if (triggerDefinitions.isEmpty()) {
                throw KapuaException.internalError("Error while searching 'Device Connect Trigger'");
            }

            deviceConnectTrigger = triggerDefinitions.getFirstItem();
        } catch (Exception e) {
            LOG.error("Error while initializing class", e);
        }

        DEVICE_CONNECT_TRIGGER = deviceConnectTrigger;
    }

    @Override
    public void processOnConnect(KapuaId scopeId, KapuaId deviceId) {

        try {
            JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);

            jobTargetQuery.setPredicate(
                    jobTargetQuery.attributePredicate("jobTargetId", deviceId)
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
                                triggerQuery.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jt.getJobId().toCompactId())
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
            LOG.error("Error while processing BIRTH message ");
        }
    }
}
