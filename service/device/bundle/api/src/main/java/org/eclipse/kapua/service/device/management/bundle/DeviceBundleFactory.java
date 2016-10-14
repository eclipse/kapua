package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.model.KapuaObjectFactory;

public interface DeviceBundleFactory extends KapuaObjectFactory
{
    public DeviceBundles newBundleListResult();

    public DeviceBundle newDeviceBundle();
}
