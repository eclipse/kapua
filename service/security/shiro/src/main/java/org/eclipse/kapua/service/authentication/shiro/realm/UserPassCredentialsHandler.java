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
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;

/**
 * {@link UsernamePasswordCredentials} {@link CredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class UserPassCredentialsHandler implements CredentialsHandler {

    @Override
    public boolean canProcess(AuthenticationCredentials authenticationCredentials) {
        return authenticationCredentials instanceof UsernamePasswordCredentials;
    }

    @Override
    public KapuaAuthenticationToken mapToShiro(AuthenticationCredentials authenticationCredentials) throws KapuaAuthenticationException {
        UsernamePasswordCredentialsImpl usernamePasswordCredentials = UsernamePasswordCredentialsImpl.parse((UsernamePasswordCredentials) authenticationCredentials);

        if (Strings.isNullOrEmpty(usernamePasswordCredentials.getUsername()) ||
                Strings.isNullOrEmpty(usernamePasswordCredentials.getPassword())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS);
        }

        return usernamePasswordCredentials;
    }
}
