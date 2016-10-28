/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to device management settings
 * 
 * @since 1.0
 *
 */
public class DeviceManagementSetting extends AbstractKapuaSetting<DeviceManagementSettingKey>
{

    /**
     * Resource file from which source properties.
     * 
     */
    private static final String                  DEVICE_MANAGEMENT_SETTING_RESOURCE = "device-management-setting.properties";

    private static final DeviceManagementSetting instance                           = new DeviceManagementSetting();

    /**
     * Constructor
     */
    private DeviceManagementSetting()
    {
        super(DEVICE_MANAGEMENT_SETTING_RESOURCE);
    }

    /**
     * Get a singleton instance of {@link DeviceManagementSetting}.
     * 
     * @return
     */
    public static DeviceManagementSetting getInstance()
    {
        return instance;
    }
}
