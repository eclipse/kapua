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
