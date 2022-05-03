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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link KapuaAuthenticationException}s.
 *
 * @since 2.0.0
 */
public enum AuthenticationErrorCodes implements KapuaErrorCode {

    /**
     * @see DuplicatedPasswordCredentialException
     * @since 2.0.0
     */
    DUPLICATED_PASSWORD_CREDENTIAL,

    /**
     * @see PasswordLengthException
     * @since 2.0.0
     */
    PASSWORD_INVALID_LENGTH
}
