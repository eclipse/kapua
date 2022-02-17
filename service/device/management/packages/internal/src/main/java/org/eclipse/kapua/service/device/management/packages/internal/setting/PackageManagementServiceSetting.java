/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to {@link PackageManagementServiceSetting} settings.
 *
 * @since 1.1.0
 */
public class PackageManagementServiceSetting extends AbstractKapuaSetting<PackageManagementServiceSettingKeys> {

    /**
     * Resource file from which source properties.
     */
    private static final String PACKAGE_MANAGEMENT_CONFIG_RESOURCE = "service-device-management-package-setting.properties";

    private static final PackageManagementServiceSetting INSTANCE = new PackageManagementServiceSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link PackageManagementServiceSettingKeys#PACKAGE_MANAGEMENT_SERVICE_SETTING_KEYS} value.
     */
    private PackageManagementServiceSetting() {
        super(PACKAGE_MANAGEMENT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link PackageManagementServiceSetting}.
     *
     * @return A singleton instance of PackageManagementServiceSetting.
     */
    public static PackageManagementServiceSetting getInstance() {
        return INSTANCE;
    }
}
