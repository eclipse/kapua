package org.eclipse.kapua.service.device.management.packages.model.install;

import org.eclipse.kapua.model.id.KapuaId;

public interface DevicePackageInstallOperation {

    public KapuaId getId();

    public void setId(KapuaId id);

    public String getName();

    public void setName(String packageName);

    public String getVersion();

    public void setVersion(String version);

    public DevicePackageInstallStatus getStatus();

    public void setStatus(DevicePackageInstallStatus status);
}
