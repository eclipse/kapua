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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.storage.TxContext;

public class DeviceConnectionImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<DeviceConnection, DeviceConnectionImpl, DeviceConnectionListResult>
        implements DeviceConnectionRepository {

    public DeviceConnectionImplJpaRepository() {
        super(DeviceConnectionImpl.class, () -> new DeviceConnectionListResultImpl());
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the update method, but
    //  without TYPE)
    public DeviceConnection update(TxContext tx, DeviceConnection updatedEntity) throws KapuaException {
        DeviceConnection currentEntity = this.find(tx, KapuaId.ANY, updatedEntity.getId());

        //
        // Updating if not null
        if (currentEntity == null) {
            throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, updatedEntity.getId());
        }

        return this.update(tx, currentEntity, updatedEntity);
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    public DeviceConnection delete(TxContext tx, KapuaId scopeId, KapuaId deviceConnectionId) throws KapuaException {
        //
        // Checking existence
        DeviceConnection entityToDelete = this.find(tx, scopeId, deviceConnectionId);

        //
        // Deleting if found
        if (entityToDelete == null) {
            throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
        }
        return this.delete(tx, entityToDelete);
    }
}
