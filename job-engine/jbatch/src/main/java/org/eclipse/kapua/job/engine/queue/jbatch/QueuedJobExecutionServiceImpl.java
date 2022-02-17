/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.queue.jbatch;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.jbatch.JobEngineEntityManagerFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.execution.JobExecution;

/**
 * {@link QueuedJobExecutionService} implementation
 */
@KapuaProvider
public class QueuedJobExecutionServiceImpl extends AbstractKapuaService implements QueuedJobExecutionService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public QueuedJobExecutionServiceImpl() {
        super(JobEngineEntityManagerFactory.getInstance(), null);
    }

    @Override
    public QueuedJobExecution create(QueuedJobExecutionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "queuedJobExecutionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "queuedJobExecutionCreator.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> QueuedJobExecutionDAO.create(em, creator));
    }

    @Override
    public QueuedJobExecution update(QueuedJobExecution queuedJobExecution) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(queuedJobExecution, "queuedJobExecution");
        ArgumentValidator.notNull(queuedJobExecution.getScopeId(), "queuedJobExecution.scopeId");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return entityManagerSession.doTransactedAction(em -> QueuedJobExecutionDAO.update(em, queuedJobExecution));
    }

    @Override
    public QueuedJobExecution find(KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(queuedJobExecutionId, "queuedJobExecutionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> QueuedJobExecutionDAO.find(em, scopeId, queuedJobExecutionId));
    }

    @Override
    public QueuedJobExecutionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> QueuedJobExecutionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> QueuedJobExecutionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(queuedJobExecutionId, "queuedJobExecutionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> {
            if (QueuedJobExecutionDAO.find(em, scopeId, queuedJobExecutionId) == null) {
                throw new KapuaEntityNotFoundException(JobExecution.TYPE, queuedJobExecutionId);
            }

            return QueuedJobExecutionDAO.delete(em, scopeId, queuedJobExecutionId);
        });

    }
}
