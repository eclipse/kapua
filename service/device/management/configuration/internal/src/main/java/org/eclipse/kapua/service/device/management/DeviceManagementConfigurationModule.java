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
package org.eclipse.kapua.service.device.management;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationFactoryImpl;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationManagementServiceImpl;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.internal.DeviceSnapshotFactoryImpl;
import org.eclipse.kapua.service.device.management.snapshot.internal.DeviceSnapshotManagementServiceImpl;

public class DeviceManagementConfigurationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceConfigurationFactory.class).to(DeviceConfigurationFactoryImpl.class);
        bind(DeviceConfigurationManagementService.class).to(DeviceConfigurationManagementServiceImpl.class);

        bind(DeviceSnapshotFactory.class).to(DeviceSnapshotFactoryImpl.class);
        bind(DeviceSnapshotManagementService.class).to(DeviceSnapshotManagementServiceImpl.class);
    }
}
