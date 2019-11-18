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
package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

/**
 * {@link KapuaAppProperties} definition for {@link org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService}.
 *
 * @since 1.0.0
 */
public enum PackageAppProperties implements KapuaAppProperties {

    /**
     * Application name
     *
     * @since 1.0.0
     */
    APP_NAME("DEPLOY"),
    /**
     * Application version
     *
     * @since 1.0.0
     */
    APP_VERSION("1.0.0"),

    // Commons exec properties
    /**
     * Operation identifier
     */
    APP_PROPERTY_PACKAGE_OPERATION_ID("kapua.package.operation.id"),
    /**
     * Device reboot
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_REBOOT("kapua.package.reboot"),
    /**
     * Reboot delay
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_REBOOT_DELAY("kapua.package.reboot.delay"),

    // Request exec download
    /**
     * Package uri
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI("kapua.package.download.uri"),
    /**
     * Package name
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME("kapua.package.download.name"),
    /**
     * Package version
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION("kapua.package.download.version"),
    /**
     * URI username
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_USERNAME("kapua.package.download.username"),
    /**
     * URI password
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PASSWORD("kapua.package.download.password"),
    /**
     * File hash
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_HASH("kapua.package.download.file.hash"),
    /**
     * File type
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_TYPE("kapua.package.download.file.type"),
    /**
     * Package install
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL("kapua.package.download.install"),

    /**
     * File download block size
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_SIZE("kapua.package.download.block.size"),

    /**
     * File download block delay
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_DELAY("kapua.package.download.block.delay"),

    /**
     * File download block timeout
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_TIMEOUT("kapua.package.download.block.timeout"),

    /**
     * File download notify block size
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE("kapua.package.download.notify.block.size"),

    /**
     * File download install verifier URI
     *
     * @since 1.1.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI("kapua.package.download.install.verifier.uri"),

    // Response get download
    /**
     * Package download size
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE("kapua.package.download.size"),
    /**
     * Package download status
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS("kapua.package.download.status"),
    /**
     * Package download progress
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS("kapua.package.download.progress"),

    // Request exec install
    /**
     * Package install name
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME("kapua.package.install.name"),
    /**
     * Package install version
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION("kapua.package.install.version"),

    // Request exec uninstall
    /**
     * Package uninstall name
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME("kapua.package.uninstall.name"),
    /**
     * Package uninstall version
     *
     * @since 1.0.0
     */
    APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION("kapua.package.uninstall.version"),
    ;

    private String value;

    /**
     * Label for the {@link KapuaAppProperties} properties.
     *
     * @param value The property name of the {@link KapuaAppProperties}.
     * @since 1.0.0
     */
    PackageAppProperties(String value) {
        this.value = value;
    }

    /**
     * Gets the {@link KapuaAppProperties} name.
     *
     * @return The {@link KapuaAppProperties} name.
     * @since 1.0.0
     */
    @Override
    public String getValue() {
        return value;
    }

}
