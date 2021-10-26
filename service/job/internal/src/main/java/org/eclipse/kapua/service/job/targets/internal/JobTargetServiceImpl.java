/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link JobTargetService} implementation
 *
 * @since 1.0.0
 */
@Singleton
public class JobTargetServiceImpl extends AbstractKapuaService implements JobTargetService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    public JobTargetServiceImpl() {
        super(JobEntityManagerFactory.getInstance(), null);
    }

    @Override
    public JobTarget create(JobTargetCreator creator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(creator, "jobTargetCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobTargetCreator.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> JobTargetDAO.create(em, creator));
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
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobTarget.getScopeId()));

        //
        // Check existence
        if (find(jobTarget.getScopeId(), jobTarget.getId()) == null) {
            throw new KapuaEntityNotFoundException(jobTarget.getType(), jobTarget.getId());
        }

        //
        // Do update
        return entityManagerSession.doTransactedAction(em -> JobTargetDAO.update(em, jobTarget));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobTargetId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobTargetId, "jobTargetId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, jobTargetId) == null) {
            throw new KapuaEntityNotFoundException(JobTarget.TYPE, jobTargetId);
        }

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> JobTargetDAO.delete(em, scopeId, jobTargetId));
    }

    @Override
    public JobTarget find(KapuaId scopeId, KapuaId jobTargetId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobTargetId, "jobTargetId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> JobTargetDAO.find(em, scopeId, jobTargetId));
    }

    @Override
    public JobTargetListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobTargetDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobTargetDAO.count(em, query));
    }
}
