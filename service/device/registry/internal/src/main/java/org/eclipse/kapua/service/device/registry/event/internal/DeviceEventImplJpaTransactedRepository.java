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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaTransactedRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class DeviceEventImplJpaTransactedRepository
        extends KapuaEntityJpaTransactedRepository<DeviceEvent, DeviceEventImpl, DeviceEventListResult>
        implements DeviceEventTransactedRepository {
    public DeviceEventImplJpaTransactedRepository(TxManager txManager, Supplier<DeviceEventListResult> listSupplier) {
        super(txManager, DeviceEventImpl.class, listSupplier);
    }

    @Override
    public DeviceEvent delete(KapuaId scopeId, KapuaId deviceEventId) throws KapuaException {
        return txManager.executeWithResult(tx -> {
            final DeviceEvent toDelete = bareRepository.find(tx, scopeId, deviceEventId);
            if (toDelete == null) {
                throw new KapuaEntityNotFoundException(DeviceEvent.TYPE, deviceEventId);
            }

            return bareRepository.doDelete(tx, toDelete);
        });
    }
}
