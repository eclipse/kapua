package org.eclipse.kapua.service.device.management.packages.model.install.internal;

import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;

/**
 * Device package install request.
 * 
 * @since 1.0
 *
 */
public class DevicePackageInstallRequestImpl implements DevicePackageInstallRequest {

    private String name;
    private String version;
    private Boolean reboot;
    private Integer rebootDelay;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Boolean isReboot() {
        return reboot;
    }

    @Override
    public void setReboot(Boolean reboot) {
        this.reboot = reboot;
    }

    @Override
    public Integer getRebootDelay() {
        return rebootDelay;
    }

    @Override
    public void setRebootDelay(Integer rebootDelay) {
        this.rebootDelay = rebootDelay;
    }
}
