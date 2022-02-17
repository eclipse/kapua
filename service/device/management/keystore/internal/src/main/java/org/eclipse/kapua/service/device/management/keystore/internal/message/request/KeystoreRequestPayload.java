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
package org.eclipse.kapua.service.device.management.keystore.internal.message.request;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;

/**
 * {@link DeviceKeystore} {@link KapuaRequestPayload} implementation.
 *
 * @since 1.5.0
 */
public class KeystoreRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    private static final long serialVersionUID = 837931637524736407L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceKeystoreManagementFactory DEVICE_KEYSTORE_MANAGEMENT_FACTORY = KapuaLocator.getInstance().getFactory(DeviceKeystoreManagementFactory.class);

    /**
     * Gets the {@link DeviceKeystoreItemQuery} from the {@link KapuaRequestPayload#getBody()}
     *
     * @return The {@link DeviceKeystoreItemQuery}.
     * @throws Exception if {@link KapuaRequestPayload#getBody()} is not a {@link DeviceKeystoreItemQuery}.
     * @since 1.5.0
     */
    public DeviceKeystoreItemQuery getItemQuery() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreItemQuery();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreItemQuery.class);
    }

    /**
     * Sets the {@link DeviceKeystoreItemQuery} into the {@link KapuaRequestPayload#getBody()}
     *
     * @param itemQuery The {@link DeviceKeystoreItemQuery}
     * @throws Exception if the given {@link DeviceKeystoreItemQuery} is not serializable into the {@link KapuaRequestPayload#getBody()}
     * @since 1.5.0
     */
    public void setItemQuery(DeviceKeystoreItemQuery itemQuery) throws Exception {
        String bodyString = XmlUtil.marshal(itemQuery);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreCertificate} from the {@link KapuaRequestPayload#getBody()}
     *
     * @return The {@link DeviceKeystoreCertificate}.
     * @throws Exception if {@link KapuaRequestPayload#getBody()} is not a {@link DeviceKeystoreCertificate}.
     * @since 1.5.0
     */
    public DeviceKeystoreCertificate getCertificate() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreCertificate();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreCertificate.class);
    }

    /**
     * Sets the {@link DeviceKeystoreCertificate} into the {@link KapuaRequestPayload#getBody()}
     *
     * @param certificate The {@link DeviceKeystoreCertificate}
     * @throws Exception if the given {@link DeviceKeystoreCertificate} is not serializable into the {@link KapuaRequestPayload#getBody()}
     * @since 1.5.0
     */
    public void setCertificate(DeviceKeystoreCertificate certificate) throws Exception {
        String bodyString = XmlUtil.marshal(certificate);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreKeypair} from the {@link KapuaRequestPayload#getBody()}
     *
     * @return The {@link DeviceKeystoreKeypair}.
     * @throws Exception if {@link KapuaRequestPayload#getBody()} is not a {@link DeviceKeystoreKeypair}.
     * @since 1.5.0
     */
    public DeviceKeystoreKeypair getKeypair() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreKeypair();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreKeypair.class);
    }

    /**
     * Sets the {@link DeviceKeystoreKeypair} into the {@link KapuaRequestPayload#getBody()}
     *
     * @param keypair The {@link DeviceKeystoreKeypair}
     * @throws Exception if the given {@link DeviceKeystoreKeypair} is not serializable into the {@link KapuaRequestPayload#getBody()}
     * @since 1.5.0
     */
    public void setKeypair(DeviceKeystoreKeypair keypair) throws Exception {
        String bodyString = XmlUtil.marshal(keypair);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceKeystoreCSRInfo} from the {@link KapuaRequestPayload#getBody()}
     *
     * @return The {@link DeviceKeystoreCSRInfo}.
     * @throws Exception if {@link KapuaRequestPayload#getBody()} is not a {@link DeviceKeystoreCSRInfo}.
     * @since 1.5.0
     */
    public DeviceKeystoreCSRInfo getCSRInfo() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreCSRInfo();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreCSRInfo.class);
    }

    /**
     * Sets the {@link DeviceKeystoreCSRInfo} into the {@link KapuaRequestPayload#getBody()}
     *
     * @param csrInfo The {@link DeviceKeystoreCSRInfo}
     * @throws Exception if the given {@link DeviceKeystoreCSRInfo} is not serializable into the {@link KapuaRequestPayload#getBody()}
     * @since 1.5.0
     */
    public void setCsrInfo(DeviceKeystoreCSRInfo csrInfo) throws Exception {
        String bodyString = XmlUtil.marshal(csrInfo);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
