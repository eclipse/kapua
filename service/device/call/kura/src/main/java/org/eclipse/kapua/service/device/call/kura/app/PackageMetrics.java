/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.app;

/**
 * Bundle metrics properties definition.
 * 
 * @since 1.0
 *
 */
public enum PackageMetrics {

    /**
     * Application identifier
     */
    APP_ID("DEPLOY"),
    /**
     * Application version
     */
    APP_VERSION("V2"),

    // Commons metrics
    /**
     * Operation identifier
     */
    APP_METRIC_PACKAGE_OPERATION_ID("job.id"),
    /**
     * Device reboot
     */
    APP_METRIC_PACKAGE_REBOOT("dp.reboot"),
    /**
     * Reboot delay
     */
    APP_METRIC_PACKAGE_REBOOT_DELAY("dp.reboot.delay"),

    // Request exec download
    /**
     * Download package uri
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI("dp.uri"),
    /**
     * Download package name
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME("dp.name"),
    /**
     * Download package version
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION("dp.version"),
    /**
     * Download package protocol
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL("dp.download.protocol"),
    /**
     * Install downloaded package
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL("dp.install"),

    // Response get download
    /**
     * Download package already downloaded
     */
    APP_METRIC_PACKAGE_DOWNLOAD_SIZE("dp.download.size"),
    /**
     * Download package status
     */
    APP_METRIC_PACKAGE_DOWNLOAD_STATUS("dp.download.status"),
    /**
     * Download package progress
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS("dp.download.progress"),

    // Request exec install
    /**
     * Install package name
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME("dp.name"),
    /**
     * Install package version
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION("dp.version"),
    /**
     * Install system update package
     */
    APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE("dp.install.system.update"),

    // Request exec uninstall
    /**
     * Uninstall package name
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME("dp.name"),
    /**
     * Uninstall package version
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION("dp.version"),

    ;

    private String value;

    PackageMetrics(String value) {
        this.value = value;
    }

    /**
     * Get a value property associated to this specific enumeration key.
     * 
     * @return
     */
    public String getValue() {
        return value;
    }
}
