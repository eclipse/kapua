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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDAO;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.exception.PasswordLengthException;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

/**
 * {@link UserCredentialsService} implementation.
 *
 * @since 2.0.0
 */
@KapuaProvider
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private static final int SYSTEM_MAXIMUM_PASSWORD_LENGTH = 255;
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);

    @Override
    public Credential changePasswordRequest(PasswordChangeRequest passwordChangeRequest) throws KapuaException {
        ArgumentValidator.notNull(passwordChangeRequest.getNewPassword(), "passwordChangeRequest.newPassword");
        ArgumentValidator.notNull(passwordChangeRequest.getCurrentPassword(), "passwordChangeRequest.currentPassword");

        return KapuaSecurityUtils.doPrivileged(() -> {
            UserService userService = locator.getService(UserService.class);
            User user = userService.find(KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId());
            if (user == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, KapuaSecurityUtils.getSession().getUserId());
            }

            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
            UsernamePasswordCredentials usernamePasswordCredentials = credentialsFactory.newUsernamePasswordCredentials(user.getName(), passwordChangeRequest.getCurrentPassword());
            try {
                authenticationService.verifyCredentials(usernamePasswordCredentials);
            } catch (KapuaAuthenticationException e) {
                throw new KapuaIllegalArgumentException("passwordChangeRequest.currentPassword", passwordChangeRequest.getCurrentPassword());
            }

            CredentialListResult credentials = credentialService.findByUserId(KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId());
            Credential passwordCredential = credentials.getItems().stream()
                    .filter(credential -> credential.getCredentialType().equals(CredentialType.PASSWORD))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("User does not have any credential of type password"));

            // Validate Password length
            int minPasswordLength = credentialService.getMinimumPasswordLength(passwordCredential.getScopeId());
            if (passwordChangeRequest.getNewPassword().length() < minPasswordLength || passwordChangeRequest.getNewPassword().length() > SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
                throw new PasswordLengthException(minPasswordLength, SYSTEM_MAXIMUM_PASSWORD_LENGTH);
            }

            //
            // Validate Password regex
            ArgumentValidator.match(passwordChangeRequest.getNewPassword(), CommonsValidationRegex.PASSWORD_REGEXP, "passwordChangeRequest.newPassword");

            String encryptedPass = AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, passwordChangeRequest.getNewPassword());
            passwordCredential.setCredentialKey(encryptedPass);

            return credentialService.update(passwordCredential);
        });
    }


    @Override
    public Credential resetPassword(KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException {
        Credential credential = credentialService.find(scopeId, credentialId);
        if (credential == null) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
        }

        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(credentialId, "credential.id");
        ArgumentValidator.notNull(passwordResetRequest.getNewPassword(), "passwordResetRequest.newPassword");

        //
        // Check access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, scopeId));

        // Validate Password length
        String plainNewPassword = passwordResetRequest.getNewPassword();
        int minPasswordLength = credentialService.getMinimumPasswordLength(credential.getScopeId());
        if (plainNewPassword.length() < minPasswordLength || plainNewPassword.length() > SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
            throw new PasswordLengthException(minPasswordLength, SYSTEM_MAXIMUM_PASSWORD_LENGTH);
        }

        //
        // Validate Password regex
        ArgumentValidator.match(plainNewPassword, CommonsValidationRegex.PASSWORD_REGEXP, "passwordResetRequest.newPassword");

        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
        CredentialCreator credentialCreator = credentialFactory.newCreator(scopeId,
                                                                           credential.getUserId(),
                                                                           CredentialType.PASSWORD,
                                                                           plainNewPassword,
                                                                           CredentialStatus.ENABLED,
                                                                           null);

        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            CredentialDAO.delete(em, scopeId, credentialId);
            credential = CredentialDAO.create(em, credentialCreator);

            em.commit();
            return credential;
        } catch (KapuaException e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
    }
}
