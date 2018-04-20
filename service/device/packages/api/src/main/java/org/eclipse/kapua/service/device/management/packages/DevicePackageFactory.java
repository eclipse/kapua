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
package org.eclipse.kapua.service.device.management.packages;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

/**
 * Device package service definition.
 * 
 * @since 1.0
 *
 */
public interface DevicePackageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link DevicePackages}
     * 
     * @return
     */
    public DevicePackages newDeviceDeploymentPackages();

    /**
     * Creates a new {@link DevicePackage}
     * 
     * @return
     */
    public DevicePackage newDeviceDeploymentPackage();

    /**
     * Creates a new device package bundle information
     * 
     * @return
     */
    public DevicePackageBundleInfo newDevicePackageBundleInfo();

    /**
     * Creates a new device package bundle informations
     * 
     * @return
     */
    public DevicePackageBundleInfos newDevicePackageBundleInfos();

    //
    // Download operation
    //
    /**
     * Creates a new device package download request
     * 
     * @return
     */
    public DevicePackageDownloadRequest newPackageDownloadRequest();

    /**
     * Creates a new device package download operation
     * 
     * @return
     */
    public DevicePackageDownloadOperation newPackageDownloadOperation();

    //
    // Install operation
    //
    /**
     * Creates a new {@link DevicePackageInstallRequest}
     *
     * @return
     */
    public DevicePackageInstallRequest newPackageInstallRequest();

    //
    // Uninstall operation
    //
    /**
     * Creates a new device package uninstall request
     * 
     * @return
     */
    public DevicePackageUninstallRequest newPackageUninstallRequest();
}
