/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
<<<<<<< HEAD
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
import org.eclipse.kapua.service.job.JobDomains;
=======
import org.eclipse.kapua.service.job.JobDomain;
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerRepository;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerRepository;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerService;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Singleton;

/**
 * {@link FiredTriggerService} implementation.
 *
 * @since 1.5.0
 */
@Singleton
public class FiredTriggerServiceImpl implements FiredTriggerService {

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final TxManager txManager;
    private final FiredTriggerRepository firedTriggerRepository;
    private final FiredTriggerFactory firedTriggerFactory;
    private final TriggerRepository triggerRepository;

    public FiredTriggerServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            FiredTriggerRepository firedTriggerRepository,
            FiredTriggerFactory firedTriggerFactory,
            TriggerRepository triggerRepository) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.txManager = txManager;
        this.firedTriggerRepository = firedTriggerRepository;
        this.firedTriggerFactory = firedTriggerFactory;
        this.triggerRepository = triggerRepository;
    }

    @Override
    public FiredTrigger create(FiredTriggerCreator firedTriggerCreator) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(firedTriggerCreator, "firedTriggerCreator");
        ArgumentValidator.notNull(firedTriggerCreator.getScopeId(), "firedTriggerCreator.scopeId");
        ArgumentValidator.notNull(firedTriggerCreator.getTriggerId(), "firedTriggerCreator.triggerId");
        ArgumentValidator.notNull(firedTriggerCreator.getFiredOn(), "firedTriggerCreator.firedOn");
        ArgumentValidator.notNull(firedTriggerCreator.getStatus(), "firedTriggerCreator.status");

        // Check access
<<<<<<< HEAD
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.JOB, Actions.write, null));
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));
=======
        authorizationService.checkPermission(permissionFactory.newPermission(new JobDomain(), Actions.write, null));
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)

        return txManager.execute(tx -> {
            // Check existence of Trigger
            if (!triggerRepository.find(tx, firedTriggerCreator.getScopeId(), firedTriggerCreator.getTriggerId()).isPresent()) {
                throw new KapuaEntityNotFoundException(Trigger.TYPE, firedTriggerCreator.getTriggerId());
            }

            // Do create
            FiredTrigger toBeCreated = firedTriggerFactory.newEntity(firedTriggerCreator.getScopeId());
            toBeCreated.setTriggerId(firedTriggerCreator.getTriggerId());
            toBeCreated.setFiredOn(firedTriggerCreator.getFiredOn());
            toBeCreated.setStatus(firedTriggerCreator.getStatus());
            toBeCreated.setMessage(firedTriggerCreator.getMessage());
            return firedTriggerRepository.create(tx, toBeCreated);
        });
    }

    @Override
    public FiredTrigger find(KapuaId scopeId, KapuaId firedTriggerId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(firedTriggerId, KapuaEntityAttributes.ENTITY_ID);
        // Check Access
<<<<<<< HEAD
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.JOB, Actions.read, scopeId));
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));
=======
        authorizationService.checkPermission(permissionFactory.newPermission(new JobDomain(), Actions.read, scopeId));
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        // Do find
        return txManager.execute(tx -> firedTriggerRepository.find(tx, scopeId, firedTriggerId))
                .orElse(null);
    }

    @Override
    public FiredTriggerListResult query(KapuaQuery query) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
<<<<<<< HEAD
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.JOB, Actions.read, query.getScopeId()));
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));
=======
        authorizationService.checkPermission(permissionFactory.newPermission(new JobDomain(), Actions.read, query.getScopeId()));
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        // Do query
        return txManager.execute(tx -> firedTriggerRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
<<<<<<< HEAD
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.JOB, Actions.read, query.getScopeId()));
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));
=======
        authorizationService.checkPermission(permissionFactory.newPermission(new JobDomain(), Actions.read, query.getScopeId()));
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        // Do query
        return txManager.execute(tx -> firedTriggerRepository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId firedTriggerId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(firedTriggerId, KapuaEntityAttributes.ENTITY_ID);
        // Check Access
<<<<<<< HEAD
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.JOB, Actions.delete, null));
||||||| parent of 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));
=======
        authorizationService.checkPermission(permissionFactory.newPermission(new JobDomain(), Actions.delete, null));
>>>>>>> 9d99c5f1ab (:enh: removed further statics, and marked for fix those that could not be changed yet)
        // Do delete
        txManager.execute(tx -> firedTriggerRepository.delete(tx, scopeId, firedTriggerId));

    }
}
