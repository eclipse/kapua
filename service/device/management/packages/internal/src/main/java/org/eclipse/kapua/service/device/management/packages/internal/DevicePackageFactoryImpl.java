/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.AdvancedPackageDownloadOptionsImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadOperationImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadOptionsImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadRequestImpl;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.internal.DevicePackageInstallOptionsImpl;
import org.eclipse.kapua.service.device.management.packages.model.install.internal.DevicePackageInstallRequestImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageBundleInfoImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageBundleInfosImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackagesImpl;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.internal.DevicePackageUninstallOptionsImpl;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.internal.DevicePackageUninstallRequestImpl;

/**
 * {@link DevicePackageFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DevicePackageFactoryImpl implements DevicePackageFactory {

    @Override
    public DevicePackages newDeviceDeploymentPackages() {
        return new DevicePackagesImpl();
    }

    @Override
    public DevicePackage newDeviceDeploymentPackage() {
        return new DevicePackageImpl();
    }

    @Override
    public DevicePackageBundleInfo newDevicePackageBundleInfo() {
        return new DevicePackageBundleInfoImpl();
    }

    @Override
    public DevicePackageBundleInfos newDevicePackageBundleInfos() {
        return new DevicePackageBundleInfosImpl();
    }

    //
    // Download operation
    //
    @Override
    public DevicePackageDownloadRequest newPackageDownloadRequest() {
        return new DevicePackageDownloadRequestImpl();
    }

    @Override
    public DevicePackageDownloadOperation newPackageDownloadOperation() {
        return new DevicePackageDownloadOperationImpl();
    }

    @Override
    public DevicePackageDownloadOptions newDevicePackageDownloadOptions() {
        return new DevicePackageDownloadOptionsImpl();
    }

    @Override
    public AdvancedPackageDownloadOptions newAdvancedPackageDownloadOptions() {
        return new AdvancedPackageDownloadOptionsImpl();
    }

    //
    // Install operation
    //
    @Override
    public DevicePackageInstallRequest newPackageInstallRequest() {
        return new DevicePackageInstallRequestImpl();
    }

    @Override
    public DevicePackageInstallOptions newDevicePackageInstallOptions() {
        return new DevicePackageInstallOptionsImpl();
    }

    //
    // Uninstall operation
    //
    @Override
    public DevicePackageUninstallRequest newPackageUninstallRequest() {
        return new DevicePackageUninstallRequestImpl();
    }

    @Override
    public DevicePackageUninstallOptions newDevicePackageUninstallOptions() {
        return new DevicePackageUninstallOptionsImpl();
    }
}
