/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for user service
 * 
 * @since 1.0
 *
 */
public enum KapuaDeviceRegistrySettingKeys implements SettingKey {

    /**
     * The key value in the configuration resources.
     */
    DEVICE_REGISTRY_KEY("device_registry.key"),
    DEVICE_EVENT_ADDRESS("device.eventAddress");

    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     * 
     * @param key
     *            The value mapped by this {@link Enum} value
     * 
     * @since 1.0
     */
    private KapuaDeviceRegistrySettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaDeviceRegistrySettingKeys}
     * 
     * @since 1.0.0
     * 
     */
    public String key() {
        return key;
    }
}