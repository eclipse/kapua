/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.user.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialMapper;
import org.eclipse.kapua.service.authentication.credential.shiro.PasswordValidator;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialsFactory;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.storage.TxContext;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * {@link UserCredentialsService} implementation.
 *
 * @since 2.0.0
 */
@Singleton
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final UserCredentialsFactory userCredentialsFactory;
    private final CredentialsFactory credentialsFactory;
    private final CredentialFactory credentialFactory;
    private final TxManager txManager;
    private final UserService userService;
    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;
    private final PasswordValidator passwordValidator;

    public UserCredentialsServiceImpl(
            AuthenticationService authenticationService,
            AuthorizationService authorizationService, PermissionFactory permissionFactory,
            UserCredentialsFactory userCredentialsFactory, CredentialsFactory credentialsFactory,
            CredentialFactory credentialFactory,
            TxManager txManager,
            UserService userService,
            CredentialRepository credentialRepository,
            CredentialMapper credentialMapper,
            PasswordValidator passwordValidator) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.userCredentialsFactory = userCredentialsFactory;
        this.credentialsFactory = credentialsFactory;
        this.credentialFactory = credentialFactory;
        this.txManager = txManager;
        this.userService = userService;
        this.credentialRepository = credentialRepository;
        this.credentialMapper = credentialMapper;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public Credential changePassword(PasswordChangeRequest passwordChangeRequest) throws KapuaException {
        ArgumentValidator.notNull(passwordChangeRequest.getNewPassword(), "passwordChangeRequest.newPassword");
        ArgumentValidator.notNull(passwordChangeRequest.getCurrentPassword(), "passwordChangeRequest.currentPassword");

        final User user = Optional.ofNullable(
                KapuaSecurityUtils.doPrivileged(() -> userService.find(KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId())
                )
        ).orElseThrow(() -> new KapuaEntityNotFoundException(User.TYPE, KapuaSecurityUtils.getSession().getUserId()));
        return txManager.execute(tx -> {
            UsernamePasswordCredentials usernamePasswordCredentials = credentialsFactory.newUsernamePasswordCredentials(user.getName(), passwordChangeRequest.getCurrentPassword());
            try {
                authenticationService.verifyCredentials(usernamePasswordCredentials);
            } catch (KapuaAuthenticationException e) {
                throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INCORRECT_CURRENT_PASSWORD);
            }

            CredentialListResult credentials = credentialRepository.findByUserId(tx, KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId());
            Credential passwordCredential = credentials.getItems().stream()
                    .filter(credential -> credential.getCredentialType().equals(CredentialType.PASSWORD))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("User does not have any credential of type password"));

            PasswordResetRequest passwordResetRequest = userCredentialsFactory.newPasswordResetRequest();
            passwordResetRequest.setNewPassword(passwordChangeRequest.getNewPassword());
            try {
                return doResetPassword(tx, KapuaSecurityUtils.getSession().getScopeId(), passwordCredential.getId(), passwordResetRequest);
            } catch (KapuaIllegalArgumentException ignored) {
                throw new KapuaIllegalArgumentException("passwordChangeRequest.newPassword", passwordChangeRequest.getNewPassword());
            }
        });
    }


    @Override
    public Credential resetPassword(KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(credentialId, "credentialId");
        ArgumentValidator.notNull(passwordResetRequest.getNewPassword(), "passwordResetRequest.newPassword");

        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, scopeId));

        return txManager.execute(tx -> {
            return doResetPassword(tx, scopeId, credentialId, passwordResetRequest);
        });
    }

    private Credential doResetPassword(TxContext tx, KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException {
        Credential credential = credentialRepository.find(tx, scopeId, credentialId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(Credential.TYPE, credentialId));

        String plainNewPassword = passwordResetRequest.getNewPassword();
        try {
            passwordValidator.validatePassword(tx, credential.getScopeId(), plainNewPassword);
        } catch (KapuaIllegalArgumentException ignored) {
            throw new KapuaIllegalArgumentException("passwordResetRequest.newPassword", plainNewPassword);
        }

        final Credential toPersists = credentialMapper.map(credentialFactory.newCreator(scopeId,
                credential.getUserId(),
                CredentialType.PASSWORD,
                plainNewPassword,
                CredentialStatus.ENABLED,
                null));
        credentialRepository.delete(tx, scopeId, credentialId);
        return credentialRepository.create(tx, toPersists);
    }
}
