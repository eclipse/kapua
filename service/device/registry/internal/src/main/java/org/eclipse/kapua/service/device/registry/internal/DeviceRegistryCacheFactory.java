/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;

/**
 * Cache factory for the {@link DeviceRegistryServiceImpl}
 */
public class DeviceRegistryCacheFactory extends AbstractEntityCacheFactory {

    public DeviceRegistryCacheFactory() {
        super("DeviceId");
    }

    /**
     * @return a {@link DeviceRegistryCache} instance.
     */
    @Override
    public EntityCache createCache() {
        return new DeviceRegistryCache(getEntityIdCacheName(), "DeviceClientId", "DeviceConnectionId");
    }

    public static DeviceRegistryCacheFactory getInstance() {
        return new DeviceRegistryCacheFactory();
    }
}
