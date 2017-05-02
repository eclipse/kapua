/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.message.internal;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;
import org.xml.sax.SAXException;

/**
 * Device bundle information request payload.
 * 
 * @since 1.0
 * 
 */
public class AssetRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    private static final DeviceAssetFactory deviceAssetFactory = KapuaLocator.getInstance().getFactory(DeviceAssetFactory.class);
    private static final DeviceManagementSetting config = DeviceManagementSetting.getInstance();
    private static final String CHAR_ENCODING = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

    public DeviceAssets getDeviceAssets() throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException, UnsupportedEncodingException {
        DeviceAssets deviceAssets = deviceAssetFactory.newAssetListResult();
        byte[] body = getBody();
        if (body != null && body.length > 0) {
            deviceAssets = XmlUtil.unmarshal(new String(body, CHAR_ENCODING), DeviceAssets.class);
        }

        return deviceAssets;
    }

    public void setDeviceAssets(DeviceAssets deviceAssets) throws JAXBException, UnsupportedEncodingException {
        if (deviceAssets != null) {
            setBody(XmlUtil.marshal(deviceAssets).getBytes(CHAR_ENCODING));
        }
    }

}
