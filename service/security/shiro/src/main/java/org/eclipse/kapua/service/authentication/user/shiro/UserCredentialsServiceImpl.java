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
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
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
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialsFactory;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Singleton;

/**
 * {@link UserCredentialsService} implementation.
 *
 * @since 2.0.0
 */
@Singleton
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);


    @Override
    public Credential changePassword(PasswordChangeRequest passwordChangeRequest) throws KapuaException {
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

            UserCredentialsFactory userCredentialsFactory = locator.getFactory(UserCredentialsFactory.class);
            PasswordResetRequest passwordResetRequest = userCredentialsFactory.newPasswordResetRequest();
            passwordResetRequest.setNewPassword(passwordChangeRequest.getNewPassword());
            try {
                return resetPassword(KapuaSecurityUtils.getSession().getScopeId(), passwordCredential.getId(), passwordResetRequest);
            } catch (KapuaIllegalArgumentException ignored) {
                throw new KapuaIllegalArgumentException("passwordChangeRequest.newPassword", passwordChangeRequest.getNewPassword());
            }
        });
    }


    @Override
    public Credential resetPassword(KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(credentialId, "credentialId");
        ArgumentValidator.notNull(passwordResetRequest.getNewPassword(), "passwordResetRequest.newPassword");

        //
        // Check access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, scopeId));

        Credential credential = credentialService.find(scopeId, credentialId);
        if (credential == null) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
        }

        String plainNewPassword = passwordResetRequest.getNewPassword();
        try {
            credentialService.validatePassword(credential.getScopeId(), plainNewPassword);
        } catch (KapuaIllegalArgumentException ignored) {
            throw new KapuaIllegalArgumentException("passwordResetRequest.newPassword", plainNewPassword);
        }

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
