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

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;
import org.eclipse.kapua.service.authentication.shiro.exceptions.MfaRequiredException;
import org.eclipse.kapua.service.authentication.shiro.mfa.MfaAuthenticatorServiceLocator;
import org.eclipse.kapua.service.user.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

/**
 * {@link ApiKeyCredentials} {@link CredentialsMatcher} implementation.
 *
 * @since 1.0.0
 */
public class UserPassCredentialsMatcher implements CredentialsMatcher {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MfaOptionService MFA_OPTION_SERVICE = LOCATOR.getService(MfaOptionService.class);
    private static final ScratchCodeService SCRATCH_CODE_SERVICE = LOCATOR.getService(ScratchCodeService.class);
    private static final MfaAuthenticatorServiceLocator MFA_AUTH_SERVICE_LOCATOR = MfaAuthenticatorServiceLocator.getInstance();
    private static final MfaAuthenticator MFA_AUTHENTICATOR = MFA_AUTH_SERVICE_LOCATOR.getMfaAuthenticator();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        //
        // Token data
        UsernamePasswordCredentials token = (UsernamePasswordCredentials) authenticationToken;
        String tokenUsername = token.getUsername();
        String tokenPassword = token.getPassword();
        String tokenAuthenticationCode = token.getAuthenticationCode();
        String tokenTrustKey = token.getTrustKey();

        //
        // Info data
        LoginAuthenticationInfo info = (LoginAuthenticationInfo) authenticationInfo;
        User infoUser = (User) info.getPrincipals().getPrimaryPrincipal();
        Credential infoCredential = (Credential) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (tokenUsername.equals(infoUser.getName()) &&
                CredentialType.PASSWORD.equals(infoCredential.getCredentialType()) &&
                BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey())) {

            if (!MFA_AUTHENTICATOR.isEnabled()) {
                credentialMatch = true;
                // FIXME: if true cache token password for authentication performance improvement
            } else {

                // Check if MFA is enabled for the current user
                MfaOption mfaOption;
                try {
                    mfaOption = KapuaSecurityUtils.doPrivileged(() -> MFA_OPTION_SERVICE.findByUserId(infoUser.getScopeId(), infoUser.getId()));
                } catch (AuthenticationException ae) {
                    throw ae;
                } catch (Exception e) {
                    throw new ShiroException("Error while finding Mfa Option!", e);
                }

                if (mfaOption != null) {
                    if (tokenAuthenticationCode != null) {

                        // Do MFA match
                        boolean isCodeValid;
                        try {
                            isCodeValid = MFA_AUTHENTICATOR.authorize(mfaOption.getMfaSecretKey(), Integer.parseInt(tokenAuthenticationCode));
                        } catch (AuthenticationException ae) {
                            throw ae;
                        } catch (Exception e) {
                            throw new ShiroException("Error while authenticating Mfa Option!", e);
                        }

                        if (!isCodeValid) {
                            //  Code is not valid, try scratch codes login
                            ScratchCodeListResult scratchCodeListResult;
                            try {
                                scratchCodeListResult = KapuaSecurityUtils.doPrivileged(() -> SCRATCH_CODE_SERVICE.findByMfaOptionId(mfaOption.getScopeId(), mfaOption.getId()));
                            } catch (AuthenticationException ae) {
                                throw ae;
                            } catch (Exception e) {
                                throw new ShiroException("Error while finding scratch codes!", e);
                            }

                            for (ScratchCode code : scratchCodeListResult.getItems()) {
                                try {
                                    if (MFA_AUTHENTICATOR.authorize(code.getCode(), tokenAuthenticationCode)) {
                                        isCodeValid = true;
                                        try {
                                            // Delete the used scratch code
                                            KapuaSecurityUtils.doPrivileged(() -> SCRATCH_CODE_SERVICE.delete(code.getScopeId(), code.getId()));
                                        } catch (AuthenticationException ae) {
                                            throw ae;
                                        } catch (Exception e) {
                                            throw new ShiroException("Error while removing used scratch code!", e);
                                        }
                                        break;
                                    }
                                } catch (KapuaException e) {
                                    throw new ShiroException("Error while validating scratch codes!", e);
                                }
                            }
                        }
                        credentialMatch = isCodeValid;
                    } else {
                        // If authentication code is null, then check the trust_key
                        if (tokenTrustKey != null) {
                            // Check trust machine authentication on the server side
                            if (mfaOption.getTrustKey() != null) {

                                Date now = new Date(System.currentTimeMillis());
                                if (mfaOption.getTrustExpirationDate().before(now)) {

                                    // The trust key is expired and must be disabled
                                    try {
                                        KapuaSecurityUtils.doPrivileged(() -> MFA_OPTION_SERVICE.disableTrust(mfaOption.getScopeId(), mfaOption.getId()));
                                    } catch (AuthenticationException ae) {
                                        throw ae;
                                    } catch (Exception e) {
                                        throw new ShiroException("Error while disabling trust!", e);
                                    }

                                } else if (BCrypt.checkpw(tokenTrustKey, mfaOption.getTrustKey())) {
                                    credentialMatch = true;
                                }
                            }
                        } else {
                            // In case both the authenticationCode and the trustKey are null, the MFA login via Rest API must be triggered.
                            // Since this method only returns true or false, the MFA request via Rest API is handled through exceptions.
                            throw new MfaRequiredException();
                        }
                    }
                } else {
                    credentialMatch = true;  // MFA service is enabled, but the user has no MFA enabled
                }
            }
        }

        return credentialMatch;
    }

}
