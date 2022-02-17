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
package org.eclipse.kapua.locator;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Kapua locator error codes.
 *
 * @since 1.0
 *
 */
public enum KapuaLocatorErrorCodes implements KapuaErrorCode {
    /**
     * Service unavailable
     */
    SERVICE_UNAVAILABLE,
    /**
     * Invalid service provider
     */
    SERVICE_PROVIDER_INVALID,
    /**
     * Factory unavailable
     */
    FACTORY_UNAVAILABLE,
    /**
     * Invalid factory provider
     */
    FACTORY_PROVIDER_INVALID,
    /**
     * Component unavailable
     */
    COMPONENT_UNAVAILABLE,
    /**
     * Invalid component provider
     */
    COMPONENT_PROVIDER_INVALID,
    /**
     * Invalid locator configuration
     */
    INVALID_CONFIGURATION,
}
