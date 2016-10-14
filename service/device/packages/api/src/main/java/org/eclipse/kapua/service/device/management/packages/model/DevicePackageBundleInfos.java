package org.eclipse.kapua.service.device.management.packages.model;

import java.util.List;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfos")
public interface DevicePackageBundleInfos {
    @XmlElement(name = "bundleInfo")
    public <B extends DevicePackageBundleInfo> List<B> getBundlesInfos();
}
