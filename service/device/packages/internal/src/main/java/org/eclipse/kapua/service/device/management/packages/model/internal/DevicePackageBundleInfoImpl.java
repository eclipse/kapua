package org.eclipse.kapua.service.device.management.packages.model.internal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;

/**
 * Device package bundle information.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundleInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "version" })
public class DevicePackageBundleInfoImpl implements DevicePackageBundleInfo
{
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "version")
    public String version;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}
