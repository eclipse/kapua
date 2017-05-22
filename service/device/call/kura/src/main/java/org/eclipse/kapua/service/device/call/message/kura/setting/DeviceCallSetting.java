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
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to device call settings
 * 
 * @since 1.0
 *
 */
public class DeviceCallSetting extends AbstractKapuaSetting<DeviceCallSettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     */
    private static final String DEVICE_CALL_SETTING_RESOURCE = "device-call-setting.properties";

    private static final DeviceCallSetting INSTANCE = new DeviceCallSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link DeviceCallSetting#DEVICE_CALL_SETTING_RESOURCE} value.
     * 
     */
    private DeviceCallSetting() {
        super(DEVICE_CALL_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link DeviceCallSetting}.
     * 
     * @return A singleton instance of DeviceCallSetting.
     */
    public static DeviceCallSetting getInstance() {
        return INSTANCE;
    }
}
