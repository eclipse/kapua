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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaTransactedRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationTransactedRepository;

import java.util.function.Supplier;

public class DeviceManagementOperationRepositoryImplJpaTransactedRepository
        extends KapuaUpdatableEntityJpaTransactedRepository<DeviceManagementOperation, DeviceManagementOperationImpl, DeviceManagementOperationListResult>
        implements DeviceManagementOperationTransactedRepository {
    public DeviceManagementOperationRepositoryImplJpaTransactedRepository(Supplier<DeviceManagementOperationListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(DeviceManagementOperationImpl.class, listSupplier, entityManagerSession);
    }

    @Override
    public DeviceManagementOperation findByOperationId(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperationQuery query = new DeviceManagementOperationQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceManagementOperationAttributes.OPERATION_ID, operationId));
        return this.query(query).getFirstItem();
    }
}
