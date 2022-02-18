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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceLifecyclePayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @see KuraDisconnectMessage
 * @since 1.0.0
 */
public class KuraDisconnectPayload extends AbstractKuraLifecyclePayload implements DeviceLifecyclePayload {

    /**
     * Uptime metric name.
     *
     * @since 1.0.0
     */
    private static final String UPTIME = "uptime";

    /**
     * {@link Device} display name metric name.
     *
     * @since 1.0.0
     */
    private static final String DISPLAY_NAME = "display_name";

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraDisconnectPayload() {
        super();
    }

    /**
     * Gets the {@link Device} uptime.
     *
     * @return The {@link Device} uptime.
     * @since 1.0.0
     */
    public String getUptime() {
        return (String) getMetrics().get(UPTIME);
    }

    /**
     * Gets the {@link Device} display name.
     *
     * @return The {@link Device} display name.
     * @since 1.0.0
     */
    public String getDisplayName() {
        return (String) getMetrics().get(DISPLAY_NAME);
    }
}
