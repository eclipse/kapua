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

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for working with {@link ConnectorDescriptorProvider} instances
 */
public final class ConnectorDescriptorProviders {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorDescriptorProviders.class);

    private ConnectorDescriptorProviders() {
    }

    private static ConnectorDescriptorProvider provider;

    /**
     * Get a {@link ConnectorDescriptorProvider} instance
     *
     * @return An instance of {@link ConnectorDescriptorProvider}, never returns {@code null}
     */
    public static ConnectorDescriptorProvider getInstance() {
        synchronized (ConnectorDescriptorProviders.class) {

            if (provider != null) {
                return provider;
            }

            provider = locateProvider();

            return provider;
        }
    }

    /**
     * Get a {@link ConnectorDescriptor} using the default instance
     *
     * @param connectorName
     *            the connector name to lookup
     * @return The connector descriptor, may be {@code null}
     */
    public static ConnectorDescriptor getDescriptor(String connectorName) {
        return getInstance().getDescriptor(connectorName);
    }

    /**
     * Locate an instance of {@link ConnectorDescriptorProvider}
     *
     * @return An instance of {@link ConnectorDescriptorProvider}, never returns {@code null}
     */
    private static ConnectorDescriptorProvider locateProvider() {
        final ConnectorDescriptorProvider provider = locateProviderFromServices();
        if (provider != null) {
            return provider;
        }

        return new DefaultConnectorDescriptionProvider();
    }

    /**
     * Locate provider instances using {@link ServiceLoader}
     *
     * @return An instance of {@link ConnectorDescriptorProvider}, or {@code null} if none was found
     */
    private static ConnectorDescriptorProvider locateProviderFromServices() {
        ConnectorDescriptorProvider result = null;

        for (final ConnectorDescriptorProvider provider : ServiceLoader.load(ConnectorDescriptorProvider.class)) {
            if (result == null) {
                result = provider;
            } else {
                logger.warn("Multiple instances of ConnectorDescriptorProvider.class found via ServiceLoader - first: {}, additional: {}", result, provider);
            }
        }

        return result;
    }
}
