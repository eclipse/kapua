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
package org.eclipse.kapua.service.device.management.inventory;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.device.management.inventory.internal.DeviceInventoryManagementFactoryImpl;
import org.eclipse.kapua.service.device.management.inventory.internal.DeviceInventoryManagementServiceImpl;

public class DeviceInventoryModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceInventoryManagementService.class).to(DeviceInventoryManagementServiceImpl.class);
        bind(DeviceInventoryManagementFactory.class).to(DeviceInventoryManagementFactoryImpl.class);
    }
}
