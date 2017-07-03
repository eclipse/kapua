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
package org.eclipse.kapua.service.device.management.packages.message.internal;

import java.net.URI;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;

/**
 * Package request message payload.
 * 
 * @since 1.0
 *
 */
public class PackageRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    /**
     * Get the operation identifier
     * 
     * @return
     */
    public KapuaId getOperationId() {
        return (KapuaId) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue());
    }

    /**
     * Set the operation identifier
     * 
     * @param operationId
     */
    public void setOperationId(KapuaId operationId) {
        if (operationId != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue(), operationId);
        }
    }

    /**
     * Get the device reboot flag
     * 
     * @return
     */
    public Boolean isReboot() {
        return (Boolean) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT.getValue());
    }

    /**
     * Set the device reboot flag
     * 
     * @param reboot
     */
    public void setReboot(Boolean reboot) {
        if (reboot != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT.getValue(), reboot);
        }
    }

    /**
     * Get the device reboot flag
     * 
     * @return
     */
    public Integer getRebootDelay() {
        return (Integer) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY.getValue());
    }

    /**
     * Set the device reboot flag
     * 
     * @param rebootDelay
     */
    public void setRebootDelay(Integer rebootDelay) {
        if (rebootDelay != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY.getValue(), rebootDelay);
        }
    }

    //
    // Download
    //
    /**
     * Get the package download URI
     * 
     * @return
     */
    public URI getPackageDownloadURI() {
        return (URI) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI.getValue());
    }

    /**
     * Set the package download URI
     * 
     * @param uri
     */
    public void setPackageDownloadURI(URI uri) {
        if (uri != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI.getValue(), uri);
        }
    }

    /**
     * Get the package name
     * 
     * @return
     */
    public String getPackageDownloadName() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME.getValue());
    }

    /**
     * Set the package name
     * 
     * @param packageName
     */
    public void setPackageDownloadName(String packageName) {
        if (packageName != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME.getValue(), packageName);
        }
    }

    /**
     * Get the package version
     * 
     * @return
     */
    public String getPackageDownloadVersion() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION.getValue());
    }

    /**
     * Set the package version
     * 
     * @param packageVersion
     */
    public void setPackageDownloadVersion(String packageVersion) {
        if (packageVersion != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    /**
     * Get the is a download package and install flag
     * 
     * @return
     */
    public Boolean isPackageDownloadnstall() {
        return (Boolean) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL.getValue());
    }

    /**
     * Set the is a download package and install flag
     * 
     * @param packageDownloadnstall
     */
    public void setPackageDownloadnstall(Boolean packageDownloadnstall) {
        if (packageDownloadnstall != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL.getValue(), packageDownloadnstall);
        }
    }

    //
    // Install
    //
    /**
     * Get the package install name
     * 
     * @return
     */
    public String getPackageInstallName() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME.getValue());
    }

    /**
     * Set the package install name
     * 
     * @param packageName
     */
    public void setPackageInstallName(String packageName) {
        if (packageName != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME.getValue(), packageName);
        }
    }

    /**
     * Get the package install version
     * 
     * @return
     */
    public String getPackageInstallVersion() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION.getValue());
    }

    /**
     * Set the package install version
     * 
     * @param packageVersion
     */
    public void setPackageInstallVersion(String packageVersion) {
        if (packageVersion != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    //
    // Uninstall
    //
    /**
     * Get the package uninstall name
     * 
     * @return
     */
    public String getPackageUninstallName() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME.getValue());
    }

    /**
     * Set the package uninstall name
     * 
     * @param packageName
     */
    public void setPackageUninstallName(String packageName) {
        if (packageName != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME.getValue(), packageName);
        }
    }

    /**
     * Get the package uninstall version
     * 
     * @return
     */
    public String getPackageUninstallVersion() {
        return (String) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION.getValue());
    }

    /**
     * Set the package uninstall version
     * 
     * @param packageVersion
     */
    public void setPackageUninstallVersion(String packageVersion) {
        if (packageVersion != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    //
    // Utility methods
    //
    /**
     * Get the is a download request flag
     * 
     * @return
     */
    public boolean isDownloadRequest() {
        return getPackageDownloadName() != null;
    }

    /**
     * Get the is an install request flag
     * 
     * @return
     */
    public boolean isInstallRequest() {
        return getPackageInstallName() != null;
    }

    /**
     * Get the is an uninstall request flag
     * 
     * @return
     */
    public boolean isUninstallRequest() {
        return getPackageUninstallName() != null;
    }
}
