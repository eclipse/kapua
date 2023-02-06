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
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;
import org.eclipse.kapua.service.authentication.exception.PasswordLengthException;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;
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

    @Override
    public Credential changePasswordRequest(PasswordChangeRequest passwordChangeRequest) throws KapuaException {
        ArgumentValidator.notNull(passwordChangeRequest.getNewPassword(), "passwordChangeRequest.newPassword");
        ArgumentValidator.notNull(passwordChangeRequest.getCurrentPassword(), "passwordChangeRequest.currentPassword");

        return KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
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

            CredentialService credentialService = locator.getService(CredentialService.class);
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
}
