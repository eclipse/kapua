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
package org.eclipse.kapua.service.device.management.commons.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to device management settings
 *
 * @since 1.0
 *
 */
public class DeviceManagementSetting extends AbstractKapuaSetting<DeviceManagementSettingKey> {

    /**
     * Resource file from which source properties.
     *
     */
    private static final String DEVICE_MANAGEMENT_SETTING_RESOURCE = "device-management-setting.properties";

    private static final DeviceManagementSetting INSTANCE = new DeviceManagementSetting();

    /**
     * Constructor
     */
    private DeviceManagementSetting() {
        super(DEVICE_MANAGEMENT_SETTING_RESOURCE);
    }

    /**
     * Get a singleton instance of {@link DeviceManagementSetting}.
     *
     * @return
     */
    public static DeviceManagementSetting getInstance() {
        return INSTANCE;
    }
}
