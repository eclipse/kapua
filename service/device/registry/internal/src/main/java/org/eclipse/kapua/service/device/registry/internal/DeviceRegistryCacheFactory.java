/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;

public class DeviceRegistryCacheFactory extends AbstractEntityCacheFactory {

    public DeviceRegistryCacheFactory() {
        super("DeviceId");
    }

    @Override
    public EntityCache createCache() {
        return new DeviceRegistryCache(getEntityIdCacheName(), "DeviceClientId", "DeviceConnectionId");
    }

    public static DeviceRegistryCacheFactory getInstance() {
        return new DeviceRegistryCacheFactory();
    }
}
