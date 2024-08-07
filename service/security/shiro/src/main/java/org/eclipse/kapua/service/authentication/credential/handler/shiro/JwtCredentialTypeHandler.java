/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.handler.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.handler.CredentialTypeHandler;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;

import javax.inject.Inject;

/**
 * JWT {@link CredentialTypeHandler} implementation
 *
 * @since 2.1.0
 */
public class JwtCredentialTypeHandler implements CredentialTypeHandler {

    public static final String TYPE = "JWT";

    private final AuthenticationUtils authenticationUtils;

    @Inject
    public JwtCredentialTypeHandler(AuthenticationUtils authenticationUtils) {
        this.authenticationUtils = authenticationUtils;
    }

    @Override
    public String getName() {
        return TYPE;
    }

    @Override
    public void validateCreator(CredentialCreator credentialCreator) {
        // Nothing to validate
    }

    @Override
    public String cryptCredentialKey(String credentialPlainKey) throws KapuaException {
        return authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey);
    }
}
