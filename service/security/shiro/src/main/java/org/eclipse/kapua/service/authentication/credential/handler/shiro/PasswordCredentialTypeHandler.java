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
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.handler.CredentialTypeHandler;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.shiro.PasswordValidator;
import org.eclipse.kapua.service.authentication.exception.DuplicatedPasswordCredentialException;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;

/**
 * Password {@link CredentialTypeHandler} implementation
 *
 * @since 2.1.0
 */
public class PasswordCredentialTypeHandler implements CredentialTypeHandler {

    public static final String TYPE = "PASSWORD";

    private final TxManager txManager;
    private final CredentialRepository credentialRepository;
    private final AuthenticationUtils authenticationUtils;
    private final PasswordValidator passwordValidator;

    @Inject
    public PasswordCredentialTypeHandler(
            TxManager txManager,
            CredentialRepository credentialService,
            AuthenticationUtils authenticationUtils,
            PasswordValidator passwordValidator
    ) {
        this.credentialRepository = credentialService;
        this.txManager = txManager;
        this.authenticationUtils = authenticationUtils;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public String getName() {
        return TYPE;
    }

    @Override
    public void validateCreator(CredentialCreator credentialCreator) throws KapuaException {
        // Check if a PASSWORD credential already exists for the user
        CredentialListResult existingCredentials = txManager.execute(
                tx -> credentialRepository.findByUserId(tx, credentialCreator.getScopeId(), credentialCreator.getUserId())
        );
        for (Credential credential : existingCredentials.getItems()) {
            if (getName().equals(credential.getCredentialType())) {
                throw new DuplicatedPasswordCredentialException();
            }
        }

        // Validate password minimum requirements
        try {
            txManager.execute(tx -> {
                passwordValidator.validatePassword(tx, credentialCreator.getScopeId(), credentialCreator.getCredentialPlainKey());
                return null;
            });
        } catch (KapuaIllegalArgumentException kiae) {
            throw new KapuaIllegalArgumentException("credentialCreator.credentialKey", credentialCreator.getCredentialPlainKey());
        }
    }

    @Override
    public String cryptCredentialKey(String credentialPlainKey) throws KapuaException {
        return authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey);
    }

}
