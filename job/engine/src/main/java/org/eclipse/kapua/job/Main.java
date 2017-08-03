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
package org.eclipse.kapua.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.command.job.definition.DeviceCommandExecStepDefinition;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetService;

public class Main {

    private static KapuaLocator locator = KapuaLocator.getInstance();

    private static DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);

    private static JobService jobService = locator.getService(JobService.class);
    private static JobFactory jobFactory = locator.getFactory(JobFactory.class);

    private static JobTargetService jobTargetService = locator.getService(JobTargetService.class);
    private static JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);

    private static JobStepService jobStepService = locator.getService(JobStepService.class);
    private static JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);

    private static JobStepDefinitionService jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
    private static JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    private static DeviceCommandExecStepDefinition commandExecStep = new DeviceCommandExecStepDefinition();

    private Main() {
    }

    public static void main(String[] args) throws KapuaException {

        try {
            KapuaSecurityUtils.doPrivileged(() -> doThings());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void doThings() throws Exception {

        Device device210 = KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.findByClientId(KapuaId.ONE, "Kura_2-1-0"));
        Device device140 = KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.findByClientId(KapuaId.ONE, "Kura_1-4-0"));

        JobStepDefinitionCreator jobStepDefinitionCreator = jobStepDefinitionFactory.newCreator(KapuaId.ONE);
        jobStepDefinitionCreator.setName(commandExecStep.getName());
        jobStepDefinitionCreator.setDescription(commandExecStep.getDescription());
        jobStepDefinitionCreator.setStepType(commandExecStep.getStepType());
        jobStepDefinitionCreator.setReaderName(commandExecStep.getReaderName());
        jobStepDefinitionCreator.setProcessorName(commandExecStep.getProcessorName());
        jobStepDefinitionCreator.setWriterName(commandExecStep.getWriterName());
        jobStepDefinitionCreator.setStepProperties(commandExecStep.getStepProperties());

        JobStepDefinition jobStepDefinition = jobStepDefinitionService.create(jobStepDefinitionCreator);

        JobCreator jobCreator = jobFactory.newCreator(KapuaId.ONE);
        jobCreator.setName("testJob");

        Job job = jobService.create(jobCreator);

        JobTargetCreator jobTargetCreator = jobTargetFactory.newCreator(KapuaId.ONE);
        jobTargetCreator.setJobId(job.getId());
        jobTargetCreator.setJobTargetId(device140.getId());

        jobTargetService.create(jobTargetCreator);

        jobTargetCreator = jobTargetFactory.newCreator(KapuaId.ONE);
        jobTargetCreator.setJobId(job.getId());
        jobTargetCreator.setJobTargetId(device210.getId());

        jobTargetService.create(jobTargetCreator);

        JobStepCreator jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Test step 1");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(jobStepDefinition.getId());
        jobStepCreator.setStepIndex(0);

        jobStepService.create(jobStepCreator);

        jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Test step 2");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(jobStepDefinition.getId());
        jobStepCreator.setStepIndex(1);

        jobStepService.create(jobStepCreator);

        JobRuntime.startJob(job.getScopeId(), job.getId());
        JobRuntime.startJob(job.getScopeId(), job.getId());

    }

}
