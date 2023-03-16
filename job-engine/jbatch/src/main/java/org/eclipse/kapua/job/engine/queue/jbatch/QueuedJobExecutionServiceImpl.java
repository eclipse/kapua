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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionRepository;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Singleton;

/**
 * {@link QueuedJobExecutionService} implementation
 */
@Singleton
public class QueuedJobExecutionServiceImpl implements QueuedJobExecutionService {

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final TxManager txManager;
    private final QueuedJobExecutionRepository repository;

    public QueuedJobExecutionServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            QueuedJobExecutionRepository repository) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.txManager = txManager;
        this.repository = repository;
    }

    @Override
    public QueuedJobExecution create(QueuedJobExecutionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "queuedJobExecutionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "queuedJobExecutionCreator.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        QueuedJobExecution queuedJobExecutionImpl = new QueuedJobExecutionImpl(creator.getScopeId());
        queuedJobExecutionImpl.setJobId(creator.getJobId());
        queuedJobExecutionImpl.setJobExecutionId(creator.getJobExecutionId());
        queuedJobExecutionImpl.setWaitForJobExecutionId(creator.getWaitForJobExecutionId());
        queuedJobExecutionImpl.setStatus(creator.getStatus());

        //
        // Do create
        return txManager.executeWithResult(tx -> repository.create(tx, queuedJobExecutionImpl));
    }

    @Override
    public QueuedJobExecution update(QueuedJobExecution queuedJobExecution) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(queuedJobExecution, "queuedJobExecution");
        ArgumentValidator.notNull(queuedJobExecution.getScopeId(), "queuedJobExecution.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return txManager.executeWithResult(tx -> repository.update(tx, queuedJobExecution));
    }

    @Override
    public QueuedJobExecution find(KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(queuedJobExecutionId, "queuedJobExecutionId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return txManager.executeWithResult(tx -> repository.find(tx, scopeId, queuedJobExecutionId));
    }

    @Override
    public QueuedJobExecutionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return txManager.executeWithResult(tx -> repository.query(tx, query));
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
        return txManager.executeWithResult(tx -> repository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(queuedJobExecutionId, "queuedJobExecutionId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        txManager.executeWithResult(tx -> repository.delete(tx, scopeId, queuedJobExecutionId));
    }
}
