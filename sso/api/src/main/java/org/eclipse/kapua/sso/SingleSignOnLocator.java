/*******************************************************************************
 * Copyright (c) 2017, 2019 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.sso;

import org.eclipse.kapua.sso.exception.SsoJwtException;

// TODO: currently two naming conventions are used (SingleSignOn and SSO) with the same meaning, adopt only one

/**
 * SingleSignOn service locator interface.
 */
public interface SingleSignOnLocator {

    /**
     * Retrieve the SSO service.
     *
     * @return a {@link SingleSignOnService} object.
     */
    SingleSignOnService getService();

    /**
     * Retrieve the JwtProcessor.
     *
     * @return a {@link JwtProcessor} object.
     * @throws SsoJwtException if it fails to retrieve the {@link JwtProcessor}.
     */
    JwtProcessor getProcessor() throws SsoJwtException;
}
