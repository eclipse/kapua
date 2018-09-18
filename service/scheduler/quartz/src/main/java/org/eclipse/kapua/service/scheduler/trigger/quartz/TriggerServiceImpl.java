/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.scheduler.SchedulerDomains;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.quartz.job.KapuaJobLauncer;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.util.TimeZone;

/**
 * {@link TriggerService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class TriggerServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Trigger, TriggerCreator, TriggerService, TriggerListResult, TriggerQuery, TriggerFactory>
        implements TriggerService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

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
        ArgumentValidator.notEmptyOrNull(triggerCreator.getName(), "triggerCreator.name");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.write, triggerCreator.getScopeId()));

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(triggerCreator.getScopeId());
        query.setPredicate(new AttributePredicateImpl<>(TriggerAttributes.NAME, triggerCreator.getName()));

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(triggerCreator.getName());
        }

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> {

            Trigger trigger = TriggerDAO.create(em, triggerCreator);

            // Quartz Job definition and creation
            JobKey jobkey = JobKey.jobKey(KapuaJobLauncer.class.getName(), "USER");

            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler;
            try {
                scheduler = sf.getScheduler();
            } catch (SchedulerException se) {
                se.printStackTrace();
                throw new RuntimeException(se);
            }

            JobDetail kapuaJobLauncherJobDetail;
            try {
                kapuaJobLauncherJobDetail = scheduler.getJobDetail(jobkey);

                if (kapuaJobLauncherJobDetail == null) {
                    kapuaJobLauncherJobDetail = JobBuilder.newJob(KapuaJobLauncer.class)
                            .withIdentity(jobkey)
                            .storeDurably()
                            .build();

                    scheduler.addJob(kapuaJobLauncherJobDetail, false);
                }
            } catch (SchedulerException se) {
                se.printStackTrace();
                throw new RuntimeException(se);
            }

            // Quartz Trigger data map definition
            TriggerKey triggerKey = TriggerKey.triggerKey(trigger.getId().toCompactId(), trigger.getScopeId().toCompactId());

            JobDataMap triggerDataMap = new JobDataMap();
            for (TriggerProperty tp : trigger.getTriggerProperties()) {
                triggerDataMap.put(tp.getName(), KapuaEid.parseCompactId(tp.getPropertyValue()));
            }

            // Quartz Trigger definition
            TriggerBuilder<org.quartz.Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                    .forJob(kapuaJobLauncherJobDetail)
                    .withIdentity(triggerKey)
                    .usingJobData(triggerDataMap)
                    .startAt(trigger.getStartsOn())
                    .endAt(trigger.getEndsOn());

            if (trigger.getRetryInterval() != null) {
                triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(trigger.getRetryInterval().intValue()));
            } else {
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCronScheduling()).inTimeZone(TimeZone.getTimeZone("UTC")));
            }

            org.quartz.Trigger quarztTrigger = triggerBuilder.build();
            try {
                scheduler.scheduleJob(quarztTrigger);
            } catch (SchedulerException se) {
                se.printStackTrace();
                throw new RuntimeException(se);
            }
            return trigger;
        });
    }

    @Override
    public Trigger update(Trigger trigger) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(trigger.getScopeId(), "trigger.scopeId");
        ArgumentValidator.notNull(trigger.getId(), "trigger.id");
        ArgumentValidator.notEmptyOrNull(trigger.getName(), "trigger.name");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.write, trigger.getScopeId()));

        //
        // Check existence
        if (find(trigger.getScopeId(), trigger.getId()) == null) {
            throw new KapuaEntityNotFoundException(trigger.getType(), trigger.getId());
        }

        //
        // Check duplicate name
        TriggerQuery query = new TriggerQueryImpl(trigger.getScopeId());
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(TriggerAttributes.NAME, trigger.getName()),
                        new AttributePredicateImpl<>(TriggerAttributes.ENTITY_ID, trigger.getId(), Operator.NOT_EQUAL)
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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.delete, scopeId));

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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> TriggerDAO.find(em, scopeId, triggerId));
    }

    @Override
    public TriggerListResult query(KapuaQuery<Trigger> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> TriggerDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Trigger> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(SchedulerDomains.SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> TriggerDAO.count(em, query));
    }

}
