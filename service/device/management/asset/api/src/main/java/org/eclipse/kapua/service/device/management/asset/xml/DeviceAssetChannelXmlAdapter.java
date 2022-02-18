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
package org.eclipse.kapua.service.device.management.asset.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * {@link DeviceAssetChannel} {@link XmlAdapter} implementation.
 *
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
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
