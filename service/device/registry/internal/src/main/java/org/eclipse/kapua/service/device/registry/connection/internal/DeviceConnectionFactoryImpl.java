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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;

/**
 * Device connection service factory implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceConnectionFactoryImpl implements DeviceConnectionFactory {

    @Override
    public DeviceConnectionCreator newCreator(KapuaId scopeId) {
        return new DeviceConnectionCreatorImpl(scopeId);
    }

    @Override
    public DeviceConnectionQuery newQuery(KapuaId scopeId) {
        return new DeviceConnectionQueryImpl(scopeId);
    }

    @Override
    public DeviceConnection newEntity(KapuaId scopeId) {
        return new DeviceConnectionImpl(scopeId);
    }

    @Override
    public DeviceConnectionListResult newListResult() {
        return new DeviceConnectionListResultImpl();
    }

}
