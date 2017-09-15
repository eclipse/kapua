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

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtExecutionQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepDefinitionQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTargetQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionPredicates;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepPredicates;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetPredicates;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerPredicates;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;

import java.util.ArrayList;
import java.util.List;

public class GwtKapuaJobModelConverter {

    private GwtKapuaJobModelConverter() { }

    public static Job convertJob(GwtJob gwtJob) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobFactory jobFactory = locator.getFactory(JobFactory.class);
        Job job = jobFactory.newEntity(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJob.getScopeId()));
        GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtJob, job);
        job.setName(gwtJob.getJobName());
        job.setDescription(gwtJob.getDescription());
        job.setJobSteps(convertJobSteps(gwtJob.getJobSteps()));
        job.setJobXmlDefinition(gwtJob.getJobXmlDefinition());
        return job;
    }

    private static List<JobStep> convertJobSteps(List<GwtJobStep> gwtJobSteps) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);
        List<JobStep> jobStepList = new ArrayList<JobStep>();
        for (GwtJobStep gwtJobStep : gwtJobSteps) {
            JobStep jobStep = jobStepFactory.newEntity(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getScopeId()));
            GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtJobStep, jobStep);
            jobStep.setName(gwtJobStep.getJobStepName());
            jobStep.setDescription(gwtJobStep.getDescription());
            jobStep.setJobId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getJobId()));
            jobStep.setJobStepDefinitionId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getJobStepDefinitionId()));
            jobStep.setStepIndex(gwtJobStep.getStepIndex());
            jobStep.setStepProperties(convertJobStepProperties(gwtJobStep.getStepProperties()));
            jobStepList.add(jobStep);
        }
        return jobStepList;
    }

    public static List<JobStepProperty> convertJobStepProperties(List<GwtJobStepProperty> gwtJobStepProperties) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);
        List<JobStepProperty> jobStepPropertyList = new ArrayList<JobStepProperty>();
        for (GwtJobStepProperty gwtProperty : gwtJobStepProperties) {
            JobStepProperty property = jobStepFactory.newStepProperty(gwtProperty.getPropertyName(), gwtProperty.getPropertyType(), gwtProperty.getPropertyValue());
            jobStepPropertyList.add(property);
        }
        return jobStepPropertyList;
    }

    public static JobQuery convertJobQuery(GwtJobQuery gwtJobQuery, PagingLoadConfig loadConfig) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobFactory jobFactory = locator.getFactory(JobFactory.class);
        JobQuery jobQuery = jobFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobQuery.getScopeId()));
        jobQuery.setLimit(loadConfig.getLimit());
        jobQuery.setOffset(loadConfig.getOffset());
        return jobQuery;
    }

    public static JobTargetQuery convertJobTargetQuery(GwtJobTargetQuery gwtJobTargetQuery, PagingLoadConfig loadConfig) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);
        JobTargetQuery jobTargetQuery = jobTargetFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetQuery.getScopeId()));
        AndPredicate andPredicate = new AndPredicate();
        if (gwtJobTargetQuery.getJobId() != null && !gwtJobTargetQuery.getJobId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicate<KapuaId>(JobTargetPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetQuery.getJobId())));
        }
        jobTargetQuery.setPredicate(andPredicate);
        if (loadConfig != null) {
            jobTargetQuery.setLimit(loadConfig.getLimit());
            jobTargetQuery.setOffset(loadConfig.getOffset());
        }
        return jobTargetQuery;
    }

    public static JobStepQuery convertJobStepQuery(GwtJobStepQuery gwtJobStepQuery, PagingLoadConfig loadConfig) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);
        JobStepQuery jobStepQuery = jobStepFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepQuery.getScopeId()));
        AndPredicate andPredicate = new AndPredicate();
        if (gwtJobStepQuery.getJobId() != null && !gwtJobStepQuery.getJobId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicate<KapuaId>(JobStepPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepQuery.getJobId())));
        }
        if (gwtJobStepQuery.getSortAttribute() != null) {
            String sortField = null;
            if (gwtJobStepQuery.getSortAttribute().equals(GwtJobStepQuery.GwtSortAttribute.STEP_INDEX)) {
                sortField = JobStepPredicates.STEP_INDEX;
            }
            SortOrder sortOrder = null;
            if (gwtJobStepQuery.getSortOrder().equals(GwtJobStepQuery.GwtSortOrder.DESCENDING)) {
                sortOrder = SortOrder.DESCENDING;
            } else {
                sortOrder = SortOrder.ASCENDING;
            }
            FieldSortCriteria criteria = new FieldSortCriteria(sortField, sortOrder);
            jobStepQuery.setSortCriteria(criteria);
        }
        jobStepQuery.setPredicate(andPredicate);
        jobStepQuery.setLimit(loadConfig.getLimit());
        jobStepQuery.setOffset(loadConfig.getOffset());
        return jobStepQuery;
    }

    public static JobStepDefinitionQuery convertJobStepDefinitionQuery(PagingLoadConfig loadConfig, GwtJobStepDefinitionQuery gwtJobStepDefinitionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);
        JobStepDefinitionQuery jobStepDefinitionQuery = jobStepDefinitionFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepDefinitionQuery.getScopeId()));
        AndPredicate andPredicate = new AndPredicate();
        jobStepDefinitionQuery.setPredicate(andPredicate);
        if (loadConfig != null) {
            jobStepDefinitionQuery.setLimit(loadConfig.getLimit());
            jobStepDefinitionQuery.setOffset(loadConfig.getOffset());
        }
        return jobStepDefinitionQuery;
    }

    public static JobStepCreator convertJobStepCreator(GwtJobStepCreator gwtJobStepCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepCreator.getScopeId());
        JobStepCreator jobStepCreator = jobStepFactory.newCreator(scopeId);

        jobStepCreator.setName(gwtJobStepCreator.getJobName());
        jobStepCreator.setDescription(gwtJobStepCreator.getJobDescription());
        jobStepCreator.setJobId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepCreator.getJobId()));
        jobStepCreator.setJobStepDefinitionId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepCreator.getJobStepDefinitionId()));
        jobStepCreator.setStepIndex(gwtJobStepCreator.getStepIndex());
        jobStepCreator.setJobStepProperties(convertJobStepProperties(gwtJobStepCreator.getProperties()));

        //
        // Return converted
        return jobStepCreator;
    }

    public static TriggerQuery convertTriggerQuery(GwtTriggerQuery gwtTriggerQuery, PagingLoadConfig loadConfig) {
        KapuaLocator locator = KapuaLocator.getInstance();
        TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);

        TriggerQuery triggerQuery = triggerFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtTriggerQuery.getScopeId()));
        AttributePredicate<String> kapuaPropertyNameAttributePredicate = new AttributePredicate<String>(TriggerPredicates.TRIGGER_PROPERTIES_NAME, "jobId");
        AttributePredicate<String> kapuaPropertyValueAttributePredicate = new AttributePredicate<String>(TriggerPredicates.TRIGGER_PROPERTIES_VALUE, gwtTriggerQuery.getJobId());
        AttributePredicate<String> kapuaPropertyTypeAttributePredicate = new AttributePredicate<String>(TriggerPredicates.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName());
        AndPredicate andPredicate = new AndPredicate().and(kapuaPropertyNameAttributePredicate).and(kapuaPropertyValueAttributePredicate).and(kapuaPropertyTypeAttributePredicate);
        triggerQuery.setPredicate(andPredicate);
        triggerQuery.setLimit(loadConfig.getLimit());
        triggerQuery.setOffset(loadConfig.getOffset());
        return triggerQuery;
    }

    public static List<TriggerProperty> convertTriggerProperties(List<GwtTriggerProperty> gwtTriggerProperties) {
        KapuaLocator locator = KapuaLocator.getInstance();
        TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);
        List<TriggerProperty> triggerPropertyList = new ArrayList<TriggerProperty>();
        for (GwtTriggerProperty gwtProperty : gwtTriggerProperties) {
            TriggerProperty property = triggerFactory.newTriggerProperty(gwtProperty.getPropertyName(), gwtProperty.getPropertyType(), gwtProperty.getPropertyValue());
            triggerPropertyList.add(property);
        }
        return triggerPropertyList;
    }

    public static JobExecutionQuery convertJobExecutionQuery(GwtExecutionQuery gwtExecutionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobExecutionFactory factory = locator.getFactory(JobExecutionFactory.class);
        JobExecutionQuery query = factory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtExecutionQuery.getScopeId()));
        query.setPredicate(new AttributePredicate<KapuaId>(JobExecutionPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtExecutionQuery.getJobId())));
        return query;
    }

}
