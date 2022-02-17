/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.scheduler.SchedulerDomains;
import org.eclipse.kapua.service.scheduler.exception.TriggerInvalidDatesException;
import org.eclipse.kapua.service.scheduler.exception.TriggerInvalidSchedulingException;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerEntityManagerFactory;
import org.eclipse.kapua.service.scheduler.quartz.driver.QuartzTriggerDriver;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.TriggerNeverFiresException;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link TriggerService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class TriggerServiceImpl extends AbstractKapuaService implements TriggerService {

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
        super(SchedulerEntityManagerFactory.getInstance(), null);
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
                    ArgumentValidator.areEqual(jsp.getPropertyType(), jsdp.getPropertyType(), "triggerCreator.triggerProperties{}." + jsp.getName() + ".propertyType");
                    ArgumentValidator.notNull(jsp.getPropertyValue(), "triggerCreator.triggerProperties{}." + jsp.getName() + ".propertyValue");
                    break;
                }
            }
        }

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(triggerCreator.getScopeId());
        query.setPredicate(query.attributePredicate(TriggerAttributes.NAME, triggerCreator.getName()));

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(triggerCreator.getName());
        }

        if (triggerCreator.getEndsOn() != null) {
            Date startTime = triggerCreator.getStartsOn();
            Date endTime = triggerCreator.getEndsOn();

            if (endTime.before(new Date()) ||
                    startTime.getTime() == endTime.getTime() ||
                    startTime.after(endTime)) {
                throw new TriggerInvalidDatesException(startTime, endTime, new Date());
            }
        }

        //
        // Do create
        try {
            return entityManagerSession.doTransactedAction(em -> {

                Trigger trigger = TriggerDAO.create(em, triggerCreator);

                // Quartz Job definition and creation
                if (getIntervalJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.createIntervalJobTrigger(trigger);
                } else if (getCronJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.createCronJobTrigger(trigger);
                }
                //else {
                // Is a DeviceConnect trigger
                //}

                return trigger;
            });
        } catch (TriggerNeverFiresException tnfe) {
            TriggerProperty schedulingTriggerProperty = null;
            if (getIntervalJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                schedulingTriggerProperty = triggerCreator.getTriggerProperty("interval");
            } else if (getCronJobTriggerDefinition().getId().equals(triggerCreator.getTriggerDefinitionId())) {
                schedulingTriggerProperty = triggerCreator.getTriggerProperty("cronExpression");
            }

            throw new TriggerInvalidSchedulingException(
                    tnfe,
                    triggerCreator.getStartsOn(),
                    triggerCreator.getEndsOn(),
                    triggerCreator.getTriggerDefinitionId(),
                    schedulingTriggerProperty != null ? schedulingTriggerProperty.getPropertyValue() : null);
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
        // Check trigger definition
        TriggerDefinition triggerDefinition = triggerDefinitionService.find(trigger.getTriggerDefinitionId());
        if (triggerDefinition == null) {
            throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, trigger.getTriggerDefinitionId());
        }

        for (TriggerProperty jsp : trigger.getTriggerProperties()) {
            for (TriggerProperty jsdp : triggerDefinition.getTriggerProperties()) {
                if (jsp.getName().equals(jsdp.getName())) {
                    ArgumentValidator.areEqual(jsp.getPropertyType(), jsdp.getPropertyType(), "trigger.triggerProperties[]." + jsp.getName());
                    ArgumentValidator.notNull(jsp.getPropertyType(), "trigger.triggerProperties{}." + jsp.getName());
                    break;
                }
            }
        }

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(trigger.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(TriggerAttributes.NAME, trigger.getName()),
                        query.attributePredicate(TriggerAttributes.ENTITY_ID, trigger.getId(), AttributePredicate.Operator.NOT_EQUAL)
                )
        );

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(trigger.getName());
        }

        if (trigger.getEndsOn() != null) {
            Date startTime = new Date(trigger.getStartsOn().getTime());
            Date endTime = new Date(trigger.getEndsOn().getTime());

            if (endTime.before(new Date()) ||
                    startTime.getTime() == (endTime.getTime()) ||
                    startTime.after(endTime)) {
                throw new TriggerInvalidDatesException(startTime, endTime, new Date());
            }
        }

        //
        // Do update
        try {
            return entityManagerSession.doTransactedAction(em -> {
                Trigger updatedTrigger = TriggerDAO.update(em, trigger);

                // Quartz Job definition and creation
                if (getIntervalJobTriggerDefinition().getId().equals(updatedTrigger.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.deleteTrigger(updatedTrigger);
                    QuartzTriggerDriver.createIntervalJobTrigger(trigger);
                } else if (getCronJobTriggerDefinition().getId().equals(updatedTrigger.getTriggerDefinitionId())) {
                    QuartzTriggerDriver.deleteTrigger(updatedTrigger);
                    QuartzTriggerDriver.createCronJobTrigger(trigger);
                }
                //else {
                // Is a DeviceConnect trigger
                //}

                return updatedTrigger;
            });
        } catch (TriggerNeverFiresException tnfe) {
            TriggerProperty schedulingTriggerProperty = null;
            if (getIntervalJobTriggerDefinition().getId().equals(trigger.getTriggerDefinitionId())) {
                schedulingTriggerProperty = trigger.getTriggerProperty("interval");
            } else if (getCronJobTriggerDefinition().getId().equals(trigger.getTriggerDefinitionId())) {
                schedulingTriggerProperty = trigger.getTriggerProperty("cronExpression");
            }

            throw new TriggerInvalidSchedulingException(
                    tnfe,
                    trigger.getStartsOn(),
                    trigger.getEndsOn(),
                    trigger.getTriggerDefinitionId(),
                    schedulingTriggerProperty != null ? schedulingTriggerProperty.getPropertyValue() : null
            );
        }
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
        entityManagerSession.doTransactedAction(em -> {
            Trigger trigger = TriggerDAO.delete(em, scopeId, triggerId);

            QuartzTriggerDriver.deleteTrigger(trigger);

            return trigger;
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
        Trigger trigger = entityManagerSession.doAction(em -> TriggerDAO.find(em, scopeId, triggerId));

        if (trigger != null) {
            adaptTrigger(trigger);
        }

        return trigger;
    }

    @Override
    public TriggerListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        TriggerListResult triggers = entityManagerSession.doAction(em -> TriggerDAO.query(em, query));

        for (Trigger trigger : triggers.getItems()) {
            adaptTrigger(trigger);
        }

        return triggers;
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(em -> TriggerDAO.count(em, query));
    }

    //
    // Private methods
    //

    /**
     * Gets the {@link TriggerDefinition} named 'Interval Job'
     *
     * @return he {@link TriggerDefinition} named 'Interval Job'
     * @throws KapuaException In case is not found.
     * @since 1.1.0
     */
    private TriggerDefinition getIntervalJobTriggerDefinition() throws KapuaException {
        if (intervalJobTriggerDefinition == null) {
            intervalJobTriggerDefinition = getTriggerDefinition("Interval Job");
        }

        return intervalJobTriggerDefinition;
    }

    /**
     * Gets the {@link TriggerDefinition} named 'Cron Job'
     *
     * @return he {@link TriggerDefinition} named 'Cron Job'
     * @throws KapuaException In case is not found.
     * @since 1.1.0
     */
    private TriggerDefinition getCronJobTriggerDefinition() throws KapuaException {
        if (cronJobTriggerDefinition == null) {
            cronJobTriggerDefinition = getTriggerDefinition("Cron Job");
        }

        return cronJobTriggerDefinition;
    }

    /**
     * Gets the {@link TriggerDefinition} by the given name.
     *
     * @param triggerDefinitionName The {@link TriggerDefinition#getName()} to look for.
     * @return The {@link TriggerDefinition} by the given name.
     * @throws KapuaException In case nothing is found.
     * @since 1.1.0
     */
    private synchronized TriggerDefinition getTriggerDefinition(String triggerDefinitionName) throws KapuaException {
        TriggerDefinition triggerDefinition = triggerDefinitionService.findByName(triggerDefinitionName);

        if (triggerDefinition == null) {
            throw new KapuaEntityNotFoundException(TriggerDefinition.TYPE, triggerDefinitionName);
        }

        return triggerDefinition;
    }

    /**
     * Adapts {@link TriggerCreator#getRetryInterval()} and {@link TriggerCreator#getCronScheduling()}  to the new model
     * which make use of {@link TriggerDefinition}s
     *
     * @param triggerCreator The {@link TriggerCreator} to adapt.
     * @throws KapuaException In case that {@link TriggerDefinition} is not found.
     * @since 1.1.0
     */
    private void adaptTriggerCreator(TriggerCreator triggerCreator) throws KapuaException {
        if (triggerCreator.getRetryInterval() != null) {
            triggerCreator.setTriggerDefinitionId(getIntervalJobTriggerDefinition().getId());
            triggerCreator.getTriggerProperties().add(triggerDefinitionFactory.newTriggerProperty("interval", Integer.class.getName(), triggerCreator.getRetryInterval().toString()));
        } else if (triggerCreator.getCronScheduling() != null) {
            triggerCreator.setTriggerDefinitionId(getCronJobTriggerDefinition().getId());
            triggerCreator.getTriggerProperties().add(triggerDefinitionFactory.newTriggerProperty("cronExpression", String.class.getName(), triggerCreator.getCronScheduling()));
        }
    }

    /**
     * Adapts {@link Trigger#getRetryInterval()} and {@link Trigger#getCronScheduling()} to the new model
     * which make use of {@link TriggerDefinition}s
     *
     * @param trigger The {@link Trigger} to adapt.
     * @throws KapuaException In case that {@link TriggerDefinition} is not found.
     * @since 1.1.0
     */
    private void adaptTrigger(@NotNull Trigger trigger) throws KapuaException {
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
                entityManagerSession.doTransactedAction(em -> TriggerDAO.update(em, trigger));
            } catch (Exception e) {
                LOG.warn("Cannot convert Trigger to new format!", e);
            }
        }
    }
}
