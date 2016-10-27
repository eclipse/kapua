package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device package uninstall operation entity definition.
 * 
 * @since 1.0
 *
 */
public interface DevicePackageUninstallOperation {

    /**
     * Get the package identifier
     * 
     * @return
     */
    public KapuaId getId();

    /**
     * Set the package identifier
     * 
     * @param id
     */
    public void setId(KapuaId id);

    /**
     * Get the package name
     * 
     * @return
     */
    public String getName();

    /**
     * Set the package name
     * 
     * @param packageName
     */
    public void setName(String packageName);

    /**
     * Get the package version
     * 
     * @return
     */
    public String getVersion();

    /**
     * Set the package version
     * 
     * @param version
     */
    public void setVersion(String version);

    /**
     * Get the package uninstall status
     * 
     * @return
     */
    public DevicePackageUninstallStatus getStatus();

    /**
     * Set the package uninstall status
     * 
     * @param status
     */
    public void setStatus(DevicePackageUninstallStatus status);
}
