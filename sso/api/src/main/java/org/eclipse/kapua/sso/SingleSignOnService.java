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
 *******************************************************************************/
package org.eclipse.kapua.sso;

import org.eclipse.kapua.sso.exception.SsoJwtException;

import javax.json.JsonObject;
import java.net.URI;

/**
 * SingleSignOn service interface.
 */
public interface SingleSignOnService {

    /**
     * Check if the service is enabled.
     *
     * @return <tt>true</tt> if the service is enabled, <tt>false</tt> otherwise.
     */
    boolean isEnabled();

    /**
     * Get the login URI.
     *
     * @param state the state parameter used by OpenID to maintain state between the request and the callback.
     * @param redirectUri a URI object representing the redirect URI.
     * @return the login URI in the form of a String.
     */
    String getLoginUri(String state, URI redirectUri);

    /**
     * Get the access token.
     *
     * @param authCode the authorization code from the HttpServletRequest.
     * @param redirectUri a URI object representing the redirect URI.
     * @return the access token in the form of a {@link JsonObject}.
     * @throws SsoJwtException if it fails to retrieve the access token.
     */
    JsonObject getAccessToken(String authCode, URI redirectUri) throws SsoJwtException;
}
