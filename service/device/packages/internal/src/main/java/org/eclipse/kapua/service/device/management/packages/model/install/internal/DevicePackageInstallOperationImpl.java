package org.eclipse.kapua.service.device.management.packages.model.install.internal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallStatus;

/**
 * Device package install operation entity.
 * 
 * @since 1.0
 *
 */
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
    public DevicePackageInstallStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DevicePackageInstallStatus status) {
        this.status = status;
    }
}
