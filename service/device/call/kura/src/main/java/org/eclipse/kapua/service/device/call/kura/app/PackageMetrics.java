/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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
 * {@link PackageMetrics} properties definition.
 * <p>
 * For documentation follow Kura MQTT namespace: https://eclipse.github.io/kura/ref/mqtt-namespace.html#deploy-v2
 *
 * @since 1.0.0
 */
public enum PackageMetrics {

    /**
     * Application identifier
     *
     * @since 1.0.0
     */
    APP_ID("DEPLOY"),

    /**
     * Application version
     *
     * @since 1.0.0
     */
    APP_VERSION("V2"),

    // Commons metrics
    /**
     * Operation identifier
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_OPERATION_ID("job.id"),

    /**
     * Device reboot
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_REBOOT("dp.reboot"),

    /**
     * Reboot delay
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_REBOOT_DELAY("dp.reboot.delay"),

    // Request exec download
    /**
     * Download package uri
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI("dp.uri"),

    /**
     * Download package name
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME("dp.name"),

    /**
     * Download package version
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION("dp.version"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_USERNAME("dp.download.username"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PASSWORD("dp.download.password"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_HASH("dp.download.hash"),

    /**
     * Download package protocol
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL("dp.download.protocol"),

    /**
     * Install downloaded package
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL("dp.install"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_SYSTEM_UPDATE("dp.install.system.update"),


    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI("dp.install.verifier.uri"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_SIZE("dp.download.block.size"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_DELAY("dp.download.block.delay"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_TIMEOUT("dp.download.timeout"),

    /**
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE("dp.download.notify.block.size"),

    // Response get download
    /**
     * Download package already downloaded
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_SIZE("dp.download.size"),
    /**
     * Download package status
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_STATUS("dp.download.status"),
    /**
     * Download package progress
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS("dp.download.progress"),

    // Request exec install
    /**
     * Install package name
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME("dp.name"),
    /**
     * Install package version
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION("dp.version"),
    /**
     * Install system update package
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE("dp.install.system.update"),

    // Request exec uninstall
    /**
     * Uninstall package name
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME("dp.name"),
    /**
     * Uninstall package version
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION("dp.version"),

    ;

    private String value;

    /**
     * Label for the {@link org.eclipse.kapua.service.device.call.kura.Kura} metric name.
     *
     * @param value The {@link org.eclipse.kapua.service.device.call.kura.Kura} metric name
     * @since 1.0.0
     */
    PackageMetrics(String value) {
        this.value = value;
    }

    /**
     * Gets the {@link org.eclipse.kapua.service.device.call.kura.Kura} metric name.
     *
     * @return The {@link org.eclipse.kapua.service.device.call.kura.Kura} metric name.
     * @since 1.0.0
     */
    public String getValue() {
        return value;
    }
}
