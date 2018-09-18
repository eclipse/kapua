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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepIndex;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link JobStepService} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class JobStepServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<JobStep, JobStepCreator, JobStepService, JobStepListResult, JobStepQuery, JobStepFactory>
        implements JobStepService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final JobStepDefinitionService JOB_STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    public JobStepServiceImpl() {
        super(JobStepService.class.getName(), JobDomains.JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobStepService.class, JobStepFactory.class);
    }

    @Override
    public JobStep create(JobStepCreator jobStepCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(jobStepCreator, "jobStepCreator");
        ArgumentValidator.notNull(jobStepCreator.getScopeId(), "jobStepCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(jobStepCreator.getName(), "jobStepCreator.name");
        ArgumentValidator.numRange(jobStepCreator.getName().length(), 1, 255, "jobStepCreator.name");
        ArgumentValidator.notNull(jobStepCreator.getJobStepDefinitionId(), "jobStepCreator.stepDefinitionId");

        if (jobStepCreator.getDescription() != null) {
            ArgumentValidator.numRange(jobStepCreator.getDescription().length(), 0, 8192, "jobStepCreator.description");
        }

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobStepCreator.getScopeId()));

        //
        // Check job step definition
        JobStepDefinition jobStepDefinition = JOB_STEP_DEFINITION_SERVICE.find(jobStepCreator.getScopeId(), jobStepCreator.getJobStepDefinitionId());
        ArgumentValidator.notNull(jobStepDefinition, "jobStepCreator.jobStepDefinitionId");

        for (JobStepProperty jsp : jobStepCreator.getStepProperties()) {
            for (JobStepProperty jsdp : jobStepDefinition.getStepProperties()) {
                if (jsp.getName().equals(jsdp.getName())) {
                    ArgumentValidator.areEqual(jsp.getPropertyType(), jsdp.getPropertyType(), "jobStepCreator.stepProperties{}." + jsp.getName());
                    break;
                }
            }
        }

        //
        // Check duplicate name
        JobStepQuery query = new JobStepQueryImpl(jobStepCreator.getScopeId());
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()),
                        new AttributePredicateImpl<>(JobStepAttributes.NAME, jobStepCreator.getName())
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(jobStepCreator.getName());
        }

        //
        // Check step index
        if (jobStepCreator.getStepIndex() == null) {
            query.setPredicate(new AttributePredicateImpl<>(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()));
            query.setSortCriteria(new FieldSortCriteria(JobStepAttributes.STEP_INDEX, FieldSortCriteria.SortOrder.DESCENDING));
            query.setLimit(1);

            JobStepListResult jobStepListResult = query(query);
            JobStep lastJobStep = jobStepListResult.getFirstItem();

            jobStepCreator.setStepIndex(lastJobStep != null ? lastJobStep.getStepIndex() + 1 : JobStepIndex.FIRST);
        } else {
            query.setPredicate(
                    new AndPredicateImpl(
                            new AttributePredicateImpl<>(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()),
                            new AttributePredicateImpl<>(JobStepAttributes.STEP_INDEX, jobStepCreator.getStepIndex())
                    )
            );

            if (count(query) > 0) {
                List<Map.Entry<String, Object>> uniquesFieldValues = new ArrayList<>();

                uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(JobStepAttributes.SCOPE_ID, jobStepCreator.getScopeId()));
                uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()));
                uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(JobStepAttributes.STEP_INDEX, jobStepCreator.getStepIndex()));

                throw new KapuaEntityUniquenessException(JobStep.TYPE, uniquesFieldValues);
            }
        }

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobStepDAO.create(em, jobStepCreator));
    }

    @Override
    public JobStep update(JobStep jobStep) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(jobStep, "jobStep");
        ArgumentValidator.notNull(jobStep.getScopeId(), "jobStep.scopeId");
        ArgumentValidator.notNull(jobStep.getName(), "jobStep.name");
        ArgumentValidator.notNull(jobStep.getJobStepDefinitionId(), "jobStep.stepDefinitionId");
        if (jobStep.getDescription() != null) {
            ArgumentValidator.numRange(jobStep.getDescription().length(), 0, 8192, "jobStep.description");
        }

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobStep.getScopeId()));

        //
        // Check existence
        if (find(jobStep.getScopeId(), jobStep.getId()) == null) {
            throw new KapuaEntityNotFoundException(jobStep.getType(), jobStep.getId());
        }

        //
        // Check job step definition
        JobStepDefinition jobStepDefinition = JOB_STEP_DEFINITION_SERVICE.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
        ArgumentValidator.notNull(jobStepDefinition, "jobStepCreator.jobStepDefinitionId");

        for (JobStepProperty jsp : jobStep.getStepProperties()) {
            for (JobStepProperty jsdp : jobStepDefinition.getStepProperties()) {
                if (jsp.getName().equals(jsdp.getName())) {
                    ArgumentValidator.areEqual(jsp.getPropertyType(), jsdp.getPropertyType(), "jobStepCreator.stepProperties{}." + jsp.getName());
                }
            }
        }

        JobStepQuery query = new JobStepQueryImpl(jobStep.getScopeId());
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(JobStepAttributes.JOB_ID, jobStep.getJobId()),
                        new AttributePredicateImpl<>(JobStepAttributes.NAME, jobStep.getName()),
                        new AttributePredicateImpl<>(JobStepAttributes.ENTITY_ID, jobStep.getId(), Operator.NOT_EQUAL)
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(jobStep.getName());
        }

        return entityManagerSession.onTransactedResult(em -> JobStepDAO.update(em, jobStep));
    }

    @Override
    public JobStep find(KapuaId scopeId, KapuaId jobStepId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobStepId, "jobStepId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobStepDAO.find(em, scopeId, jobStepId));
    }

    @Override
    public JobStepListResult query(KapuaQuery<JobStep> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobStepDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<JobStep> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobStepDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobStepId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobStepId, "jobStepId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, jobStepId) == null) {
            throw new KapuaEntityNotFoundException(JobStep.TYPE, jobStepId);
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> JobStepDAO.delete(em, scopeId, jobStepId));
    }
}
