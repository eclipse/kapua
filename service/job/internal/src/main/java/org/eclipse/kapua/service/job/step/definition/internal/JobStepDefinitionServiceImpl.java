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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

/**
 * {@link JobStepDefinitionService} exposes APIs to manage JobStepDefinition objects.<br>
 * It includes APIs to create, update, find, list and delete StepDefinitions.<br>
 * Instances of the JobStepDefinitionService can be acquired through the ServiceLocator object.
 *
 * @since 1.0
 */
@KapuaProvider
public class JobStepDefinitionServiceImpl
        extends
        AbstractKapuaConfigurableResourceLimitedService<JobStepDefinition, JobStepDefinitionCreator, JobStepDefinitionService, JobStepDefinitionListResult, JobStepDefinitionQuery, JobStepDefinitionFactory>
        implements JobStepDefinitionService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public JobStepDefinitionServiceImpl() {
        super(JobStepDefinitionService.class.getName(), JobDomains.JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobStepDefinitionService.class, JobStepDefinitionFactory.class);
    }

    @Override
    public JobStepDefinition create(JobStepDefinitionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "stepDefinitionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "stepDefinitionCreator.scopeId");
        ArgumentValidator.notNull(creator.getStepType(), "stepDefinitionCreator.stepType");
        ArgumentValidator.notEmptyOrNull(creator.getName(), "stepDefinitionCreator.name");
        ArgumentValidator.notEmptyOrNull(creator.getProcessorName(), "stepDefinitionCreator.processorName");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobStepDefinitionDAO.create(em, creator));
    }

    @Override
    public JobStepDefinition update(JobStepDefinition jobStepDefinition) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(jobStepDefinition, "stepDefinition");
        ArgumentValidator.notNull(jobStepDefinition.getScopeId(), "stepDefinition.scopeId");
        ArgumentValidator.notNull(jobStepDefinition.getStepType(), "jobStepDefinition.stepType");
        ArgumentValidator.notEmptyOrNull(jobStepDefinition.getName(), "jobStepDefinition.name");
        ArgumentValidator.notEmptyOrNull(jobStepDefinition.getProcessorName(), "jobStepDefinition.processorName");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return entityManagerSession.onTransactedResult(em -> JobStepDefinitionDAO.update(em, jobStepDefinition));
    }

    @Override
    public JobStepDefinition find(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobStepDefinitionDAO.find(em, scopeId, stepDefinitionId));
    }

    @Override
    public JobStepDefinitionListResult query(KapuaQuery<JobStepDefinition> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobStepDefinitionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<JobStepDefinition> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobStepDefinitionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (JobStepDefinitionDAO.find(em, scopeId, stepDefinitionId) == null) {
                throw new KapuaEntityNotFoundException(JobStepDefinition.TYPE, stepDefinitionId);
            }

            JobStepDefinitionDAO.delete(em, scopeId, stepDefinitionId);
        });

    }
}
