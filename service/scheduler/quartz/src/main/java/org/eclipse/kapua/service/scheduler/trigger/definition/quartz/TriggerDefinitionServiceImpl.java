/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerEntityManagerFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;

/**
 * {@link TriggerDefinitionService} exposes APIs to manage {@link TriggerDefinition} objects.<br>
 * It includes APIs to create, update, find, list and delete {@link TriggerDefinition}s.<br>
 *
 * @since 1.1.0
 */
@KapuaProvider
public class TriggerDefinitionServiceImpl extends AbstractKapuaService implements TriggerDefinitionService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public TriggerDefinitionServiceImpl() {
        super(SchedulerEntityManagerFactory.getInstance());
    }

    @Override
    public TriggerDefinition create(TriggerDefinitionCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "stepDefinitionCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "stepDefinitionCreator.scopeId");
        ArgumentValidator.notNull(creator.getTriggerType(), "stepDefinitionCreator.stepType");
        ArgumentValidator.validateEntityName(creator.getName(), "stepDefinitionCreator.name");
        ArgumentValidator.notEmptyOrNull(creator.getProcessorName(), "stepDefinitionCreator.processorName");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> TriggerDefinitionDAO.create(em, creator));
    }

    @Override
    public TriggerDefinition update(TriggerDefinition triggerDefinition) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(triggerDefinition, "stepDefinition");
        ArgumentValidator.notNull(triggerDefinition.getScopeId(), "stepDefinition.scopeId");
        ArgumentValidator.notNull(triggerDefinition.getTriggerType(), "triggerDefinition.stepType");
        ArgumentValidator.validateEntityName(triggerDefinition.getName(), "triggerDefinition.name");
        ArgumentValidator.notEmptyOrNull(triggerDefinition.getProcessorName(), "triggerDefinition.processorName");

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return entityManagerSession.onTransactedResult(em -> TriggerDefinitionDAO.update(em, triggerDefinition));
    }

    @Override
    public TriggerDefinition find(KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.onResult(em -> TriggerDefinitionDAO.find(em, stepDefinitionId));
    }

    @Override
    public TriggerDefinition find(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.onResult(em -> TriggerDefinitionDAO.find(em, scopeId, stepDefinitionId));
    }

    @Override
    public TriggerDefinitionListResult query(KapuaQuery<TriggerDefinition> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.onResult(em -> TriggerDefinitionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<TriggerDefinition> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.onResult(em -> TriggerDefinitionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(stepDefinitionId, "stepDefinitionId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            if (TriggerDefinitionDAO.find(em, scopeId, stepDefinitionId) == null) {
                throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, stepDefinitionId);
            }

            TriggerDefinitionDAO.delete(em, scopeId, stepDefinitionId);
        });

    }
}
