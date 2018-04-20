/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionCreator;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionQuery;

/**
 * Device connection service factory implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceConnectionOptionFactoryImpl implements DeviceConnectionOptionFactory {

    @Override
    public DeviceConnectionOptionCreator newCreator(KapuaId scopeId) {
        return new DeviceConnectionOptionCreatorImpl(scopeId);
    }

    @Override
    public DeviceConnectionOptionQuery newQuery(KapuaId scopeId) {
        return new DeviceConnectionOptionQueryImpl(scopeId);
    }

    @Override
    public DeviceConnectionOption newEntity(KapuaId scopeId) {
        return new DeviceConnectionOptionImpl(scopeId);
    }

    @Override
    public DeviceConnectionOptionListResult newListResult() {
        return new DeviceConnectionOptionListResultImpl();
    }

}
