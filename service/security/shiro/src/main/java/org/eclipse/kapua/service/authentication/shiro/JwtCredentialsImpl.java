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

import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.shiro.realm.KapuaAuthenticationToken;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * {@link JwtCredentials} implementation.
 * <p>
 * This implements also {@link KapuaAuthenticationToken} to allow usage in Apache Shiro.
 *
 * @since 1.0.0
 */
public class JwtCredentialsImpl implements JwtCredentials, KapuaAuthenticationToken {

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

    @Override
    public Optional<String> getOpenIdToken() {
        return Optional.of(getIdToken());
    }
}
