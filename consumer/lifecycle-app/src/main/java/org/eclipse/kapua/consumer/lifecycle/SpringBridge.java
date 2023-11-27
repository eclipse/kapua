/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.lifecycle;

import org.eclipse.kapua.commons.liquibase.DatabaseCheckUpdate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.JobDeviceManagementTriggerManagerService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.translator.TranslatorHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBridge {

    @Bean
    DatabaseCheckUpdate databaseCheckUpdate() {
        return KapuaLocator.getInstance().getComponent(DatabaseCheckUpdate.class);
    }

    @Bean
    MetricsCamel metricsCamel() {
        return KapuaLocator.getInstance().getComponent(MetricsCamel.class);
    }

    @Bean
    MetricsLifecycle metricsLifecycle() {
        return KapuaLocator.getInstance().getComponent(MetricsLifecycle.class);
    }

    @Bean
    TranslatorHub translatorHub() {
        return KapuaLocator.getInstance().getComponent(TranslatorHub.class);
    }

    @Bean
    DeviceLifeCycleService deviceLifeCycleService() {
        return KapuaLocator.getInstance().getComponent(DeviceLifeCycleService.class);
    }

    @Bean
    JobDeviceManagementTriggerManagerService jobDeviceManagementTriggerManagerService() {
        return KapuaLocator.getInstance().getComponent(JobDeviceManagementTriggerManagerService.class);
    }
}
