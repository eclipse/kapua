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
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;

import java.util.Optional;

/**
 * {@link UsernamePasswordCredentials} {@link LoginCredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class UserPassCredentialsHandler implements LoginCredentialsHandler {

    @Override
    public boolean canProcess(LoginCredentials loginCredentials) {
        return loginCredentials instanceof UsernamePasswordCredentials;
    }

    @Override
    public ImmutablePair<AuthenticationToken, Optional<String>> mapToShiro(LoginCredentials loginCredentials) throws KapuaAuthenticationException {
        UsernamePasswordCredentialsImpl usernamePasswordCredentials = UsernamePasswordCredentialsImpl.parse((UsernamePasswordCredentials) loginCredentials);

        if (Strings.isNullOrEmpty(usernamePasswordCredentials.getUsername()) ||
                Strings.isNullOrEmpty(usernamePasswordCredentials.getPassword())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS);
        }

        return ImmutablePair.of(usernamePasswordCredentials, Optional.empty());
    }
}
