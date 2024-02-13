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

import com.google.inject.Inject;
import org.eclipse.kapua.commons.jpa.EntityCacheFactory;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;

/**
 * Cache factory for the {@link DeviceRegistryServiceImpl}
 */
public class DeviceRegistryCacheFactory extends EntityCacheFactory {

    @Inject
    public DeviceRegistryCacheFactory(KapuaCacheManager cacheManager, CommonsMetric commonsMetric) {
        super(cacheManager, commonsMetric);
    }

    /**
     * @return a {@link DeviceRegistryCache} instance.
     */
    public DeviceRegistryCache createCache() {
        return new DeviceRegistryCache(this.cacheManager, this.commonsMetric, "DeviceId", "DeviceClientId", "DeviceConnectionId");
    }
}
