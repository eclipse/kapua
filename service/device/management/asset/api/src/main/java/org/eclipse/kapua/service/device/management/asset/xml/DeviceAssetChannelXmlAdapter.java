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
package org.eclipse.kapua.service.device.management.asset.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML adapter for {@link DeviceAssetChannel}.
 *
 * @since 1.0.0
 */
public class DeviceAssetChannelXmlAdapter extends XmlAdapter<XmlAdaptedDeviceAssetChannel, DeviceAssetChannel> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceAssetFactory DEVICE_ASSET_FACTORY = LOCATOR.getFactory(DeviceAssetFactory.class);

    @Override
    public XmlAdaptedDeviceAssetChannel marshal(DeviceAssetChannel deviceAssetChannel) throws Exception {

        XmlAdaptedDeviceAssetChannel xmlAdaptedDeviceAssetChannel = new XmlAdaptedDeviceAssetChannel();
        xmlAdaptedDeviceAssetChannel.setName(deviceAssetChannel.getName());
        xmlAdaptedDeviceAssetChannel.setValueType(deviceAssetChannel.getType());
        xmlAdaptedDeviceAssetChannel.setValue(ObjectValueConverter.toString(deviceAssetChannel.getValue()));
        xmlAdaptedDeviceAssetChannel.setMode(deviceAssetChannel.getMode());
        xmlAdaptedDeviceAssetChannel.setError(deviceAssetChannel.getError());
        xmlAdaptedDeviceAssetChannel.setTimestamp(deviceAssetChannel.getTimestamp());

        return xmlAdaptedDeviceAssetChannel;
    }

    @Override
    public DeviceAssetChannel unmarshal(XmlAdaptedDeviceAssetChannel xmlAdaptedDeviceAssetChannel) throws Exception {

        DeviceAssetChannel adaptedDeviceAssetChannel = DEVICE_ASSET_FACTORY.newDeviceAssetChannel();
        adaptedDeviceAssetChannel.setName(xmlAdaptedDeviceAssetChannel.getName());
        adaptedDeviceAssetChannel.setType(xmlAdaptedDeviceAssetChannel.getValueType());
        adaptedDeviceAssetChannel.setValue(ObjectValueConverter.fromString(xmlAdaptedDeviceAssetChannel.getValue(), adaptedDeviceAssetChannel.getType()));
        adaptedDeviceAssetChannel.setMode(xmlAdaptedDeviceAssetChannel.getMode());
        adaptedDeviceAssetChannel.setError(xmlAdaptedDeviceAssetChannel.getError());
        adaptedDeviceAssetChannel.setTimestamp(xmlAdaptedDeviceAssetChannel.getTimestamp());

        return adaptedDeviceAssetChannel;
    }
}
