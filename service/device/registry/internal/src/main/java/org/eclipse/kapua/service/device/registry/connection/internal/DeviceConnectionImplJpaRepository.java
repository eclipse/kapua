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
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionAttributes;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class DeviceConnectionImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<DeviceConnection, DeviceConnectionImpl, DeviceConnectionListResult>
        implements DeviceConnectionRepository {

    public DeviceConnectionImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(DeviceConnectionImpl.class, () -> new DeviceConnectionListResultImpl(), jpaRepoConfig);
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the update method, but
    //  without TYPE)
    public DeviceConnection update(TxContext txContext, DeviceConnection updatedEntity) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);

        return this.doFind(em, KapuaId.ANY, updatedEntity.getId())
                // Updating if not null
                .map(current -> this.doUpdate(em, current, updatedEntity))
                .orElseThrow(() -> new KapuaEntityNotFoundException(DeviceConnection.TYPE, updatedEntity.getId()));
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    public DeviceConnection delete(TxContext txContext, KapuaId scopeId, KapuaId deviceConnectionId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        // Checking existence
        return this.doFind(em, scopeId, deviceConnectionId)
                // Deleting if found
                .map(found -> this.doDelete(em, found))
                .orElseThrow(() -> new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId));
    }

    @Override
    public long countByClientId(TxContext tx, KapuaId scopeId, String clientId) throws KapuaException {
        final DeviceConnectionQuery query = new DeviceConnectionQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceConnectionAttributes.CLIENT_ID, clientId));
        return this.count(tx, query);
    }
}
