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
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMetrics;

/**
 * {@link DeviceRequestMetrics} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public enum KuraNotifyMetrics implements DeviceRequestMetrics {

    /**
     * @since 1.0.0
     */
    OPERATION_ID("job.id"),
    /**
     * @since 1.2.0
     */
    OPERATION_ID_ALTERNATIVE("operation.id"),

    /**
     * @since 1.2.0
     */
    STATUS("status"),
    /**
     * @since 1.0.0
     */
    DOWNLOAD_STATUS("dp.download.status"),
    /**
     * @since 1.0.0
     */
    INSTALL_STATUS("dp.install.status"),
    /**
     * @since 1.0.0
     */
    UNINSTALL_STATUS("dp.uninstall.status"),

    /**
     * @since 1.2.0
     */
    PROGRESS("progress"),
    /**
     * @since 1.0.0
     */
    DOWNLOAD_PROGRESS("dp.download.progress"),
    /**
     * @since 1.0.0
     */
    INSTALL_PROGRESS("dp.install.progress"),
    /**
     * @since 1.0.0
     */
    UNINSTALL_PROGRESS("dp.uninstall.progress"),

    /**
     * @since 1.2.0
     */
    MESSAGE("message"),
    /**
     * @since 1.2.0
     */
    NOTIFY_MESSAGE("dp.notify.message"),
    /**
     * @since 1.2.0
     */
    DOWNLOAD_MESSAGE("dp.download.message"),
    /**
     * @since 1.2.0
     */
    DOWNLOAD_ERROR_MESSAGE("dp.download.error.message"),
    /**
     * @since 1.2.0
     */
    INSTALL_MESSAGE("dp.install.message"),
    /**
     * @since 1.2.0
     */
    INSTALL_ERROR_MESSAGE("dp.install.error.message"),
    /**
     * @since 1.2.0
     */
    UNINSTALL_MESSAGE("dp.uninstall.message"),
    /**
     * @since 1.2.0
     */
    UNINSTALL_ERROR_MESSAGE("dp.uninstall.error.message"),
    ;

    /**
     * The metric name
     *
     * @since 1.0.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The metric name
     * @since 1.0.0
     */
    KuraNotifyMetrics(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return getName();
    }
}
