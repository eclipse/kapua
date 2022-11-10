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

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * {@link SettingKey}s for {@link DeviceConfigurationManagementSettings}.
 *
 * @since 2.0.0
 */
public enum DeviceConfigurationManagementSettingsKeys implements SettingKey {

    /**
     * Character encoding.
     *
     * @since 2.0.0
     */
    PAYLOAD_TO_DISPLAY_STRING_MODE("device.management.configuration.payload.toDisplayString.mode"),
    ;

    private final String key;

    /**
     * Constructor.
     *
     * @param key The value of the {@link DeviceConfigurationManagementSettingsKeys}.
     * @since 1.0.0
     */
    DeviceConfigurationManagementSettingsKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
