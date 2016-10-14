package org.eclipse.kapua.service.device.management.packages.model.install.internal;

import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;

public class DevicePackageInstallRequestImpl implements DevicePackageInstallRequest {

    private String name;
    private String version;
    private Boolean reboot;
    private Integer rebootDelay;

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
