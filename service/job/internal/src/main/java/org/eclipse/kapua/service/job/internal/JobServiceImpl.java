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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;

/**
 * {@link JobService} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class JobServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Job, JobCreator, JobService, JobListResult, JobQuery, JobFactory> implements JobService {

    private static final Domain JOB_DOMAIN = new JobDomain();
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public JobServiceImpl() {
        super(JobService.class.getName(), JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobService.class, JobFactory.class);
    }

    @Override
    public Job create(JobCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "jobCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobCreator.scopeId");
        ArgumentValidator.notNull(creator.getName(), "jobCreator.name");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, creator.getScopeId()));

        if (allowedChildEntities(creator.getScopeId()) <= 0) {
            throw new KapuaIllegalArgumentException("scopeId", "max jobs reached");
        }

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobDAO.create(em, creator));
    }

    @Override
    public Job update(Job job) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(job, "job");
        ArgumentValidator.notNull(job.getScopeId(), "job.scopeId");
        ArgumentValidator.notNull(job.getName(), "job.name");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, job.getScopeId()));

        return entityManagerSession.onTransactedResult(em -> JobDAO.update(em, job));
    }

    @Override
    public Job find(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobDAO.find(em, jobId));
    }

    @Override
    public JobListResult query(KapuaQuery<Job> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Job> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (JobDAO.find(em, jobId) == null) {
                throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
            }

            JobDAO.delete(em, jobId);

        });

    }

}
