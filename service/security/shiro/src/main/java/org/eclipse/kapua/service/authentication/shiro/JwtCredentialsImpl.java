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
import org.eclipse.kapua.service.authentication.JwtCredentials;

import javax.annotation.Nullable;
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

    private String jwt;
    private String idToken;

    /**
     * Constructor.
     *
     * @param jwt     The credential JWT.
     * @param idToken The credential token.
     * @since 1.4.0
     */
    public JwtCredentialsImpl(String jwt, String idToken) {
        setJwt(jwt);
        setIdToken(idToken);
    }

    /**
     * Clone constructor.
     *
     * @param jwtCredentials The {@link JwtCredentials} to clone
     * @since 1.5.0
     */
    public JwtCredentialsImpl(@NotNull JwtCredentials jwtCredentials) {
        setJwt(jwtCredentials.getJwt());
        setIdToken(jwtCredentials.getIdToken());
    }

    @Override
    public String getJwt() {
        return jwt;
    }

    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
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
        return getJwt();
    }

    @Override
    public Object getCredentials() {
        return getJwt();
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
