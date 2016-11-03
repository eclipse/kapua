package org.eclipse.kapua.service.device.management.packages.model;

import java.util.List;

import javax.xml.bind.annotation.*;

/**
 * Package bundle informations list container definition.
 * 
 * @since 1.0
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfos")
public interface DevicePackageBundleInfos {

    /**
     * Get the device package bundle informations
     * 
     * @return
     */
    @XmlElement(name = "bundleInfo")
    public <B extends DevicePackageBundleInfo> List<B> getBundlesInfos();
}
