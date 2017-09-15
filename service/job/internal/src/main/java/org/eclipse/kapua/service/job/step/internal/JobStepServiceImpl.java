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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
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
import org.eclipse.kapua.service.job.internal.JobDomain;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;

/**
 * {@link JobStepService} implementation
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class JobStepServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<JobStep, JobStepCreator, JobStepService, JobStepListResult, JobStepQuery, JobStepFactory>
        implements JobStepService {

    private static final Domain JOB_DOMAIN = new JobDomain();
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    protected JobStepServiceImpl() {
        super(JobStepService.class.getName(), JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobStepService.class, JobStepFactory.class);
    }

    @Override
    public JobStep create(JobStepCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "jobStepCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobStepCreator.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobStepDAO.create(em, creator));
    }

    @Override
    public JobStep update(JobStep jobStep) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(jobStep, "jobStep");
        ArgumentValidator.notNull(jobStep.getScopeId(), "jobStep.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, jobStep.getScopeId()));

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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobStepDAO.find(em, jobStepId));
    }

    @Override
    public JobStepListResult query(KapuaQuery<JobStep> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.read, query.getScopeId()));

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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.read, query.getScopeId()));

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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (JobStepDAO.find(em, jobStepId) == null) {
                throw new KapuaEntityNotFoundException(JobStep.TYPE, jobStepId);
            }

            JobStepDAO.delete(em, jobStepId);
        });

    }
}
