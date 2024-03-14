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
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class PasswordResetterImpl implements PasswordResetter {
    private final CredentialFactory credentialFactory;
    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;
    private final PasswordValidator passwordValidator;

    public PasswordResetterImpl(
            CredentialFactory credentialFactory,
            CredentialRepository credentialRepository,
            CredentialMapper credentialMapper,
            PasswordValidator passwordValidator) {
        this.credentialFactory = credentialFactory;
        this.credentialRepository = credentialRepository;
        this.credentialMapper = credentialMapper;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public Credential resetPassword(TxContext tx, KapuaId scopeId, KapuaId userId, boolean failIfAbsent, PasswordResetRequest passwordResetRequest) throws KapuaException {
        final CredentialListResult credentials = credentialRepository.findByUserId(tx, scopeId, userId);
        final Optional<Credential> passwordCredential = credentials.getItems().stream()
                .filter(credential -> credential.getCredentialType().equals(CredentialType.PASSWORD))
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
        if (credential.getCredentialType() != CredentialType.PASSWORD) {
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
        if (currentCredential.isPresent()) {
            credentialRepository.delete(tx, currentCredential.get());
        }
        final Credential toPersists = credentialMapper.map(
                credentialFactory.newCreator(scopeId,
                        userId,
                        CredentialType.PASSWORD,
                        plainNewPassword,
                        CredentialStatus.ENABLED,
                        null));
        return credentialRepository.create(tx, toPersists);
    }

}
