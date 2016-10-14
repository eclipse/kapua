package org.eclipse.kapua.service.device.management.packages.model.install;

public interface DevicePackageInstallRequest {

    public String getName();

    public void setName(String name);

    public String getVersion();

    public void setVersion(String version);

    public Boolean isReboot();

    public void setReboot(Boolean reboot);

    public Integer getRebootDelay();

    public void setRebootDelay(Integer rebootDelay);
}
