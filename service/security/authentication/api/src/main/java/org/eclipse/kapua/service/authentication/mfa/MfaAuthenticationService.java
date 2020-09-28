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
package org.eclipse.kapua.service.authentication.mfa;

import org.eclipse.kapua.service.KapuaService;

import java.util.List;

/**
 * MfaAuthenticationService interface
 */
public interface MfaAuthenticationService extends KapuaService {

    /**
     * @return true if the MfaAuthenticationService is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Validates a code generated with the Google Authenticator app
     *
     * @param encryptedSecret  the encoded secret key
     * @param verificationCode the verification code
     * @return true if the verficationCode is valid, false otherwise
     */
    boolean authorize(String encryptedSecret, int verificationCode);

    /**
     * Validates a scratch code
     *
     * @param hasedScratchCode the hashed scratch code
     * @param authCode    the plaintext authentication code
     * @return true if the code match, false otherwise
     */
    boolean authorize(String hasedScratchCode, String authCode);

    /**
     * Generates the secret key
     *
     * @return the secret key in the form of a String
     */
    String generateKey();

    /**
     * Generates the scratch codes
     *
     * @return the list of scratch codes in the form of Strings
     */
    List<String> generateCodes();

}
