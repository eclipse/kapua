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
package org.eclipse.kapua.service.authentication.mfa;

import org.eclipse.kapua.KapuaException;

import java.util.List;

/**
 * {@link MfaAuthenticator} definition.
 *
 * @since 1.3.0
 */
public interface MfaAuthenticator {

    /**
     * Whether the {@link MfaAuthenticator} service is enabled or not.
     *
     * @return {@code true} if the {@link MfaAuthenticator} is enabled, {@code false} otherwise.
     * @since 1.3.0
     */
    boolean isEnabled();

    /**
     * Validates a code generated with the authenticator app.
     *
     * @param mfaSecretKey     The MFA secret key.
     * @param verificationCode The verification code
     * @return {@code true} if the verficationCode is valid, {@code false} otherwise.
     * @throws KapuaException
     * @since 1.3.0
     */
    boolean authorize(String mfaSecretKey, int verificationCode) throws KapuaException;

    /**
     * Validates a scratch code.
     *
     * @param hashedScratchCode The hashed scratch code
     * @param authCode          The plaintext authentication code
     * @return {@code true} if the code match, {@code false} otherwise
     * @throws KapuaException
     * @since 1.3.0
     */
    boolean authorize(String hashedScratchCode, String authCode) throws KapuaException;

    /**
     * Generates the secret key
     *
     * @return The secret key in the form of a {@link String}
     * @throws KapuaException
     * @since 1.3.0
     */
    String generateKey() throws KapuaException;

    /**
     * Generates the {@link List} of scratch codes.
     *
     * @return the list of scratch codes in the form of Strings
     * @throws KapuaException
     * @since 1.3.0
     */
    List<String> generateCodes() throws KapuaException;
}
