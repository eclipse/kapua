package org.eclipse.kapua.service.device.management.packages.model.download.internal;

import java.net.URI;

import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;

/**
 * Device package download request.
 * 
 * @since 1.0
 *
 */
public class DevicePackageDownloadRequestImpl implements DevicePackageDownloadRequest
{
    public URI    uri;
    public String name;
    public String version;

    public Boolean install;

    public Boolean reboot;
    public Integer rebootDelay;

    @Override
    public URI getURI()
    {
        return uri;
    }

    @Override
    public void setURI(URI uri)
    {
        this.uri = uri;
    }

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
    public Boolean isInstall()
    {
        return install;
    }

    @Override
    public void setInstall(Boolean install)
    {
        this.install = install;
    }

    @Override
    public Boolean isReboot()
    {
        return reboot;
    }

    @Override
    public void setReboot(Boolean reboot)
    {
        this.reboot = reboot;
    }

    @Override
    public Integer getRebootDelay()
    {
        return rebootDelay;
    }

    @Override
    public void setRebootDelay(Integer rebootDelay)
    {
        this.rebootDelay = rebootDelay;
    }
}
