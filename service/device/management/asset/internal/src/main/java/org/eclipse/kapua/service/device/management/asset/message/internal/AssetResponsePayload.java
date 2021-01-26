/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;

/**
 * {@link DeviceAssets} information {@link KapuaResponsePayload}.
 *
 * @since 1.0.0
 */
public class AssetResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final DeviceAssetFactory DEVICE_ASSET_FACTORY = KapuaLocator.getInstance().getFactory(DeviceAssetFactory.class);

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    public DeviceAssets getDeviceAssets() throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException, UnsupportedEncodingException {
        DeviceAssets deviceAssets = DEVICE_ASSET_FACTORY.newAssetListResult();

        if (hasBody()) {
            deviceAssets = XmlUtil.unmarshal(new String(getBody(), CHAR_ENCODING), DeviceAssets.class);
        }

        return deviceAssets;
    }

    public void setDeviceAssets(DeviceAssets deviceAssets) throws JAXBException, UnsupportedEncodingException {
        if (deviceAssets != null) {
            setBody(XmlUtil.marshal(deviceAssets).getBytes(CHAR_ENCODING));
        }
    }

}
