/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * {@link MfaAuthenticator} implementation.
 *
 * @since 1.3.0
 */
public class MfaAuthenticatorImpl implements MfaAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(MfaAuthenticatorImpl.class);

    private static final KapuaAuthenticationSetting AUTHENTICATION_SETTING = KapuaAuthenticationSetting.getInstance();
    private static final GoogleAuthenticatorConfig GOOGLE_AUTHENTICATOR_CONFIG;

    static {
        // Setup of Google Authenticator Configs
        int timeStepSize = AUTHENTICATION_SETTING.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_TIME_STEP_SIZE);
        long timeStepSizeInMillis = TimeUnit.SECONDS.toMillis(timeStepSize);
        int windowSize = AUTHENTICATION_SETTING.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_WINDOW_SIZE);
        int scratchCodeNumber = AUTHENTICATION_SETTING.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_SCRATCH_CODES_NUMBER);
        int codeDigitsNumber = AUTHENTICATION_SETTING.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_CODE_DIGITS_NUMBER);

        try {
            ArgumentValidator.notNegative(timeStepSizeInMillis, "timeStepSizeInMillis");
            ArgumentValidator.notNegative(windowSize, "windowSize");
            ArgumentValidator.numRange(scratchCodeNumber, 0, 1000, "scratchCodeNumber");
            ArgumentValidator.numRange(codeDigitsNumber, 6, 8, "codeDigitsNumber");

            GOOGLE_AUTHENTICATOR_CONFIG = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                    .setTimeStepSizeInMillis(timeStepSizeInMillis) // The time step size, in milliseconds
                    .setWindowSize(windowSize)  // The number of windows of size timeStepSizeInMillis checked during the validation
                    .setNumberOfScratchCodes(scratchCodeNumber)  // Number of scratch codes
                    .setCodeDigits(codeDigitsNumber)  // The number of digits in the generated code
                    .build();

        } catch (KapuaException e) {
            LOG.error("Error while initializing the Google Authenticator configuration", e);
            throw new ExceptionInInitializerError(e);
        }

        // Print Configuration
        ConfigurationPrinter configurationPrinter =
                ConfigurationPrinter
                        .create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("Google Authenticator Configuration")
                        .addParameter("Time step", timeStepSizeInMillis)
                        .addParameter("Window size", windowSize)
                        .addParameter("Scratch code number", scratchCodeNumber)
                        .addParameter("Scratch code digits", codeDigitsNumber);

        configurationPrinter.printLog();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean authorize(String mfaSecretKey, int verificationCode) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(mfaSecretKey, "mfaSecretKey");
        ArgumentValidator.notNegative(verificationCode, "verificationCode");

        //
        // Do check
        GoogleAuthenticator ga = new GoogleAuthenticator(GOOGLE_AUTHENTICATOR_CONFIG);

        return ga.authorize(mfaSecretKey, verificationCode);
    }

    @Override
    public boolean authorize(String hashedScratchCode, String verificationCode) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notEmptyOrNull(hashedScratchCode, "hashedScratchCode");
        ArgumentValidator.notEmptyOrNull(verificationCode, "verificationCode");

        //
        // Do check
        return BCrypt.checkpw(verificationCode, hashedScratchCode);
    }

    @Override
    public String generateKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }


    /**
     * We're not using the same secret as the secret key for the scratch code generation
     * this allows re-generating scratch codes independently of the secret.
     *
     * @see MfaAuthenticator#generateCodes()
     */
    @Override
    public List<String> generateCodes() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator(GOOGLE_AUTHENTICATOR_CONFIG);
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        List<String> scratchCodes = new ArrayList<>();
        for (Integer scratchCode : key.getScratchCodes()) {
            scratchCodes.add(scratchCode.toString());
        }
        return scratchCodes;
    }
}
