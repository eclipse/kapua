/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;

/**
 * {@link DeviceLifecyclePayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraDisconnectPayload extends KuraPayload implements DeviceLifecyclePayload {

    /**
     * Uptime metric name
     */
    private static final String UPTIME = "uptime";
    /**
     * Uptime device displayble name metric name
     */
    private static final String DISPLAY_NAME = "display_name";

    /**
     * Constructor
     */
    public KuraDisconnectPayload() {
        super();
    }

    /**
     * Returns a displayable representation string
     *
     * @param uptime
     * @param displayName
     */
    public KuraDisconnectPayload(String uptime, String displayName) {
        super();

        getMetrics().put(UPTIME, uptime);
        getMetrics().put(DISPLAY_NAME, displayName);
    }

    /**
     * Ge tthe device uptime
     *
     * @return
     */
    public String getUptime() {
        return (String) getMetrics().get(UPTIME);
    }

    /**
     * Get the device displayable name
     *
     * @return
     */
    public String getDisplayName() {
        return (String) getMetrics().get(DISPLAY_NAME);
    }

    /**
     * Returns a displayable representation string
     *
     * @return
     */
    @Override
    public String toDisplayString() {
        return new StringBuilder()
                .append("[ getUptime()=")
                .append(getUptime())
                .append(", getDisplayName()=")
                .append(getDisplayName())
                .append("]")
                .toString();
    }
}
