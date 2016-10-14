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
package org.eclipse.kapua.locator.guice;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Kapua locator error codes.
 * 
 * @since 1.0
 *
 */
public enum KapuaLocatorErrorCodes implements KapuaErrorCode
{
    /**
     * Service unavailable
     */
    SERVICE_UNAVAILABLE,
    /**
     * Factory unavailable
     */
    FACTORY_UNAVAILABLE,
    /**
     * Service configuration service unavailable
     */
    SERVICE_CONFIG_SPI_UNAVAILABLE
}
