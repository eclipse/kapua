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
public enum KapuaAuthenticationErrorCodes implements KapuaErrorCode
{
    /**
     * Invalid credentials provided
     */
    INVALID_CREDENTIALS_TOKEN_PROVIDED,
    /**
     * User already logged
     */
    SUBJECT_ALREADY_LOGGED,
    /**
     * Invalid username
     */
    INVALID_USERNAME,
    /**
     * Invalid credentials
     */
    INVALID_CREDENTIALS,
    /**
     * Expired credentials
     */
    EXPIRED_CREDENTIALS,
    /**
     * User locked
     */
    LOCKED_USERNAME,
    /**
     * User disabled
     */
    DISABLED_USERNAME,
    /**
     * Authentication error
     */
    AUTHENTICATION_ERROR,
    /**
     * Credential crypt error
     */
    CREDENTIAL_CRYPT_ERROR
}
