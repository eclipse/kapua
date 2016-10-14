package org.eclipse.kapua.service.device.management.packages.model.download.internal;

import java.net.URI;

import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;

public class DevicePackageDownloadRequestImpl implements DevicePackageDownloadRequest
{
    public URI    uri;
    public String name;
    public String version;

    public Boolean install;

    public Boolean reboot;
    public Integer rebootDelay;

    public URI getURI()
    {
        return uri;
    }

    public void setURI(URI uri)
    {
        this.uri = uri;
    }

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

    public Boolean isInstall()
    {
        return install;
    }

    public void setInstall(Boolean install)
    {
        this.install = install;
    }

    public Boolean isReboot()
    {
        return reboot;
    }

    public void setReboot(Boolean reboot)
    {
        this.reboot = reboot;
    }

    public Integer getRebootDelay()
    {
        return rebootDelay;
    }

    public void setRebootDelay(Integer rebootDelay)
    {
        this.rebootDelay = rebootDelay;
    }
}
