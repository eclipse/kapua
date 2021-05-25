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
package org.eclipse.kapua.service.device.management.keystore.message.internal.request;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
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

    public DeviceKeystoreItemQuery getItemQuery() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreItemQuery();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreItemQuery.class);
    }

    public void setItemQuery(DeviceKeystoreItemQuery itemQuery) throws Exception {
        String bodyString = XmlUtil.marshal(itemQuery);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    public DeviceKeystoreCertificate getCertificate() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreCertificate();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreCertificate.class);
    }

    public void setCertificate(DeviceKeystoreCertificate certificate) throws Exception {
        String bodyString = XmlUtil.marshal(certificate);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    public DeviceKeystoreKeypair getKeypair() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreKeypair();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreKeypair.class);
    }

    public void setKeypair(DeviceKeystoreKeypair keypair) throws Exception {
        String bodyString = XmlUtil.marshal(keypair);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    public DeviceKeystoreCSR getCSR() throws Exception {
        if (!hasBody()) {
            return DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreCSR();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceKeystoreCSR.class);
    }

    public void setCsr(DeviceKeystoreCSR csr) throws Exception {
        String bodyString = XmlUtil.marshal(csr);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
