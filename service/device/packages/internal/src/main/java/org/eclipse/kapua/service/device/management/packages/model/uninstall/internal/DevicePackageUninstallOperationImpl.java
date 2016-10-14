package org.eclipse.kapua.service.device.management.packages.model.uninstall.internal;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallStatus;

public class DevicePackageUninstallOperationImpl implements DevicePackageUninstallOperation {

    @XmlElement(name = "id")
    private KapuaEid id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "status")
    private DevicePackageUninstallStatus status;

    @Override
    public KapuaEid getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        if (id != null) {
            this.id = new KapuaEid(id.getId());
        }
    }

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

    public DevicePackageUninstallStatus getStatus() {
        return status;
    }

    public void setStatus(DevicePackageUninstallStatus status) {
        this.status = status;
    }
}
