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

import jdk.internal.joptsimple.internal.Strings;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;

/**
 * {@link AccessTokenCredentials} {@link CredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class AccessTokenCredentialsHandler implements CredentialsHandler {

    @Override
    public boolean canProcess(AuthenticationCredentials authenticationCredentials) {
        return authenticationCredentials instanceof AccessTokenCredentials;
    }

    @Override
    public KapuaAuthenticationToken mapToShiro(AuthenticationCredentials authenticationCredentials) throws KapuaAuthenticationException {
        final AccessTokenCredentialsImpl accessTokenCredentials = authenticationCredentials instanceof AccessTokenCredentialsImpl ?
                (AccessTokenCredentialsImpl) authenticationCredentials :
                new AccessTokenCredentialsImpl((AccessTokenCredentials) authenticationCredentials);

        if (Strings.isNullOrEmpty(accessTokenCredentials.getTokenId())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_SESSION_CREDENTIALS);
        }
        return accessTokenCredentials;
    }
}
