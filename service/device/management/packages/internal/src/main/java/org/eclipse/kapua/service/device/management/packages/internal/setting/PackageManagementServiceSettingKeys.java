/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for {@link PackageManagementServiceSetting}
 *
 * @since 1.1.0
 */
public enum PackageManagementServiceSettingKeys implements SettingKey {
    /**
     * The key value in the configuration resources.
     *
     * @since 1.1.0
     */
    PACKAGE_MANAGEMENT_SERVICE_SETTING_KEYS("service.device.management.package.key"),

    /**
     * @since 1.1.0
     */
    PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_SIZE("service.device.management.package.key.download.default.block.size"),
    /**
     * @since 1.1.0
     */
    PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_DELAY("service.device.management.package.key.download.default.block.delay"),
    /**
     * @since 1.1.0
     */
    PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_TIMEOUT("service.device.management.package.key.download.default.block.timeout"),
    /**
     * @since 1.1.0
     */
    PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_NOTIFY_BLOCK_SIZE("service.device.management.package.key.download.default.notify.block.size");

    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key The value mapped by this {@link Enum} value
     */
    private PackageManagementServiceSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link PackageManagementServiceSettingKeys}
     */
    @Override
    public String key() {
        return key;
    }
}
