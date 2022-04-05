/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s implementation for {@link KapuaAuthorizationException}.
 *
 * @since 1.0.0
 */
public enum KapuaAuthorizationErrorCodes implements KapuaErrorCode {
    /**
     * Invalid string permission representation
     */
    INVALID_STRING_PERMISSION,

    /**
     * See {@link SubjectUnauthorizedException}.
     *
     * @since 1.0.0
     */
    SUBJECT_UNAUTHORIZED,

    /**
     * One or more embedded entities is not found or does not match the scopeId of the root entity
     */
    ENTITY_SCOPE_MISSMATCH,

    /**
     * Subject is doing something on behalf of someone else
     */
    SELF_MANAGED_ONLY,

    /**
     * The operation is only allowed on internal users
     */
    INTERNAL_USER_ONLY
}
