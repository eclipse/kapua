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

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;

import javax.inject.Inject;

/**
 * {@link DeviceConfigurationManagementService} {@link AbstractBaseKapuaSetting}s
 *
 * @since 2.0.0
 */
public class DeviceConfigurationManagementSettings extends AbstractKapuaSetting<DeviceConfigurationManagementSettingsKeys> {

    private static final String DEVICE_CONFIGURATION_MANAGEMENT_SETTING_RESOURCE = "device-configuration-management-setting.properties";

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    @Inject
    public DeviceConfigurationManagementSettings() {
        super(DEVICE_CONFIGURATION_MANAGEMENT_SETTING_RESOURCE);
    }
}
