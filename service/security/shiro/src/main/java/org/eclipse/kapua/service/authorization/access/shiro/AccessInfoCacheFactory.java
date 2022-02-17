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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;

/**
 * Cache factory for the {@link AccessInfoImpl}
 */
public class AccessInfoCacheFactory extends AbstractEntityCacheFactory {

    public AccessInfoCacheFactory() {
        super("AccessInfoId");
    }

    /**
     * @return an {@link AccessInfoCache}
     */
    @Override
    public EntityCache createCache() {
        return new AccessInfoCache(getEntityIdCacheName(), "AccessInfoUserIdId");
    }

    protected static AccessInfoCacheFactory getInstance() {
        return new AccessInfoCacheFactory();
    }
}
