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
package org.eclipse.kapua.commons.jpa;

import com.google.inject.Inject;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;

public class NamedEntityCacheFactory implements NamedCacheFactory {

    protected final KapuaCacheManager cacheManager;
    protected final CommonsMetric commonsMetric;

    @Inject
    public NamedEntityCacheFactory(KapuaCacheManager cacheManager, CommonsMetric commonsMetric) {
        this.cacheManager = cacheManager;
        this.commonsMetric = commonsMetric;
    }

    @Override
    public NamedEntityCache createCache(String idCacheName, String nameCacheName) {
        return new NamedEntityCache(this.cacheManager, this.commonsMetric, idCacheName, nameCacheName);
    }
}
