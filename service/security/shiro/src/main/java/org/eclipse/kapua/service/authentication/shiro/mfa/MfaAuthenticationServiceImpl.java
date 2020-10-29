/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticationService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The {@link MfaAuthenticationService} implementation that wraps code generation and authentication methods.
 *
 */
@KapuaProvider
public class MfaAuthenticationServiceImpl implements MfaAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MfaAuthenticationServiceImpl.class);

    private static final GoogleAuthenticatorConfig GOOGLE_AUTHENTICATOR_CONFIG;

    static {
        // settings
        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        int timeStepSize = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_TIME_STEP_SIZE);
        long timeStepSizeInMillis = TimeUnit.SECONDS.toMillis(timeStepSize);
        int windowSize = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_WINDOW_SIZE);
        int scratchCodeNumber = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_SCRATCH_CODES_NUMBER);
        int codeDigitsNumber = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_CODE_DIGITS_NUMBER);

        try {
            ArgumentValidator.notNegative(timeStepSizeInMillis, "timeStepSizeInMillis");
            ArgumentValidator.notNegative(windowSize, "windowSize");
            ArgumentValidator.numRange(scratchCodeNumber, 0, 1000, "scratchCodeNumber");
            ArgumentValidator.numRange(codeDigitsNumber, 6, 8, "codeDigitsNumber");

            GOOGLE_AUTHENTICATOR_CONFIG = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                    .setTimeStepSizeInMillis(timeStepSizeInMillis) // the time step size, in milliseconds
                    .setWindowSize(windowSize)  // the number of windows of size timeStepSizeInMillis checked during the validation
                    .setNumberOfScratchCodes(scratchCodeNumber)  // number of scratch codes
                    .setCodeDigits(codeDigitsNumber)  // the number of digits in the generated code
                    .build();

        } catch (KapuaException e) {
            LOGGER.error("Error while initializing the Google Authenticator configuration", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean authorize(String encryptedSecret, int verificationCode) {
        boolean isCodeValid;
        String secret = AuthenticationUtils.decryptAes(encryptedSecret);

        GoogleAuthenticator ga = new GoogleAuthenticator(GOOGLE_AUTHENTICATOR_CONFIG);
        isCodeValid = ga.authorize(secret, verificationCode);
        return isCodeValid;
    }

    @Override
    public boolean authorize(String hasedScratchCode, String verificationCode) {
        return BCrypt.checkpw(verificationCode, hasedScratchCode);
    }

    @Override
    public String generateKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    @Override
    public List<String> generateCodes() {

        // we're not using the same secret as the secret key for the scratch code generation
        // this allows re-generating scratch codes independently from the secret

        GoogleAuthenticator gAuth = new GoogleAuthenticator(GOOGLE_AUTHENTICATOR_CONFIG);
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        List<String> scratchCodes = new ArrayList<>();
        for (Integer scratchCode : key.getScratchCodes()) {
            scratchCodes.add(scratchCode.toString());
        }
        return scratchCodes;
    }
}
