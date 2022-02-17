/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.internal.message.response;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceKeystore} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.5.0
 */
public class KeystoreResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = 4380715272822080425L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceKeystoreManagementFactory DEVICE_KEYSTORE_MANAGEMENT_FACTORY = KapuaLocator.getInstance().getFactory(DeviceKeystoreManagementFactory.class);

    /**
     * Gets the {@link DeviceKeystores} from the {@link #getBody()}.
     *
     * @return The {@link DeviceKeystores} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceKeystores getKeystores() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystores();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystores.class);
    }

    /**
     * Sets the {@link DeviceKeystores} in the {@link #getBody()}.
     *
     * @param keystores The {@link DeviceKeystores} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setKeystores(@NotNull DeviceKeystores keystores) throws Exception {
        String bodyString = XmlUtil.marshal(keystores);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreItems} from the {@link #getBody()}.
     *
     * @return The {@link DeviceKeystoreItems} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceKeystoreItems getKeystoreItems() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreItems();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreItems.class);
    }

    /**
     * Sets the {@link DeviceKeystoreItems} in the {@link #getBody()}.
     *
     * @param keystoreItems The {@link DeviceKeystoreItems} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setKeystoreItems(@NotNull DeviceKeystoreItems keystoreItems) throws Exception {
        String bodyString = XmlUtil.marshal(keystoreItems);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreItem} from the {@link #getBody()}.
     *
     * @return The {@link DeviceKeystoreItem} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceKeystoreItem getKeystoreItem() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreItem();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreItem.class);
    }

    /**
     * Sets the {@link DeviceKeystoreItem} in the {@link #getBody()}.
     *
     * @param keystoreItem The {@link DeviceKeystoreItem} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setKeystoreItem(@NotNull DeviceKeystoreItem keystoreItem) throws Exception {
        String bodyString = XmlUtil.marshal(keystoreItem);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreCSR} from the {@link #getBody()}.
     *
     * @return The {@link DeviceKeystoreCSR} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceKeystoreCSR getCSR() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreCSR();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreCSR.class);
    }

    /**
     * Sets the {@link DeviceKeystoreCSR} in the {@link #getBody()}.
     *
     * @param deviceCSR The {@link DeviceKeystoreCSR} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setCSR(@NotNull DeviceKeystoreCSR deviceCSR) throws Exception {
        String bodyString = XmlUtil.marshal(deviceCSR);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
