/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others
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
package org.eclipse.kapua.broker.client.protocol;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CachingProtocolDescriptorProvider implements ProtocolDescriptorProvider {

    private final Map<String, Optional<ProtocolDescriptor>> cache = new ConcurrentHashMap<>();

    protected abstract ProtocolDescriptor lookupDescriptor(String transportName);

    @Override
    public ProtocolDescriptor getDescriptor(final String connectorName) {
        Optional<ProtocolDescriptor> result = cache.get(connectorName);

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
