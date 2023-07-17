/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;

import java.util.Map;

/**
 * Datastore cache manager.<br>
 * It keeps informations about channels, metrics and clients to speed up the store operation and avoid time consuming unnecessary operations.
 *
 * @since 1.0.0
 */
//TODO: FIXME: singletons should not be handled manually, we have DI for that
public class DatastoreCacheManager {

    private static final DatastoreCacheManager INSTANCE = new DatastoreCacheManager();

    private final LocalCache<String, Map<String, Metric>> schemaCache;
    private final LocalCache<String, Boolean> channelsCache;
    private final LocalCache<String, Boolean> metricsCache;
    private final LocalCache<String, Boolean> clientsCache;

    private DatastoreCacheManager() {
        DatastoreSettings config = DatastoreSettings.getInstance();
        int expireAfter = config.getInt(DatastoreSettingsKey.CONFIG_CACHE_LOCAL_EXPIRE_AFTER);
        int sizeMax = config.getInt(DatastoreSettingsKey.CONFIG_CACHE_LOCAL_SIZE_MAXIMUM);
        int sizeMaxMetadata = config.getInt(DatastoreSettingsKey.CONFIG_CACHE_METADATA_LOCAL_SIZE_MAXIMUM);

        // TODO set expiration to happen frequently because the reset cache method will not get
        // called from service clients any more
        channelsCache = new LocalCache<>(sizeMax, expireAfter, false);
        metricsCache = new LocalCache<>(sizeMax, expireAfter, false);
        clientsCache = new LocalCache<>(sizeMax, expireAfter, false);
        schemaCache = new LocalCache<>(sizeMaxMetadata, null);
    }

    /**
     * Get the cache manager instance
     *
     * @return
     * @since 1.0.0
     */
    public static DatastoreCacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * Get the channels informations cache
     *
     * @return
     * @since 1.0.0
     */
    public LocalCache<String, Boolean> getChannelsCache() {
        return channelsCache;
    }

    /**
     * Get the metrics informations cache
     *
     * @return
     * @since 1.0.0
     */
    public LocalCache<String, Boolean> getMetricsCache() {
        return metricsCache;
    }

    /**
     * Get the clients informations cache
     *
     * @return
     * @since 1.0.0
     */
    public LocalCache<String, Boolean> getClientsCache() {
        return clientsCache;
    }

    /**
     * Get the metadata informations cache
     *
     * @return
     * @since 1.0.0
     */
    public LocalCache<String, Map<String, Metric>> getMetadataCache() {
        return schemaCache;
    }
}
