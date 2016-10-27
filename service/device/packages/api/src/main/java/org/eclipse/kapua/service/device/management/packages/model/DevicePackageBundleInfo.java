package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device package bundle information definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "devicePackageBundleInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfo")
public interface DevicePackageBundleInfo {

    /**
     * Get the package bundle name
     * 
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the package bundle name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the package bundle version
     * 
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set the package bundle version
     * 
     * @param version
     */
    public void setVersion(String version);
}
