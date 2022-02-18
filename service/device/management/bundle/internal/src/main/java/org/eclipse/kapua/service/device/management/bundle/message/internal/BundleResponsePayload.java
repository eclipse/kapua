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
package org.eclipse.kapua.service.device.management.bundle.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceBundle} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.0.0
 */
public class BundleResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = 4380715272822080425L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceBundleFactory DEVICE_BUNDLE_FACTORY = KapuaLocator.getInstance().getFactory(DeviceBundleFactory.class);

    /**
     * Gets the {@link DeviceBundles} from the {@link #getBody()}.
     *
     * @return The {@link DeviceBundles} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceBundles getDeviceBundles() throws Exception {
        if (!hasBody()) {
            return DEVICE_BUNDLE_FACTORY.newBundleListResult();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceBundles.class);
    }

    /**
     * Sets the {@link DeviceBundles} in the {@link #getBody()}.
     *
     * @param deviceBundles The {@link DeviceBundles} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceBundles(@NotNull DeviceBundles deviceBundles) throws Exception {
        String bodyString = XmlUtil.marshal(deviceBundles);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
