package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "devicePackageBundleInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfo")
public interface DevicePackageBundleInfo {

    @XmlElement(name = "name")
    public String getName();

    public void setName(String name);

    @XmlElement(name = "version")
    public String getVersion();

    public void setVersion(String version);
}
