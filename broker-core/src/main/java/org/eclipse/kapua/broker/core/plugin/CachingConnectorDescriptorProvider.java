/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CachingConnectorDescriptorProvider implements ConnectorDescriptorProvider {

    private final Map<String, Optional<ConnectorDescriptor>> cache = new ConcurrentHashMap<>();

    protected abstract ConnectorDescriptor lookupDescriptor(String transportName);

    @Override
    public ConnectorDescriptor getDescriptor(final String connectorName) {
        Optional<ConnectorDescriptor> result = cache.get(connectorName);

        if (result != null) {
            // we have a cache hit, return it
            return result.orElse(null);
        }

        result = Optional.ofNullable(lookupDescriptor(connectorName));
        /*
         * Put result into cache if it is not already there. Since we
         * not synchronized it might be that someone else did put
         * something into the cache in the meantime.
         */
        cache.putIfAbsent(connectorName, result);

        /*
         * We are returning the value from the cache instead of
         * our local value, since it may be that someone else did
         * populate our cache in the meantime. This way we are
         * always returning the same value, from the cache.
         *
         * Using the default value (empty()) should not be necessary,
         * but then you never know.
         */
        return cache.getOrDefault(connectorName, Optional.empty()).orElse(null);
    }

}
