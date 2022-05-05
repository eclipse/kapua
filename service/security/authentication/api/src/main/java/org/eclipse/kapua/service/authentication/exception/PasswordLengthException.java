/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.exception;

import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

/**
 * {@link KapuaAuthenticationException} to {@code throw} when a {@link Credential} with {@link CredentialType#PASSWORD}
 * has a {@link Credential#getCredentialKey()}/{@link CredentialCreator#getCredentialPlainKey()} too short or too long.
 *
 * @since 2.0.0
 */
public class PasswordLengthException extends KapuaAuthenticationException {

    private final int minLength;
    private final int maxLength;

    /**
     * Constructor.
     *
     * @param minLength The minimum allowed length for password.
     * @param maxLength The maximum allowed length for password.
     * @since 1.0.0
     */
    public PasswordLengthException(int minLength, int maxLength) {
        super(AuthenticationErrorCodes.PASSWORD_INVALID_LENGTH, minLength, maxLength);

        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Gets the minimum allowed length for password.
     *
     * @return The minimum allowed length for password.
     * @since 2.0.0
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Gets the maximum allowed length for password.
     *
     * @return The maximum allowed length for password.
     * @since 2.0.0
     */
    public int getMaxLength() {
        return maxLength;
    }
}
