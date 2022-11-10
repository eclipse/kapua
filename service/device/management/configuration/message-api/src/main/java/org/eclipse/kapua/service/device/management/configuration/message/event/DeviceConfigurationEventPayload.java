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
package org.eclipse.kapua.service.device.management.configuration.message.event;

import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.message.event.KapuaManagementEventPayload;

import java.util.List;

/**
 * {@link DeviceConfiguration}  {@link KapuaManagementEventPayload} definition.
 *
 * @since 2.0.0
 */
public interface DeviceConfigurationEventPayload extends KapuaManagementEventPayload {

    /**
     * Gets the {@link List} of changed {@link DeviceComponentConfiguration}s
     *
     * @return The {@link List} of changed {@link DeviceComponentConfiguration}s
     * @since 2.0.0
     */
    List<DeviceComponentConfiguration> getDeviceComponentConfigurations() throws Exception;

    /**
     * Sets the {@link List} of changed {@link DeviceComponentConfiguration}s
     *
     * @param deviceComponentConfigurations The {@link List} of changed {@link DeviceComponentConfiguration}s
     * @since 2.0.0
     */
    void setDeviceComponentConfigurations(List<DeviceComponentConfiguration> deviceComponentConfigurations) throws Exception;
}
