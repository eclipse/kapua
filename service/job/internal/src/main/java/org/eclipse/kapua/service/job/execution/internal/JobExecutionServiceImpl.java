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
package org.eclipse.kapua.service.job.execution.internal;

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
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;

/**
 * {@link JobExecutionService} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class JobExecutionServiceImpl
        extends AbstractKapuaConfigurableResourceLimitedService<JobExecution, JobExecutionCreator, JobExecutionService, JobExecutionListResult, JobExecutionQuery, JobExecutionFactory>
        implements JobExecutionService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public JobExecutionServiceImpl() {
        super(JobExecutionService.class.getName(), JobDomains.JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobExecutionService.class, JobExecutionFactory.class);
    }

    @Override
    public JobExecution create(JobExecutionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "jobExecutionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobExecutionCreator.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobExecutionDAO.create(em, creator));
    }

    @Override
    public JobExecution update(JobExecution jobExecution) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(jobExecution, "jobExecution");
        ArgumentValidator.notNull(jobExecution.getScopeId(), "jobExecution.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobExecution.getScopeId()));

        return entityManagerSession.onTransactedResult(em -> JobExecutionDAO.update(em, jobExecution));
    }

    @Override
    public JobExecution find(KapuaId scopeId, KapuaId jobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobExecutionId, "jobExecutionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobExecutionDAO.find(em, scopeId, jobExecutionId));
    }

    @Override
    public JobExecutionListResult query(KapuaQuery<JobExecution> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobExecutionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<JobExecution> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobExecutionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobExecutionId, "jobExecutionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (JobExecutionDAO.find(em, scopeId, jobExecutionId) == null) {
                throw new KapuaEntityNotFoundException(JobExecution.TYPE, jobExecutionId);
            }

            JobExecutionDAO.delete(em, scopeId, jobExecutionId);
        });

    }
}
