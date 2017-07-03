/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * DeviceConnectionService exposes APIs to retrieve Device connections under a scope.
 * It includes APIs to find, list, and update devices connections associated with a scope.
 * 
 * @since 1.0
 */
public interface DeviceConnectionService extends KapuaEntityService<DeviceConnection, DeviceConnectionCreator>,
        KapuaUpdatableEntityService<DeviceConnection>,
        KapuaConfigurableService {

    /**
     * Find the connection by client identifier
     * 
     * @param scopeId
     * @param clientId
     * @return
     * @throws KapuaException
     */
    public DeviceConnection findByClientId(KapuaId scopeId, String clientId)
            throws KapuaException;

    /**
     * Returns the {@link DeviceConnectionListResult} with elements matching the provided query.
     * 
     * @param query
     *            The {@link DeviceConnectionQuery} used to filter results.
     * @return The {@link DeviceConnectionListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    public DeviceConnectionListResult query(KapuaQuery<DeviceConnection> query)
            throws KapuaException;

    /**
     * Updated the status of provided device connection to connected;
     * if a device connection for the provided clientId is not found,
     * a new device connection is created and updated.
     * 
     * @param creator
     * @throws KapuaException
     */
    public void connect(DeviceConnectionCreator creator)
            throws KapuaException;

    /**
     * Register a device message when a client disconnects from the broker
     * 
     * @param scopeId
     * @param clientId
     * @throws KapuaException
     */
    public void disconnect(KapuaId scopeId, String clientId)
            throws KapuaException;
}
