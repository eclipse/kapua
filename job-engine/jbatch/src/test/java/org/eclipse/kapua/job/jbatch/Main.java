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
package org.eclipse.kapua.job.jbatch;

import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.JobEngineJAXBContextProvider;
import org.eclipse.kapua.job.engine.jbatch.step.definition.LogPropertyKeys;
import org.eclipse.kapua.job.engine.jbatch.step.definition.LogStepDefinition;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.internal.DeviceCommandInputImpl;
import org.eclipse.kapua.service.device.management.command.job.definition.DeviceCommandExecPropertyKeys;
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
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionPredicates;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepPropertyImpl;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static KapuaLocator locator = KapuaLocator.getInstance();

    private static DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);

    private static JobService jobService = locator.getService(JobService.class);
    private static JobFactory jobFactory = locator.getFactory(JobFactory.class);

    private static JobEngineService jobEngineService = locator.getService(JobEngineService.class);

    private static JobTargetService jobTargetService = locator.getService(JobTargetService.class);
    private static JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);

    private static JobStepService jobStepService = locator.getService(JobStepService.class);
    private static JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);

    private static JobStepDefinitionService jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
    private static JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    private static TriggerService triggerService = locator.getService(TriggerService.class);
    private static TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);

    private static List<JobStepDefinition> jobStepDefinitions = Lists.newArrayList(new LogStepDefinition()); //new DeviceCommandExecStepDefinition()

    private static Scheduler scheduler;

    private Main() {
    }

    public static void main(String[] args) throws KapuaException, SchedulerException {

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            scheduler.start();

        } catch (Exception e) {
            // TODO: handle exception
            if (scheduler != null) {
                scheduler.shutdown();
            }
        }

        try {

            XmlUtil.setContextProvider(new JobEngineJAXBContextProvider());

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Job job;
        try {
            job = KapuaSecurityUtils.doPrivileged(() -> doThings());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // try {
        // TriggerService triggerService = locator.getService(TriggerService.class);
        // TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);
        //
        //
        // TriggerCreator triggerCreator = triggerFactory.newCreator(KapuaId.ONE);
        // triggerCreator.setName("testTrigger");
        // triggerCreator.setTriggerProperties(triggerProperties);
        // Trigger trigger = KapuaSecurityUtils.doPrivileged(() -> triggerService.create(triggerCreator));
        //
        // } catch (Throwable e) {
        // e.printStackTrace();
        // return;
        // }

        //        KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(job.getScopeId(), job.getId()));

        try {
            List<TriggerProperty> triggerProperties = new ArrayList<>();
            triggerProperties.add(triggerFactory.newTriggerProperty("scopeId", KapuaId.class.getName(), job.getScopeId().toCompactId()));
            triggerProperties.add(triggerFactory.newTriggerProperty("jobId", KapuaId.class.getName(), job.getId().toCompactId()));

            TriggerCreator triggerCreator = triggerFactory.newCreator(KapuaId.ONE);
            triggerCreator.setName("testTrigger");
            triggerCreator.setTriggerProperties(triggerProperties);
            triggerCreator.setStartsOn(new Date());
            triggerCreator.setCronScheduling("*/15 * * * * ? *");
            //            triggerCreator.setRetryInterval(15L);
            Trigger trigger = KapuaSecurityUtils.doPrivileged(() -> triggerService.create(triggerCreator));

        } catch (Throwable e) {
            e.printStackTrace();
            return;
        }

        //        KapuaSecurityUtils.doPrivileged(() -> jobEngineService.startJob(job.getScopeId(), job.getId()));

        if (scheduler != null) {
            // scheduler.shutdown();
        }
    }

    private static Job doThings() throws Exception {

        Device device210 = KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.findByClientId(KapuaId.ONE, "Kura_2-1-0"));
        Device device140 = KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.findByClientId(KapuaId.ONE, "Kura_1-4-0"));

        for (JobStepDefinition jsp : jobStepDefinitions) {
            JobStepDefinitionCreator jobStepDefinitionCreator = jobStepDefinitionFactory.newCreator(KapuaId.ONE);
            jobStepDefinitionCreator.setName(jsp.getName());
            jobStepDefinitionCreator.setDescription(jsp.getDescription());
            jobStepDefinitionCreator.setStepType(jsp.getStepType());
            jobStepDefinitionCreator.setReaderName(jsp.getReaderName());
            jobStepDefinitionCreator.setProcessorName(jsp.getProcessorName());
            jobStepDefinitionCreator.setWriterName(jsp.getWriterName());
            jobStepDefinitionCreator.setStepProperties(jsp.getStepProperties());

            jobStepDefinitionService.create(jobStepDefinitionCreator);
        }

        JobStepDefinitionQuery jspQuery = jobStepDefinitionFactory.newQuery(KapuaId.ONE);
        jspQuery.setPredicate(new AttributePredicate<>(JobStepDefinitionPredicates.NAME, "Device Command Management Execution"));

        JobStepDefinition commandJobStepDefinition = jobStepDefinitionService.query(jspQuery).getFirstItem();

        jspQuery.setPredicate(new AttributePredicate<>(JobStepDefinitionPredicates.NAME, "Log step"));
        JobStepDefinition logJobStepDefinition = jobStepDefinitionService.query(jspQuery).getFirstItem();

        JobCreator jobCreator = jobFactory.newCreator(KapuaId.ONE);
        jobCreator.setName("testJob");

        Job job = jobService.create(jobCreator);

        JobTargetCreator jobTargetCreator = jobTargetFactory.newCreator(KapuaId.ONE);
        jobTargetCreator.setJobId(job.getId());
        jobTargetCreator.setJobTargetId(device210.getId());

        jobTargetService.create(jobTargetCreator);

        jobTargetCreator = jobTargetFactory.newCreator(KapuaId.ONE);
        jobTargetCreator.setJobId(job.getId());
        jobTargetCreator.setJobTargetId(device140.getId());

        jobTargetService.create(jobTargetCreator);

        JobStepCreator jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Command Exec 1");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(commandJobStepDefinition.getId());
        jobStepCreator.setJobStepProperties(createCommandStepProperties1());
        jobStepCreator.setStepIndex(0);

        jobStepService.create(jobStepCreator);

        jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Log step 1");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(logJobStepDefinition.getId());
        jobStepCreator.setStepIndex(1);

        jobStepService.create(jobStepCreator);

        jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Command Exec 2");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(commandJobStepDefinition.getId());
        jobStepCreator.setJobStepProperties(createCommandStepProperties2());
        jobStepCreator.setStepIndex(2);

        jobStepService.create(jobStepCreator);

        jobStepCreator = jobStepFactory.newCreator(KapuaId.ONE);
        jobStepCreator.setName("Log step 2");
        jobStepCreator.setJobId(job.getId());
        jobStepCreator.setJobStepDefinitionId(logJobStepDefinition.getId());
        jobStepCreator.setJobStepProperties(createLogStepProperties());
        jobStepCreator.setStepIndex(3);

        jobStepService.create(jobStepCreator);

        // jobEngineService.startJob(job.getScopeId(), job.getId());

        // jobEngineService.stopJob(job.getScopeId(), job.getId());
        //
        // jobEngineService.resumeJob(job.getScopeId(), job.getId());

        return job;
    }

    private static List<JobStepProperty> createCommandStepProperties1() throws JAXBException {

        DeviceCommandInput commandInput = new DeviceCommandInputImpl();
        commandInput.setCommand("ls");
        commandInput.setTimeout(30000);

        String commandInputString = XmlUtil.marshal(commandInput);

        JobStepProperty stepProperty1 = new JobStepPropertyImpl(DeviceCommandExecPropertyKeys.COMMAND_INPUT, DeviceCommandInput.class.getName(), commandInputString);

        return Lists.newArrayList(stepProperty1);
    }

    private static List<JobStepProperty> createCommandStepProperties2() throws JAXBException {

        DeviceCommandInput commandInput = new DeviceCommandInputImpl();
        commandInput.setCommand("ping");
        commandInput.setArguments(new String[] { "8.8.8.8", "-c 3" });
        commandInput.setTimeout(13000);

        String commandInputString = XmlUtil.marshal(commandInput);

        JobStepProperty stepProperty1 = new JobStepPropertyImpl(DeviceCommandExecPropertyKeys.COMMAND_INPUT, DeviceCommandInput.class.getName(), commandInputString);
        JobStepProperty stepProperty2 = new JobStepPropertyImpl(DeviceCommandExecPropertyKeys.TIMEOUT, Long.class.getName(), "16000");

        return Lists.newArrayList(stepProperty1, stepProperty2);
    }

    private static List<JobStepProperty> createLogStepProperties() throws JAXBException {

        JobStepProperty stepProperty1 = new JobStepPropertyImpl(LogPropertyKeys.LOG_STRING, String.class.getName(), "Finish!");
        return Lists.newArrayList(stepProperty1);
    }

}
