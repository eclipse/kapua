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
package org.eclipse.kapua.service.device.call.message.app.event;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;

/**
 * {@link DeviceManagementEventChannel} definition.
 *
 * @since 2.0.0
 */
public interface DeviceManagementEventChannel extends DeviceChannel {

    /**
     * Gets the device application name.
     *
     * @return The device application name.
     * @since 2.0.0
     */
    String getAppName();

    /**
     * Sets the device application name.
     *
     * @param appName The device application name.
     * @since 2.0.0
     */
    void setAppName(String appName);

    /**
     * Gets the device application version.
     *
     * @return The device application version.
     * @since 2.0.0
     */
    String getAppVersion();

    /**
     * Sets the device application version.
     *
     * @param appVersion The device application version.
     * @since 2.0.0
     */
    void setAppVersion(String appVersion);

    /**
     * Get the request resources
     *
     * @return
     * @since 2.0.0
     */
    String[] getResources();

    /**
     * Set the request resources
     *
     * @param resources
     * @since 2.0.0
     */
    void setResources(String[] resources);

}
