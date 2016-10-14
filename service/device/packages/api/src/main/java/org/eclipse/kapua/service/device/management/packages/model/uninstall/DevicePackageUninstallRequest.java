package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.*;

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

    @XmlElement(name = "name")
    public String getName();

    public void setName(String name);

    @XmlElement(name = "version")
    public String getVersion();

    public void setVersion(String version);

    @XmlElement(name = "reboot")
    public Boolean isReboot();

    public void setReboot(Boolean reboot);

    @XmlElement(name = "rebootDelay")
    public Integer getRebootDelay();

    public void setRebootDelay(Integer rebootDelay);
}
