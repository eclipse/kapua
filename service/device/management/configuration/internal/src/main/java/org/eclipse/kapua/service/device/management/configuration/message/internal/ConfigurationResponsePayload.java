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
package org.eclipse.kapua.service.device.management.configuration.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.internal.settings.DeviceConfigurationManagementSettings;
import org.eclipse.kapua.service.device.management.configuration.internal.settings.DeviceConfigurationManagementSettingsKeys;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

/**
 * {@link DeviceConfiguration} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.0.0
 */
public class ConfigurationResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationResponsePayload.class);

    private static final String PAYLOAD_TO_DISPLAY_STRING_MODE = DeviceConfigurationManagementSettings.getInstance().getString(DeviceConfigurationManagementSettingsKeys.PAYLOAD_TO_DISPLAY_STRING_MODE, "NONE");
    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceConfigurationFactory DEVICE_CONFIGURATION_FACTORY = KapuaLocator.getInstance().getFactory(DeviceConfigurationFactory.class);

    /**
     * Gets the {@link DeviceConfiguration}from the {@link #getBody()}.
     *
     * @return The {@link DeviceConfiguration}from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceConfiguration getDeviceConfigurations() throws Exception {
        if (!hasBody()) {
            return DEVICE_CONFIGURATION_FACTORY.newConfigurationInstance();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceConfiguration.class);
    }

    /**
     * Sets the {@link DeviceConfiguration} in the {@link #getBody()}.
     *
     * @param deviceConfiguration The {@link DeviceConfiguration}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceConfigurations(@NotNull DeviceConfiguration deviceConfiguration) throws Exception {
        String bodyString = XmlUtil.marshal(deviceConfiguration);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    @Override
    public String toDisplayString() {
        try {
            PayloadToDisplayStringMode toDisplayStringMode;
            try {
                toDisplayStringMode = PayloadToDisplayStringMode.valueOf(PAYLOAD_TO_DISPLAY_STRING_MODE);
            } catch (IllegalArgumentException iae) {
                LOG.warn("Invalid device.management.configuration.payload.toDisplayString.mode setting value {}. Please fix the configuration value. Allowed values are: NONE, DEFAULT, NUMBER_OF_COMPONENTS, LIST_OF_COMPONENTS. Defaulting to DEFAULT", PAYLOAD_TO_DISPLAY_STRING_MODE);
                toDisplayStringMode = PayloadToDisplayStringMode.DEFAULT;
            }

            switch (toDisplayStringMode) {
                case NONE:
                    return "";
                case DEFAULT:
                    return super.toDisplayString();
                case NUMBER_OF_COMPONENTS:
                    return "Read " + getDeviceConfigurations().getComponentConfigurations().size() + " configuration components: " + getDeviceConfigurations().getComponentConfigurations().stream().map(DeviceComponentConfiguration::getId).sorted(String::compareTo).collect(Collectors.joining(", "));
                case LIST_OF_COMPONENTS:
                    return "Read configuration components: " + getDeviceConfigurations().getComponentConfigurations().stream().map(DeviceComponentConfiguration::getId).sorted(String::compareTo).collect(Collectors.joining(", "));
            }
        } catch (Exception e) {
            LOG.warn("Error while invoking ConfigurationResponsePayload.toDisplayString(). Defaulting to KapuaResponsePayload.toDisplayString(). Error: {}", e.getMessage());
        }

        return super.toDisplayString();
    }

    /**
     * Defines the way the {@link #toDisplayString()} will behave.
     *
     * @since 2.0.0
     */
    private enum PayloadToDisplayStringMode {

        /**
         * Displays nothing.
         *
         * @since 2.0.0
         */
        NONE,

        /**
         * Displays what {@link KapuaPayload#toDisplayString()} displays (only metrics currently).
         *
         * @since 2.0.0
         */
        DEFAULT,

        /**
         * Displays the number of {@link DeviceComponentConfiguration} in the {@link DeviceConfiguration}.
         *
         * @since 2.0.0
         */
        NUMBER_OF_COMPONENTS,

        /**
         * Displays the list of {@link DeviceComponentConfiguration} in the {@link DeviceConfiguration}.
         *
         * @since 2.0.0
         */
        LIST_OF_COMPONENTS,
    }
}


