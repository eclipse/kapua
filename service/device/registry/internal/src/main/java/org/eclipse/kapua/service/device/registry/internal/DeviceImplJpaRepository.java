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

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdateableEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRepository;

import java.util.function.Supplier;

public class DeviceImplJpaRepository
        extends KapuaUpdateableEntityRepositoryJpaImpl<Device, DeviceImpl>
        implements DeviceRepository {
    public DeviceImplJpaRepository(Supplier<? extends KapuaListResult<Device>> listProvider, EntityManagerSession entityManagerSession) {
        super(DeviceImpl.class, listProvider, entityManagerSession);
    }
}
