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
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;

/**
 * {@link ApiKeyCredentials} implementation.
 * <p>
 * This implements also {@link AuthenticationToken} to allow usage in Shiro.
 *
 * @since 1.0.0
 */
public class ApiKeyCredentialsImpl implements ApiKeyCredentials, AuthenticationToken {

    private static final long serialVersionUID = -5920944517814926028L;

    private String apiKey;

    /**
     * Constructor.
     *
     * @param apiKey The crential key.
     * @since 1.0.0
     */
    public ApiKeyCredentialsImpl(String apiKey) {
        setApiKey(apiKey);
    }

    /**
     * Clone constructor.
     *
     * @param apiKeyCredentials The {@link ApiKeyCredentials} to clone.
     * @since 1.5.0
     */
    public ApiKeyCredentialsImpl(@NotNull ApiKeyCredentials apiKeyCredentials) {
        setApiKey(apiKeyCredentials.getApiKey());
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Object getPrincipal() {
        return getApiKey();
    }

    @Override
    public Object getCredentials() {
        return getApiKey();
    }

    /**
     * Parses a {@link ApiKeyCredentials} into a {@link ApiKeyCredentialsImpl}.
     *
     * @param apiKeyCredentials The {@link ApiKeyCredentials} to parse.
     * @return A instance of {@link ApiKeyCredentialsImpl}.
     * @since 1.5.0
     */
    public static ApiKeyCredentialsImpl parse(@Nullable ApiKeyCredentials apiKeyCredentials) {
        return apiKeyCredentials != null ?
                (apiKeyCredentials instanceof ApiKeyCredentialsImpl ?
                        (ApiKeyCredentialsImpl) apiKeyCredentials :
                        new ApiKeyCredentialsImpl(apiKeyCredentials))
                : null;
    }

}
