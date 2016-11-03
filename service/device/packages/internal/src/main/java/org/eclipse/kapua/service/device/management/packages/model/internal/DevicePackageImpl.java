package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.Date;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;

/**
 * Device package.
 * 
 * @since 1.0
 *
 */
public class DevicePackageImpl implements DevicePackage
{
    private String name;

    private String version;

    private DevicePackageBundleInfosImpl bundleInfos;

    private Date installDate;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    @Override
    public void setVersion(String version)
    {
        this.version = version;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DevicePackageBundleInfosImpl getBundleInfos()
    {
        if (bundleInfos == null) {
            bundleInfos = new DevicePackageBundleInfosImpl();
        }
        return bundleInfos;
    }

    @Override
    public Date getInstallDate()
    {
        return installDate;
    }

    @Override
    public void setInstallDate(Date installDate)
    {
        this.installDate = installDate;
    }
}
