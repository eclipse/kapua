/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * Device registry service definition.
 *
 * @since 1.0
 */
public interface DeviceRegistryService extends KapuaEntityService<Device, DeviceCreator>,
        KapuaUpdatableEntityService<Device>,
        KapuaConfigurableService {

    /**
     * Returns the {@link DeviceListResult} with elements matching the provided query.
     *
     * @param query The {@link DeviceQuery} used to filter results.
     * @return The {@link DeviceListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    DeviceListResult query(KapuaQuery query) throws KapuaException;

    /**
     * Finds a device by its unique clientId and loads it with all its properties.
     *
     * @param scopeId
     * @param clientId
     * @return
     * @throws KapuaException
     */
    Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException;
}
