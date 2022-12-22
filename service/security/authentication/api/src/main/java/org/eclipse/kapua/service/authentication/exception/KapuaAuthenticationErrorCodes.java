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
package org.eclipse.kapua.service.authentication.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link KapuaAuthenticationException}s.
 *
 * @since 1.0.0
 */
public enum KapuaAuthenticationErrorCodes implements KapuaErrorCode {
    /**
     * @see DuplicatedPasswordCredentialException
     * @since 2.0.0
     */
    DUPLICATED_PASSWORD_CREDENTIAL,

    /**
     * @see PasswordLengthException
     * @since 2.0.0
     */
    PASSWORD_INVALID_LENGTH,

    /**
     * User already logged
     *
     * @since 1.0.0
     */
    SUBJECT_ALREADY_LOGGED,

    /**
     * Invalid credentials provided
     *
     * @since 1.0.0
     */
    INVALID_CREDENTIALS_TYPE_PROVIDED,

    /**
     * Authentication error
     *
     * @since 1.0.0
     */
    AUTHENTICATION_ERROR,

    /**
     * Credential crypt error
     *
     * @since 1.0.0
     */
    CREDENTIAL_CRYPT_ERROR,

    /**
     * Login credential not found
     *
     * @since 1.0.0
     */
    UNKNOWN_LOGIN_CREDENTIAL,

    /**
     * Login credentials invalid
     *
     * @since 1.0.0
     */
    INVALID_LOGIN_CREDENTIALS,

    /**
     * Login credentials expired
     *
     * @since 1.0.0
     */
    EXPIRED_LOGIN_CREDENTIALS,

    /**
     * Login credentials locked
     *
     * @since 1.0.0
     */
    LOCKED_LOGIN_CREDENTIAL,

    /**
     * Login credentials disabled
     *
     * @since 1.0.0
     */
    DISABLED_LOGIN_CREDENTIAL,

    /**
     * Session credential not found
     *
     * @since 1.0.0
     */
    UNKNOWN_SESSION_CREDENTIAL,

    /**
     * Session credentials invalid
     *
     * @since 1.0.0
     */
    INVALID_SESSION_CREDENTIALS,

    /**
     * Session credentials expired
     *
     * @since 1.0.0
     */
    EXPIRED_SESSION_CREDENTIALS,

    /**
     * Session credentials locked
     *
     * @since 1.0.0
     */
    LOCKED_SESSION_CREDENTIAL,

    /**
     * Session credentials disabled
     *
     * @since 1.0.0
     */
    DISABLED_SESSION_CREDENTIAL,

    /**
     * JWK file error
     *
     * @since 1.0.0
     * @deprecated Since 2.0.0. No longer used.
     */
    @Deprecated
    JWK_FILE_ERROR,

    /**
     * Refresh error
     *
     * @since 1.0.0
     */
    REFRESH_ERROR,

    /**
     * JWK generation error
     *
     * @since 1.0.0
     * @deprecated Since 2.0.0. No longer used.
     */
    @Deprecated
    JWK_GENERATION_ERROR,

    /**
     * A Certificate with CertificateUsage equal to {@code JWT} is not present in the database.
     * This certificate must be installed at deployment time.
     *
     * @since 1.0.0
     */
    JWT_CERTIFICATE_NOT_FOUND,

    /**
     * The password cannot be changed
     *
     * @since 1.3.0
     * @deprecated Since 2.0.0. No longer used.
     */
    @Deprecated
    PASSWORD_CANNOT_BE_CHANGED,

    /**
     * Full MFA credentials are required.
     *
     * @since 1.4.0
     */
    REQUIRE_MFA_CREDENTIALS
}
