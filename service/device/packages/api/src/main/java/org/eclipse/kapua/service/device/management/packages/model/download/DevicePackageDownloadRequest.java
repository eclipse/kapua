package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.*;
import java.net.URI;

/**
 * Device package download request definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "downloadRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "URI",
        "name",
        "version",
        "install",
        "reboot",
        "rebootDelay"},
        factoryClass = DevicePackageXmlRegistry.class,
        factoryMethod = "newDevicePackageDownloadRequest")
public interface DevicePackageDownloadRequest
{

    /**
     * Get the download URI
     * 
     * @return
     */
    @XmlElement(name = "uri")
    public URI getURI();

    /**
     * Set the download URI
     * 
     * @param uri
     */
    public void setURI(URI uri);

    /**
     * Get the package name
     * 
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the package name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the package version
     * 
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set the package version
     * 
     * @param version
     */
    public void setVersion(String version);

    /**
     * Get the package installed flag
     * 
     * @return
     */
    @XmlElement(name = "install")
    public Boolean isInstall();

    /**
     * Set the package installed flag
     * 
     * @param install
     */
    public void setInstall(Boolean install);

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
