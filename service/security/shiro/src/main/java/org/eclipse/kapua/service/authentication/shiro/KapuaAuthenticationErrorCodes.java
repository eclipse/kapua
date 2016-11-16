/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Authentication error codes
 * 
 * since 1.0
 * 
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
}
