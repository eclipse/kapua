package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;

public class DevicePackageImpl implements DevicePackage
{
    private String name;

    private String version;

    private DevicePackageBundleInfosImpl bundleInfos;

    private Date installDate;

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

    @SuppressWarnings("unchecked")
    public DevicePackageBundleInfosImpl getBundleInfos()
    {
        if (bundleInfos == null) {
            bundleInfos = new DevicePackageBundleInfosImpl();
        }
        return bundleInfos;
    }

    public Date getInstallDate()
    {
        return installDate;
    }

    public void setInstallDate(Date installDate)
    {
        this.installDate = installDate;
    }
}
