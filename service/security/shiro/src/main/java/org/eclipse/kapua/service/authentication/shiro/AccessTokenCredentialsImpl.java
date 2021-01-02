/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;

/**
 * Access token {@link AuthenticationCredentials} implementation.
 *
 * @since 1.0
 *
 */
public class AccessTokenCredentialsImpl implements AccessTokenCredentials, AuthenticationToken {

    private static final long serialVersionUID = -7549848672967689716L;

    private String tokenId;

    private AccessTokenCredentialsImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param tokenId
     */
    public AccessTokenCredentialsImpl(String tokenId) {
        this();
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Object getPrincipal() {
        return getTokenId();
    }

    @Override
    public Object getCredentials() {
        return getTokenId();
    }
}
