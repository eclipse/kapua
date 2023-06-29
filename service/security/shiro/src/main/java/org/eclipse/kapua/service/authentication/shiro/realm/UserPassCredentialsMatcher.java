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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.Cache;
import org.eclipse.kapua.commons.cache.LocalCache;
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
import org.eclipse.kapua.service.authentication.shiro.AuthenticationServiceShiroImpl;
import org.eclipse.kapua.service.authentication.shiro.exceptions.MfaRequiredException;
import org.eclipse.kapua.service.authentication.shiro.mfa.MfaAuthenticatorServiceLocator;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Base64;
import java.util.Date;

/**
 * {@link ApiKeyCredentials} {@link CredentialsMatcher} implementation.
 *
 * @since 1.0.0
 */
public class UserPassCredentialsMatcher implements CredentialsMatcher {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceShiroImpl.class);

    private final KapuaLocator locator;
    private final MfaOptionService mfaOptionService;
    private final ScratchCodeService scratchCodeService;
    private final MfaAuthenticatorServiceLocator mfaAuthServiceLocator;
    private final MfaAuthenticator mfaAuthenticator;
    private PasswordChecker passwordChecker;

    public UserPassCredentialsMatcher() {
        locator = KapuaLocator.getInstance();
        mfaOptionService = locator.getService(MfaOptionService.class);
        scratchCodeService = locator.getService(ScratchCodeService.class);
        mfaAuthServiceLocator = MfaAuthenticatorServiceLocator.getInstance();
        mfaAuthenticator = mfaAuthServiceLocator.getMfaAuthenticator();
        if (KapuaAuthenticationSetting.getInstance().getBoolean(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_ENABLE, true)) {
            logger.info("Cache enabled. Initializing CachePasswordChecker...");
            passwordChecker = new CachePasswordChecker();
        }
        else {
            logger.info("Cache disabled. Initializing NoCachePasswordChecker...");
            passwordChecker = new NoCachePasswordChecker();
        }
    }

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
                passwordChecker.checkPassword(tokenPassword, infoCredential)) {

            if (!mfaAuthenticator.isEnabled()) {
                credentialMatch = true;
                // FIXME: if true cache token password for authentication performance improvement
            } else {

                // Check if MFA is enabled for the current user
                MfaOption mfaOption;
                try {
                    mfaOption = KapuaSecurityUtils.doPrivileged(() -> mfaOptionService.findByUserId(infoUser.getScopeId(), infoUser.getId()));
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
                            isCodeValid = mfaAuthenticator.authorize(mfaOption.getMfaSecretKey(), Integer.parseInt(tokenAuthenticationCode));
                        } catch (AuthenticationException ae) {
                            throw ae;
                        } catch (Exception e) {
                            throw new ShiroException("Error while authenticating Mfa Option!", e);
                        }

                        if (!isCodeValid) {
                            //  Code is not valid, try scratch codes login
                            ScratchCodeListResult scratchCodeListResult;
                            try {
                                scratchCodeListResult = KapuaSecurityUtils.doPrivileged(() -> scratchCodeService.findByMfaOptionId(mfaOption.getScopeId(), mfaOption.getId()));
                            } catch (AuthenticationException ae) {
                                throw ae;
                            } catch (Exception e) {
                                throw new ShiroException("Error while finding scratch codes!", e);
                            }

                            for (ScratchCode code : scratchCodeListResult.getItems()) {
                                try {
                                    if (mfaAuthenticator.authorize(code.getCode(), tokenAuthenticationCode)) {
                                        isCodeValid = true;
                                        try {
                                            // Delete the used scratch code
                                            KapuaSecurityUtils.doPrivileged(() -> scratchCodeService.delete(code.getScopeId(), code.getId()));
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
                                        KapuaSecurityUtils.doPrivileged(() -> mfaOptionService.disableTrust(mfaOption.getScopeId(), mfaOption.getId()));
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

interface PasswordChecker {

    boolean checkPassword(String tokenPassword, Credential infoCredential);

}

class CachePasswordChecker implements PasswordChecker {

    private static final Cache<String, String> CACHED_CREDENTIALS = new LocalCache<String, String>(
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_SIZE, 1000),
            KapuaAuthenticationSetting.getInstance().getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_TTL, 60),
            null);

    public boolean checkPassword(String tokenPassword, Credential infoCredential) {
        String hashedTokenPassword = Base64.getEncoder().encodeToString(DigestUtils.sha3_512(tokenPassword));
        String cachedCredential = CACHED_CREDENTIALS.get(hashedTokenPassword);
        if (cachedCredential!=null && cachedCredential.equals(infoCredential.getCredentialKey())) {
            return true;
        }
        else if (BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey())) {
            CACHED_CREDENTIALS.put(hashedTokenPassword, infoCredential.getCredentialKey());
            return true;
        }
        else {
            CACHED_CREDENTIALS.remove(tokenPassword);
            return false;
        }
    }

}

class NoCachePasswordChecker implements PasswordChecker {

    public boolean checkPassword(String tokenPassword, Credential infoCredential) {
        return BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey());
    }

}