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
package org.eclipse.kapua.service.device.management.packages;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

/**
 * {@link DevicePackageFactory} definition.
 *
 * @since 1.0.0
 */
public interface DevicePackageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link DevicePackages}
     *
     * @return the newly created {@link DevicePackages}
     * @since 1.0.0
     */
    DevicePackages newDeviceDeploymentPackages();

    /**
     * Creates a new {@link DevicePackage}
     *
     * @return The newly created {@link DevicePackage}
     * @since 1.0.0
     */
    DevicePackage newDeviceDeploymentPackage();

    /**
     * Creates a new {@link DevicePackageBundleInfo}
     *
     * @return the newly created {@link DevicePackageBundleInfo}
     * @since 1.0.0
     */
    DevicePackageBundleInfo newDevicePackageBundleInfo();

    /**
     * Creates a new {@link DevicePackageBundleInfos}
     *
     * @return the newly created {@link DevicePackageBundleInfos}
     * @since 1.0.0
     */
    DevicePackageBundleInfos newDevicePackageBundleInfos();

    //
    // Download operation
    //

    /**
     * Creates a new {@link DevicePackageDownloadRequest}
     *
     * @return The newly created {@link DevicePackageDownloadRequest}
     * @since 1.0.0
     */
    DevicePackageDownloadRequest newPackageDownloadRequest();

    /**
     * Creates a new {@link DevicePackageDownloadOptions}
     *
     * @return the newly created {@link DevicePackageDownloadOptions}
     * @since 1.1.0
     */
    DevicePackageDownloadOptions newDevicePackageDownloadOptions();

    /**
     * Creates a new {@link AdvancedPackageDownloadOptions}
     *
     * @return the newly created {@link AdvancedPackageDownloadOptions}
     * @since 1.1.0
     */
    AdvancedPackageDownloadOptions newAdvancedPackageDownloadOptions();

    /**
     * Creates a new {@link DevicePackageDownloadOperation}
     *
     * @return the newly created {@link DevicePackageDownloadOperation}
     * @since 1.0.0
     */
    DevicePackageDownloadOperation newPackageDownloadOperation();

    //
    // Install operation
    //

    /**
     * Creates a new {@link DevicePackageInstallRequest}
     *
     * @return the newly created {@link DevicePackageInstallRequest}
     * @since 1.0.0
     */
    DevicePackageInstallRequest newPackageInstallRequest();

    /**
     * Creates a new {@link DevicePackageInstallOptions}
     *
     * @return the newly created {@link DevicePackageInstallOptions}
     * @since 1.1.0
     */
    DevicePackageInstallOptions newDevicePackageInstallOptions();

    //
    // Uninstall operation
    //

    /**
     * Creates a new {@link DevicePackageUninstallRequest}
     *
     * @return the newly created {@link DevicePackageUninstallRequest}
     * @since 1.0.0
     */
    DevicePackageUninstallRequest newPackageUninstallRequest();

    /**
     * Creates a new {@link DevicePackageUninstallOptions}
     *
     * @return the newly created {@link DevicePackageUninstallOptions}
     * @since 1.1.0
     */
    DevicePackageUninstallOptions newDevicePackageUninstallOptions();
}
