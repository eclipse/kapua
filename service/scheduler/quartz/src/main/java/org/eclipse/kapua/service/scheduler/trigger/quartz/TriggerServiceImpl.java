/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import javax.inject.Inject;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
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
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * {@link TriggerService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class TriggerServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Trigger, TriggerCreator, TriggerService, TriggerListResult, TriggerQuery, TriggerFactory>
        implements TriggerService {

    private static final Domain SCHEDULER_DOMAIN = new SchedulerDomain();

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    /**
     * Constructor.
     * 
     * @since 1.0.0
     */
    public TriggerServiceImpl() {
        super(TriggerService.class.getName(), SCHEDULER_DOMAIN, SchedulerEntityManagerFactory.getInstance(), TriggerService.class, TriggerFactory.class);
    }

    @Override
    public Trigger create(TriggerCreator triggerCreator)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(triggerCreator, "triggerCreator");
        ArgumentValidator.notNull(triggerCreator.getScopeId(), "triggerCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(triggerCreator.getName(), "triggerCreator.name");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, Actions.write, triggerCreator.getScopeId()));

        return entityManagerSession.onTransactedInsert(em -> {
            Trigger trigger = TriggerDAO.create(em, triggerCreator);

            JobDataMap triggerDataMap = new JobDataMap();
            for (TriggerProperty tp : trigger.getTriggerProperties()) {
                triggerDataMap.put(tp.getName(), KapuaEid.parseCompactId(tp.getPropertyValue()));
            }

            JobDetail jobLauncherDetail = JobBuilder.newJob(KapuaJobLauncer.class)
                    .withIdentity(JobKey.createUniqueName(KapuaJobLauncer.class.getName()), KapuaJobLauncer.class.getName())
                    .build();

            org.quartz.Trigger quarztTrigger = null;
            if (trigger.getRetryInterval() != null) {
                quarztTrigger = TriggerBuilder.newTrigger()
                        .usingJobData(triggerDataMap)
                        .startAt(trigger.getStartsOn())
                        .endAt(trigger.getEndsOn())
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(trigger.getRetryInterval().intValue()))
                        .build();
            } else {
                quarztTrigger = TriggerBuilder.newTrigger()
                        .usingJobData(triggerDataMap)
                        .startAt(trigger.getStartsOn())
                        .endAt(trigger.getEndsOn())
                        .withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCronScheduling()))
                        .build();
            }

            SchedulerFactory sf = new StdSchedulerFactory();
            try {
                sf.getScheduler().scheduleJob(jobLauncherDetail, quarztTrigger);
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return trigger;
        });
    }

    @Override
    public Trigger update(Trigger trigger)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(trigger.getId(), "trigger.id");
        ArgumentValidator.notEmptyOrNull(trigger.getName(), "trigger.name");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, Actions.write, trigger.getScopeId()));

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {
            return TriggerDAO.update(em, trigger);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId triggerId)
            throws KapuaException {

        //
        // Validation of the fields
        ArgumentValidator.notNull(triggerId, "scopeId");
        ArgumentValidator.notNull(scopeId, "triggerId");

        //
        // Check Access
        Actions action = Actions.write;
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, action, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            // Entity needs to be loaded in the context of the same EntityManger to be able to delete it afterwards
            Trigger triggerx = TriggerDAO.find(em, triggerId);
            if (triggerx == null) {
                throw new KapuaEntityNotFoundException(Trigger.TYPE, triggerId);
            }

            // do not allow deletion of the kapua admin trigger
            SystemSetting settings = SystemSetting.getInstance();
            if (settings.getString(SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME).equals(triggerx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            if (settings.getString(SystemSettingKey.SYS_ADMIN_ACCOUNT).equals(triggerx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            TriggerDAO.delete(em, triggerId);
        });
    }

    @Override
    public Trigger find(KapuaId scopeId, KapuaId triggerId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(triggerId, "triggerId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, Actions.read, scopeId));

        //
        // Make sure trigger exists
        return entityManagerSession.onResult(em -> TriggerDAO.find(em, triggerId));
    }

    @Override
    public TriggerListResult query(KapuaQuery<Trigger> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> TriggerDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Trigger> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(SCHEDULER_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> TriggerDAO.count(em, query));
    }
}
