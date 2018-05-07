/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtExecutionQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepDefinitionQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobPredicates;
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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobExecutionFactory JOB_EXECUTION_FACTORY = LOCATOR.getFactory(JobExecutionFactory.class);
    private static final JobFactory JOB_FACTORY = LOCATOR.getFactory(JobFactory.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);
    private static final JobStepDefinitionFactory JOB_STEP_DEFINITION_FACTORY = LOCATOR.getFactory(JobStepDefinitionFactory.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    private static final TriggerFactory TRIGGER_FACTORY = LOCATOR.getFactory(TriggerFactory.class);

    private GwtKapuaJobModelConverter() {
    }

    public static Job convertJob(GwtJob gwtJob) {
        Job job = JOB_FACTORY.newEntity(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJob.getScopeId()));

        GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtJob, job);

        job.setName(gwtJob.getJobName());
        job.setDescription(gwtJob.getDescription());
        job.setJobSteps(convertJobSteps(gwtJob.getJobSteps()));
        job.setJobXmlDefinition(gwtJob.getJobXmlDefinition());

        return job;
    }

    private static List<JobStep> convertJobSteps(List<GwtJobStep> gwtJobSteps) {
        List<JobStep> jobStepList = new ArrayList<JobStep>();

        for (GwtJobStep gwtJobStep : gwtJobSteps) {
            JobStep jobStep = JOB_STEP_FACTORY.newEntity(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStep.getScopeId()));

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
        List<JobStepProperty> jobStepPropertyList = new ArrayList<JobStepProperty>();

        for (GwtJobStepProperty gwtProperty : gwtJobStepProperties) {
            JobStepProperty property = JOB_STEP_FACTORY.newStepProperty(gwtProperty.getPropertyName(), gwtProperty.getPropertyType(), gwtProperty.getPropertyValue());

            jobStepPropertyList.add(property);
        }
        return jobStepPropertyList;
    }

    public static JobQuery convertJobQuery(GwtJobQuery gwtJobQuery, PagingLoadConfig loadConfig) {

        AndPredicateImpl predicate = new AndPredicateImpl();
        // Convert query
        JobQuery jobQuery = JOB_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobQuery.getScopeId()));
        if (gwtJobQuery.getName() != null && !gwtJobQuery.getName().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(JobPredicates.NAME, gwtJobQuery.getName(), Operator.LIKE));
        }
        jobQuery.setLimit(loadConfig.getLimit());
        jobQuery.setOffset(loadConfig.getOffset());
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? JobPredicates.NAME : loadConfig.getSortField();
        if (sortField.equals("jobName")) {
            sortField = JobPredicates.NAME;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        jobQuery.setSortCriteria(sortCriteria);
        jobQuery.setPredicate(predicate);

        return jobQuery;
    }

    public static JobTargetQuery convertJobTargetQuery(GwtJobTargetQuery gwtJobTargetQuery, PagingLoadConfig loadConfig) {
        JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetQuery.getScopeId()));

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        if (gwtJobTargetQuery.getJobId() != null && !gwtJobTargetQuery.getJobId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicateImpl<KapuaId>(JobTargetPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetQuery.getJobId())));
        }
        jobTargetQuery.setPredicate(andPredicate);

        if (loadConfig != null) {
            jobTargetQuery.setLimit(loadConfig.getLimit());
            jobTargetQuery.setOffset(loadConfig.getOffset());

            String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? JobTargetPredicates.ENTITY_ID : loadConfig.getSortField();
            SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
            FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
            jobTargetQuery.setSortCriteria(sortCriteria);
        }

        return jobTargetQuery;
    }

    public static JobStepQuery convertJobStepQuery(GwtJobStepQuery gwtJobStepQuery, PagingLoadConfig loadConfig) {
        JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepQuery.getScopeId()));

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        if (gwtJobStepQuery.getJobId() != null && !gwtJobStepQuery.getJobId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicateImpl<KapuaId>(JobStepPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepQuery.getJobId())));
        }
        jobStepQuery.setPredicate(andPredicate);

        FieldSortCriteria sortCriteria;
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
            sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        } else {
            String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? JobStepPredicates.STEP_INDEX : loadConfig.getSortField();
            if (sortField.equals("jobStepName")) {
                sortField = JobStepPredicates.JOB_STEP_NAME;
            } else if (sortField.equals("jobStepDefinitionName")) {
                sortField = JobStepPredicates.JOB_STEP_DEFINITION_ID;
            }
            SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
            sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        }
        jobStepQuery.setSortCriteria(sortCriteria);

        jobStepQuery.setLimit(loadConfig.getLimit());
        jobStepQuery.setOffset(loadConfig.getOffset());

        return jobStepQuery;
    }

    public static JobStepDefinitionQuery convertJobStepDefinitionQuery(PagingLoadConfig loadConfig, GwtJobStepDefinitionQuery gwtJobStepDefinitionQuery) {
        JobStepDefinitionQuery jobStepDefinitionQuery = JOB_STEP_DEFINITION_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepDefinitionQuery.getScopeId()));

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        jobStepDefinitionQuery.setPredicate(andPredicate);

        if (loadConfig != null) {
            jobStepDefinitionQuery.setLimit(loadConfig.getLimit());
            jobStepDefinitionQuery.setOffset(loadConfig.getOffset());
        }
        return jobStepDefinitionQuery;
    }

    public static JobStepCreator convertJobStepCreator(GwtJobStepCreator gwtJobStepCreator) {

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobStepCreator.getScopeId());
        JobStepCreator jobStepCreator = JOB_STEP_FACTORY.newCreator(scopeId);

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

        TriggerQuery triggerQuery = TRIGGER_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtTriggerQuery.getScopeId()));

        AttributePredicateImpl<String> kapuaPropertyNameAttributePredicate = new AttributePredicateImpl<String>(TriggerPredicates.TRIGGER_PROPERTIES_NAME, "jobId");
        AttributePredicateImpl<String> kapuaPropertyValueAttributePredicate = new AttributePredicateImpl<String>(TriggerPredicates.TRIGGER_PROPERTIES_VALUE, gwtTriggerQuery.getJobId());
        AttributePredicateImpl<String> kapuaPropertyTypeAttributePredicate = new AttributePredicateImpl<String>(TriggerPredicates.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName());

        AndPredicateImpl andPredicate = new AndPredicateImpl()
                .and(kapuaPropertyNameAttributePredicate)
                .and(kapuaPropertyValueAttributePredicate)
                .and(kapuaPropertyTypeAttributePredicate);

        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? TriggerPredicates.ENTITY_ID : loadConfig.getSortField();
        if (sortField.equals("triggerName")) {
            sortField = TriggerPredicates.TRIGGER_NAME;
        } else if (sortField.equals("startsOnFormatted")) {
            sortField = TriggerPredicates.STARTS_ON;
        } else if (sortField.equals("endsOnFormatted")) {
            sortField = TriggerPredicates.ENDS_ON;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        triggerQuery.setSortCriteria(sortCriteria);
        triggerQuery.setPredicate(andPredicate);
        triggerQuery.setLimit(loadConfig.getLimit());
        triggerQuery.setOffset(loadConfig.getOffset());

        return triggerQuery;
    }

    public static List<TriggerProperty> convertTriggerProperties(List<GwtTriggerProperty> gwtTriggerProperties) {
        List<TriggerProperty> triggerPropertyList = new ArrayList<TriggerProperty>();

        for (GwtTriggerProperty gwtProperty : gwtTriggerProperties) {
            TriggerProperty property = TRIGGER_FACTORY.newTriggerProperty(gwtProperty.getPropertyName(), gwtProperty.getPropertyType(), gwtProperty.getPropertyValue());
            triggerPropertyList.add(property);
        }
        return triggerPropertyList;
    }

    public static JobExecutionQuery convertJobExecutionQuery(PagingLoadConfig pagingLoadConfig, GwtExecutionQuery gwtExecutionQuery) {
        JobExecutionQuery query = JOB_EXECUTION_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtExecutionQuery.getScopeId()));
        query.setPredicate(new AttributePredicateImpl<KapuaId>(JobExecutionPredicates.JOB_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtExecutionQuery.getJobId())));

        if (pagingLoadConfig.getSortField() != null) {
            query.setSortCriteria(new FieldSortCriteria(pagingLoadConfig.getSortField(), pagingLoadConfig.getSortDir() == SortDir.ASC ? SortOrder.ASCENDING : SortOrder.DESCENDING));
        }

        query.setLimit(pagingLoadConfig.getLimit());
        query.setOffset(pagingLoadConfig.getOffset());

        return query;
    }
}
