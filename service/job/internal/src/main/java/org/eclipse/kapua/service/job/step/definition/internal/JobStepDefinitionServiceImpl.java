/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
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
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

import javax.inject.Inject;

/**
 * {@link JobStepDefinitionService} exposes APIs to manage JobStepDefinition objects.<br>
 * It includes APIs to create, update, find, list and delete StepDefinitions.<br>
 * Instances of the JobStepDefinitionService can be acquired through the ServiceLocator object.
 *
 * @since 1.0
 */
@KapuaProvider
public class JobStepDefinitionServiceImpl extends AbstractKapuaService implements JobStepDefinitionService {

    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    public JobStepDefinitionServiceImpl() {
        super(JobEntityManagerFactory.getInstance(), null);
    }

    @Override
    public JobStepDefinition create(JobStepDefinitionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "stepDefinitionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "stepDefinitionCreator.scopeId");
        ArgumentValidator.notNull(creator.getStepType(), "stepDefinitionCreator.stepType");
        ArgumentValidator.validateEntityName(creator.getName(), "stepDefinitionCreator.name");
        ArgumentValidator.notEmptyOrNull(creator.getProcessorName(), "stepDefinitionCreator.processorName");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> JobStepDefinitionDAO.create(em, creator));
    }

    @Override
    public JobStepDefinition update(JobStepDefinition jobStepDefinition) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(jobStepDefinition, "stepDefinition");
        ArgumentValidator.notNull(jobStepDefinition.getScopeId(), "stepDefinition.scopeId");
        ArgumentValidator.notNull(jobStepDefinition.getStepType(), "jobStepDefinition.stepType");
        ArgumentValidator.validateEntityName(jobStepDefinition.getName(), "jobStepDefinition.name");
        ArgumentValidator.notEmptyOrNull(jobStepDefinition.getProcessorName(), "jobStepDefinition.processorName");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return entityManagerSession.doTransactedAction(em -> JobStepDefinitionDAO.update(em, jobStepDefinition));
    }

    @Override
    public JobStepDefinition find(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.doAction(em -> JobStepDefinitionDAO.find(em, scopeId, stepDefinitionId));
    }

    @Override
    public JobStepDefinition findByName(String name) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(name, "name");

        //
        // Do find
        return entityManagerSession.doAction(em -> {

            JobStepDefinition jobStepDefinition = JobStepDefinitionDAO.findByName(em, name);
            if (jobStepDefinition != null) {
                //
                // Check Access
                authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));
            }
            return jobStepDefinition;
        });
    }

    @Override
    public JobStepDefinitionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobStepDefinitionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobStepDefinitionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> {
            if (JobStepDefinitionDAO.find(em, scopeId, stepDefinitionId) == null) {
                throw new KapuaEntityNotFoundException(JobStepDefinition.TYPE, stepDefinitionId);
            }

            return JobStepDefinitionDAO.delete(em, scopeId, stepDefinitionId);
        });

    }
}
