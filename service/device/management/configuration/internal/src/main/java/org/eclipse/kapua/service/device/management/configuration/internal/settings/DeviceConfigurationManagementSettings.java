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
package org.eclipse.kapua.service.device.management.configuration.internal.settings;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;

/**
 * {@link DeviceConfigurationManagementService} {@link AbstractBaseKapuaSetting}s
 *
 * @since 2.0.0
 */
public class DeviceConfigurationManagementSettings extends AbstractKapuaSetting<DeviceConfigurationManagementSettingsKeys> {

    private static final String DEVICE_CONFIGURATION_MANAGEMENT_SETTING_RESOURCE = "device-configuration-management-setting.properties";

    private static final DeviceConfigurationManagementSettings INSTANCE = new DeviceConfigurationManagementSettings();

    /**
     * Constructor.
     * @since 2.0.0
     */
    private DeviceConfigurationManagementSettings() {
        super(DEVICE_CONFIGURATION_MANAGEMENT_SETTING_RESOURCE);
    }

    /**
     * Gets the instance of {@link DeviceConfigurationManagementSettings}.
     *
     * @return The instance of {@link DeviceConfigurationManagementSettings}.
     * @since 2.0.0
     */
    public static DeviceConfigurationManagementSettings getInstance() {
        return INSTANCE;
    }
}
