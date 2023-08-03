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

import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;

import java.util.Optional;

public class ApiKeyCredentialsHandler implements CredentialsHandler {

    @Override
    public boolean canProcess(LoginCredentials loginCredentials) {
        return loginCredentials instanceof ApiKeyCredentials;
    }

    @Override
    public ImmutablePair<AuthenticationToken, Optional<String>> mapToShiro(LoginCredentials loginCredentials) throws KapuaAuthenticationException {
        ApiKeyCredentialsImpl apiKeyCredentials = ApiKeyCredentialsImpl.parse((ApiKeyCredentials) loginCredentials);

        if (Strings.isNullOrEmpty(apiKeyCredentials.getApiKey())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS);
        }

        return ImmutablePair.of(apiKeyCredentials, Optional.empty());
    }
}
