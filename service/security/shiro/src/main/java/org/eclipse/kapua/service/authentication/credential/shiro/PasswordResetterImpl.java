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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.handler.CredentialTypeHandler;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

/**
 * The {@link PasswordResetter} implementation.
 *
 * @since 2.0.0
 */
public class PasswordResetterImpl implements PasswordResetter {
    private final CredentialRepository credentialRepository;
    private final PasswordValidator passwordValidator;

    private final CredentialTypeHandler credentialTypeHandler;

    public PasswordResetterImpl(
            CredentialRepository credentialRepository,
            CredentialTypeHandler credentialTypeHandler,
            PasswordValidator passwordValidator) {
        this.credentialRepository = credentialRepository;
        this.credentialTypeHandler = credentialTypeHandler;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public Credential resetPassword(TxContext tx, KapuaId scopeId, KapuaId userId, boolean failIfAbsent, PasswordResetRequest passwordResetRequest) throws KapuaException {
        final CredentialListResult credentials = credentialRepository.findByUserId(tx, scopeId, userId);
        final Optional<Credential> passwordCredential = credentials.getItems().stream()
                .filter(credential -> credentialTypeHandler.getName().equals(credential.getCredentialType()))
                .findAny();
        if (failIfAbsent && !passwordCredential.isPresent()) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, "User does not have any credential of type password");
        }
        return doResetPassword(tx, scopeId, userId, passwordCredential, passwordResetRequest);
    }

    @Override
    public Credential resetPassword(TxContext tx, KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException {
        final Credential credential = credentialRepository.find(tx, scopeId, credentialId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(Credential.TYPE, credentialId));
        if (!credentialTypeHandler.getName().equals(credential.getCredentialType())) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, "User does not have any credential of type password");
        }

        return doResetPassword(tx, scopeId, credential.getUserId(), Optional.of(credential), passwordResetRequest);
    }

    private Credential doResetPassword(TxContext tx, KapuaId scopeId, KapuaId userId, Optional<Credential> currentCredential, PasswordResetRequest passwordResetRequest) throws KapuaException {
        final String plainNewPassword = passwordResetRequest.getNewPassword();
        try {
            passwordValidator.validatePassword(tx, scopeId, plainNewPassword);
        } catch (KapuaIllegalArgumentException ignored) {
            throw new KapuaIllegalArgumentException("passwordResetRequest.newPassword", plainNewPassword);
        }

        // Delete Credential if exist
        currentCredential.ifPresent(credential -> credentialRepository.delete(tx, credential));

        // Create the new one
        String encryptedKey = credentialTypeHandler.cryptCredentialKey(plainNewPassword);
        Credential toPersists = new CredentialImpl(
                scopeId,
                userId,
                credentialTypeHandler.getName(),
                encryptedKey,
                CredentialStatus.ENABLED,
                null // FIXME: no expiration date can be set upon reset?
        );

        return credentialRepository.create(tx, toPersists);
    }

}
