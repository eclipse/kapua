/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceInventory} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.5.0
 */
public class InventoryResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = 4380715272822080425L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceInventoryManagementFactory DEVICE_INVENTORY_MANAGEMENT_FACTORY = KapuaLocator.getInstance().getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Gets the {@link DeviceInventory} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventory} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventory getDeviceInventory() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventory();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventory.class);
    }

    /**
     * Sets the {@link DeviceInventory} in the {@link #getBody()}.
     *
     * @param deviceInventory The {@link DeviceInventory} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventory(@NotNull DeviceInventory deviceInventory) throws Exception {
        String bodyString = XmlUtil.marshal(deviceInventory);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceInventoryBundles} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventoryBundles} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventoryBundles getDeviceInventoryBundles() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventoryBundles();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventoryBundles.class);
    }

    /**
     * Sets the {@link DeviceInventoryBundles} in the {@link #getBody()}.
     *
     * @param deviceInventoryBundles The {@link DeviceInventoryBundles} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventoryBundles(@NotNull DeviceInventoryBundles deviceInventoryBundles) throws Exception {
        String bodyString = XmlUtil.marshal(deviceInventoryBundles);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
