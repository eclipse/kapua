/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.keystore;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;

/**
 * Keystore {@link DeviceMetrics}.
 *
 * @since 1.5.0
 */
public enum KeystoreMetrics implements DeviceAppMetrics {

    /**
     * Application identifier.
     *
     * @since 1.5.0
     */
    APP_ID("KEYS"),

    /**
     * Application version.
     *
     * @since 1.5.0
     */
    APP_VERSION("V1"),
    ;

    /**
     * The name of the metric.
     *
     * @since 1.5.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The name of the metric.
     * @since 1.5.0
     */
    KeystoreMetrics(String name) {
        this.name = name;
    }

    /**
     * Gets the value property associated to this specific enumeration key.
     *
     * @return the value property associated to this specific enumeration key.
     * @since 1.5.0
     * @deprecated Since 1.2.0. Renamed to {@link #getName()}.
     */
    @Deprecated
    public String getValue() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
