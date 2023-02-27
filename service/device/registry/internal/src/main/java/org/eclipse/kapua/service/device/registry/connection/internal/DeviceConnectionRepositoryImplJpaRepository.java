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
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;

import java.util.function.Supplier;

public class DeviceConnectionRepositoryImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<DeviceConnection, DeviceConnectionImpl, DeviceConnectionListResult>
        implements DeviceConnectionRepository {
    public DeviceConnectionRepositoryImplJpaRepository(Supplier<DeviceConnectionListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(DeviceConnectionImpl.class, listSupplier, entityManagerSession);
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the update method, but
    //  without TYPE)
    public DeviceConnection update(DeviceConnection deviceConnection) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> {

            //
            // Checking existence
            DeviceConnection entityToUpdate = em.find(concreteClass, deviceConnection.getId());

            //
            // Updating if not null
            if (entityToUpdate == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnection.getId());
            }

            return doUpdate(em, entityToUpdate);
        });
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    public DeviceConnection delete(KapuaId scopeId, KapuaId deviceConnectionId) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> {
            //
            // Checking existence
            DeviceConnection entityToDelete = doFind(em, scopeId, deviceConnectionId);

            //
            // Deleting if found
            if (entityToDelete == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
            }
            return doDelete(em, entityToDelete);
        });
    }
}
