/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.device.lifecycle;

/**
 * {@link KapuaDisconnectPayload} definition.
 *
 * @since 1.0.0
 */
public interface KapuaDisconnectPayload extends KapuaLifecyclePayload {

    /**
     * Gets the device uptime.
     *
     * @return The device uptime.
     * @since 1.0.0
     */
    String getUptime();

    /**
     * Sets the device uptime.
     *
     * @param uptime The device uptime.
     * @since 1.1.0
     */
    void setUptime(String uptime);

    /**
     * Gets the device display name.
     *
     * @return The device display name.
     * @since 1.0.0
     */
    String getDisplayName();

    /**
     * Sets the device display name.
     *
     * @param displayName The device display name
     * @since 1.1.0
     */
    void setDisplayName(String displayName);
}
