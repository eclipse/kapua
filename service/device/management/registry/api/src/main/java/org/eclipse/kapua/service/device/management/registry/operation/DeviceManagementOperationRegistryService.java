/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

public interface DeviceManagementOperationRegistryService
        extends
        KapuaEntityService<DeviceManagementOperation, DeviceManagementOperationCreator>,
        KapuaUpdatableEntityService<DeviceManagementOperation> {

    /**
     * Returns the {@link DeviceManagementOperationListResult} with elements matching the provided query.
     *
     * @param query The {@link DeviceManagementOperationQuery} used to filter results.
     * @return The {@link DeviceManagementOperationListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    DeviceManagementOperationListResult query(KapuaQuery<DeviceManagementOperation> query)
            throws KapuaException;

}
