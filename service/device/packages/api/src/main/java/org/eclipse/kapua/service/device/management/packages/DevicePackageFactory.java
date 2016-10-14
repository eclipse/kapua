package org.eclipse.kapua.service.device.management.packages;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

public interface DevicePackageFactory extends KapuaObjectFactory {

    public DevicePackages newDeviceDeploymentPackages();

    public DevicePackage newDeviceDeploymentPackage();

    public DevicePackageBundleInfo newDevicePackageBundleInfo();
    
    public DevicePackageBundleInfos newDevicePackageBundleInfos();

    //
    // Download operation
    //
    public DevicePackageDownloadRequest newPackageDownloadRequest();

    public DevicePackageDownloadOperation newPackageDownloadOperation();

    //
    // Uninstall operation
    //
    public DevicePackageUninstallRequest newPackageUninstallRequest();
}
