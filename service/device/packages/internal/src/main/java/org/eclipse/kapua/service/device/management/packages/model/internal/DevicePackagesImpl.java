package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;

/**
 * Device packages list container.
 * 
 * @since 1.0
 *
 */
public class DevicePackagesImpl implements DevicePackages
{
    public List<DevicePackageImpl> deploymentPackages;

    @Override
    public List<DevicePackageImpl> getPackages()
    {
        if (deploymentPackages == null) {
            deploymentPackages = new ArrayList<>();
        }

        return deploymentPackages;
    }

}
