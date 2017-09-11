/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadOperationImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadRequestImpl;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.internal.DevicePackageInstallRequestImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageBundleInfoImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageBundleInfosImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackageImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackagesImpl;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.internal.DevicePackageUninstallRequestImpl;

/**
 * Device package service implementation.
 * 
 * @since 1.0
 *
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

    //
    // Install operation
    //
    @Override
    public DevicePackageInstallRequest newPackageInstallRequest() {
        return new DevicePackageInstallRequestImpl();
    }

    //
    // Uninstall operation
    //
    @Override
    public DevicePackageUninstallRequest newPackageUninstallRequest() {
        return new DevicePackageUninstallRequestImpl();
    }

}
