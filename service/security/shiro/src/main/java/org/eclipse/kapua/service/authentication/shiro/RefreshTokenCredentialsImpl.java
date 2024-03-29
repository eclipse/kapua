/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * {@link RefreshTokenCredentials} implementation.
 * <p>
 * This implements also {@link AuthenticationToken} to allow usage in Apache Shiro.
 *
 * @since 1.0.0
 */
public class RefreshTokenCredentialsImpl implements RefreshTokenCredentials {

    private String tokenId;
    private String refreshToken;

    /**
     * Constructor.
     *
     * @param tokenId      The {@link AccessToken#getTokenId()}
     * @param refreshToken TThe {@link AccessToken#getRefreshToken()}
     * @since 1.0.0
     */
    public RefreshTokenCredentialsImpl(String tokenId, String refreshToken) {
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
