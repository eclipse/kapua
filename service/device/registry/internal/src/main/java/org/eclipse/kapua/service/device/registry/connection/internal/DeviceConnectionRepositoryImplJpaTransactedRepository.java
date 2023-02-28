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
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaTransactedRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class DeviceConnectionRepositoryImplJpaTransactedRepository
        extends KapuaUpdatableEntityJpaTransactedRepository<DeviceConnection, DeviceConnectionImpl, DeviceConnectionListResult>
        implements DeviceConnectionTransactedRepository {
    public DeviceConnectionRepositoryImplJpaTransactedRepository(TxManager txManager, Supplier<DeviceConnectionListResult> listSupplier) {
        super(txManager, DeviceConnectionImpl.class, listSupplier);
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the update method, but
    //  without TYPE)
    public DeviceConnection update(DeviceConnection updatedEntity) throws KapuaException {
        return txManager.executeWithResult(tx -> {
            DeviceConnection currentEntity = this.bareRepository.find(tx, KapuaId.ANY, updatedEntity.getId());

            //
            // Updating if not null
            if (currentEntity == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, updatedEntity.getId());
            }

            return updatableEntityBareRepository.doUpdate(tx, currentEntity, updatedEntity);
        });
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    public DeviceConnection delete(KapuaId scopeId, KapuaId deviceConnectionId) throws KapuaException {
        return txManager.executeWithResult(tx -> {
            //
            // Checking existence
            DeviceConnection entityToDelete = bareRepository.find(tx, scopeId, deviceConnectionId);

            //
            // Deleting if found
            if (entityToDelete == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
            }
            return bareRepository.doDelete(tx, entityToDelete);
        });
    }
}
