/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.configurationstore.dummy;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.configurationstore.api.ConfigurationStoreService;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;

public class ConfigurationStoreServiceDummy implements ConfigurationStoreService {

    @Override
    public DeviceConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId, String configurationComponentPid) {
        //nothing to do
        return null;
    }

    @Override
    public void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration componentConfiguration) throws KapuaException {
        //nothing to do
    }

    @Override
    public void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration) throws KapuaException {
        //nothing to do
    }

}