/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.job;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.device.management.job.internal.JobDeviceManagementOperationFactoryImpl;
import org.eclipse.kapua.service.device.management.job.internal.JobDeviceManagementOperationServiceImpl;
import org.eclipse.kapua.service.device.management.job.manager.JobDeviceManagementOperationManagerService;
import org.eclipse.kapua.service.device.management.job.manager.internal.JobDeviceManagementOperationManagerServiceImpl;
import org.eclipse.kapua.service.device.management.job.scheduler.internal.JobDeviceManagementTriggerManagerServiceImpl;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.JobDeviceManagementTriggerManagerService;

public class JobDeviceManagementModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobDeviceManagementTriggerManagerService.class).to(JobDeviceManagementTriggerManagerServiceImpl.class);

        bind(JobDeviceManagementOperationManagerService.class).to(JobDeviceManagementOperationManagerServiceImpl.class);
        bind(JobDeviceManagementOperationService.class).to(JobDeviceManagementOperationServiceImpl.class);
        bind(JobDeviceManagementOperationFactory.class).to(JobDeviceManagementOperationFactoryImpl.class);
    }
}