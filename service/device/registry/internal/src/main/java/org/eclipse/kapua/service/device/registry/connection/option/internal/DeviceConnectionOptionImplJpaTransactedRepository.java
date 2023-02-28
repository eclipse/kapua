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
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaTransactedRepository;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionTransactedRepository;

import java.util.function.Supplier;

public class DeviceConnectionOptionImplJpaTransactedRepository
        extends KapuaUpdatableEntityJpaTransactedRepository<DeviceConnectionOption, DeviceConnectionOptionImpl, DeviceConnectionOptionListResult>
        implements DeviceConnectionOptionTransactedRepository {
    public DeviceConnectionOptionImplJpaTransactedRepository(Supplier<DeviceConnectionOptionListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(DeviceConnectionOptionImpl.class, listSupplier, entityManagerSession);
    }
}
