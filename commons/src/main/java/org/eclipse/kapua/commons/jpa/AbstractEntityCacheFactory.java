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

import org.eclipse.kapua.commons.service.internal.cache.EntityCache;

public abstract class AbstractEntityCacheFactory implements CacheFactory {

    private String idCacheName;

    public AbstractEntityCacheFactory(String idCacheName) {
        this.idCacheName = idCacheName;
    }

    public String getEntityIdCacheName() {
        return idCacheName;
    }

    @Override
    public EntityCache createCache() {
        return new EntityCache(getEntityIdCacheName());
    }
}
