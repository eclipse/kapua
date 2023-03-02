/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.storage.KapuaUpdatableEntityRepositoryCachingWrapper;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCache;
import org.eclipse.kapua.storage.TxContext;

public class CachingDeviceConnectionRepository
        extends KapuaUpdatableEntityRepositoryCachingWrapper<DeviceConnection, DeviceConnectionListResult>
        implements DeviceConnectionRepository {

    public CachingDeviceConnectionRepository(DeviceConnectionRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }

    @Override
    public DeviceConnection update(TxContext tx, DeviceConnection deviceConnection) throws KapuaException {
        ((DeviceRegistryCache) entityCache).removeByDeviceConnectionId(deviceConnection.getScopeId(), deviceConnection.getId());
        return super.update(tx, deviceConnection);
    }
}
