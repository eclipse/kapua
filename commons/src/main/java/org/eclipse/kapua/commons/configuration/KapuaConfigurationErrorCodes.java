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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Configuration error codes.
 *
 * @since 1.0
 */
public enum KapuaConfigurationErrorCodes implements KapuaErrorCode {

    /**
     * Internal error
     */
    INTERNAL_ERROR,
    /**
     * Illegal argument
     */
    ILLEGAL_ARGUMENT,
    /**
     * Operation not allowed
     */
    OPERATION_NOT_ALLOWED,
    /**
     * Invalid attribute
     */
    ATTRIBUTE_INVALID,
    /**
     * missing required attribute
     */
    REQUIRED_ATTRIBUTE_MISSING,
    /**
     * Self limit exceeded in config
     */
    SELF_LIMIT_EXCEEDED_IN_CONFIG,
    /**
     * Parent limit exceeded in config
     */
    PARENT_LIMIT_EXCEEDED_IN_CONFIG,
    /**
     * The service is not available
     */
    SERVICE_UNAVAILABLE
}
