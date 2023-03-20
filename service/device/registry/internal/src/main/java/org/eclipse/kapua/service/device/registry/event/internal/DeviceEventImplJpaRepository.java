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
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;
import org.eclipse.kapua.storage.TxContext;

public class DeviceEventImplJpaRepository
        extends KapuaEntityJpaRepository<DeviceEvent, DeviceEventImpl, DeviceEventListResult>
        implements DeviceEventRepository {
    public DeviceEventImplJpaRepository() {
        super(DeviceEventImpl.class, () -> new DeviceEventListResultImpl());
    }

    @Override
    public DeviceEvent delete(TxContext tx, KapuaId scopeId, KapuaId deviceEventId) throws KapuaException {
        final DeviceEvent toDelete = this.find(tx, scopeId, deviceEventId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(DeviceEvent.TYPE, deviceEventId));

        return this.delete(tx, toDelete);
    }
}
