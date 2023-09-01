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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;

import java.util.Optional;

/**
 * {@link JwtCredentials} {@link LoginCredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class JwtCredentialsHandler implements LoginCredentialsHandler {

    @Override
    public boolean canProcess(LoginCredentials loginCredentials) {
        return loginCredentials instanceof JwtCredentials;
    }

    @Override
    public ImmutablePair<AuthenticationToken, Optional<String>> mapToShiro(LoginCredentials loginCredentials) throws KapuaAuthenticationException {
        JwtCredentialsImpl jwtCredentials = JwtCredentialsImpl.parse((JwtCredentials) loginCredentials);
        final String openIDidToken = Optional.ofNullable(jwtCredentials.getIdToken())
                .filter(token -> !Strings.isNullOrEmpty(token))
                .orElseThrow(() -> new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS));

        return ImmutablePair.of(jwtCredentials, Optional.of(openIDidToken));
    }
}
