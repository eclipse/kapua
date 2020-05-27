/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider;

import org.eclipse.kapua.sso.SingleSignOnLocator;

/**
 * The SingleSignOn service provider interface.
 */
public interface SingleSignOnProvider {

    /**
     * Get the provider ID
     *
     * @return the provider ID in the form of a string.
     */
    String getId();

    /**
     * Call the provider locator constructor.
     *
     * @return a {@link ProviderLocator}.
     */
    ProviderLocator createLocator();

    /**
     * The ProviderLocator interface
     */
    interface ProviderLocator extends SingleSignOnLocator, AutoCloseable {
    }
}
