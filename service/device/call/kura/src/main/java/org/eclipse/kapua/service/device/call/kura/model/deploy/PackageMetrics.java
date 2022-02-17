/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.deploy;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;

/**
 * Package {@link DeviceMetrics}.
 * <p>
 * For documentation follow Kura MQTT namespace: https://eclipse.github.io/kura/ref/mqtt-namespace.html#deploy-v2
 *
 * @since 1.0.0
 */
public enum PackageMetrics implements DeviceAppMetrics {

    /**
     * Application identifier.
     *
     * @since 1.0.0
     */
    APP_ID("DEPLOY"),

    /**
     * Application version.
     *
     * @since 1.0.0
     */
    APP_VERSION("V2"),

    //
    // Commons metrics
    /**
     * Operation identifier metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_OPERATION_ID("job.id"),

    /**
     * Device reboot metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_REBOOT("dp.reboot"),

    /**
     * Reboot delay metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_REBOOT_DELAY("dp.reboot.delay"),

    //
    // Download Exec

    /**
     * Download package uri metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI("dp.uri"),

    /**
     * Download package name metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME(Constants.DP_NAME),

    /**
     * Download package version metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION(Constants.DP_VERSION),

    /**
     * Download package HTTP basic auth usename metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_USERNAME("dp.download.username"),

    /**
     * Download package HTTP basic auth password metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PASSWORD("dp.download.password"),

    /**
     * Dowload package file hash metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_HASH("dp.download.hash"),

    /**
     * Download package protocol metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL("dp.download.protocol"),

    /**
     * Download package install metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL("dp.install"),

    /**
     * Download package install .sh metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_SYSTEM_UPDATE("dp.install.system.update"),

    /**
     * Download package installer verifier metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI("dp.install.verifier.uri"),

    /**
     * Download package download force metric name.
     *
     * @since 1.2.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_FORCE("dp.download.force"),

    /**
     * Download package download block size metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_SIZE("dp.download.block.size"),

    /**
     * Download package download block delay metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_DELAY("dp.download.block.delay"),

    /**
     * Download package download block timeout metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_TIMEOUT("dp.download.timeout"),

    /**
     * Download package notify block size metric name.
     *
     * @since 1.1.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE("dp.download.notify.block.size"),

    //
    // Download Get

    /**
     * Download get download size metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_SIZE("dp.download.size"),

    /**
     * Download get download status metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_STATUS("dp.download.status"),

    /**
     * Download get download progress metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS("dp.download.progress"),

    //
    // Install Exec

    /**
     * Install package name metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME(Constants.DP_NAME),

    /**
     * Install package version metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION(Constants.DP_VERSION),

    /**
     * Install package system update metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE("dp.install.system.update"),

    //
    // Request exec uninstall

    /**
     * Uninstall package name metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME(Constants.DP_NAME),

    /**
     * Uninstall package version metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION(Constants.DP_VERSION);

    /**
     * The name of the metric.
     *
     * @since 1.0.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The name of the metric.
     * @since 1.0.0
     */
    PackageMetrics(String name) {
        this.name = name;
    }

    /**
     * Gets the value property associated to this specific enumeration key.
     *
     * @return the value property associated to this specific enumeration key.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to {@link #getName()}.
     */
    @Deprecated
    public String getValue() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    protected static class Constants {
        protected static final String DP_NAME = "dp.name";
        protected static final String DP_VERSION = "dp.version";
    }

}
