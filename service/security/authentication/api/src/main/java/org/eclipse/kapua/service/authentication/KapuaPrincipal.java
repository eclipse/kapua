/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import java.security.Principal;

import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * Kapua {@link Principal}.<br>
 * Extends {@link Principal} with additional properties used by the broker.<br>
 * Uniquely identifies a {@link Device}.
 * 
 * @since 1.0.0
 * 
 */
public interface KapuaPrincipal extends Principal, java.io.Serializable {

    /**
     * Returns the {@link AccessToken} of this session.
     * 
     * @return The {@link AccessToken} of this session.
     * @since 1.0.0
     */
    public AccessToken getAccessToken();

    /**
     * Returns the remote client IP address from which the user should be connected.
     * 
     * @return The remote client IP address from which the user should be connected.
     * @since 1.0.0
     */
    public String getClientIp();

    /**
     * Returns the client identifiers from which the user should be connected.
     * 
     * @return The client identifiers from which the user should be connected.
     * @since 1.0.0
     */
    public String getClientId();

}
