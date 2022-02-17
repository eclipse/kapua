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

import org.eclipse.kapua.service.authentication.JwtCredentials;

import javax.validation.constraints.NotNull;

/**
 * {@link JwtCredentials} implementation.
 * <p>
 * This implements also {@link AuthenticationToken} to allow usage in Shiro.
 *
 * @since 1.0.0
 */
public class JwtCredentialsImpl implements JwtCredentials, AuthenticationToken {

    private static final long serialVersionUID = -5920944517814926028L;

    private String accessToken;
    private String idToken;

    /**
     * Constructor.
     *
     * @param accessToken The credential access token
     * @param idToken     The credential id token.
     * @since 1.4.0
     */
    public JwtCredentialsImpl(String accessToken, String idToken) {
        setAccessToken(accessToken);
        setIdToken(idToken);
    }

    /**
     * Clone constructor.
     *
     * @param jwtCredentials The {@link JwtCredentials} to clone
     * @since 1.5.0
     */
    public JwtCredentialsImpl(@NotNull JwtCredentials jwtCredentials) {
        setAccessToken(jwtCredentials.getAccessToken());
        setIdToken(jwtCredentials.getIdToken());
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public Object getPrincipal() {
        return getAccessToken();
    }

    @Override
    public Object getCredentials() {
        return getAccessToken();
    }

    /**
     * Parses a {@link JwtCredentials} into a {@link JwtCredentialsImpl}.
     *
     * @param jwtCredentials The {@link JwtCredentials} to parse.
     * @return A instance of {@link JwtCredentialsImpl}.
     * @since 1.5.0
     */
    public static JwtCredentialsImpl parse(@Nullable JwtCredentials jwtCredentials) {
        return jwtCredentials != null ?
                (jwtCredentials instanceof JwtCredentialsImpl ?
                        (JwtCredentialsImpl) jwtCredentials :
                        new JwtCredentialsImpl(jwtCredentials))
                : null;
    }
}
