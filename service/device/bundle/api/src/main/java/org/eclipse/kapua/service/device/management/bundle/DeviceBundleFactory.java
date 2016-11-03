package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Device bundle entity service factory definition.
 *
 * @since 1.0
 * 
 */
public interface DeviceBundleFactory extends KapuaObjectFactory
{

    /**
     * Creates a new device bundle list
     * 
     * @return
     */
    public DeviceBundles newBundleListResult();

    /**
     * Create a new {@link DeviceBundle}
     * 
     * @return
     */
    public DeviceBundle newDeviceBundle();
}
