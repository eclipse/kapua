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

/**
 * Cache factory definition
 */
public interface CacheFactory {

    /**
     * Creates the cache for the given service.
     *
     * @return an {@link EntityCache} instance.
     */
    EntityCache createCache();
}
