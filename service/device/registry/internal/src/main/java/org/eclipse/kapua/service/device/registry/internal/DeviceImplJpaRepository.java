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
package org.eclipse.kapua.service.device.registry.internal;

import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceRepository;

import java.util.function.Supplier;

public class DeviceImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<Device, DeviceImpl, DeviceListResult>
        implements DeviceRepository {
    public DeviceImplJpaRepository(Supplier<DeviceListResult> listProvider, EntityManagerSession entityManagerSession) {
        super(DeviceImpl.class, listProvider, entityManagerSession);
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        DeviceQueryImpl query = new DeviceQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceAttributes.CLIENT_ID, clientId));
        query.setFetchAttributes(Lists.newArrayList(DeviceAttributes.CONNECTION, DeviceAttributes.LAST_EVENT));
        return this.query(query).getFirstItem();
    }
}
