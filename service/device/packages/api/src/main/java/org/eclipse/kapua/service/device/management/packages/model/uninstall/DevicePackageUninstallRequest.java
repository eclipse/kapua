package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.*;

/**
 * Device package uninstall request definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "uninstallRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "name",
        "version",
        "reboot",
        "rebootDelay"},
        factoryClass = DevicePackageXmlRegistry.class,
        factoryMethod = "newDevicePackageUninstallRequest")
public interface DevicePackageUninstallRequest {

    /**
     * Get package name
     * 
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set package name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get package version
     * 
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set package version
     * 
     * @param version
     */
    public void setVersion(String version);

    /**
     * Get the device reboot flag
     * 
     * @return
     */
    @XmlElement(name = "reboot")
    public Boolean isReboot();

    /**
     * Set the device reboot flag
     * 
     * @param reboot
     */
    public void setReboot(Boolean reboot);

    /**
     * Get the reboot delay
     * 
     * @return
     */
    @XmlElement(name = "rebootDelay")
    public Integer getRebootDelay();

    /**
     * Set the reboot delay
     * 
     * @param rebootDelay
     */
    public void setRebootDelay(Integer rebootDelay);
}
