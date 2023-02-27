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
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;

import java.util.function.Supplier;

public class DeviceEventImplJpaRepository
        extends KapuaEntityRepositoryJpaImpl<DeviceEvent, DeviceEventImpl, DeviceEventListResult>
        implements DeviceEventRepository {
    public DeviceEventImplJpaRepository(Supplier<DeviceEventListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(DeviceEventImpl.class, listSupplier, entityManagerSession);
    }

    @Override
    public DeviceEvent delete(KapuaId scopeId, KapuaId deviceEventId) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> {
            final DeviceEvent toDelete = doFind(em, scopeId, deviceEventId);
            if (toDelete == null) {
                throw new KapuaEntityNotFoundException(DeviceEvent.TYPE, deviceEventId);
            }

            return doDelete(em, toDelete);
        });
    }
}
