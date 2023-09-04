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
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;

import java.util.Optional;

/**
 * {@link AccessTokenCredentials} {@link SessionCredentialsHandler} implementation.
 *
 * @since 2.0.0
 */
public class AccessTokenCredentialsHandler implements SessionCredentialsHandler {

    @Override
    public boolean canProcess(SessionCredentials loginCredentials) {
        return loginCredentials instanceof AccessTokenCredentials;
    }

    @Override
    public ImmutablePair<AuthenticationToken, Optional<String>> mapToShiro(SessionCredentials loginCredentials) throws KapuaAuthenticationException {
        AccessTokenCredentialsImpl accessTokenCredentials = AccessTokenCredentialsImpl.parse((AccessTokenCredentials) loginCredentials);

        if (Strings.isNullOrEmpty(accessTokenCredentials.getTokenId())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_SESSION_CREDENTIALS);
        }

        return ImmutablePair.of(accessTokenCredentials, Optional.empty());
    }
}
