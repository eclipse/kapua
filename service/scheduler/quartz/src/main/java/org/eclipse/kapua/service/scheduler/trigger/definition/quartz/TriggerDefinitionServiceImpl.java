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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

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
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;

import javax.inject.Inject;

/**
 * {@link TriggerDefinitionService} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class TriggerDefinitionServiceImpl extends AbstractKapuaService implements TriggerDefinitionService {

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

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
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> TriggerDefinitionDAO.create(em, creator));
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
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, null));

        return entityManagerSession.doTransactedAction(em -> TriggerDefinitionDAO.update(em, triggerDefinition));
    }

    @Override
    public TriggerDefinition find(KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.doAction(em -> TriggerDefinitionDAO.find(em, stepDefinitionId));
    }

    @Override
    public TriggerDefinition find(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(stepDefinitionId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do find
        return entityManagerSession.doAction(em -> TriggerDefinitionDAO.find(em, scopeId, stepDefinitionId));
    }

    @Override
    public TriggerDefinition findByName(String name) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(name, "name");

        //
        // Do find
        return entityManagerSession.doAction(em -> {
            TriggerDefinition triggerDefinition = TriggerDefinitionDAO.findByName(em, name);
            if (triggerDefinition != null) {
                //
                // Check Access
                authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));
            }
            return triggerDefinition;
        });
    }

    @Override
    public TriggerDefinitionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.doAction(em -> TriggerDefinitionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, KapuaId.ANY));

        //
        // Do query
        return entityManagerSession.doAction(em -> TriggerDefinitionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(stepDefinitionId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> {
            if (TriggerDefinitionDAO.find(em, scopeId, stepDefinitionId) == null) {
                throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, stepDefinitionId);
            }

            return TriggerDefinitionDAO.delete(em, scopeId, stepDefinitionId);
        });

    }
}
