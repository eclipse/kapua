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
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Singleton;

/**
 * {@link UserCredentialService} implementation.
 *
 * @since 2.0.0
 */
@Singleton
public class UserCredentialServiceImpl implements UserCredentialService {
    @Override
    public Credential changePasswordRequest(PasswordChangeRequest passwordChangeRequest) throws KapuaException {
        ArgumentValidator.notNull(passwordChangeRequest.getNewPassword(), "passwordChangeRequest.newPassword");
        ArgumentValidator.notNull(passwordChangeRequest.getOldPassword(), "passwordChangeRequest.oldPassword");

        return KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            User user = userService.find(KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId());
            if (user == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, KapuaSecurityUtils.getSession().getUserId());
            }

            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
            UsernamePasswordCredentials usernamePasswordCredentials = credentialsFactory.newUsernamePasswordCredentials(user.getName(), passwordChangeRequest.getOldPassword());
            try {
                authenticationService.verifyCredentials(usernamePasswordCredentials);
            } catch (KapuaException e) {
                throw new KapuaException(KapuaErrorCodes.OPERATION_NOT_ALLOWED, "passwordChangeRequest.oldPassword");
            }

            CredentialService credentialService = locator.getService(CredentialService.class);
            CredentialListResult credentials = credentialService.findByUserId(KapuaSecurityUtils.getSession().getScopeId(), KapuaSecurityUtils.getSession().getUserId());
            Credential passwordCredential = credentials.getItems().stream()
                                                       .filter(credential -> credential.getCredentialType().equals(CredentialType.PASSWORD))
                                                       .findAny()
                                                       .orElseThrow(() -> new IllegalStateException("User does not have any credential of type password"));

            String encryptedPass = AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, passwordCredential.getCredentialKey());
            passwordCredential.setCredentialKey(encryptedPass);

            return credentialService.update(passwordCredential);
        });
    }
}
