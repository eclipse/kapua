/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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

public enum PackageMetrics {
    APP_ID("DEPLOY"),//
    APP_VERSION("V2"), //

    // Commons metrics
    APP_METRIC_PACKAGE_OPERATION_ID("job.id"), //
    APP_METRIC_PACKAGE_REBOOT("dp.reboot"), //
    APP_METRIC_PACKAGE_REBOOT_DELAY("dp.reboot.delay"), //

    // Request exec download
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI("dp.uri"), //
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME("dp.name"), //
    APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION("dp.version"), //
    APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL("dp.download.protocol"), //
    APP_METRIC_PACKAGE_DOWNLOAD_INSTALL("dp.install"), //

    // Response get download
    APP_METRIC_PACKAGE_DOWNLOAD_SIZE("dp.download.size"), //
    APP_METRIC_PACKAGE_DOWNLOAD_STATUS("dp.download.status"), //
    APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS("dp.download.progress"), //

    // Request exec install
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME("dp.name"), //
    APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION("dp.version"), //
    APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE("dp.install.system.update"), //

    // Request exec uninstall
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME("dp.name"), //
    APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION("dp.version"), //

    ;

    private String value;

    PackageMetrics(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
