/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

public interface DeviceManagementOperationRegistryService extends KapuaEntityService<DeviceManagementOperation, DeviceManagementOperationCreator>, KapuaUpdatableEntityService<DeviceManagementOperation> {

    /**
     * Gets a {@link DeviceManagementOperation} by its {@link DeviceManagementOperation#getOperationId()}
     *
     * @param scopeId     The {@link DeviceManagementOperation#getScopeId()}
     * @param operationId The {@link DeviceManagementOperation#getOperationId()}.
     * @return The {@link DeviceManagementOperation} found, or {@code null}
     * @throws KapuaException
     * @since 1.2.0
     */
    DeviceManagementOperation findByOperationId(KapuaId scopeId, KapuaId operationId) throws KapuaException;

    /**
     * Returns the {@link DeviceManagementOperationListResult} with elements matching the provided query.
     *
     * @param query The {@link DeviceManagementOperationQuery} used to filter results.
     * @return The {@link DeviceManagementOperationListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    DeviceManagementOperationListResult query(KapuaQuery query) throws KapuaException;

}
