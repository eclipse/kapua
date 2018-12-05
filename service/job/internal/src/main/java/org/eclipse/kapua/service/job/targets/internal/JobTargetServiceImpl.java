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
package org.eclipse.kapua.service.job.targets.internal;

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
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

/**
 * {@link JobTargetService} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class JobTargetServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<JobTarget, JobTargetCreator, JobTargetService, JobTargetListResult, JobTargetQuery, JobTargetFactory>
        implements JobTargetService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public JobTargetServiceImpl() {
        super(JobTargetService.class.getName(), JobDomains.JOB_DOMAIN, JobEntityManagerFactory.getInstance(), JobTargetService.class, JobTargetFactory.class);
    }

    @Override
    public JobTarget create(JobTargetCreator creator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(creator, "jobTargetCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobTargetCreator.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> JobTargetDAO.create(em, creator));
    }

    @Override
    public JobTarget update(JobTarget jobTarget) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(jobTarget, "jobTarget");
        ArgumentValidator.notNull(jobTarget.getScopeId(), "jobTarget.scopeId");
        ArgumentValidator.notNull(jobTarget.getId(), "jobTarget.id");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobTarget.getScopeId()));

        //
        // Check existence
        if (find(jobTarget.getScopeId(), jobTarget.getId()) == null) {
            throw new KapuaEntityNotFoundException(jobTarget.getType(), jobTarget.getId());
        }

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> JobTargetDAO.update(em, jobTarget));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobTargetId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobTargetId, "jobTargetId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, jobTargetId) == null) {
            throw new KapuaEntityNotFoundException(JobTarget.TYPE, jobTargetId);
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> JobTargetDAO.delete(em, scopeId, jobTargetId));
    }

    @Override
    public JobTarget find(KapuaId scopeId, KapuaId jobTargetId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobTargetId, "jobTargetId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> JobTargetDAO.find(em, scopeId, jobTargetId));
    }

    @Override
    public JobTargetListResult query(KapuaQuery<JobTarget> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobTargetDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<JobTarget> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> JobTargetDAO.count(em, query));
    }
}
