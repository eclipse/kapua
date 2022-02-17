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

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Device Management {@link SettingKey}s for {@link DeviceManagementSetting}.
 *
 * @since 1.0.0
 */
public enum DeviceManagementSettingKey implements SettingKey {

    /**
     * Character encoding.
     *
     * @since 1.0.0
     */
    CHAR_ENCODING("device.management.character.encoding"),

    /**
     * Request timeout.
     *
     * @since 1.0.0
     */
    REQUEST_TIMEOUT("device.management.request.timeout"),

    /**
     * Request timeout.
     *
     * @since 1.0.0
     */
    SHOW_STACKTRACE("device.management.response.stacktrace.show"),
    ;

    private final String key;

    /**
     * Constructor.
     *
     * @param key The value of the {@link DeviceManagementSettingKey}.
     * @since 1.0.0
     */
    DeviceManagementSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
