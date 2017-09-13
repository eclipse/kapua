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
package org.eclipse.kapua.app.console.module.job.shared.util;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtExecution;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepDefinition;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerProperty;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;

import java.util.ArrayList;
import java.util.List;

public class KapuaGwtJobModelConverter {

    private KapuaGwtJobModelConverter() { }

    public static GwtJob convertJob(Job job) {
        GwtJob gwtJob = new GwtJob();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(job, gwtJob);
        gwtJob.setJobName(job.getName());
        gwtJob.setDescription(job.getDescription());
        gwtJob.setJobXmlDefinition(job.getJobXmlDefinition());
        gwtJob.setJobSteps(convertJobSteps(job.getJobSteps()));
        return gwtJob;
    }

    private static List<GwtJobStep> convertJobSteps(List<JobStep> jobStepList) {
        List<GwtJobStep> gwtJobStepList = new ArrayList<GwtJobStep>();
        for (JobStep jobStep : jobStepList) {
            gwtJobStepList.add(convertJobStep(jobStep));
        }
        return gwtJobStepList;
    }

    public static GwtJobStep convertJobStep(JobStep jobStep) {
        GwtJobStep gwtJobStep = new GwtJobStep();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(jobStep, gwtJobStep);
        gwtJobStep.setJobStepName(jobStep.getName());
        gwtJobStep.setDescription(jobStep.getDescription());
        gwtJobStep.setJobId(KapuaGwtCommonsModelConverter.convertKapuaId(jobStep.getJobId()));
        gwtJobStep.setJobStepDefinitionId(KapuaGwtCommonsModelConverter.convertKapuaId(jobStep.getJobStepDefinitionId()));
        gwtJobStep.setStepIndex(jobStep.getStepIndex());
        gwtJobStep.setStepProperties(convertJobStepProperties(jobStep.getStepProperties()));
        return gwtJobStep;
    }

    private static List<GwtJobStepProperty> convertJobStepProperties(List<JobStepProperty> jobStepPropertyList) {
        List<GwtJobStepProperty> gwtJobStepPropertyList = new ArrayList<GwtJobStepProperty>();
        for (JobStepProperty jobStepProperty : jobStepPropertyList) {
            GwtJobStepProperty gwtJobStepProperty = new GwtJobStepProperty();
            gwtJobStepProperty.setPropertyName(jobStepProperty.getName());
            gwtJobStepProperty.setPropertyType(jobStepProperty.getPropertyType());
            gwtJobStepProperty.setPropertyValue(jobStepProperty.getPropertyValue());
            gwtJobStepPropertyList.add(gwtJobStepProperty);
        }
        return gwtJobStepPropertyList;
    }

    public static GwtJobTarget convertJobTarget(JobTarget jobTarget) {
        GwtJobTarget gwtJobTarget = new GwtJobTarget();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(jobTarget, gwtJobTarget);
        gwtJobTarget.setJobTargetId(KapuaGwtCommonsModelConverter.convertKapuaId(jobTarget.getJobTargetId()));
        gwtJobTarget.setStatus(jobTarget.getStatus().toString());
        gwtJobTarget.setStepIndex(jobTarget.getStepIndex());
        gwtJobTarget.setErrorMessage(jobTarget.getException() != null ? jobTarget.getException().getMessage() : null);
        return gwtJobTarget;
    }

    public static GwtJobStepDefinition convertJobStepDefinition(JobStepDefinition jobStepDefinition) {
        GwtJobStepDefinition gwtJobStepDefinition = new GwtJobStepDefinition();
        KapuaGwtCommonsModelConverter.convertEntity(jobStepDefinition, gwtJobStepDefinition);
        gwtJobStepDefinition.setJobStepDefinitionName(jobStepDefinition.getName());
        gwtJobStepDefinition.setDescription(jobStepDefinition.getDescription());
        gwtJobStepDefinition.setProcessorName(jobStepDefinition.getProcessorName());
        gwtJobStepDefinition.setReaderName(jobStepDefinition.getReaderName());
        gwtJobStepDefinition.setWriterName(jobStepDefinition.getWriterName());
        gwtJobStepDefinition.setStepProperties(convertJobStepProperties(jobStepDefinition.getStepProperties()));
        gwtJobStepDefinition.setStepType(jobStepDefinition.getStepType().name());
        return gwtJobStepDefinition;
    }

    public static GwtTrigger convertTrigger(Trigger trigger) {
        GwtTrigger gwtTrigger = new GwtTrigger();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(trigger, gwtTrigger);
        gwtTrigger.setTriggerName(trigger.getName());
        gwtTrigger.setCronScheduling(trigger.getCronScheduling());
        gwtTrigger.setStartsOn(trigger.getStartsOn());
        gwtTrigger.setEndsOn(trigger.getEndsOn());
        gwtTrigger.setRetryInterval(trigger.getRetryInterval());
        gwtTrigger.setTriggerProperties(convertTriggerProperties(trigger.getTriggerProperties()));
        return gwtTrigger;
    }

    private static List<GwtTriggerProperty> convertTriggerProperties(List<TriggerProperty> triggerPropertyList) {
        List<GwtTriggerProperty> gwtTriggerPropertyList = new ArrayList<GwtTriggerProperty>();
        for (TriggerProperty triggerProperty : triggerPropertyList) {
            GwtTriggerProperty gwtTriggerProperty = new GwtTriggerProperty();
            gwtTriggerProperty.setPropertyName(triggerProperty.getName());
            gwtTriggerProperty.setPropertyType(triggerProperty.getPropertyType());
            gwtTriggerProperty.setPropertyValue(triggerProperty.getPropertyValue());
            gwtTriggerPropertyList.add(gwtTriggerProperty);
        }
        return gwtTriggerPropertyList;
    }

    public static GwtExecution convertJobExecution(JobExecution jobExecution) {
        GwtExecution gwtExecution = new GwtExecution();
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(jobExecution, gwtExecution);
        gwtExecution.setStartedOn(jobExecution.getStartedOn());
        gwtExecution.setEndedOn(jobExecution.getEndedOn());
        return gwtExecution;
    }

}
