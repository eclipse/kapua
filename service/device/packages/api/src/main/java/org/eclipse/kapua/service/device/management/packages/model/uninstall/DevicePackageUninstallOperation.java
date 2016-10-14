package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.model.id.KapuaId;

public interface DevicePackageUninstallOperation {

    public KapuaId getId();

    public void setId(KapuaId id);

    public String getName();

    public void setName(String packageName);

    public String getVersion();

    public void setVersion(String version);

    public DevicePackageUninstallStatus getStatus();

    public void setStatus(DevicePackageUninstallStatus status);
}
