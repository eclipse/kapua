/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

        if (result.isPresent()) {
            // we have a cache hit, return it
            return result.get();
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
         */
        return cache.get(connectorName).orElse(null);
    }

}
