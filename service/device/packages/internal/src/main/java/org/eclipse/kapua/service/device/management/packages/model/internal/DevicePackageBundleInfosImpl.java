package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;

/**
 * Package bundle informations list container.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundleInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class DevicePackageBundleInfosImpl implements DevicePackageBundleInfos
{
    @XmlElement(name = "bundleInfo")
    List<DevicePackageBundleInfoImpl> bundleInfos;

    @Override
    public List<DevicePackageBundleInfoImpl> getBundlesInfos()
    {
        if (bundleInfos == null) {
            bundleInfos = new ArrayList<>();
        }
        return bundleInfos;
    }

}
