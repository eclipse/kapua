package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.service.device.management.KapuaAppProperties;

/**
 * Device application properties definition.
 * 
 * @since 1.0
 *
 */
public enum PackageAppProperties implements KapuaAppProperties {

    /**
     * Application name
     */
    APP_NAME("DEPLOY"),
    /**
     * Application version
     */
    APP_VERSION("1.0.0"),

    // Commons exec properties
    /**
     * Operation identifier
     */
    APP_PROPERTY_PACKAGE_OPERATION_ID("kapua.package.operation.id"),
    /**
     * Device reboot
     */
    APP_PROPERTY_PACKAGE_REBOOT("kapua.package.reboot"),
    /**
     * Reboot delay
     */
    APP_PROPERTY_PACKAGE_REBOOT_DELAY("kapua.package.reboot.delay"),

    // Request exec download
    /**
     * Package uri
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI("kapua.package.download.uri"),
    /**
     * Package name
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME("kapua.package.download.name"),
    /**
     * Package version
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION("kapua.package.download.version"),
    /**
     * Package install
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL("kapua.package.download.install"),

    // Response get download
    /**
     * Package download size
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE("kapua.package.download.size"),
    /**
     * Package download status
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS("kapua.package.download.status"),
    /**
     * Package download progress
     */
    APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS("kapua.package.download.progress"),

    // Request exec install
    /**
     * Package install name
     */
    APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME("kapua.package.install.name"),
    /**
     * Package install version
     */
    APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION("kapua.package.install.version"),

    // Request exec uninstall
    /**
     * Package uninstall name
     */
    APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME("kapua.package.uninstall.name"),
    /**
     * Package uninstall version
     */
    APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION("kapua.package.uninstall.version"),

    ;

    private String value;

    PackageAppProperties(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
