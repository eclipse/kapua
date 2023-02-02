/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;

/**
 * {@link SettingKey}s for KapuaDeviceRegistrySettings.
 *
 * @since 1.0.0
 */
public enum KapuaDeviceRegistrySettingKeys implements SettingKey {

    /**
     * The key value in the configuration resources.
     *
     * @since 1.0.0
     */
    DEVICE_REGISTRY_KEY("device.registry.key"),

    /**
     * @since 1.0.0
     */
    DEVICE_EVENT_ADDRESS("device.eventAddress"),

    /**
     * Gets the hard limit for {@link KapuaBirthMessage} generic fields length.
     *
     * @since 2.0.0
     */
    DEVICE_LIFECYCLE_BIRTH_VAR_FIELDS_LENGTH_MAX("device.lifecycle.birth.var.fields.length.max"),

    /**
     * Gets the hard limit for {@link KapuaBirthMessage} {@link DeviceExtendedProperty#getValue()} fields length hdrd limit.
     *
     * @since 2.0.0
     */
    DEVICE_LIFECYCLE_BIRTH_EXTENDED_PROPERTIES_LENGTH_MAX("device.lifecycle.birth.extended.properties.length.max"),

    ;

    private final String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key The value mapped by this {@link Enum} value
     * @since 1.0.0
     */
    KapuaDeviceRegistrySettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaDeviceRegistrySettingKeys}
     *
     * @since 1.0.0
     */
    @Override
    public String key() {
        return key;
    }
}
