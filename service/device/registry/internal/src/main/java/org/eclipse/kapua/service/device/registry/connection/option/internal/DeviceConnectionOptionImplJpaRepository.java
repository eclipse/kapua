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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionRepository;

import java.util.function.Supplier;

public class DeviceConnectionOptionImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<DeviceConnectionOption, DeviceConnectionOptionImpl, DeviceConnectionOptionListResult>
        implements DeviceConnectionOptionRepository {
    public DeviceConnectionOptionImplJpaRepository(Supplier<DeviceConnectionOptionListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(DeviceConnectionOptionImpl.class, listSupplier, entityManagerSession);
    }
}
