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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.KapuaIllegalArgumentException;

/**
 * Device connection status.
 *
 * @since 1.0
 */
public enum DeviceConnectionStatus {
    /**
     * Connected
     */
    CONNECTED,
    /**
     * Disconnected
     */
    DISCONNECTED,
    /**
     * Missing
     */
    MISSING,
    /**
     * Value when andPredicate is null
     */
    NULL;

    public static DeviceConnectionStatus fromString(String value) throws KapuaIllegalArgumentException {
        String ucValue = value.toUpperCase();
        try {
            return valueOf(ucValue);
        } catch (Exception e) {
            throw new KapuaIllegalArgumentException("connectionStatus", value);
        }
    }

}
