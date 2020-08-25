/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.exception;

import org.eclipse.kapua.KapuaErrorCode;


public enum SsoErrorCodes implements KapuaErrorCode {

    /**
     * An error occurred while building the SSO login URI.
     *
     * @since 1.2.0
     */
    LOGIN_URI_ERROR,

    /**
     * An error occurred while building the SSO logout URI.
     *
     * @since 1.2.0
     */
    LOGOUT_URI_ERROR,

    /**
     * An error occurred while getting the access token.
     *
     * @since 1.2.0
     */
    ACCESS_TOKEN_ERROR,

    /**
     * An error occurred while extracting the Jwt.
     *
     * @since 1.2.0
     */
    JWT_EXTRACTION_ERROR,

    /**
     * An error occurred while processing the Jwt.
     *
     * @since 1.2.0
     */
    JWT_PROCESS_ERROR,

    /**
     * An error occurred while retrieving the SSO Jwt URI.
     *
     * @since 1.2.0
     */
    JWT_URI_ERROR,

    /**
     * Illegal argument
     *
     * @since 1.2.0
     */
    ILLEGAL_ARGUMENT,

    /**
     * Illegal URI
     *
     * @since 1.2.0
     */
    ILLEGAL_URI

}
