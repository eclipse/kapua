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
package org.eclipse.kapua.service.device.management.message.event;

import org.eclipse.kapua.service.device.management.message.KapuaAppChannel;
import org.eclipse.kapua.service.device.management.message.KapuaEventChannel;

import javax.xml.bind.annotation.XmlElement;

/**
 * Device event {@link KapuaAppChannel} definition.
 *
 * @since 2.0.0
 */
public interface KapuaManagementEventChannel extends KapuaEventChannel {


    /**
     * Gets the device application name.
     *
     * @return The device application name.
     * @since 2.0.0
     */
    @XmlElement(name = "appName")
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
    @XmlElement(name = "appVersion")
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
     * @return test
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
