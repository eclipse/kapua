/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEndBeforeStartTimeException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.scheduler.SchedulerDomains;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerEntityManagerFactory;
import org.eclipse.kapua.service.scheduler.quartz.driver.QuartzTriggerDriver;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.TriggerNeverFiresException;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionAttributes;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TriggerService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class TriggerServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Trigger, TriggerCreator, TriggerService, TriggerListResult, TriggerQuery, TriggerFactory> implements TriggerService {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerServiceImpl.class);

    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    @Inject
    private TriggerDefinitionService triggerDefinitionService;
    @Inject
    private TriggerDefinitionFactory triggerDefinitionFactory;

    private static TriggerDefinition intervalJobTriggerDefinition;
    private static TriggerDefinition cronJobTriggerDefinition;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public TriggerServiceImpl() {
        super(TriggerService.class.getName(), SchedulerDomains.SCHEDULER_DOMAIN, SchedulerEntityManagerFactory.getInstance(), TriggerService.class, TriggerFactory.class);
    }

    @Override
    public Trigger create(TriggerCreator triggerCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(triggerCreator, "triggerCreator");
        ArgumentValidator.notNull(triggerCreator.getScopeId(), "triggerCreator.scopeId");
        ArgumentValidator.validateEntityName(triggerCreator.getName(), "triggerCreator.name");
        ArgumentValidator.notNull(triggerCreator.getStartsOn(), "triggerCreator.startsOn");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.write, triggerCreator.getScopeId()));

        //
        // Convert creator to new model.
        // To be removed after removing of TriggerCreator.cronScheduling and TriggerCreator.retryInterval
        adaptTriggerCreator(triggerCreator);

        //
        // Check trigger definition
        TriggerDefinition triggerDefinition = triggerDefinitionService.find(triggerCreator.getTriggerDefinitionId());
        if (triggerDefinition == null) {
            throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, triggerCreator.getTriggerDefinitionId());
        }

        for (TriggerProperty jsp : triggerCreator.getTriggerProperties()) {
            for (TriggerProperty jsdp : triggerDefinition.getTriggerProperties()) {
                if (jsp.getName().equals(jsdp.getName())) {
                    ArgumentValidator.areEqual(jsp.getPropertyType(), jsdp.getPropertyType(), "triggerCreator.triggerProperties{}." + jsp.getName());
                    break;
                }
            }
        }

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(triggerCreator.getScopeId());
        query.setPredicate(query.attributePredicate(TriggerAttributes.NAME, triggerCreator.getName()));

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException();
        }

        if (triggerCreator.getStartsOn().equals(triggerCreator.getEndsOn()) && triggerCreator.getStartsOn().getTime() == (triggerCreator.getEndsOn().getTime())) {
            throw new KapuaException(KapuaErrorCodes.SAME_START_AND_DATE);
        }

        if (triggerCreator.getEndsOn() != null) {
            Date startTime = new Date(triggerCreator.getStartsOn().getTime());
            Date endTime = new Date(triggerCreator.getEndsOn().getTime());
            if (startTime.after(endTime)) {
                throw new KapuaEndBeforeStartTimeException();
            }
        }

        //
        // Do create
        try {
            return entityManagerSession.onTransactedInsert(em -> {

                Trigger trigger = TriggerDAO.create(em, triggerCreator);

                // Quartz Job definition and creation
                if (getIntervalJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.createIntervalJobTrigger(trigger);
                } else if (getCronJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.createCronJobTrigger(trigger);
                }

                return trigger;
            });
        } catch (TriggerNeverFiresException tnfe) {
            throw new KapuaException(KapuaErrorCodes.TRIGGER_NEVER_FIRE, tnfe);
        }
    }

    @Override
    public Trigger update(Trigger trigger) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(trigger.getScopeId(), "trigger.scopeId");
        ArgumentValidator.notNull(trigger.getId(), "trigger.id");
        ArgumentValidator.validateEntityName(trigger.getName(), "trigger.name");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.write, trigger.getScopeId()));

        //
        // Check existence
        if (find(trigger.getScopeId(), trigger.getId()) == null) {
            throw new KapuaEntityNotFoundException(trigger.getType(), trigger.getId());
        }

        adaptTrigger(trigger);

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(trigger.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(TriggerAttributes.NAME, trigger.getName()),
                        query.attributePredicate(TriggerAttributes.ENTITY_ID, trigger.getId(), Operator.NOT_EQUAL)
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(trigger.getName());
        }

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> TriggerDAO.update(em, trigger));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId triggerId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(triggerId, "scopeId");
        ArgumentValidator.notNull(scopeId, "triggerId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, triggerId) == null) {
            throw new KapuaEntityNotFoundException(Trigger.TYPE, triggerId);
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            TriggerDAO.delete(em, scopeId, triggerId);

            try {
                SchedulerFactory sf = new StdSchedulerFactory();
                Scheduler scheduler = sf.getScheduler();

                TriggerKey triggerKey = TriggerKey.triggerKey(triggerId.toCompactId(), scopeId.toCompactId());

                scheduler.unscheduleJob(triggerKey);

            } catch (SchedulerException se) {
                throw new RuntimeException(se);
            }
        });
    }

    @Override
    public Trigger find(KapuaId scopeId, KapuaId triggerId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(triggerId, "triggerId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        Trigger trigger = entityManagerSession.onResult(em -> TriggerDAO.find(em, scopeId, triggerId));
        adaptTrigger(trigger);
        return trigger;
    }

    @Override
    public TriggerListResult query(KapuaQuery<Trigger> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        TriggerListResult triggers = entityManagerSession.onResult(em -> TriggerDAO.query(em, query));

        for (Trigger trigger : triggers.getItems()) {
            adaptTrigger(trigger);
        }

        return triggers;
    }

    @Override
    public long count(KapuaQuery<Trigger> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> TriggerDAO.count(em, query));
    }

    //
    // Private methods
    //
    private TriggerDefinition getIntervalJobTriggerDefinition() throws KapuaException {
        if (intervalJobTriggerDefinition == null) {
            intervalJobTriggerDefinition = getTriggerDefinition("Interval Job");
        }

        return intervalJobTriggerDefinition;
    }

    private TriggerDefinition getCronJobTriggerDefinition() throws KapuaException {
        if (cronJobTriggerDefinition == null) {
            cronJobTriggerDefinition = getTriggerDefinition("Cron Job");

        }

        return cronJobTriggerDefinition;
    }

    private synchronized TriggerDefinition getTriggerDefinition(String triggerDefinitionName) throws KapuaException {
        TriggerDefinitionQuery query = triggerDefinitionFactory.newQuery(null);
        query.setPredicate(query.attributePredicate(TriggerDefinitionAttributes.NAME, triggerDefinitionName));

        TriggerDefinitionListResult triggerDefinitions = KapuaSecurityUtils.doPrivileged(() -> triggerDefinitionService.query(query));

        if (triggerDefinitions.isEmpty()) {
            throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, triggerDefinitionName);
        }

        return triggerDefinitions.getFirstItem();
    }

    private void adaptTriggerCreator(TriggerCreator triggerCreator) throws KapuaException {
        if (triggerCreator.getRetryInterval() != null) {
            triggerCreator.setTriggerDefinitionId(getIntervalJobTriggerDefinition().getId());
            triggerCreator.getTriggerProperties().add(triggerDefinitionFactory.newTriggerProperty("interval", Integer.class.getName(), triggerCreator.getRetryInterval().toString()));
        } else if (triggerCreator.getCronScheduling() != null) {
            triggerCreator.setTriggerDefinitionId(getCronJobTriggerDefinition().getId());
            triggerCreator.getTriggerProperties().add(triggerDefinitionFactory.newTriggerProperty("cronExpression", String.class.getName(), triggerCreator.getCronScheduling()));
        }
    }

    private void adaptTrigger(Trigger trigger) throws KapuaException {
        boolean converted = false;
        if (trigger.getRetryInterval() != null) {
            trigger.setTriggerDefinitionId(getIntervalJobTriggerDefinition().getId());

            List<TriggerProperty> triggerProperties = new ArrayList<>(trigger.getTriggerProperties());
            triggerProperties.add(triggerDefinitionFactory.newTriggerProperty("interval", Integer.class.getName(), trigger.getRetryInterval().toString()));
            trigger.setTriggerProperties(triggerProperties);

            trigger.setRetryInterval(null);

            converted = true;

        } else if (trigger.getCronScheduling() != null) {
            trigger.setTriggerDefinitionId(getCronJobTriggerDefinition().getId());

            List<TriggerProperty> triggerProperties = new ArrayList<>(trigger.getTriggerProperties());
            triggerProperties.add(triggerDefinitionFactory.newTriggerProperty("cronExpression", String.class.getName(), trigger.getCronScheduling()));
            trigger.setTriggerProperties(triggerProperties);

            trigger.setCronScheduling(null);

            converted = true;
        }

        if (converted) {
            try {
                entityManagerSession.onTransactedResult(em -> TriggerDAO.update(em, trigger));
            } catch (Exception e) {
                LOG.warn("Cannot convert Trigger to new format!", e);
            }
        }
    }
}
