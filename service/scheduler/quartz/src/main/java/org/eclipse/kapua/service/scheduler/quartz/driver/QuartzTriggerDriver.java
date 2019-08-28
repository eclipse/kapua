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
package org.eclipse.kapua.service.scheduler.quartz.driver;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.CannotAddQuartzJobException;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.CannotScheduleJobException;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.QuartzTriggerDriverException;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.SchedulerNotAvailableException;
import org.eclipse.kapua.service.scheduler.quartz.driver.exception.TriggerNeverFiresException;
import org.eclipse.kapua.service.scheduler.quartz.job.KapuaJobLauncher;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class to crate Quartz {@link org.quartz.Trigger}s.
 *
 * @since 1.1.0
 */
public class QuartzTriggerDriver {

    private QuartzTriggerDriver() {
    }

    /**
     * Creates a Quartz {@link org.quartz.Trigger} that starts now.
     *
     * @param scopeId        The {@link Job#getScopeId()} to start.
     * @param jobId          The {@link Job#getId()} to start
     * @param uniqueId       A unique {@link KapuaId} to associate with the Quartz {@link org.quartz.Trigger}.
     * @param triggerDataMap The {@link JobDataMap} with properties for the Quartz {@link org.quartz.Trigger}
     * @since 1.1.0
     */
    public static void createQuartzTrigger(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId uniqueId, @NotNull JobDataMap triggerDataMap) throws QuartzTriggerDriverException {

        Scheduler scheduler = getScheduler();

        JobKey jobkey = JobKey.jobKey(KapuaJobLauncher.class.getName(), "USER");
        JobDetail kapuaJobLauncherJobDetail;
        try {
            kapuaJobLauncherJobDetail = scheduler.getJobDetail(jobkey);

            if (kapuaJobLauncherJobDetail == null) {
                kapuaJobLauncherJobDetail = JobBuilder.newJob(KapuaJobLauncher.class)
                        .withIdentity(jobkey)
                        .storeDurably()
                        .build();

                scheduler.addJob(kapuaJobLauncherJobDetail, false);
            }
        } catch (SchedulerException se) {
            throw new CannotAddQuartzJobException(se, KapuaJobLauncher.class, jobkey);
        }

        // Quartz Trigger data map definition
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId.toCompactId().concat("-").concat(uniqueId.toCompactId()), scopeId.toCompactId());

        // Quartz Trigger definition
        TriggerBuilder<org.quartz.Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .forJob(kapuaJobLauncherJobDetail)
                .withIdentity(triggerKey)
                .usingJobData(triggerDataMap)
                .startNow();

        org.quartz.Trigger quarztTrigger = triggerBuilder.build();
        try {
            scheduler.scheduleJob(quarztTrigger);
        } catch (SchedulerException se) {
            throw new CannotScheduleJobException(se, kapuaJobLauncherJobDetail, triggerKey, triggerDataMap);
        }
    }

    public static void createIntervalJobTrigger(@NotNull Trigger trigger) throws KapuaIllegalNullArgumentException, QuartzTriggerDriverException {
        Integer interval = null;

        for (TriggerProperty tp : trigger.getTriggerProperties()) {
            if ("interval".equals(tp.getName())) {
                interval = (Integer) ObjectValueConverter.fromString(tp.getPropertyValue(), Integer.class);
                break;
            }
        }

        if (interval == null) {
            throw new KapuaIllegalNullArgumentException("interval");
        }

        createQuartzTriggerWithSchedule(
                trigger,
                SimpleScheduleBuilder.repeatSecondlyForever(interval)
                        .withMisfireHandlingInstructionFireNow() // This option force a misfired trigger to be always fired
        );
    }


    public static void createCronJobTrigger(@NotNull Trigger trigger) throws KapuaIllegalNullArgumentException, QuartzTriggerDriverException {
        String cron = null;

        for (TriggerProperty tp : trigger.getTriggerProperties()) {
            if ("cronExpression".equals(tp.getName())) {
                cron = (String) ObjectValueConverter.fromString(tp.getPropertyValue(), String.class);
                break;
            }
        }

        if (Strings.isNullOrEmpty(cron)) {
            throw new KapuaIllegalNullArgumentException("cronExpression");
        }

        createQuartzTriggerWithSchedule(
                trigger,
                CronScheduleBuilder.cronSchedule(cron)
                        .withMisfireHandlingInstructionFireAndProceed() // This option force a misfired trigger to be always fired
                        .inTimeZone(TimeZone.getTimeZone("UTC"))
        );
    }

    //
    // Private methods
    //

    private static void createQuartzTriggerWithSchedule(Trigger trigger, ScheduleBuilder<?> scheduleBuilder) throws QuartzTriggerDriverException {
        Scheduler scheduler = getScheduler();

        JobKey jobkey = JobKey.jobKey(KapuaJobLauncher.class.getName(), "USER");
        JobDetail kapuaJobLauncherJobDetail;
        try {
            kapuaJobLauncherJobDetail = scheduler.getJobDetail(jobkey);

            if (kapuaJobLauncherJobDetail == null) {
                kapuaJobLauncherJobDetail = JobBuilder.newJob(KapuaJobLauncher.class)
                        .withIdentity(jobkey)
                        .storeDurably()
                        .build();

                scheduler.addJob(kapuaJobLauncherJobDetail, false);
            }
        } catch (SchedulerException se) {
            throw new CannotAddQuartzJobException(se, KapuaJobLauncher.class, jobkey);
        }

        //
        // Quartz Trigger data map definition
        TriggerKey triggerKey = TriggerKey.triggerKey(trigger.getId().toCompactId(), trigger.getScopeId().toCompactId());

        JobDataMap triggerDataMap = new JobDataMap();
        try {
            for (TriggerProperty tp : trigger.getTriggerProperties()) {
                if (KapuaId.class.getName().equals(tp.getPropertyType())) {
                    triggerDataMap.put(tp.getName(), KapuaEid.parseCompactId(tp.getPropertyValue()));
                } else {
                    triggerDataMap.put(tp.getName(), ObjectValueConverter.fromString(tp.getPropertyValue(), ObjectTypeConverter.fromString(tp.getPropertyType())));
                }
            }
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }

        //
        // Quartz Trigger definition
        TriggerBuilder<org.quartz.Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .forJob(kapuaJobLauncherJobDetail)
                .withIdentity(triggerKey)
                .usingJobData(triggerDataMap)
                .startAt(trigger.getStartsOn())
                .endAt(trigger.getEndsOn());

        triggerBuilder.withSchedule(scheduleBuilder);

        org.quartz.Trigger quartzTrigger = triggerBuilder.build();

        //
        // Check that fires
        if (quartzTrigger.getFireTimeAfter(new Date()) == null) {
            throw new TriggerNeverFiresException(quartzTrigger);
        }

        //
        // Schedule trigger
        try {
            scheduler.scheduleJob(quartzTrigger);
        } catch (SchedulerException se) {
            throw new CannotScheduleJobException(se, kapuaJobLauncherJobDetail, triggerKey, triggerDataMap);
        }
    }

    private static Scheduler getScheduler() throws SchedulerNotAvailableException {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler;

        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException se) {
            throw new SchedulerNotAvailableException(se);
        }

        return scheduler;
    }
}
