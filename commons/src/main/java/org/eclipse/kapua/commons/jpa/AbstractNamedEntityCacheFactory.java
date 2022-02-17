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
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;

public abstract class AbstractNamedEntityCacheFactory extends AbstractEntityCacheFactory {

    private String nameCacheName;

    public AbstractNamedEntityCacheFactory(String idCacheName, String nameCacheName) {
        super(idCacheName);
        this.nameCacheName = nameCacheName;
    }

    public String getEntityNameCacheName() {
        return nameCacheName;
    }

    @Override
    public EntityCache createCache() {
        return new NamedEntityCache(getEntityIdCacheName(), getEntityNameCacheName());
    }
}
