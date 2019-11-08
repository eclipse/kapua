/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
