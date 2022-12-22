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
package org.eclipse.kapua.service.authorization.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link KapuaAuthorizationException}.
 *
 * @since 1.0.0
 */
public enum KapuaAuthorizationErrorCodes implements KapuaErrorCode {

    /**
     * See {@link SubjectUnauthorizedException}.
     *
     * @since 1.0.0
     */
    SUBJECT_UNAUTHORIZED,

    /**
     * See {@link SelfManagedOnlyException}.
     *
     * @since 1.4.0
     */
    SELF_MANAGED_ONLY,

    /**
     * See {@link InternalUserOnlyException}.
     *
     * @since 1.4.0
     */
    INTERNAL_USER_ONLY
}
