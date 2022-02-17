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
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceConfiguration} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.0.0
 */
public class ConfigurationResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = 3537585208524333690L;

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
}
