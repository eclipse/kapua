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
package org.eclipse.kapua.commons.service.internal;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    private static Map<String, Cache> cacheMap = new HashMap<>();

    private CacheManager() {
    }

    public static Cache getCache(String cacheName) {
        //TODO check configuration to choose the proper cache type to instantiate
        Cache cache = cacheMap.get(cacheName);
        if (cache==null) {
            synchronized (cacheMap) {
                cache = cacheMap.get(cacheName);
                if (cache==null) {
                    cache = new DummyCache();
                    cacheMap.put(cacheName, cache);
                }
            }
        }
        return cache;
    }

}
