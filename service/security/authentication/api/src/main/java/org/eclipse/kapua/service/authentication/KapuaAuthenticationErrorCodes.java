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
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Authentication error codes
 *
 * since 1.0
 */
public enum KapuaAuthenticationErrorCodes implements KapuaErrorCode {
    /**
     * User already logged
     */
    SUBJECT_ALREADY_LOGGED,

    /**
     * Invalid credentials provided
     */
    INVALID_CREDENTIALS_TYPE_PROVIDED,

    /**
     * Authentication error
     */
    AUTHENTICATION_ERROR,

    /**
     * Credential crypt error
     */
    CREDENTIAL_CRYPT_ERROR,

    /**
     * Login credential not found
     */
    UNKNOWN_LOGIN_CREDENTIAL,

    /**
     * Login credentials invalid
     */
    INVALID_LOGIN_CREDENTIALS,

    /**
     * Login credentials expired
     */
    EXPIRED_LOGIN_CREDENTIALS,

    /**
     * Login credentials locked
     */
    LOCKED_LOGIN_CREDENTIAL,

    /**
     * Login credentials disabled
     */
    DISABLED_LOGIN_CREDENTIAL,

    /**
     * Session credential not found
     */
    UNKNOWN_SESSION_CREDENTIAL,

    /**
     * Session credentials invalid
     */
    INVALID_SESSION_CREDENTIALS,

    /**
     * Session credentials expired
     */
    EXPIRED_SESSION_CREDENTIALS,

    /**
     * Session credentials locked
     */
    LOCKED_SESSION_CREDENTIAL,

    /**
     * Session credentials disabled
     */
    DISABLED_SESSION_CREDENTIAL,

    /**
     * JWK file error
     */
    JWK_FILE_ERROR,

    /**
     * Refresh error
     */
    REFRESH_ERROR,

    /**
     * JWK generation error
     */
    JWK_GENERATION_ERROR,

    /**
     * A Certificate with CertificateUsage equal to {@code JWT} is not present in the database.
     * This certificate must be installed at deployment time.
     */
    JWT_CERTIFICATE_NOT_FOUND,

    /**
     * The password cannot be changed
     */
    PASSWORD_CANNOT_BE_CHANGED,

    /**
     * Full MFA credentials are required.
     */
    REQUIRE_MFA_CREDENTIALS;
}
