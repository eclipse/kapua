/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;

public class DatastoreCacheManager
{
    private static DatastoreCacheManager instance = new DatastoreCacheManager();
    
    private final LocalCache<String, Boolean> topicsCache;
    private final LocalCache<String, Boolean> metricsCache;
    private final LocalCache<String, Boolean> assetsCache;

    private DatastoreCacheManager()
    {
        DatastoreSettings config = DatastoreSettings.getInstance();
        int expireAfter = config.getInt(DatastoreSettingKey.CONFIG_CACHE_LOCAL_EXPIRE_AFTER);
        int sizeMax = config.getInt(DatastoreSettingKey.CONFIG_CACHE_LOCAL_SIZE_MAXIMUM);

        // TODO set expiration to happen frequently because the reset cache method will not get
        // called from service clients any more
        // TODO wrap the caches into a Statically accessible method
        topicsCache = new LocalCache<String, Boolean>(sizeMax, expireAfter, false);
        metricsCache = new LocalCache<String, Boolean>(sizeMax, expireAfter, false);
        assetsCache = new LocalCache<String, Boolean>(sizeMax, expireAfter, false);
    }

    public static DatastoreCacheManager getInstance()
    {
        return instance;
    }
    
    public LocalCache<String, Boolean> getTopicsCache(){
       return topicsCache;
    }
    
    public LocalCache<String, Boolean> getMetricsCache(){
       return metricsCache;
    }
    
    public LocalCache<String, Boolean> getAssetsCache(){
       return assetsCache;
    }
}
