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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link DeviceConnection} {@link KapuaEntityService} definition.
 *
 * @since 1.0.0
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
     * @since 1.0.0
     */
    DeviceConnection findByClientId(KapuaId scopeId, String clientId) throws KapuaException;

    /**
     * Returns the {@link DeviceConnectionListResult} with elements matching the provided query.
     *
     * @param query The {@link DeviceConnectionQuery} used to filter results.
     * @return The {@link DeviceConnectionListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    DeviceConnectionListResult query(KapuaQuery query) throws KapuaException;

    /**
     * Updated the status of provided device connection to connected;
     * if a device connection for the provided clientId is not found,
     * a new device connection is created and updated.
     *
     * @param creator The {@link DeviceConnectionCreator} from which to create the {@link DeviceConnection}.
     * @throws KapuaException In case of errors.
     * @since 1.0.0
     * @deprecated since 2.0.0. It has never been implemented.
     */
    @Deprecated
    void connect(DeviceConnectionCreator creator) throws KapuaException;

    /**
     * Register a device message when a client disconnects from the broker
     *
     * @param scopeId  The {@link DeviceConnection#getScopeId()}.
     * @param clientId TThe {@link DeviceConnection#getClientId()}.
     * @throws KapuaException In case of errors.
     * @since 1.0.0
     * @deprecated since 2.0.0. It has never been implemented.
     */
    @Deprecated
    void disconnect(KapuaId scopeId, String clientId) throws KapuaException;
}
