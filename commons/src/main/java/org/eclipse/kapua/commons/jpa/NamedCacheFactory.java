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

/**
 * Cache factory definition
 */
public interface NamedCacheFactory {

    /**
     * Creates the cache for the given service.
     *
     * @return an {@link EntityCache} instance.
     */
    NamedEntityCache createCache(String idCacheName, String nameCacheName);
}
