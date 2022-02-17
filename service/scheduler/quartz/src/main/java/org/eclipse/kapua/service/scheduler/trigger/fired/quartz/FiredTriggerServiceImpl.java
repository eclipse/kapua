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
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerEntityManagerFactory;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerService;

import javax.inject.Inject;

/**
 * {@link FiredTriggerService} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class FiredTriggerServiceImpl extends AbstractKapuaService implements FiredTriggerService {

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    @Inject
    private TriggerService triggerService;

    public FiredTriggerServiceImpl() {
        super(SchedulerEntityManagerFactory.getInstance());
    }

    @Override
    public FiredTrigger create(FiredTriggerCreator firedTriggerCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(firedTriggerCreator, "firedTriggerCreator");
        ArgumentValidator.notNull(firedTriggerCreator.getScopeId(), "firedTriggerCreator.scopeId");
        ArgumentValidator.notNull(firedTriggerCreator.getTriggerId(), "firedTriggerCreator.triggerId");
        ArgumentValidator.notNull(firedTriggerCreator.getFiredOn(), "firedTriggerCreator.firedOn");
        ArgumentValidator.notNull(firedTriggerCreator.getStatus(), "firedTriggerCreator.status");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Check existence of Trigger
        if (triggerService.find(firedTriggerCreator.getScopeId(), firedTriggerCreator.getTriggerId()) == null) {
            throw new KapuaEntityNotFoundException(Trigger.TYPE, firedTriggerCreator.getTriggerId());
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> FiredTriggerDAO.create(em, firedTriggerCreator));
    }

    @Override
    public FiredTrigger find(KapuaId scopeId, KapuaId firedTriggerId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(firedTriggerId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> FiredTriggerDAO.find(em, scopeId, firedTriggerId));
    }

    @Override
    public FiredTriggerListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> FiredTriggerDAO.query(em, query));
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
        return entityManagerSession.doAction(em -> FiredTriggerDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId firedTriggerId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(firedTriggerId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> {
            if (FiredTriggerDAO.find(em, scopeId, firedTriggerId) == null) {
                throw new KapuaEntityNotFoundException(FiredTrigger.TYPE, firedTriggerId);
            }

            return FiredTriggerDAO.delete(em, scopeId, firedTriggerId);
        });

    }
}
