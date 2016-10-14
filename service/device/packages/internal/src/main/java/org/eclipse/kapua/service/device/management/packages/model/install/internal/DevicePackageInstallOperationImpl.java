package org.eclipse.kapua.service.device.management.packages.model.install.internal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallStatus;

@XmlRootElement(name = "packageInstallOperation")
public class DevicePackageInstallOperationImpl implements DevicePackageInstallOperation {

    @XmlElement(name = "id")
    private KapuaEid id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "status")
    private DevicePackageInstallStatus status;

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

    public DevicePackageInstallStatus getStatus() {
        return status;
    }

    public void setStatus(DevicePackageInstallStatus status) {
        this.status = status;
    }
}
