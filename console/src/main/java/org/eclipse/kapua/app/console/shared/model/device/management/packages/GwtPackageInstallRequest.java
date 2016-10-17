package org.eclipse.kapua.app.console.shared.model.device.management.packages;

import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

public class GwtPackageInstallRequest extends KapuaBaseModel {

    public void setScopeId(String scopeId) {
        set("scopeId", scopeId);
    }

    public String getScopeId() {
        return (String) get("scopeId");
    }

    public void setDeviceId(String deviceId) {
        set("deviceId", deviceId);
    }

    public String getDeviceId() {
        return (String) get("deviceId");
    }

    public void setPackageURI(String packageURI) {
        set("packageURI", packageURI);
    }

    public String getPackageURI() {
        return get("packageURI");
    }

    public void setPackageName(String packageName) {
        set("packageName", packageName);

    }

    public String getPackageName() {
        return get("packageName");
    }

    public void setPackageVersion(String packageVersion) {
        set("packageVersion", packageVersion);
    }

    public String getPackageVersion() {
        return get("packageVersion");
    }

    public void setReboot(Boolean reboot) {
        set("reboot", reboot);
    }

    public Boolean isReboot() {
        return get("reboot");
    }

    public void setRebootDelay(int rebootDelay) {
        set("rebootDelay", rebootDelay);
    }

    public Integer getRebootDelay() {
        return get("rebootDelay");
    }

}
