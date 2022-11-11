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
package org.eclipse.kapua.service.device.management.configuration.message.event.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.commons.message.event.KapuaEventPayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.message.event.DeviceConfigurationEventPayload;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * {@link DeviceConfigurationEventPayload} implementation.
 *
 * @since 2.0.0
 */
public class DeviceConfigurationEventPayloadImpl extends KapuaEventPayloadImpl implements DeviceConfigurationEventPayload {

    private static final long serialVersionUID = 1400605735748313538L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceConfigurationFactory DEVICE_CONFIGURATION_FACTORY = KapuaLocator.getInstance().getFactory(DeviceConfigurationFactory.class);

    @Override
    public List<DeviceComponentConfiguration> getDeviceComponentConfigurations() throws Exception {
        if (!hasBody()) {
            return Collections.emptyList();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceConfiguration.class).getComponentConfigurations();
    }

    @Override
    public void setDeviceComponentConfigurations(@NotNull List<DeviceComponentConfiguration> deviceComponentConfigurations) throws Exception {
        DeviceConfiguration deviceConfiguration = DEVICE_CONFIGURATION_FACTORY.newConfigurationInstance();
        deviceConfiguration.setComponentConfigurations(deviceComponentConfigurations);

        String bodyString = XmlUtil.marshal(deviceConfiguration);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
