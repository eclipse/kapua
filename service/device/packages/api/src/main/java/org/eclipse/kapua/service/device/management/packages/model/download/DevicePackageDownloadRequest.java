package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.*;
import java.net.URI;

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
    @XmlElement(name = "uri")
    public URI getURI();

    public void setURI(URI uri);

    @XmlElement(name = "name")
    public String getName();

    public void setName(String name);

    @XmlElement(name = "version")
    public String getVersion();

    public void setVersion(String version);

    @XmlElement(name = "install")
    public Boolean isInstall();

    public void setInstall(Boolean install);

    @XmlElement(name = "reboot")
    public Boolean isReboot();

    public void setReboot(Boolean reboot);

    @XmlElement(name = "rebootDelay")
    public Integer getRebootDelay();

    public void setRebootDelay(Integer rebootDelay);
}
