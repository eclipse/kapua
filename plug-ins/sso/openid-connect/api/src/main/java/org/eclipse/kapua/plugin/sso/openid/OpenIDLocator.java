/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid;

import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;

/**
 * OpenID Connect single sign-on service locator interface.
 */
public interface OpenIDLocator {

    /**
     * Retrieve the OpenID Connect single sign-on service.
     *
     * @return a {@link OpenIDService} object.
     */
    OpenIDService getService();

    /**
     * Retrieve the JwtProcessor.
     *
     * @return a {@link JwtProcessor} object.
     * @throws OpenIDException if it fails to retrieve the {@link JwtProcessor}.
     */
    JwtProcessor getProcessor() throws OpenIDException;
}
