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
package org.eclipse.kapua.service.device.management.inventory.internal.message;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceInventory} {@link KapuaRequestPayload} implementation.
 *
 * @since 1.5.0
 */
public class InventoryRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    private static final long serialVersionUID = 837931637524736407L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceInventoryManagementFactory DEVICE_INVENTORY_MANAGEMENT_FACTORY = KapuaLocator.getInstance().getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Gets the {@link DeviceInventoryBundle} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventoryBundle} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventoryBundle getDeviceInventoryBundle() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventoryBundle();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventoryBundle.class);
    }

    /**
     * Sets the {@link DeviceInventoryBundle} in the {@link #getBody()}.
     *
     * @param deviceInventoryBundle The {@link DeviceInventoryBundle} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventoryBundle(@NotNull DeviceInventoryBundle deviceInventoryBundle) throws Exception {
        String bodyString = XmlUtil.marshal(deviceInventoryBundle);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
