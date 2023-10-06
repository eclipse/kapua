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

import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.realm.KapuaAuthenticationToken;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * {@link AccessTokenCredentials} implementation.
 * <p>
 * This implements also {@link KapuaAuthenticationToken} to allow usage in Apache Shiro.
 *
 * @since 1.0.0
 */
public class AccessTokenCredentialsImpl implements AccessTokenCredentials, KapuaAuthenticationToken {

    private static final long serialVersionUID = -7549848672967689716L;

    private String tokenId;

    /**
     * Constructor.
     *
     * @param tokenId The credential JWT
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

    @Override
    public Optional<String> getOpenIdToken() {
        return Optional.empty();
    }
}

