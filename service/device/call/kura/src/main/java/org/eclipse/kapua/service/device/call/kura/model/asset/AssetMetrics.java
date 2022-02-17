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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.asset;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;

/**
 * Asset {@link DeviceMetrics}.
 *
 * @since 1.0.0
 */
public enum AssetMetrics implements DeviceAppMetrics {

    /**
     * Application identifier.
     *
     * @since 1.0.0
     */
    APP_ID("ASSET"),

    /**
     * Application version.
     *
     * @since 1.0.0
     */
    APP_VERSION("V1"),
    ;

    /**
     * The name of the metric.
     *
     * @since 1.0.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The name of the metric.
     * @since 1.0.0
     */
    AssetMetrics(String name) {
        this.name = name;
    }

    /**
     * Gets the value property associated to this specific enumeration key.
     *
     * @return the value property associated to this specific enumeration key.
     * @since 1.0.0
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
