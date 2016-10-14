package org.eclipse.kapua.service.device.management.bundle.internal;

import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;

public class DeviceBundleFactoryImpl implements DeviceBundleFactory
{

    @Override
    public DeviceBundles newBundleListResult()
    {
        return new DeviceBundlesImpl();
    }

    @Override
    public DeviceBundle newDeviceBundle()
    {
        return new DeviceBundleImpl();
    }

}
