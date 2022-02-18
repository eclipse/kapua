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
package org.eclipse.kapua.service.device.call.message.kura.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link DeviceCallSettings} for {@code kapua-device-call-kura} module.
 *
 * @see AbstractKapuaSetting
 * @since 1.0.0
 */
public class DeviceCallSettings extends AbstractKapuaSetting<DeviceCallSettingKeys> {

    /**
     * Setting filename.
     *
     * @since 1.0.0
     */
    private static final String DEVICE_CALL_SETTING_RESOURCE = "device-call-settings.properties";

    /**
     * Singleton instance.
     *
     * @since 1.0.0
     */
    private static final DeviceCallSettings INSTANCE = new DeviceCallSettings();

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private DeviceCallSettings() {
        super(DEVICE_CALL_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link DeviceCallSettings}.
     *
     * @return A singleton instance of {@link DeviceCallSettings}.
     * @since 1.0.0
     */
    public static DeviceCallSettings getInstance() {
        return INSTANCE;
    }
}
