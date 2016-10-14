package org.eclipse.kapua.service.device.management.packages.model.uninstall.internal;

import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

public class DevicePackageUninstallRequestImpl implements DevicePackageUninstallRequest {

    public String name;
    public String version;

    public Boolean reboot;
    public Integer rebootDelay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean isReboot() {
        return reboot;
    }

    public void setReboot(Boolean reboot) {
        this.reboot = reboot;
    }

    public Integer getRebootDelay() {
        return rebootDelay;
    }

    public void setRebootDelay(Integer rebootDelay) {
        this.rebootDelay = rebootDelay;
    }
}
