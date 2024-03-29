/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.manager.internal;

import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.domain.DomainEntry;
import org.eclipse.kapua.service.device.management.registry.manager.DeviceManagementRegistryManagerService;

public class DeviceManagementRegistryModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceManagementRegistryManagerService.class).to(DeviceManagementRegistryManagerServiceImpl.class);
    }

    @ProvidesIntoSet
    public Domain deviceManagementRegistryDomain() {
        return new DomainEntry(Domains.DEVICE_MANAGEMENT_REGISTRY, DeviceManagementRegistryManagerService.class.getName(), false, Actions.delete, Actions.read, Actions.write);
    }

}
