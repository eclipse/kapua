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
import org.checkerframework.checker.nullness.qual.Nullable;

import org.eclipse.kapua.service.authentication.AccessTokenCredentials;

import javax.validation.constraints.NotNull;

/**
 * {@link AccessTokenCredentials} implementation.
 * <p>
 * This implements also {@link AuthenticationToken} to allow usage in Shiro.
 *
 * @since 1.0.0
 */
public class AccessTokenCredentialsImpl implements AccessTokenCredentials, AuthenticationToken {

    private static final long serialVersionUID = -7549848672967689716L;

    private String tokenId;

    /**
     * Constructor.
     *
     * @param tokenId The credential TokenId
     * @since 1.0.0
     */
    public AccessTokenCredentialsImpl(@NotNull String tokenId) {
        setTokenId(tokenId);
    }

    /**
     * Clone constructor.
     *
     * @param accessTokenCredentials The {@link AccessTokenCredentials} to clone.
     * @since 1.5.0
     */
    public AccessTokenCredentialsImpl(@NotNull AccessTokenCredentials accessTokenCredentials) {
        setTokenId(accessTokenCredentials.getTokenId());
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
    public Object getPrincipal() {
        return getTokenId();
    }

    @Override
    public Object getCredentials() {
        return getTokenId();
    }

    /**
     * Parses a {@link AccessTokenCredentials} into a {@link AccessTokenCredentialsImpl}.
     *
     * @param accessTokenCredentials The {@link AccessTokenCredentials} to parse.
     * @return A instance of {@link AccessTokenCredentialsImpl}.
     * @since 1.5.0
     */
    public static AccessTokenCredentialsImpl parse(@Nullable AccessTokenCredentials accessTokenCredentials) {
        return accessTokenCredentials != null ?
                (accessTokenCredentials instanceof AccessTokenCredentialsImpl ?
                        (AccessTokenCredentialsImpl) accessTokenCredentials :
                        new AccessTokenCredentialsImpl(accessTokenCredentials))
                : null;
    }
}

