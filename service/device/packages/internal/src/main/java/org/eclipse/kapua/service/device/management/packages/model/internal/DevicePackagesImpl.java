package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;

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
