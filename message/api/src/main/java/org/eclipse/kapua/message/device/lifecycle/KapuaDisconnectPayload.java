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
