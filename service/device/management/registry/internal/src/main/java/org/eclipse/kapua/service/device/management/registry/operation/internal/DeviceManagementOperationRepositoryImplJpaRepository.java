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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRepository;
import org.eclipse.kapua.storage.TxContext;

public class DeviceManagementOperationRepositoryImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<DeviceManagementOperation, DeviceManagementOperationImpl, DeviceManagementOperationListResult>
        implements DeviceManagementOperationRepository {
    public DeviceManagementOperationRepositoryImplJpaRepository() {
        super(DeviceManagementOperationImpl.class, () -> new DeviceManagementOperationListResultImpl());
    }

    @Override
    public DeviceManagementOperation findByOperationId(TxContext tx, KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperationQuery query = new DeviceManagementOperationQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceManagementOperationAttributes.OPERATION_ID, operationId));
        return this.query(tx, query).getFirstItem();
    }

    @Override
    public DeviceManagementOperation delete(TxContext tx, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // Check existence
        final DeviceManagementOperation toBeDeleted = this.find(tx, scopeId, entityId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, entityId));
        return this.delete(tx, toBeDeleted);
    }
}
