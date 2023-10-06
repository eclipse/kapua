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
package org.eclipse.kapua.service.authentication.shiro.realm;

import com.google.common.base.Strings;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.ApiKeyCredentialsImpl;

/**
 * {@link ApiKeyCredentials} {@link CredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class ApiKeyCredentialsHandler implements CredentialsHandler {

    @Override
    public boolean canProcess(AuthenticationCredentials credentials) {
        return credentials instanceof ApiKeyCredentials;
    }

    @Override
    public KapuaAuthenticationToken mapToShiro(AuthenticationCredentials authenticationCredentials) throws KapuaAuthenticationException {

        ApiKeyCredentialsImpl apiKeyCredentials = authenticationCredentials instanceof ApiKeyCredentialsImpl ?
                (ApiKeyCredentialsImpl) authenticationCredentials :
                new ApiKeyCredentialsImpl((ApiKeyCredentials) authenticationCredentials);

        if (Strings.isNullOrEmpty(apiKeyCredentials.getApiKey())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS);
        }

        return apiKeyCredentials;
    }
}
